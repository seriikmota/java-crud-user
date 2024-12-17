package dev.erikmota.usermanager.exception.message;

import lombok.Getter;

@Getter
public enum MessageEnum {
    GENERAL("ME001", Message.MessageType.ERROR),
    MANDATORY_FIELD("ME002", Message.MessageType.ERROR),
    PASSWORDS_DIFFERENT("ME003", Message.MessageType.ERROR),
    EMAIL_INVALID("ME004", Message.MessageType.ERROR),
    EMAIL_EXISTS("ME005", Message.MessageType.ERROR),
    LOGIN_EXISTS("ME006", Message.MessageType.ERROR),
    PASSWORD_MIN_LENGTH("ME007", Message.MessageType.ERROR),
    PASSWORD_NUM_LETTER("ME008", Message.MessageType.ERROR),
    PARAMETER_REQUIRED("ME009", Message.MessageType.ERROR),
    NOT_FOUND("ME010", Message.MessageType.ERROR),
    DELETE_USER_ACTIVE("ME011", Message.MessageType.ERROR);

    private final String code;
    private final Message.MessageType type;

    MessageEnum(String code, Message.MessageType type){
        this.code = code;
        this.type = type;
    }
}
