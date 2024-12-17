package dev.erikmota.usermanager.exception.message;

import lombok.Data;

@Data
public class Message {

    private String message;

    private String code;
    private MessageType type;
    private Object[] params;

    public Message(MessageEnum messageEnum, Object... params) {
        this.type = messageEnum.getType();
        this.code = messageEnum.getCode();
        this.params = params;
    }

    public enum MessageType {
        INFORMATION, SUCCESS, ALERT, ERROR
    }
}

