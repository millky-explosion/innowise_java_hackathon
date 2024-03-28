package hackathon.cryptobot.adapter.errorhandlers.exceptions;

public class UsernameMustBeFilled extends RuntimeException {
    public UsernameMustBeFilled() {
        super("Username Must Be Filled");
    }
}
