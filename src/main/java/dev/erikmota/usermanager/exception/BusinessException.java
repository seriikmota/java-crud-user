package dev.erikmota.usermanager.exception;

import dev.erikmota.usermanager.exception.message.Message;
import dev.erikmota.usermanager.exception.message.MessageEnum;
import dev.erikmota.usermanager.exception.message.MessageResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@Getter
public class BusinessException extends RuntimeException {
    private final MessageResponse messageResponse;

    public BusinessException(MessageResponse messageResponse){
        super();
        this.messageResponse = messageResponse;
    }

    public BusinessException(MessageEnum messageEnum){
        super();
        messageResponse = new MessageResponse();
        messageResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        messageResponse.setMessages(new ArrayList<>());
        messageResponse.getMessages().add(new Message(messageEnum));
    }
}
