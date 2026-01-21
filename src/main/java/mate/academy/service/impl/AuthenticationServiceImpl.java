package mate.academy.service.impl;

import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userByEmail = userService.findByEmail(email);

        if (userByEmail.isEmpty() || !userByEmail.get().getPassword()
                .equals(HashUtil.hashPassword(password, userByEmail.get().getSalt()))) {
            throw new AuthenticationException("User with email " + email
                    + " doesn't exist or password is incorrect", null);
        }

        return userByEmail.get();
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("User with such email "
                    + email + " already exist", null);
        }

        return userService.add(new User(email, password));
    }
}
