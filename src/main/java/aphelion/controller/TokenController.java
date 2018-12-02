package aphelion.controller;

import aphelion.model.dto.RevokeTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class TokenController {
    private final TokenStore tokenStore;

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/oauth/revoke-token")
    public ResponseEntity<?> revokeToken(@RequestBody @Valid RevokeTokenDTO revokeTokenDTO) {
        tokenStore.removeAccessToken(new DefaultOAuth2AccessToken(revokeTokenDTO.getAccessToken()));
        if (revokeTokenDTO.getRefreshToken() != null && !revokeTokenDTO.getRefreshToken().isEmpty()) {
            tokenStore.removeRefreshToken(new DefaultOAuth2RefreshToken(revokeTokenDTO.getRefreshToken()));
        }
        return ResponseEntity.noContent().build();
    }
}
