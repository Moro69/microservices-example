package com.example.authservice.domain.oauth;

import com.example.authservice.utils.AuthConverter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Document(collection = "accessTokens")
@Data
public class OAuthAccessToken {

    @Id
    private String id;

    private String tokenId;
    private OAuth2AccessToken token;
    private String authenticationId;
    private String username;
    private String clientId;
    private String authentication;
    private String refreshToken;

    public OAuth2Authentication getAuthentication() {
        return AuthConverter.deserialize(authentication);
    }

    public void setAuthentication(OAuth2Authentication authentication) {
        this.authentication = AuthConverter.serialize(authentication);
    }
}
