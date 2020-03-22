/**
 * © 2020 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package tr.com.ogedik.authentication.validation.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tr.com.ogedik.authentication.constants.AuthenticationConstants;
import tr.com.ogedik.authentication.exception.AuthenticationException;
import tr.com.ogedik.authentication.service.UserService;
import tr.com.ogedik.commons.models.User;
import tr.com.ogedik.commons.validation.Validator;

/**
 * @author orkun.gedik
 */
@Component
public class UserCreationValidator<T extends User> implements Validator<T> {

  @Autowired
  private UserService userService;

  @Override
  public void validate(T validationRequest) {
    if (userService.isExist(validationRequest.getUsername())) {
      throw new AuthenticationException(AuthenticationConstants.Exception.USER_EXIST);
    }
  }
}
