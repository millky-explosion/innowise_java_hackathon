package hackathon.cryptobot.adapter.controller;

import hackathon.cryptobot.adapter.errorhandlers.exceptions.UserNotFoundException;
import hackathon.cryptobot.adapter.errorhandlers.exceptions.UsernameAlreadyTaken;
import hackathon.cryptobot.adapter.errorhandlers.exceptions.UsernameMustBeFilled;
import hackathon.cryptobot.core.models.User;
import hackathon.cryptobot.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/main")
public class MainController {

    @Autowired
    private UserService service;

    @PostMapping("/add")
    public ResponseEntity <Void> add(@RequestBody User user) {
        if (service.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyTaken();
        } if (user.getUsername().isEmpty()) {
            throw new UsernameMustBeFilled();
        } else service.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteLocationById
            (@PathVariable(name = "username") String username) {
        if (service.findByUsername(username).isEmpty()) {
            throw new UserNotFoundException(username);
        } else service.delete(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/current-list")
    public ResponseEntity<List<User>> findAllLocations () {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/current-user")
    public ResponseEntity<Optional<User>> findUserByUsername
            (@RequestParam String username) {
        return ResponseEntity.ok(service.findByUsername(username));
    }

}
