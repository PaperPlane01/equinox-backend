package aphelion.service;

import aphelion.model.dto.CreatePersonalInformationDTO;
import aphelion.model.dto.PersonalInformationDTO;
import aphelion.model.dto.UpdatePersonalInformationDTO;

public interface PersonalInformationService {
    PersonalInformationDTO save(CreatePersonalInformationDTO createPersonalInformationDTO);
    PersonalInformationDTO update(Long id, UpdatePersonalInformationDTO updatePersonalInformationDTO);
    PersonalInformationDTO updateByCurrentUser(UpdatePersonalInformationDTO updatePersonalInformationDTO);
    void delete(Long id);
    void deleteByUserId(Long userId);
    void deleteByCurrentUser();
    PersonalInformationDTO findById(Long id);
    PersonalInformationDTO findByUserId(Long userId);
    PersonalInformationDTO findByCurrentUser();
}
