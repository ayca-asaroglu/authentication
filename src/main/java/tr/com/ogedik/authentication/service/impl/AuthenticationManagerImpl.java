package tr.com.ogedik.authentication.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tr.com.ogedik.authentication.constants.AuthenticationConstants;
import tr.com.ogedik.authentication.controller.AuthenticationController;
import tr.com.ogedik.authentication.model.AuthenticationDetails;
import tr.com.ogedik.authentication.model.AuthenticationUser;
import tr.com.ogedik.authentication.service.UserService;
import tr.com.ogedik.authentication.util.AuthenticationUtil;

import java.util.Objects;

/**
 * @author orkun.gedik
 */
@Service
@Primary
public class AuthenticationManagerImpl implements AuthenticationManager {

  private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Override
  public AuthenticationDetails authenticate(Authentication authentication) throws AuthenticationException {
    AuthenticationUser user = userService.getUserByUsername(authentication.getPrincipal().toString());

    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException(AuthenticationConstants.Exception.USER_NOT_FOUND);
    }
    if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
      throw new BadCredentialsException(AuthenticationConstants.Exception.AUTH_FAIL);
    }

    logger.info("Username and password validations are OK. Authentication is being initialized.");

    return AuthenticationDetails.builder()
        .authorities(AuthenticationUtil.getAuthorities(user.getGroups()))
        .principal(user.getUsername())
        .credentials(user.getPassword())
        .details(user)
        .isAuthenticated(true)
        .build();
  }

}
