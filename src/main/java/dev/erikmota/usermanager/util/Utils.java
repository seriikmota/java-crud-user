 package dev.erikmota.usermanager.util;

import dev.erikmota.usermanager.exception.message.MessageEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {

    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }

    public static Boolean comparePasswords(String password1, String password2) {
        return password1 != null && password1.equals(password2);
    }

    public static String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static List<MessageEnum> validatePassword(String password) {
        List<MessageEnum> errors = new ArrayList<>();
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d).+$";

        if (password != null) {
            if (password.trim().length() < 8) {
                errors.add(MessageEnum.PASSWORD_MIN_LENGTH);
            }
            if (!Pattern.matches(regex, password)) {
                errors.add(MessageEnum.PASSWORD_NUM_LETTER);
            }
        }

        return errors;
    }

    public static List<MessageEnum> validateEmail(String email) {
        List<MessageEnum> errors = new ArrayList<>();
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (email != null) {
            if (!Pattern.matches(emailRegex, email)) {
                errors.add(MessageEnum.EMAIL_INVALID);
            }
        }

        return errors;
    }
}
