package com.example.authservice.domain.oauth;

import com.example.authservice.utils.AuthConverter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Document(collection = "refreshTokens")
@Data
public class OAuthRefreshToken {

    @Id
    private String id;

    private String tokenId;
    private OAuth2RefreshToken token;
    private String authentication;

    public OAuth2Authentication getAuthentication() {
        return AuthConverter.deserialize(authentication);
    }

    public void setAuthentication(OAuth2Authentication authentication) {
        this.authentication = AuthConverter.serialize(authentication);
    }
}
