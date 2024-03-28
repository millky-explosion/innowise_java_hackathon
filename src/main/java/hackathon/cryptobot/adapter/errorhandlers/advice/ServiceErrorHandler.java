package hackathon.cryptobot.adapter.errorhandlers.advice;

import hackathon.cryptobot.adapter.errorhandlers.exceptions.UserNotFoundException;
import hackathon.cryptobot.adapter.errorhandlers.exceptions.UsernameAlreadyTaken;
import hackathon.cryptobot.adapter.errorhandlers.exceptions.UsernameMustBeFilled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ServiceErrorHandler {
    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundUserWithUsername(UserNotFoundException ex) {
        return ex.getMessage();
    }
    @ResponseBody
    @ExceptionHandler(UsernameMustBeFilled.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String usernameMustBeFilled(UsernameMustBeFilled ex) {
        return ex.getMessage();
    }
    @ResponseBody
    @ExceptionHandler(UsernameAlreadyTaken.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String usernameAlreadyTaken(UsernameAlreadyTaken ex) {
        return ex.getMessage();
    }


}
