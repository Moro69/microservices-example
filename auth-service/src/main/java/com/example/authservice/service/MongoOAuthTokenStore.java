package com.example.authservice.service;

import com.example.authservice.domain.oauth.OAuthAccessToken;
import com.example.authservice.domain.oauth.OAuthRefreshToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Log4j2
public class MongoOAuthTokenStore implements TokenStore {

    public static final String TOKEN_ID = "tokenId";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String AUTHENTICATION_ID = "authenticationId";
    public static final String CLIENT_ID = "clientId";
    public static final String USERNAME = "username";

    private final AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private final MongoTemplate mongoTemplate;

    public MongoOAuthTokenStore(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
        log.debug("readAuthentication: ");

        return readAuthentication(accessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        log.debug("readAuthentication: ");

        Query query = new Query();
        query.addCriteria(Criteria.where(TOKEN_ID).is(extractTokenKey(token)));

        OAuthAccessToken mongoAccessToken = mongoTemplate.findOne(query, OAuthAccessToken.class);
        return mongoAccessToken != null ? mongoAccessToken.getAuthentication() : null;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        log.debug("storeAccessToken: ");

        String refreshToken = null;
        if (accessToken.getRefreshToken() != null) {
            refreshToken = accessToken.getRefreshToken().getValue();
        }

        if (readAccessToken(accessToken.getValue()) != null) {
            this.removeAccessToken(accessToken);
        }

        OAuthAccessToken mongoAccessToken = new OAuthAccessToken();
        mongoAccessToken.setTokenId(extractTokenKey(accessToken.getValue()));
        mongoAccessToken.setToken(accessToken);
        mongoAccessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        mongoAccessToken.setUsername(authentication.isClientOnly() ? null : authentication.getName());
        mongoAccessToken.setClientId(authentication.getOAuth2Request().getClientId());
        mongoAccessToken.setAuthentication(authentication);
        mongoAccessToken.setRefreshToken(extractTokenKey(refreshToken));

        mongoTemplate.save(mongoAccessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        log.debug("readAccessToken: ");

        Query query = new Query();
        query.addCriteria(Criteria.where(TOKEN_ID).is(extractTokenKey(tokenValue)));

        OAuthAccessToken mongoAccessToken = mongoTemplate.findOne(query, OAuthAccessToken.class);
        return mongoAccessToken != null ? mongoAccessToken.getToken() : null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        log.debug("removeAccessToken: ");

        Query query = new Query();
        query.addCriteria(Criteria.where(TOKEN_ID).is(extractTokenKey(oAuth2AccessToken.getValue())));
        mongoTemplate.remove(query, OAuthAccessToken.class);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        log.debug("storeRefreshToken: ");

        OAuthRefreshToken token = new OAuthRefreshToken();
        token.setTokenId(extractTokenKey(refreshToken.getValue()));
        token.setToken(refreshToken);
        token.setAuthentication(authentication);
        mongoTemplate.save(token);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        log.debug("readRefreshToken: ");

        Query query = new Query();
        query.addCriteria(Criteria.where(TOKEN_ID).is(extractTokenKey(tokenValue)));

        OAuthRefreshToken mongoRefreshToken = mongoTemplate.findOne(query, OAuthRefreshToken.class);
        return mongoRefreshToken != null ? mongoRefreshToken.getToken() : null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {
        log.debug("readAuthenticationForRefreshToken: ");

        Query query = new Query();
        query.addCriteria(Criteria.where(TOKEN_ID).is(extractTokenKey(refreshToken.getValue())));

        OAuthRefreshToken mongoRefreshToken = mongoTemplate.findOne(query, OAuthRefreshToken.class);
        return mongoRefreshToken != null ? mongoRefreshToken.getAuthentication() : null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        log.debug("removeRefreshToken: ");

        Query query = new Query();
        query.addCriteria(Criteria.where(TOKEN_ID).is(extractTokenKey(refreshToken.getValue())));
        mongoTemplate.remove(query, OAuthRefreshToken.class);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        log.debug("removeAccessTokenUsingRefreshToken: ");

        Query query = new Query();
        query.addCriteria(Criteria.where(REFRESH_TOKEN).is(extractTokenKey(refreshToken.getValue())));
        mongoTemplate.remove(query, OAuthAccessToken.class);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        log.debug("getAccessToken: ");

        OAuth2AccessToken accessToken = null;
        String authenticationId = authenticationKeyGenerator.extractKey(authentication);

        Query query = new Query();
        query.addCriteria(Criteria.where(AUTHENTICATION_ID).is(authenticationId));

        OAuthAccessToken mongoAccessToken = mongoTemplate.findOne(query, OAuthAccessToken.class);
        if (mongoAccessToken != null) {
            accessToken = mongoAccessToken.getToken();
            if(accessToken != null && !authenticationId.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken)))) {
                this.removeAccessToken(accessToken);
                this.storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        log.debug("findTokensByClientIdAndUserName: ");

        Collection<OAuth2AccessToken> tokens = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where(CLIENT_ID).is(clientId));
        query.addCriteria(Criteria.where(USERNAME).is(username));
        List<OAuthAccessToken> accessTokens = mongoTemplate.find(query, OAuthAccessToken.class);
        for (OAuthAccessToken accessToken : accessTokens) {
            tokens.add(accessToken.getToken());
        }
        return tokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        log.debug("findTokensByClientId: ");

        Collection<OAuth2AccessToken> tokens = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where(CLIENT_ID).is(clientId));
        List<OAuthAccessToken> accessTokens = mongoTemplate.find(query, OAuthAccessToken.class);
        for (OAuthAccessToken accessToken : accessTokens) {
            tokens.add(accessToken.getToken());
        }
        return tokens;
    }

    private String extractTokenKey(String value) {
        if(value == null) {
            return null;
        } else {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var5) {
                throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
            }

            byte[] e = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, e));
        }
    }
}
