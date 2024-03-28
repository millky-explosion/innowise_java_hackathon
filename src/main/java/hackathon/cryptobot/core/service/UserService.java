package hackathon.cryptobot.core.service;

import hackathon.cryptobot.core.models.User;
import hackathon.cryptobot.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    UserRepository repository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public User save(User user) {
       return repository.insert(user);
    }

    public void delete (String username) {
        repository.deleteUserByUsername(username);
    }

    public Optional<User> findByUsername (String username) {
        return repository.findUserByUsername(username);
    }

    public List<User> findAll () {
        return repository.findAll();
    }

}
