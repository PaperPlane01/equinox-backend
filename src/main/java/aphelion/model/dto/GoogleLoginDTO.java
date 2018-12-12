package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GoogleLoginDTO {
    @NotBlank
    private String googleToken;

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;
}
