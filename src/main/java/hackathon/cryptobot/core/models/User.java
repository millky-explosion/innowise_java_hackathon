package hackathon.cryptobot.core.models;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@ToString
@Document(collection = "users")
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String info;
    private LocalDateTime time;

    public User(String username, String info, LocalDateTime time) {
        this.username = username;
        this.info = info;
        this.time = time;
    }

    public User(String username, String firstName, String lastName, String info, LocalDateTime time) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.info = info;
        this.time = time;
    }

    public User(String id, String username, String firstName, String lastName, String info, LocalDateTime time) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.info = info;
        this.time = time;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getInfo() {
        return info;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
