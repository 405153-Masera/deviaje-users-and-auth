package masera.deviajeusersandauth.services.impl;

import masera.deviajeusersandauth.services.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Value("${deviaje.app.email.enabled:false}")
  private boolean emailEnabled;

  @Override
  public void sendEmail(String to, String subject, String content) throws Exception {
    if (!emailEnabled) {
      logger.info("Email sending is disabled. Would have sent email to {} with subject: {}", to, subject);
      logger.debug("Email content: {}", content);
      return;
    }

    // Implementar el envío real de correos electrónicos aquí
    // Puedes usar JavaMail o Spring Mail
    logger.info("Sending email to {} with subject: {}", to, subject);

    // Simulación de envío
    try {
      // Aquí iría la lógica real de envío
      Thread.sleep(100); // Simular tiempo de envío
      logger.info("Email sent successfully");
    } catch (Exception e) {
      logger.error("Failed to send email: {}", e.getMessage());
      throw new Exception("Failed to send email: " + e.getMessage());
    }
  }
}
