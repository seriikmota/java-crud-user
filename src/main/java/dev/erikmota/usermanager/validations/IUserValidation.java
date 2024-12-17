package dev.erikmota.usermanager.validations;

import dev.erikmota.usermanager.entities.User;
import dev.erikmota.usermanager.enums.ValidationActionsEnum;
import dev.erikmota.usermanager.exception.message.Message;

import java.util.List;

public interface IUserValidation {
    void validate(User data, ValidationActionsEnum action, List<Message> messagesToThrow);
}
