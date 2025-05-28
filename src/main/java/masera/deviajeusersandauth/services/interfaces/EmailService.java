package masera.deviajeusersandauth.services.interfaces;

import masera.deviajeusersandauth.entities.UserEntity;
import org.springframework.stereotype.Service;

/**
 * Interfaz para el servicio de envío de correos electrónicos.
 * Permite enviar correos electrónicos a los usuarios.
 */
@Service
public interface EmailService {

  /**
   * Envía un correo electrónico de forma asíncrona.
   *
   * @param to destinatario del correo electrónico
   * @param subject asunto del correo electrónico
   * @param content contenido del correo electrónico (puede ser HTML)
   * @throws Exception si ocurre un error al enviar el correo electrónico
   */
  void sendEmail(String to, String subject, String content) throws Exception;

  /**
   * Envía un correo electrónico de registro al usuario.
   *
   * @param user usuario al que se le enviará el correo electrónico de registro
   * @throws Exception si ocurre un error al enviar el correo electrónico
   */
  void sendRegistrationEmail(UserEntity user) throws Exception;

  /**
   * Envía un correo electrónico de registro al usuario con la contraseña en texto plano.
   *
   * @param user usuario al que se le enviará el correo electrónico de registro
   * @param plainPassword contraseña en texto plano del usuario
   * @throws Exception si ocurre un error al enviar el correo electrónico
   */
  void sendRegistrationEmail(UserEntity user, String plainPassword) throws Exception;
}
