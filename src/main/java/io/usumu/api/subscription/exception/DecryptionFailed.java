package io.usumu.api.subscription.exception;

public class DecryptionFailed extends RuntimeException {
    public DecryptionFailed(Exception e) {
        super(e);
    }
}
