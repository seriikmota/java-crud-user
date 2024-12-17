package dev.erikmota.usermanager.service.impl;

import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.entities.User;
import dev.erikmota.usermanager.enums.ValidationActionsEnum;
import dev.erikmota.usermanager.exception.BusinessException;
import dev.erikmota.usermanager.exception.message.Message;
import dev.erikmota.usermanager.exception.message.MessageEnum;
import dev.erikmota.usermanager.exception.message.MessageResponse;
import dev.erikmota.usermanager.mapper.UserMapper;
import dev.erikmota.usermanager.reflection.ReflectionUtils;
import dev.erikmota.usermanager.repository.UserRepository;
import dev.erikmota.usermanager.service.IUserService;
import dev.erikmota.usermanager.util.Utils;
import dev.erikmota.usermanager.validations.IUserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private List<IUserValidation> validations = new ArrayList<>();

    @Override
    public User create(UserRequestDTO dtoCreate) {
        List<Message> messagesToThrow = new ArrayList<>();

        validateMandatoryFieldsDTO(dtoCreate, messagesToThrow);
        prepareToMapCreate(dtoCreate);
        validateToMapCreate(dtoCreate, messagesToThrow);

        throwMessages(messagesToThrow);

        User data = userMapper.toModel(dtoCreate);
        return this.create(data);
    }

    private User create(User dataCreate) {
        List<Message> messagesToThrow = new ArrayList<>();
        prepareToCreate(dataCreate);

        validateBusinessLogicForInsert(dataCreate, messagesToThrow);

        throwMessages(messagesToThrow);
        return userRepository.save(dataCreate);
    }

    private void prepareToMapCreate(UserRequestDTO dtoCreate) {
        dtoCreate.setLogin(Utils.trim(dtoCreate.getLogin()));
        dtoCreate.setPassword(Utils.trim(dtoCreate.getPassword()));
        dtoCreate.setConfirmPassword(Utils.trim(dtoCreate.getConfirmPassword()));
    }

    private void validateToMapCreate(UserRequestDTO dtoCreate, List<Message> messagesToThrow) {
        this.validateAnnotations(dtoCreate, messagesToThrow);
        if (dtoCreate.getPassword() == null) {
            messagesToThrow.add(new Message(MessageEnum.MANDATORY_FIELD, "Senha"));
        }
        if (!Utils.comparePasswords(dtoCreate.getPassword(), dtoCreate.getConfirmPassword())) {
            messagesToThrow.add(new Message(MessageEnum.PASSWORDS_DIFFERENT));
        }
    }

    private void prepareToCreate(User entity) {
        entity.setPassword(Utils.encryptPassword(entity.getPassword()));
    }

    private void validateMandatoryFieldsDTO(UserRequestDTO dto, List<Message> messagesToThrow) {
        List<String> fieldsInvalid = new ArrayList<>();
        ReflectionUtils.validateMandatoryFields(dto, fieldsInvalid);

        if (!fieldsInvalid.isEmpty()) {
            for (String field : fieldsInvalid) {
                messagesToThrow.add(new Message(MessageEnum.MANDATORY_FIELD, field));
            }
        }
    }

    private void validateAnnotations(Object object, List<Message> messagesToThrow) {
        Map<String, List<MessageEnum>> mapMessageEnums = ReflectionUtils.validateAnnotations(object);
        if (!mapMessageEnums.isEmpty()) {
            for (String fieldKey : mapMessageEnums.keySet()) {
                for (MessageEnum messageEnum : mapMessageEnums.get(fieldKey)) {
                    messagesToThrow.add(new Message(messageEnum, fieldKey));
                }
            }
        }
    }

    private void validateBusinessLogicForInsert(User entity, List<Message> messagesToThrow) {
        validations.forEach(v -> v.validate(entity, ValidationActionsEnum.CREATE, messagesToThrow));
    }

    private void throwMessages(List<Message> messagesToThrow) {
        if (!messagesToThrow.isEmpty()) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessages(messagesToThrow);
            messageResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            throw new BusinessException(messageResponse);
        }
    }
}
