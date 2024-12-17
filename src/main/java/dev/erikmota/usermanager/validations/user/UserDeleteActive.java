package dev.erikmota.usermanager.validations.user;

import dev.erikmota.usermanager.entities.User;
import dev.erikmota.usermanager.enums.ValidationActionsEnum;
import dev.erikmota.usermanager.exception.message.Message;
import dev.erikmota.usermanager.exception.message.MessageEnum;
import dev.erikmota.usermanager.validations.IUserValidation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDeleteActive implements IUserValidation {

    @Override
    public void validate(User data, ValidationActionsEnum action, List<Message> messagesToThrow) {
        if (action.equals(ValidationActionsEnum.DELETE)) {
            if (data.getEnabled()) {
                messagesToThrow.add(new Message(MessageEnum.DELETE_USER_ACTIVE));
            }
        }
    }
}
