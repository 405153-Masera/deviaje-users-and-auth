package masera.deviajeusersandauth.services.interfaces;

import masera.deviajeusersandauth.dtos.post.ForgotPasswordRequest;
import masera.deviajeusersandauth.dtos.post.PasswordChangeRequest;
import masera.deviajeusersandauth.dtos.post.ResetPasswordRequest;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface PasswordService {

  MessageResponse changePassword(Integer userId, PasswordChangeRequest request);
  MessageResponse forgotPassword(ForgotPasswordRequest request);
  MessageResponse resetPassword(ResetPasswordRequest request);
}
