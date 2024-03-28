package hackathon.cryptobot.core.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.time.LocalDateTime;


public class Session implements Serializable {

    @Id
    private Long id;
    private String username;
    private LocalDateTime startedAt;
    private LocalDateTime overAt;

    public Session(Long id, String username, LocalDateTime startedAt, LocalDateTime overAt) {
        this.id = id;
        this.username = username;
        this.startedAt = startedAt;
        this.overAt = overAt;
    }
    public Session() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getOverAt() {
        return overAt;
    }

    public void setOverAt(LocalDateTime overAt) {
        this.overAt = overAt;
    }
}