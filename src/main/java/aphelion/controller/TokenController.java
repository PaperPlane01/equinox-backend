package aphelion.controller;

import aphelion.exception.GoogleLoginException;
import aphelion.model.domain.User;
import aphelion.model.dto.GoogleLoginDTO;
import aphelion.model.dto.RevokeTokenDTO;
import aphelion.repository.UserRepository;
import aphelion.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenStore tokenStore;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ClientDetailsService clientDetailsService;

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/oauth/revoke-token")
    public ResponseEntity<?> revokeToken(@RequestBody @Valid RevokeTokenDTO revokeTokenDTO) {
        tokenStore.removeAccessToken(new DefaultOAuth2AccessToken(revokeTokenDTO.getAccessToken()));
        if (revokeTokenDTO.getRefreshToken() != null && !revokeTokenDTO.getRefreshToken().isEmpty()) {
            tokenStore.removeRefreshToken(new DefaultOAuth2RefreshToken(revokeTokenDTO.getRefreshToken()));
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/oauth/google")
    public OAuth2AccessToken loginWithGoogle(@RequestBody @Valid GoogleLoginDTO googleLoginDTO) {
        GoogleCredential googleCredential = new GoogleCredential();
        googleCredential.setAccessToken(googleLoginDTO.getGoogleToken());
        Oauth2 oauth2 = new Oauth2
                .Builder(new NetHttpTransport(), new JacksonFactory(), googleCredential)
                .build();
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(googleLoginDTO.getClientId());

        try {
            Userinfoplus userinfoplus = oauth2.userinfo().get().execute();
            Optional<User> user = userRepository.findByGoogleId(userinfoplus.getId());
            User googleUser = user.orElseGet(() -> userService.registerGoogleUser(userinfoplus));
            HashMap<String, String> authorizationParameters = new LinkedHashMap<>();
            authorizationParameters.put(OAuth2Utils.SCOPE, convertScopeToString(clientDetails.getScope()));
            authorizationParameters.put(OAuth2Utils.CLIENT_ID, clientDetails.getClientId());
            authorizationParameters.put(OAuth2Utils.GRANT_TYPE, "implicit");

            String redirectUri = null;

            if (clientDetails.getRegisteredRedirectUri() != null
                    && !clientDetails.getRegisteredRedirectUri().isEmpty()) {
                redirectUri = (new ArrayList<>(clientDetails.getRegisteredRedirectUri())).get(0);
            }
            OAuth2Request oAuth2Request = new OAuth2Request(
                    authorizationParameters,
                    clientDetails.getClientId(),
                    googleUser.getAuthorities(),
                    true,
                    clientDetails.getScope(),
                    clientDetails.getResourceIds(),
                    redirectUri,
                    clientDetails.getAuthorizedGrantTypes(),
                    new HashMap<>()
            );
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(googleUser, null,
                    googleUser.getAuthorities());
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request,
                    usernamePasswordAuthenticationToken);
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setSupportRefreshToken(true);
            defaultTokenServices.setTokenStore(tokenStore);
            defaultTokenServices.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
            defaultTokenServices.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds());
            return defaultTokenServices.createAccessToken(oAuth2Authentication);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GoogleLoginException("Failed to log in with Google");
        }
    }

    private String convertScopeToString(Set<String> scope) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> scopeList = new ArrayList<>(scope);

        for (int index = 0; index < scopeList.size(); index++) {
            stringBuilder.append(scopeList.get(index));

            if (index != scopeList.size() - 1) {
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }
}