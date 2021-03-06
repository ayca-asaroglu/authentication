package tr.com.ogedik.authentication.validation.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import tr.com.ogedik.authentication.exception.AuthenticationErrorType;
import tr.com.ogedik.authentication.model.AuthenticationUser;
import tr.com.ogedik.commons.abstraction.AbstractService;
import tr.com.ogedik.commons.constants.Services;
import tr.com.ogedik.commons.expection.ErrorException;
import tr.com.ogedik.commons.validator.Validator;
import tr.com.ogedik.commons.rest.request.model.AuthenticationRequest;
import tr.com.ogedik.commons.rest.request.client.HttpRestClient;
import tr.com.ogedik.commons.rest.request.client.helper.RequestURLDetails;

/**
 * @author orkun.gedik
 */
@Component
public class JiraUserValidator<T extends AuthenticationUser> extends AbstractService implements Validator<T> {

    private static final Logger logger = LogManager.getLogger(JiraUserValidator.class);

    @Override
    public void validate(T validationRequest) {
        String username = validationRequest.getUsername();
        String password = validationRequest.getPassword();

        try {
            RequestURLDetails requestURLDetails =
                    new RequestURLDetails(getRequestUrl(Services.INTEGRATION), Services.Path.JIRA_AUTH, null);
            HttpRestClient.doPost(requestURLDetails,
                    AuthenticationRequest.builder().username(username).password(password).build(), null);
        } catch (HttpServerErrorException e) {
            logger.warn("Username: {} cannot authenticated to Jira connected instance");
            throw new ErrorException(AuthenticationErrorType.USER_NOT_FOUND,
                    "User cannot be found in connect Jira instance. Please check your username and password.");
        }
    }
}
