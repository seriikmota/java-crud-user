package dev.erikmota.usermanager.validations.user;

import dev.erikmota.usermanager.entities.User;
import dev.erikmota.usermanager.enums.ValidationActionsEnum;
import dev.erikmota.usermanager.exception.message.Message;
import dev.erikmota.usermanager.exception.message.MessageEnum;
import dev.erikmota.usermanager.repository.UserRepository;
import dev.erikmota.usermanager.validations.IUserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserExistEmail implements IUserValidation {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(User data, ValidationActionsEnum action, List<Message> messagesToThrow) {
        if (action.equals(ValidationActionsEnum.CREATE)) {
            if (data.getEmail() != null && userRepository.existsByEmail(data.getEmail())) {
                messagesToThrow.add(new Message(MessageEnum.EMAIL_EXISTS));
            }
        }
    }
}
