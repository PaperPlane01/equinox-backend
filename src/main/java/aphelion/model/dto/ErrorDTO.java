package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private Integer status;
    private String exception;
    private String message;
    private Map<Object, Object> additionalInformation;
}
