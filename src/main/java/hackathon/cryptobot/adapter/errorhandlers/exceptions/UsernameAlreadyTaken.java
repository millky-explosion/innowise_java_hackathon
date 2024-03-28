package hackathon.cryptobot.adapter.errorhandlers.exceptions;

public class UsernameAlreadyTaken extends RuntimeException{
    public UsernameAlreadyTaken() {
        super("Username Already Taken");
    }
}
