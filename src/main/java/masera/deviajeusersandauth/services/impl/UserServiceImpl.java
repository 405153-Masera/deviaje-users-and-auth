package masera.deviajeusersandauth.services.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.get.UserDto;
import masera.deviajeusersandauth.dtos.post.users.SignupRequest;
import masera.deviajeusersandauth.dtos.post.users.UserBase;
import masera.deviajeusersandauth.dtos.post.users.UserCreateRequest;
import masera.deviajeusersandauth.dtos.put.UserPut;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import masera.deviajeusersandauth.entities.DniTypeEntity;
import masera.deviajeusersandauth.entities.RoleEntity;
import masera.deviajeusersandauth.entities.UserEntity;
import masera.deviajeusersandauth.entities.UserRoleEntity;
import masera.deviajeusersandauth.exceptions.EmailAlreadyExistsException;
import masera.deviajeusersandauth.exceptions.ResourceNotFoundException;
import masera.deviajeusersandauth.exceptions.UsernameAlreadyExistsException;
import masera.deviajeusersandauth.repositories.DniTypeRepository;
import masera.deviajeusersandauth.repositories.RoleRepository;
import masera.deviajeusersandauth.repositories.UserRepository;
import masera.deviajeusersandauth.repositories.UserRoleRepository;
import masera.deviajeusersandauth.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final UserRoleRepository userRoleRepository;

  private final DniTypeRepository dniTypeRepository;

  private PasswordEncoder passwordEncoder;

  private final ModelMapper modelMapper;


  /**
   * Crea un nuevo usuario desde el rol SuperAdmin y Gerente.
   *
   * @param userCreateRequest datos del usuario a crear.
   * @return el usuario creado.
   */
  @Override
  @Transactional
  public UserDto createUser(UserCreateRequest userCreateRequest) {

    validateUser(userCreateRequest);

    // Creación del usuario
    UserEntity userEntity = modelMapper.map(userCreateRequest, UserEntity.class);
    userEntity.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
    userEntity.setActive(true);
    userEntity.setCreatedUser(userCreateRequest.getCreatedUser());
    userEntity.setLastUpdatedUser(userCreateRequest.getCreatedUser());

    // seteo el tipo de dni
    if (userCreateRequest.getDniTypeId() != null) {
      DniTypeEntity dniType = dniTypeRepository.findById(userCreateRequest.getDniTypeId())
              .orElseThrow(() -> new ResourceNotFoundException("DniType not found with id: "
                      + userCreateRequest.getDniTypeId()));
      userEntity.setDniType(dniType);
    }


    if (userCreateRequest.getRoleIds() != null && !userCreateRequest.getRoleIds().isEmpty()) {
      for (Integer roleId : userCreateRequest.getRoleIds()) {
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        UserRoleEntity userRole = UserRoleEntity.builder()
                .user(userEntity)
                .role(role)
                .createdUser(userCreateRequest.getCreatedUser())
                .build();

        userEntity.getUserRoles().add(userRole);
      }
    }

    userRepository.save(userEntity);
    return getUserById(userEntity.getId());
  }

  @Override
  @Transactional
  public MessageResponse registerUser(SignupRequest signupRequest) {
    if (userRepository.existsByUsername(signupRequest.getUsername())) {
      throw new UsernameAlreadyExistsException("Error: Username is already taken!");
    }

    if (userRepository.existsByEmail(signupRequest.getEmail())) {
      throw new EmailAlreadyExistsException("Error: Email is already in use!");
    }

    UserEntity userEntity = modelMapper.map(signupRequest, UserEntity.class);
    userEntity.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    userEntity.setActive(true);

    if (signupRequest.getDniTypeId() != null) {
      DniTypeEntity dniType = dniTypeRepository.findById(signupRequest.getDniTypeId())
              .orElse(null);
      userEntity.setDniType(dniType);
    }

    Set<RoleEntity> roles = new HashSet<>();
    RoleEntity clienteRole = roleRepository.findByDescription("CLIENTE")
            .orElseThrow(() -> new RuntimeException("Error: Role CLIENTE is not found."));
    roles.add(clienteRole);


    // Assign roles to user
    for (RoleEntity role : roles) {
      UserRoleEntity userRole = UserRoleEntity.builder()
              .user(userEntity)
              .role(role)
              .build();
      userRoleRepository.save(userRole);
      userEntity.getUserRoles().add(userRole);
    }

    userRepository.save(userEntity);
    return new MessageResponse("User registered successfully!");
  }

  @Override
  public UserDto updateUser(Integer id, UserPut userCreateRequest) {
    return null;
  }

  /**
   * Metodo que contiene las validaciones necesarias para crear un usuario.
   *
   * @param userBase usuario a validar.
   */
  private void validateUser(UserBase userBase) {
    validateUsername(userBase.getUsername());
    validateEmail(userBase.getEmail());
  }

  /**
   * Valida si el username ya existe en la base de datos.
   *
   * @param username nombre de usuario a validar.
   */
  private void validateUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyExistsException("Username is already taken");
    }
  }

  /**
   * Valida si el email ya existe en la base de datos.
   *
   * @param email email a validar.
   */
  private void validateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new EmailAlreadyExistsException("Email is already in use");
    }
  }

  /*@Override
  @Transactional
  public UserResponse updateUser(Integer id, UserUpdateRequest request) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    // Check if username is being updated and is unique
    if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
      if (userRepository.existsByUsername(request.getUsername())) {
        throw new UsernameAlreadyExistsException("Username is already taken");
      }
      user.setUsername(request.getUsername());
    }

    // Check if email is being updated and is unique
    if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
      if (userRepository.existsByEmail(request.getEmail())) {
        throw new EmailAlreadyExistsException("Email is already in use");
      }
      user.setEmail(request.getEmail());
    }

    // Update password if provided
    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    // Update other fields if provided
    if (request.getFirstName() != null) {
      user.setFirstName(request.getFirstName());
    }

    if (request.getLastName() != null) {
      user.setLastName(request.getLastName());
    }

    if (request.getPhone() != null) {
      user.setPhone(request.getPhone());
    }

    if (request.getBirthDate() != null) {
      user.setBirthDate(request.getBirthDate());
    }

    if (request.getDni() != null) {
      user.setDni(request.getDni());
    }

    if (request.getDniTypeId() != null) {
      DniType dniType = dniTypeRepository.findById(request.getDniTypeId())
              .orElseThrow(() -> new ResourceNotFoundException("DniType not found with id: " + request.getDniTypeId()));
      user.setDniType(dniType);
    }

    if (request.getAvatarUrl() != null) {
      user.setAvatarUrl(request.getAvatarUrl());
    }

    if (request.getActive() != null) {
      user.setActive(request.getActive());
    }

    user.setLastUpdatedUser(request.getLastUpdatedUser());
    user.setLastUpdatedDatetime(LocalDateTime.now());

    userRepository.save(user);

    // Update roles if provided
    if (request.getRoleIds() != null) {
      // Remove existing roles
      List<UserRole> existingRoles = userRoleRepository.findByUserId(user.getId());
      userRoleRepository.deleteAll(existingRoles);

      // Add new roles
      for (Integer roleId : request.getRoleIds()) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .createdUser(request.getLastUpdatedUser())
                .build();

        userRoleRepository.save(userRole);
      }
    }

    // Update membership if provided
    if (request.getMembershipId() != null) {
      UserMembership userMembership = userMembershipRepository.findByUserId(user.getId())
              .orElse(null);

      Membership membership = membershipRepository.findById(request.getMembershipId())
              .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + request.getMembershipId()));

      if (userMembership == null) {
        // Create new membership if not exists
        userMembership = UserMembership.builder()
                .user(user)
                .membership(membership)
                .currentPoints(0)
                .startDate(LocalDateTime.now())
                .createdUser(request.getLastUpdatedUser())
                .build();
      } else {
        // Update existing membership
        userMembership.setMembership(membership);
        userMembership.setLastUpdatedUser(request.getLastUpdatedUser());
        userMembership.setLastUpdatedDatetime(LocalDateTime.now());
      }

      userMembershipRepository.save(userMembership);
    }

    return getUserById(id);
  }*/

  @Override
  public UserDto getUserById(Integer id) {
    UserEntity user = userRepository.findByIdWithRoles(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    return mapUserToUserResponse(user);
  }

  @Override
  public List<UserDto> getAllUsers() {
    return userRepository.findAll().stream()
            .map(this::mapUserToUserResponse)
            .collect(Collectors.toList());
  }

  @Override
  public List<UserDto> getUsersByRole(String role) {
    return userRepository.findAllByRoleName(role).stream()
            .map(this::mapUserToUserResponse)
            .collect(Collectors.toList());
  }

  @Override
  public void activateUser(Integer id) {
    UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    user.setActive(true);
    userRepository.save(user);
  }

  @Override
  public void deactivateUser(Integer id) {
    UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    user.setActive(false);
    userRepository.save(user);
  }

  private UserDto mapUserToUserResponse(UserEntity user) {
    UserDto.UserDtoBuilder builder = UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phone(user.getPhone())
            .birthDate(user.getBirthDate())
            .dni(user.getDni())
            .active(user.getActive())
            .avatarUrl(user.getAvatarUrl());

    // Add DniType if exists
    if (user.getDniType() != null) {
      builder.dniType(user.getDniType().getDescription());
    }

    // Add roles
    List<String> roles = userRoleRepository.findByUserIdWithRole(user.getId()).stream()
            .map(userRole -> userRole.getRole().getDescription())
            .collect(Collectors.toList());

    builder.roles(roles);

    return builder.build();
  }
}
