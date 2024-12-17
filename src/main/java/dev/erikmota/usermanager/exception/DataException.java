package dev.erikmota.usermanager.exception;

import dev.erikmota.usermanager.exception.message.Message;
import dev.erikmota.usermanager.exception.message.MessageEnum;
import dev.erikmota.usermanager.exception.message.MessageResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@Getter
public class DataException extends RuntimeException {
    private final MessageResponse messageResponse;

    public DataException(MessageEnum messageEnum, HttpStatus status){
        super();
        messageResponse = new MessageResponse();
        messageResponse.setStatusCode(status.value());
        messageResponse.setMessages(new ArrayList<>());
        messageResponse.getMessages().add(new Message(messageEnum));
    }
}
