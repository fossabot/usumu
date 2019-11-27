package io.usumu.api.smtp;

import org.subethamail.smtp.AuthenticationHandler;
import org.subethamail.smtp.AuthenticationHandlerFactory;

import java.util.ArrayList;
import java.util.List;

public class AuthHandlerFactory implements AuthenticationHandlerFactory {
    @Override
    public List<String> getAuthenticationMechanisms() {
        List<String> result = new ArrayList<String>();
        result.add("LOGIN");
        return result;
    }

    @Override
    public AuthenticationHandler create() {
        return new AuthHandler();
    }
}
