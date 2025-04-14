package masera.deviajeusersandauth.services.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
  void sendEmail(String to, String subject, String content) throws Exception;
}
