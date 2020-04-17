/**
 * © 2020 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package tr.com.ogedik.authentication.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tr.com.ogedik.authentication.constants.AuthenticationConstants;
import tr.com.ogedik.commons.models.User;
import tr.com.ogedik.commons.response.AbstractResponse;
import tr.com.ogedik.authentication.service.UserService;

/**
 * @author orkun.gedik
 */
@RestController
public class UserController {

  private static final Logger logger = LogManager.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  @GetMapping(AuthenticationConstants.Paths.USERS)
  public AbstractResponse getUsers() {
    logger.info("The request has been received to return all users.");
    return AbstractResponse.build(userService.getAllUsers());
  }

  @GetMapping(AuthenticationConstants.Paths.USERS + AuthenticationConstants.Paths.IDENTIFIER)
  public AbstractResponse getUser(@PathVariable String identifier) {
    logger.info("The request has been received to return an user with id {}.", identifier);
    return AbstractResponse.build(userService.getUserByUsername(identifier));
  }

  @PostMapping(AuthenticationConstants.Paths.USERS)
  public AbstractResponse createUser(@RequestBody User requestUser) {
    logger.info("The request has been received to create an user.");
    return AbstractResponse.build(userService.create(requestUser));
  }

  @PutMapping(AuthenticationConstants.Paths.USERS)
  public AbstractResponse updateUser(@RequestBody User requestUser) {
    logger.info("The request has been received to update an user.");
    return AbstractResponse.build(userService.update(requestUser));
  }

  @DeleteMapping(AuthenticationConstants.Paths.USERS + AuthenticationConstants.Paths.IDENTIFIER)
  public AbstractResponse deleteUser(@PathVariable String identifier) {
    logger.info("The request has been received to delete an user with id {}.", identifier);
    userService.delete(identifier);

    return AbstractResponse.OK();
  }

}
