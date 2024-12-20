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
public class UserExistLogin implements IUserValidation {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(User data, ValidationActionsEnum action, List<Message> messagesToThrow) {
        if (action.equals(ValidationActionsEnum.CREATE)) {
            if (data.getLogin() != null && userRepository.existsByLogin(data.getLogin())) {
                messagesToThrow.add(new Message(MessageEnum.LOGIN_EXISTS));
            }
        }
        if (action.equals(ValidationActionsEnum.UPDATE)) {
            if (data.getLogin() != null && userRepository.existsByLogin(data.getLogin())) {
                if (!userRepository.existsByLoginAndId(data.getLogin(), data.getId())) {
                    messagesToThrow.add(new Message(MessageEnum.LOGIN_EXISTS));
                }
            }
        }
    }
}
