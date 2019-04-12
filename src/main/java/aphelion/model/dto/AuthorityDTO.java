package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@ApiModel(value = "Authority", description = "Object representing an authority")
public class AuthorityDTO {
    @ApiModelProperty("Id of authority")
    private Long id;
    @ApiModelProperty("Role of authority")
    private String name;
}
