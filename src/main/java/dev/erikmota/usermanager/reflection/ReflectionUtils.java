package dev.erikmota.usermanager.reflection;

import dev.erikmota.usermanager.annotations.EmailValidate;
import dev.erikmota.usermanager.annotations.MandatoryField;
import dev.erikmota.usermanager.annotations.PasswordValidate;
import dev.erikmota.usermanager.exception.message.MessageEnum;
import dev.erikmota.usermanager.util.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReflectionUtils {
    public static void validateMandatoryFields(Object object, List<String> namesOfInvalidFields) {
        List<Field> mandatoryFields = getFieldsWithAnnotation(object, MandatoryField.class);

        for (Field field : mandatoryFields) {
            Object valueField = getFieldValue(object, field);
            boolean isValid = true;

            if (valueField == null) {
                isValid = false;
            }
            else if (valueField instanceof String) {
                if (((String) valueField).trim().isEmpty() || ((String) valueField).trim().length() > field.getAnnotation(MandatoryField.class).length()) {
                    isValid = false;
                }
            }
            else if (valueField instanceof byte[]) {
                isValid = ((byte[]) valueField).length > 0;
            }
            else if (valueField instanceof Collection<?>) {
                isValid = !((Collection<?>) valueField).isEmpty();
            }
            else if (isComplexObject(field)) {
                validateMandatoryFields(valueField, namesOfInvalidFields);
            }

            if (!isValid) {
                namesOfInvalidFields.add(getNameMandatoryField(field));
            }
        }
    }

    public static Map<String, List<MessageEnum>> validateAnnotations(Object object) {
        List<Field> fields = getFields(object);
        Map<String, List<MessageEnum>> mapFieldErrors = new HashMap<>();

        for (Field field : fields) {
            if (isComplexObject(field)) {
                Object fieldValue = getFieldValue(object, field);
                if (fieldValue != null) {
                    mapFieldErrors.putAll(validateAnnotations(fieldValue));
                }
            }

            validateFieldAnnotation(object, field, mapFieldErrors);
        }

        return mapFieldErrors;
    }

    private static boolean isComplexObject(Field field) {
        return !field.getType().isPrimitive() && !field.getType().getName().startsWith("java.lang") &&
                !field.getType().getName().startsWith("java.time") && !field.getType().isEnum();
    }

    private static void validateFieldAnnotation(Object object, Field field, Map<String, List<MessageEnum>> errorsMap) {
        Object fieldValue = getFieldValue(object, field);

        if (field.isAnnotationPresent(EmailValidate.class)) {
            addFieldErrors(errorsMap, getNameEmailAnnotation(field), Utils.validateEmail((String) fieldValue));
        }

        if (field.isAnnotationPresent(PasswordValidate.class)) {
            addFieldErrors(errorsMap, getNamePasswordAnnotation(field), Utils.validatePassword((String) fieldValue));
        }
    }

    private static void addFieldErrors(Map<String, List<MessageEnum>> errorsMap, String fieldName, List<MessageEnum> errors) {
        if (!errors.isEmpty()) {
            errorsMap.computeIfAbsent(fieldName, list -> new ArrayList<>()).addAll(errors);
        }
    }

    public static List<Field> getFieldsWithAnnotation(Object object, Class annotationClass) {
        List<Field> mandatoryFields = new ArrayList<>();

        List<Field> fields = getFields(object);
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                mandatoryFields.add(field);
            }
        }

        return mandatoryFields;
    }

    public static List<Field> getFields(Object object){
        List<Field> resultFields = new ArrayList<>();
        Class<?> clazz = object.getClass();
        while(clazz != null && !clazz.equals(Object.class)){
            Field[] modelFields = clazz.getDeclaredFields();
            resultFields.addAll(Arrays.asList(modelFields));
            clazz = clazz.getSuperclass();
        }
        return resultFields;
    }

    public static Object getFieldValue(Object object, Field field) {
        try {
            Class<?> objectClass = object.getClass();
            String methodGetFieldName = "get" + uCFirst(field.getName());
            return objectClass.getMethod(methodGetFieldName).invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String uCFirst(String str){
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }

    public static String getNameMandatoryField(Field field) {
        String nameOfAnnotation = field.getAnnotation(MandatoryField.class).name();
        if (nameOfAnnotation != null && !nameOfAnnotation.isBlank()) {
            return field.getAnnotation(MandatoryField.class).name();
        } else {
            return field.getName();
        }
    }


    public static String getNamePasswordAnnotation(Field field) {
        String nameOfAnnotation = field.getAnnotation(PasswordValidate.class).name();
        if (nameOfAnnotation != null && !nameOfAnnotation.isBlank()) {
            return field.getAnnotation(PasswordValidate.class).name();
        } else {
            return field.getName();
        }
    }

    public static String getNameEmailAnnotation(Field field) {
        String nameOfAnnotation = field.getAnnotation(EmailValidate.class).name();
        if (nameOfAnnotation != null && !nameOfAnnotation.isBlank()) {
            return field.getAnnotation(EmailValidate.class).name();
        } else {
            return field.getName();
        }
    }
}
