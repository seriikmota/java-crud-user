package dev.erikmota.usermanager.service.impl;

import dev.erikmota.usermanager.dto.request.UserRequestDTO;
import dev.erikmota.usermanager.entities.User;
import dev.erikmota.usermanager.enums.ValidationActionsEnum;
import dev.erikmota.usermanager.exception.BusinessException;
import dev.erikmota.usermanager.exception.DataException;
import dev.erikmota.usermanager.exception.ParameterRequiredException;
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

import java.util.*;

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

    @Override
    public User update(UserRequestDTO dtoUpdate, Long id) {
        List<Message> messagesToThrow = new ArrayList<>();

        validateMandatoryFieldsDTO(dtoUpdate, messagesToThrow);
        prepareToMapUpdate(dtoUpdate);
        validateToMapUpdate(dtoUpdate, messagesToThrow);

        throwMessages(messagesToThrow);

        User dataUpdate = userMapper.toModel(dtoUpdate);
        return this.update(dataUpdate, id);
    }

    private User update(User dataUpdate, Long id) {
        List<Message> messagesToThrow = new ArrayList<>();
        var dataDB = validateIdModelExistsAndGet(id);
        dataUpdate.setId(id);

        prepareToUpdate(dataUpdate);
        validateBusinessLogicForUpdate(dataUpdate, messagesToThrow);

        throwMessages(messagesToThrow);

        userMapper.updateModelFromModel(dataDB, dataUpdate);
        return userRepository.save(dataDB);
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

    private void prepareToMapUpdate(UserRequestDTO dtoUpdate) {

    }

    private void validateToMapUpdate(UserRequestDTO dtoUpdate, List<Message> messagesToThrow) {
        this.validateAnnotations(dtoUpdate, messagesToThrow);
        if (dtoUpdate.getPassword() != null && !Utils.comparePasswords(dtoUpdate.getPassword(), dtoUpdate.getConfirmPassword())) {
            messagesToThrow.add(new Message(MessageEnum.PASSWORDS_DIFFERENT));
        }
    }

    private void prepareToUpdate(User dataUpdate) {
        dataUpdate.setPassword(Utils.encryptPassword(dataUpdate.getPassword()));
    }

    public User validateIdModelExistsAndGet(Long id){
        if (!Objects.nonNull(id)) throw new ParameterRequiredException("id");

        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new DataException(MessageEnum.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
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

    private void validateBusinessLogicForUpdate(User entity, List<Message> messagesToThrow) {
        validations.forEach(v -> v.validate(entity, ValidationActionsEnum.UPDATE, messagesToThrow));
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
