package masera.deviajeusersandauth.services.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.get.PassportDto;
import masera.deviajeusersandauth.dtos.get.UserDto;
import masera.deviajeusersandauth.dtos.post.users.PassportRequest;
import masera.deviajeusersandauth.dtos.post.users.SignupRequest;
import masera.deviajeusersandauth.dtos.post.users.UserBase;
import masera.deviajeusersandauth.dtos.post.users.UserCreateRequest;
import masera.deviajeusersandauth.dtos.put.UserPut;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import masera.deviajeusersandauth.entities.PassportEntity;
import masera.deviajeusersandauth.entities.RoleEntity;
import masera.deviajeusersandauth.entities.UserEntity;
import masera.deviajeusersandauth.entities.UserRoleEntity;
import masera.deviajeusersandauth.exceptions.EmailAlreadyExistsException;
import masera.deviajeusersandauth.exceptions.PassportAlreadyExistsException;
import masera.deviajeusersandauth.exceptions.ResourceNotFoundException;
import masera.deviajeusersandauth.exceptions.UsernameAlreadyExistsException;
import masera.deviajeusersandauth.repositories.PassportRepository;
import masera.deviajeusersandauth.repositories.RoleRepository;
import masera.deviajeusersandauth.repositories.UserRepository;
import masera.deviajeusersandauth.repositories.UserRoleRepository;
import masera.deviajeusersandauth.services.interfaces.EmailService;
import masera.deviajeusersandauth.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de usuario que maneja la lógica de negocio
 * relacionada con la creación, actualización, obtención y eliminación de usuarios.
 */
