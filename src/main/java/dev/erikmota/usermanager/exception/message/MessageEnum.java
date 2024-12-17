package dev.erikmota.usermanager.exception.message;

import lombok.Getter;

@Getter
public enum MessageEnum {
    GENERAL("ME001", Message.MessageType.ERROR);

    private final String code;
    private final Message.MessageType type;

    MessageEnum(String code, Message.MessageType type){
        this.code = code;
        this.type = type;
    }
}
