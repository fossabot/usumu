package io.usumu.api.mail;

import org.subethamail.smtp.AuthenticationHandler;
import org.subethamail.smtp.RejectException;

public class AuthHandler implements AuthenticationHandler {
    private static final String USER_IDENTITY = "User";
    private static final String PROMPT_USERNAME = "334 VXNlcm5hbWU6";
    private static final String PROMPT_PASSWORD = "334 UGFzc3dvcmQ6";

    private int pass = 0;

    @Override
    public String auth(String s) throws RejectException {
        String prompt;

        if (++pass == 1) {
            prompt = AuthHandler.PROMPT_USERNAME;
        } else if (pass == 2) {
            prompt = AuthHandler.PROMPT_PASSWORD;
        } else {
            pass = 0;
            prompt = null;
        }
        return prompt;
    }

    @Override
    public Object getIdentity() {
        return AuthHandler.USER_IDENTITY;
    }
}
