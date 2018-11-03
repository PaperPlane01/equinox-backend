package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PersonalInformationDTO {
    private Long id;
    private Long userId;
    private String email;
    private String bio;
    private Date birthDate;
}
