package dev.erikmota.usermanager.exception;

import dev.erikmota.usermanager.exception.message.MessageEnum;

public class ParameterRequiredException extends RuntimeException {
    private final MessageEnum messageEnum;

    public ParameterRequiredException(String message) {
        super(MessageEnum.PARAMETER_REQUIRED.getCode() + message);
        this.messageEnum = MessageEnum.PARAMETER_REQUIRED;
    }
}