@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final UserRoleRepository userRoleRepository;

  private final PassportRepository passportRepository;

  private final PasswordEncoder passwordEncoder;

  private final ModelMapper modelMapper;

  private final EmailService emailService;

  private static final String PASSWORD_CHARS =
          "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!";

  private static final int TEMP_PASSWORD_LENGTH = 12;

  private static final SecureRandom secureRandom = new SecureRandom();

  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  /**
   * Crea un nuevo usuario desde el rol Administrador y Agente.
   *
   * @param userCreateRequest datos del usuario a crear.
   * @return el usuario creado.
   */
  @Override
  @Transactional
  public UserDto createUser(UserCreateRequest userCreateRequest) {

    validateUser(userCreateRequest);

    UserEntity userEntity = modelMapper.map(userCreateRequest, UserEntity.class);
    userEntity.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
    userEntity.setIsTemporaryPassword(true);
    userEntity.setActive(true);
    userEntity.setCreatedUser(userCreateRequest.getCreatedUser());
    userEntity.setLastUpdatedUser(userCreateRequest.getCreatedUser());
    UserEntity userSaved = userRepository.save(userEntity);

    if (userCreateRequest.getRoleIds() != null && !userCreateRequest.getRoleIds().isEmpty()) {
      for (Integer roleId : userCreateRequest.getRoleIds()) {
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol no encontrado con el id " + roleId));

        UserRoleEntity userRole = UserRoleEntity.builder()
                .user(userEntity)
                .role(role)
                .createdUser(userCreateRequest.getCreatedUser())
                .build();

        userRoleRepository.save(userRole);
      }
    }


    if (userCreateRequest.getPassport() != null) {
      PassportEntity passport = modelMapper.map(
              userCreateRequest.getPassport(), PassportEntity.class);
      passport.setUser(userSaved);
      passport.setCreatedUser(userCreateRequest.getCreatedUser());
      passportRepository.save(passport);
    }

    try {
      emailService.sendRegistrationEmail(userEntity, userCreateRequest.getPassword());
      logger.info("Email de notificación enviado al usuario creado "
              + "por administrador: {}", userEntity.getEmail());
    } catch (Exception e) {
      logger.error("Error al enviar email de notificación: {}", e.getMessage(), e);
    }

    return modelMapper.map(userSaved, UserDto.class);
  }

  @Override
  @Transactional
  public MessageResponse registerUser(SignupRequest signupRequest) {
    validateUser(signupRequest);

    UserEntity userEntity = modelMapper.map(signupRequest, UserEntity.class);
    userEntity.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    userEntity.setIsTemporaryPassword(false);
    userEntity.setActive(true);

    userEntity.setId(null);
    userEntity = userRepository.save(userEntity);

    RoleEntity roleEntity = roleRepository.findByDescription("CLIENTE")
            .orElseThrow(() -> new RuntimeException("Error: Rol CLIENTE no se encontró"));


    // Assign roles to user
    UserRoleEntity userRole = UserRoleEntity.builder()
            .user(userEntity)
            .role(roleEntity)
            .build();
    userRoleRepository.save(userRole);

    if (signupRequest.getPassport() != null) {
      PassportEntity passport = modelMapper.map(signupRequest.getPassport(), PassportEntity.class);
      passport.setUser(userEntity);
      passportRepository.save(passport);
    }

    // Enviar email de confirmación
    try {
      emailService.sendRegistrationEmail(userEntity);
      logger.info("Email de confirmación enviado exitosamente a: {}", userEntity.getEmail());
    } catch (Exception e) {
      // Log el error pero no interrumpir el flujo de registro
      logger.error("Error al enviar email de confirmación: {}", e.getMessage(), e);
    }

    return new MessageResponse("Usuario registrado exitosamente", true);
  }

  @Override
  @Transactional
  public UserDto updateUser(Integer id, UserPut request) {
    logger.info("Actualizando usuario con id: {}", id);

    UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado con el id: " + id));

    if (request.getUsername() != null
            && !request.getUsername().equalsIgnoreCase(user.getUsername())) {
      if (userRepository.existsByUsername(request.getUsername())) {
        throw new UsernameAlreadyExistsException(
                "Este nombre de usuario ya está en uso");
      }
      user.setUsername(request.getUsername());
    }

    if (request.getEmail() != null
            && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
      if (userRepository.existsByEmail(request.getEmail())) {
        throw new EmailAlreadyExistsException(
                "Este correo electrónico ya está registrado");
      }
      user.setEmail(request.getEmail());
    }

    if (request.getFirstName() != null) {
      user.setFirstName(request.getFirstName());
    }

    if (request.getLastName() != null) {
      user.setLastName(request.getLastName());
    }

    if (request.getGender() != null) {
      user.setGender(request.getGender());
    }

    if (request.getCountryCallingCode() != null) {
      user.setCountryCallingCode(request.getCountryCallingCode());
    }

    if (request.getPhone() != null) {
      user.setPhone(request.getPhone());
    }

    if (request.getBirthDate() != null) {
      user.setBirthDate(request.getBirthDate());
    }

    // Actualizar o crear pasaporte
    if (request.getPassport() != null) {
      PassportRequest passportRequest = request.getPassport();

      Optional<PassportEntity> existingPassport = passportRepository.findByUser(user);

      if (existingPassport.isPresent()) {
        // Actualizar pasaporte existente
        PassportEntity passport = existingPassport.get();

        // Validar si el número de pasaporte cambió y si ya existe
        if (passportRequest.getPassportNumber() != null
                && !passportRequest.getPassportNumber()
                .equalsIgnoreCase(passport.getPassportNumber())) {
          if (passportRepository.existsByPassportNumber(
                  passportRequest.getPassportNumber())) {
            throw new PassportAlreadyExistsException(
                    "Este número de pasaporte ya está registrado");
          }
          passport.setPassportNumber(passportRequest.getPassportNumber());
        }

        if (passportRequest.getExpiryDate() != null) {
          passport.setExpiryDate(passportRequest.getExpiryDate());
        }

        if (passportRequest.getIssuanceCountry() != null) {
          passport.setIssuanceCountry(passportRequest.getIssuanceCountry());
        }

        if (passportRequest.getNationality() != null) {
          passport.setNationality(passportRequest.getNationality());
        }

        passportRepository.save(passport);
      } else {
        // Crear nuevo pasaporte
        if (passportRequest.getPassportNumber() != null) {
          if (passportRepository.existsByPassportNumber(
                  passportRequest.getPassportNumber())) {
            throw new PassportAlreadyExistsException(
                    "Este número de pasaporte ya está registrado");
          }
        }

        PassportEntity newPassport = modelMapper.map(passportRequest, PassportEntity.class);
        newPassport.setUser(user);
        passportRepository.save(newPassport);
      }
    }

    userRepository.save(user);

    logger.info("Usuario {} actualizado exitosamente", user.getUsername());
    return mapUserToUserResponse(user);
  }

  /**
   * Metodo que contiene las validaciones necesarias para crear un usuario.
   *
   * @param userBase usuario a validar.
   */
  private void validateUser(UserBase userBase) {
    validateUsername(userBase.getUsername());
    validateEmail(userBase.getEmail());

    // Validar pasaporte si se proporciona
    if (userBase.getPassport() != null && userBase.getPassport().getPassportNumber() != null) {
      validatePassport(userBase.getPassport().getPassportNumber());
    }
  }

  /**
   * Valida si el username ya existe en la base de datos.
   *
   * @param username nombre de usuario a validar.
   */
  private void validateUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyExistsException("Este nombre de usuario ya está en uso");
    }
  }

  /**
   * Valida si el email ya existe en la base de datos.
   *
   * @param email email a validar.
   */
  private void validateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new EmailAlreadyExistsException("Este correo electrónico ya está registrado");
    }
  }

  /**
   * Valida si el número de pasaporte ya existe en la base de datos.
   *
   * @param passportNumber número de pasaporte a validar.
   */
  private void validatePassport(String passportNumber) {
    if (passportRepository.existsByPassportNumber(passportNumber)) {
      throw new PassportAlreadyExistsException("Este número de pasaporte ya está registrado");
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
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado con el id: " + id));

    return mapUserToUserResponse(user);
  }

  @Override
  public UserDto getUserByUsername(String username) {
    UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado con username: " + username));

    return mapUserToUserResponse(userEntity);
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
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado con el id: " + id));

    user.setActive(true);
    userRepository.save(user);
  }

  @Override
  public void deactivateUser(Integer id) {
    UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado con id: " + id));

    user.setActive(false);
    userRepository.save(user);
  }

  /**
   * Resetea la contraseña de un usuario por parte de un administrador.
   * Genera una contraseña temporal aleatoria y la envía por email.
   */
  @Override
  @Transactional
  public MessageResponse adminResetPassword(Integer id) {
    logger.info("🔄 Iniciando reseteo de contraseña por admin para usuario con id: {}", id);

    UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado con el id: " + id));

    logger.info("👤 Usuario encontrado: {} ({})", user.getUsername(), user.getEmail());

    // Generar contraseña temporal aleatoria
    String temporaryPassword = generateSecureTemporaryPassword();

    logger.info("🔑 Contraseña temporal generada para usuario: {}", user.getUsername());

    // Actualizar contraseña y marcar como temporal
    user.setPassword(passwordEncoder.encode(temporaryPassword));
    user.setIsTemporaryPassword(true);
    user.setLastUpdatedDatetime(LocalDateTime.now());

    userRepository.save(user);

    logger.info("💾 Usuario {} actualizado con contraseña temporal", user.getUsername());

    // Enviar email con contraseña temporal
    try {
      emailService.sendPasswordResetByAdminEmail(user, temporaryPassword);
      logger.info("✅ Email de reseteo enviado exitosamente a: {}", user.getEmail());
    } catch (Exception e) {
      logger.error("❌ Error al enviar email de reseteo: {}", e.getMessage(), e);
      // No lanzar excepción para que el reseteo se complete aunque falle el email
    }

    return new MessageResponse(
            "Contraseña reseteada exitosamente. Se envió un email al usuario con la nueva contraseña temporal.",
            true
    );
  }

  /**
   * Genera una contraseña temporal aleatoria segura.
   * Garantiza que contenga al menos:
   * - 1 letra mayúscula
   * - 1 letra minúscula
   * - 1 número
   * - 1 carácter especial
   *
   * @return Contraseña temporal de 12 caracteres.
   */
  private String generateSecureTemporaryPassword() {
    StringBuilder password = new StringBuilder(TEMP_PASSWORD_LENGTH);

    // Asegurar al menos un carácter de cada tipo
    password.append(getRandomChar("ABCDEFGHIJKLMNOPQRSTUVWXYZ")); // Mayúscula
    password.append(getRandomChar("abcdefghijklmnopqrstuvwxyz")); // Minúscula
    password.append(getRandomChar("0123456789")); // Número
    password.append(getRandomChar("@#$%&*!")); // Especial

    // Completar el resto con caracteres aleatorios
    for (int i = 4; i < TEMP_PASSWORD_LENGTH; i++) {
      password.append(PASSWORD_CHARS.charAt(secureRandom.nextInt(PASSWORD_CHARS.length())));
    }

    // Mezclar los caracteres para que no sean predecibles
    return shuffleString(password.toString());
  }

  /**
   * Obtiene un carácter aleatorio de un conjunto de caracteres.
   */
  private char getRandomChar(String chars) {
    return chars.charAt(secureRandom.nextInt(chars.length()));
  }

  /**
   * Mezcla los caracteres de un string de forma aleatoria.
   */
  private String shuffleString(String input) {
    char[] characters = input.toCharArray();
    for (int i = characters.length - 1; i > 0; i--) {
      int j = secureRandom.nextInt(i + 1);
      char temp = characters[i];
      characters[i] = characters[j];
      characters[j] = temp;
    }
    return new String(characters);
  }

  private UserDto mapUserToUserResponse(UserEntity user) {
    UserDto.UserDtoBuilder builder = UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .gender(user.getGender())
            .phone(user.getPhone())
            .countryCallingCode(user.getCountryCallingCode())
            .birthDate(user.getBirthDate())
            .active(user.getActive())
            .avatarUrl(user.getAvatarUrl());

    // Add roles
    List<String> roles = userRoleRepository.findByUserIdWithRole(user.getId()).stream()
            .map(userRole -> userRole.getRole().getDescription())
            .collect(Collectors.toList());

    builder.roles(roles);

    Optional<PassportEntity> passport = passportRepository.findByUser(user);
    if (passport.isPresent()) {
      PassportDto passportDto = modelMapper.map(passport.get(), PassportDto.class);
      builder.passport(passportDto);
    }

    return builder.build();
  }
}
