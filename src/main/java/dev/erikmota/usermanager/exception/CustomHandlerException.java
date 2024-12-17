package dev.erikmota.usermanager.exception;

import dev.erikmota.usermanager.exception.message.Message;
import dev.erikmota.usermanager.exception.message.MessageEnum;
import dev.erikmota.usermanager.exception.message.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class CustomHandlerException extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<MessageResponse> handleBusinessException(BusinessException ex) {
        mountMessages(ex.getMessageResponse());

        return ResponseEntity.status(ex.getMessageResponse().getStatusCode()).body(ex.getMessageResponse());
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<MessageResponse> handleDataException(DataException ex){
        mountMessages(ex.getMessageResponse());

        return ResponseEntity.status(ex.getMessageResponse().getStatusCode()).body(ex.getMessageResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleException(Exception ex) {
        ex.printStackTrace();
        MessageResponse messageResponse = mountMessageResponse(HttpStatus.BAD_REQUEST, MessageEnum.GENERAL);

        return ResponseEntity.status(messageResponse.getStatusCode()).body(messageResponse);
    }

    protected MessageResponse mountMessageResponse(HttpStatus status, MessageEnum messageEnum) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatusCode(status.value());
        messageResponse.setMessages(new ArrayList<>());
        messageResponse.getMessages().add(new Message(messageEnum));

        mountMessages(messageResponse);

        return messageResponse;
    }

    protected void mountMessages(MessageResponse messageResponse) {
        if (messageResponse != null && messageResponse.getMessages() != null) {
            for (Message message : messageResponse.getMessages()) {
                if (message.getParams() == null || message.getParams().length == 0)
                    message.setMessage(this.getMessage(message.getCode()));
                else
                    message.setMessage(this.getMessage(message.getCode(), message.getParams()));
            }
        }
    }

    private String getMessage(final String code, final Object... params) {
        return messageSource.getMessage(code, params, LocaleContextHolder.getLocale());
    }
}
