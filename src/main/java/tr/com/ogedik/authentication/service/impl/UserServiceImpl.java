/**
 * © 2020 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package tr.com.ogedik.authentication.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import tr.com.ogedik.authentication.constants.AuthenticationConstants;
import tr.com.ogedik.authentication.entity.UserEntity;
import tr.com.ogedik.authentication.exception.AuthenticationException;
import tr.com.ogedik.authentication.mapper.UserMapper;
import tr.com.ogedik.commons.models.User;
import tr.com.ogedik.authentication.repository.UserRepository;
import tr.com.ogedik.authentication.service.UserService;
import tr.com.ogedik.authentication.validation.user.UserValidationFacade;
import tr.com.ogedik.commons.utils.ResourceIdGenerator;

/**
 * @author orkun.gedik
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository repository;
  @Autowired
  private UserValidationFacade validationFacade;
  @Autowired
  private UserMapper mapper;

  @Override
  public boolean isExist(String username) {
    return repository.existsByUsername(username);
  }

  @Override
  public User getUserByUsername(String username) {
    UserEntity entity = repository.findByUsername(username);
    return ObjectUtils.isEmpty(entity) ? null : mapper.convert(entity);
  }

  @Override
  public List<User> getAllUsers() {
    return mapper.convertToBoList(repository.findAll());
  }

  @Override
  public User create(User user) {
    user.setResourceId(ResourceIdGenerator.generate());
    validationFacade.validateCreate(user);
    user.setEnrolmentDate(LocalDateTime.now());

    UserEntity savedEntity = repository.save(mapper.convert(user));

    return mapper.convert(savedEntity);
  }

  @Override
  public User update(User user) {
    validationFacade.validateUpdate(user);
    UserEntity entity = repository.save(mapper.convert(user));

    return mapper.convert(entity);
  }

  @Override
  public void delete(String username) {
    if (BooleanUtils.isFalse(repository.existsByUsername(username))) {
      throw new AuthenticationException(AuthenticationConstants.Exception.USER_NOT_FOUND);
    }

    repository.deleteByUsername(username);
  }
}