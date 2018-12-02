package aphelion.service.impl;

import aphelion.exception.PersonalInformationNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.CreatePersonalInformationDTOToPersonalInformationMapper;
import aphelion.mapper.PersonalInformationToPersonalInformationDTOMapper;
import aphelion.model.domain.PersonalInformation;
import aphelion.model.domain.User;
import aphelion.model.dto.CreatePersonalInformationDTO;
import aphelion.model.dto.PersonalInformationDTO;
import aphelion.model.dto.UpdatePersonalInformationDTO;
import aphelion.repository.PersonalInformationRepository;
import aphelion.repository.UserRepository;
import aphelion.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import aphelion.service.PersonalInformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonalInformationServiceImpl implements PersonalInformationService {
    private final PersonalInformationRepository personalInformationRepository;
    private final UserRepository userRepository;
    private final PersonalInformationToPersonalInformationDTOMapper
            personalInformationToPersonalInformationDTOMapper;
    private final CreatePersonalInformationDTOToPersonalInformationMapper
            createPersonalInformationDTOToPersonalInformationMapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public PersonalInformationDTO save(CreatePersonalInformationDTO createPersonalInformationDTO) {
        PersonalInformation personalInformation = createPersonalInformationDTOToPersonalInformationMapper
                .map(createPersonalInformationDTO);
        personalInformation = personalInformationRepository.save(personalInformation);
        return personalInformationToPersonalInformationDTOMapper.map(personalInformation);
    }

    @Override
    public PersonalInformationDTO update(Long id,
                                         UpdatePersonalInformationDTO updatePersonalInformationDTO) {
        PersonalInformation personalInformation = findPersonalInformationById(id);
        personalInformation.setEmail(updatePersonalInformationDTO.getEmail());
        personalInformation.setBio(updatePersonalInformationDTO.getBio());
        personalInformation.setBirthDate(updatePersonalInformationDTO.getBirthDate());
        return personalInformationToPersonalInformationDTOMapper.map(personalInformation);
    }

    @Override
    public PersonalInformationDTO updateByCurrentUser(UpdatePersonalInformationDTO updatePersonalInformationDTO) {
        User user = authenticationFacade.getCurrentUser();
        PersonalInformation personalInformation = findPersonalInformationByUser(user);
        personalInformation.setBirthDate(updatePersonalInformationDTO.getBirthDate());
        personalInformation.setBio(updatePersonalInformationDTO.getBio());
        personalInformation.setEmail(updatePersonalInformationDTO.getEmail());
        return personalInformationToPersonalInformationDTOMapper.map(personalInformation);
    }

    @Override
    public void delete(Long id) {
        PersonalInformation personalInformation = findPersonalInformationById(id);
        personalInformationRepository.delete(personalInformation);
    }

    @Override
    public void deleteByUserId(Long userId) {
        User user = findUserById(userId);
        PersonalInformation personalInformation = findPersonalInformationByUser(user);
        personalInformationRepository.delete(personalInformation);
    }

    @Override
    public void deleteByCurrentUser() {
        User user = authenticationFacade.getCurrentUser();
        PersonalInformation personalInformation = findPersonalInformationByUser(user);
        personalInformationRepository.delete(personalInformation);
    }

    @Override
    public PersonalInformationDTO findById(Long id) {
        return personalInformationToPersonalInformationDTOMapper.map(findPersonalInformationById(id));
    }

    private PersonalInformation findPersonalInformationById(Long id) {
        return personalInformationRepository.findById(id)
                .filter(personalInformation -> !personalInformation.isDeleted())
                .orElseThrow(() -> new PersonalInformationNotFoundException("Personal information with given id " +
                        id + " could not be found."));
    }

    @Override
    public PersonalInformationDTO findByUserId(Long userId) {
        User user = findUserById(userId);
        PersonalInformation personalInformation = findPersonalInformationByUser(user);
        return personalInformationToPersonalInformationDTOMapper.map(personalInformation);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + userId + " could not be found."));
    }

    private PersonalInformation findPersonalInformationByUser(User user) {
        return personalInformationRepository.findByUser(user)
                .filter(personalInformation -> !personalInformation.isDeleted())
                .orElseThrow(() -> new PersonalInformationNotFoundException("No personal " +
                        "information could be found for user with given id " + user.getId()));
    }

    @Override
    public PersonalInformationDTO findByCurrentUser() {
        User user = authenticationFacade.getCurrentUser();
        PersonalInformation personalInformation = findPersonalInformationByUser(user);
        return personalInformationToPersonalInformationDTOMapper.map(personalInformation);
    }
}
