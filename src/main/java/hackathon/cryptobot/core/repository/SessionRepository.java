package hackathon.cryptobot.core.repository;

import hackathon.cryptobot.core.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SessionRepository {
    private static final String HASH_KEY = "Session";

    @Autowired
    private RedisTemplate template;

    public Session save (Session session) {
        template.opsForHash().put(HASH_KEY, session.getId().toString(), session);
        return session;
    }

    public List<Session> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }

    public Session findSessionById(Long id){
        return (Session) template.opsForHash().get(HASH_KEY,id.toString());
    }


    public boolean deleteUser(Long id) {
        try {
            template.opsForHash()
                    .delete(HASH_KEY, id.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
