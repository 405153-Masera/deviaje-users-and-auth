package masera.deviajeusersandauth.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.entities.UserEntity;
import masera.deviajeusersandauth.services.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de envío de correos electrónicos.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Value("${deviaje.app.email.enabled:true}")
  private boolean emailEnabled;

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Value("${deviaje.app.frontend-url}")
  private String frontendUrl;

  private final JavaMailSender mailSender;

  /**
   * Envía un email de forma asíncrona.
   *
   * @param to destinatario
   * @param subject asunto
   * @param content contenido (puede ser HTML)
   * @throws Exception si ocurre un error al enviar el email
   */
  @Override
  @Async
  public void sendEmail(String to, String subject, String content) throws Exception {
    if (!emailEnabled) {
      logger.info("Email sending is disabled."
              + " Would have sent email to {} with subject: {}", to, subject);
      logger.debug("Email content: {}", content);
      return;
    }

    // Implementar el envío real de correos electrónicos aquí
    // Puedes usar JavaMail o Spring Mail
    logger.info("Sending email to {} with subject: {}", to, subject);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom(fromEmail);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(content, true); // true indica que el contenido es HTML

      mailSender.send(message);
      logger.info("Email enviado exitosamente a: {}", to);
    } catch (MessagingException e) {
      logger.error("Error al enviar email: {}", e.getMessage(), e);
      throw new Exception("Error al enviar email: " + e.getMessage());
    }
  }

  /**
   * Envía un email de confirmación al usuario registrado.
   *
   * @param user usuario registrado
   * @throws Exception si ocurre un error al enviar el email
   */
  @Override
  public void sendRegistrationEmail(UserEntity user) throws Exception {
    String to = user.getEmail();
    String subject = "Bienvenido a DeViaje - Registro Exitoso";

    String content =
            "<html>"
                    + "<head>"
                    + "<style>"
                    + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }"
                    + ".container { max-width: 600px; margin: 0 auto; }"
                    + ".header { background-color: #8B5CF6; padding: 20px; text-align: center; }"
                    + ".header h1 { color: white; margin: 0; }"
                    + ".content { padding: 20px; border: 1px solid #ddd; border-top: none; }"
                    + ".button { display: inline-block; background-color: #8B5CF6; "
                    + "color: white; padding: 10px 20px; "
                    + "text-decoration: none; border-radius: 5px; margin-top: 20px; }"
                    + ".footer { margin-top: 30px; font-size: 12px; color: #666; "
                    + "text-align: center; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<div class='header'>"
                    + "<h1>DeViaje</h1>"
                    + "</div>"
                    + "<div class='content'>"
                    + "<h2>¡Bienvenido/a " + (user.getFirstName() != null
                                    ? user.getFirstName() : user.getUsername())
                    + "!</h2>"
                    + "<p>Tu cuenta ha sido creada exitosamente en DeViaje.</p>"
                    + "<p>Ahora puedes acceder a nuestra plataforma con los siguientes datos:</p>"
                    + "<p><strong>Usuario:</strong> " + user.getUsername() + "<br>"
                    + "<strong>Email:</strong> " + user.getEmail() + "</p>"
                    + "<p>Ya puedes comenzar a explorar nuestras opciones de viaje y "
                    + "realizar reservas de vuelos, hoteles y paquetes turísticos.</p>"
                    + "<div style='text-align: center;'>"
                    + "<a href='" + frontendUrl + "/user/login"
                    + "' class='button'>Iniciar Sesión</a>"
                    + "</div>"
                    + "<p>Si tienes alguna pregunta o necesitas asistencia, "
                    + "no dudes en contactar a nuestro equipo de soporte.</p>"
                    + "<p>¡Gracias por elegirnos para tus próximas aventuras!</p>"
                    + "<div class='footer'>"
                    + "<p>Este es un correo automático, por favor no responder.<br>"
                    + "© " + java.time.Year.now().getValue()
                    + " DeViaje. Todos los derechos reservados.</p>"
                    + "</div>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

    sendEmail(to, subject, content);
  }

  @Override
  public void sendRegistrationEmail(UserEntity user, String plainPassword) throws Exception {
    String to = user.getEmail();
    String subject = "Bienvenido a DeViaje - Cuenta Creada";

    String content =
            "<html>"
                    + "<head>"
                    + "<style>"
                    + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }"
                    + ".container { max-width: 600px; margin: 0 auto; }"
                    + ".header { background-color: #8B5CF6; padding: 20px; text-align: center; }"
                    + ".header h1 { color: white; margin: 0; }"
                    + ".content { padding: 20px; border: 1px solid #ddd; border-top: none; }"
                    + ".button { display: inline-block; background-color: #8B5CF6; "
                    + "color: white; padding: 10px 20px; "
                    + "text-decoration: none; border-radius: 5px; margin-top: 20px; }"
                    + ".footer { margin-top: 30px; font-size: 12px; "
                    + "color: #666; text-align: center; }"
                    + ".alert { background-color: #f8d7da; border: 1px solid #f5c6cb; "
                    + "color: #721c24; padding: 10px; border-radius: 5px; margin: 15px 0; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<div class='header'>"
                    + "<h1>DeViaje</h1>"
                    + "</div>"
                    + "<div class='content'>"
                    + "<h2>¡Bienvenido/a " + (user.getFirstName() != null
                            ? user.getFirstName() : user.getUsername()) + "!</h2>"
                    + "<p>Un administrador ha creado una cuenta para ti en DeViaje.</p>"
                    + "<p>Tus credenciales de acceso son:</p>"
                    + "<p><strong>Usuario:</strong> " + user.getUsername() + "<br>"
                    + "<strong>Email:</strong> " + user.getEmail() + "<br>"
                    + "<strong>Contraseña temporal:</strong> " + plainPassword + "</p>"
                    + "<div class='alert'>"
                    + "<strong>Importante:</strong> Por seguridad, te recomendamos cambiar "
                    + "tu contraseña temporal la primera vez que inicies sesión."
                    + "</div>"
                    + "<div style='text-align: center;'>"
                    + "<a href='" + frontendUrl + "/user/login"
                    + "' class='button'>Iniciar Sesión</a>"
                    + "</div>"
                    + "<p>Si tienes alguna pregunta o necesitas asistencia, no "
                    + "dudes en contactar a nuestro equipo de soporte.</p>"
                    + "<div class='footer'>"
                    + "<p>Este es un correo automático, por favor no responder.<br>"
                    + "© " + java.time.Year.now().getValue() + " DeViaje. "
                    + "Todos los derechos reservados.</p>"
                    + "</div>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

    sendEmail(to, subject, content);
  }

  @Override
  public void sendPasswordResetByAdminEmail(UserEntity user, String temporaryPassword) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom(fromEmail);
      helper.setTo(user.getEmail());
      helper.setSubject("Tu contraseña de DeViaje ha sido reseteada");

      String htmlContent = String.format("""
      <html>
      <head>
        <style>
          body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
          .container { max-width: 600px; margin: 0 auto; padding: 20px; }
          .header { 
            background-color: #FF9800; 
            padding: 20px; 
            text-align: center; 
            border-radius: 10px 10px 0 0;
          }
          .header h1 { color: white; margin: 0; }
          .content { 
            padding: 30px; 
            border: 1px solid #ddd; 
            border-top: none; 
            background-color: #fff;
            border-radius: 0 0 10px 10px;
          }
          .password-box { 
            background-color: #f5f5f5; 
            padding: 20px; 
            border-radius: 5px; 
            margin: 20px 0;
            border-left: 4px solid #FF9800;
          }
          .password-box p { margin: 5px 0; font-size: 14px; }
          .password-value {
            font-size: 20px;
            font-weight: bold;
            color: #FF9800;
            font-family: 'Courier New', monospace;
            letter-spacing: 1px;
          }
          .warning-box { 
            background-color: #fff3cd; 
            border-left: 4px solid #ffc107; 
            padding: 15px; 
            margin: 20px 0;
            border-radius: 5px;
          }
          .warning-box p { margin: 0; }
          .button { 
            display: inline-block; 
            background-color: #FF9800; 
            color: white; 
            padding: 12px 30px; 
            text-decoration: none; 
            border-radius: 5px;
            font-weight: bold;
          }
          .footer { 
            margin-top: 30px; 
            padding-top: 20px;
            border-top: 1px solid #ddd;
            color: #777; 
            font-size: 12px;
            text-align: center;
          }
        </style>
      </head>
      <body>
        <div class="container">
          <div class="header">
            <h1>DeViaje</h1>
          </div>
          <div class="content">
            <h2 style="color: #FF9800; margin-top: 0;">Contraseña Reseteada</h2>
            
            <p>Hola <strong>%s</strong>,</p>
            
            <p>Un administrador ha reseteado tu contraseña en el sistema DeViaje por motivos de seguridad.</p>
            
            <div class="password-box">
              <p><strong>Usuario:</strong> %s</p>
              <p><strong>Nueva contraseña temporal:</strong></p>
              <p class="password-value">%s</p>
            </div>
            
            <div class="warning-box">
              <p><strong>IMPORTANTE:</strong> Debes cambiar esta contraseña temporal en tu próximo inicio de sesión. Por seguridad, no podrás acceder al sistema hasta que establezcas una contraseña nueva.</p>
            </div>
            
            <p style="text-align: center; margin: 30px 0;">
              <a href="%s/user/login" class="button">Iniciar Sesión Ahora</a>
            </p>
            
            <p style="margin-top: 30px; font-size: 13px; color: #666;">
              <strong>Consejos de seguridad:</strong><br>
              • Cambia tu contraseña inmediatamente después de iniciar sesión<br>
              • Usa una contraseña única y segura<br>
              • No compartas tu contraseña con nadie
            </p>
          </div>
          <div class="footer">
            <p>Este es un correo automático, por favor no respondas a este mensaje.<br>
            © %d DeViaje. Todos los derechos reservados.</p>
          </div>
        </div>
      </body>
      </html>
      """,
              user.getFirstName() != null ? user.getFirstName() : user.getUsername(),
              user.getUsername(),
              temporaryPassword,
              frontendUrl,
              java.time.Year.now().getValue()
      );

      helper.setText(htmlContent, true);
      mailSender.send(message);

      logger.info("✅ Email de reseteo de contraseña enviado a: {}", user.getEmail());
    } catch (Exception e) {
      logger.error("❌ Error al enviar email de reseteo: {}", e.getMessage(), e);
      throw new RuntimeException("Error al enviar email de reseteo de contraseña", e);
    }
  }
}
