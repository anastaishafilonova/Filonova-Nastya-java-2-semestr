package book.service.controller;

import book.service.controller.request.UserRegisterRequest;
import book.service.entity.Role;
import book.service.entity.User;
import book.service.repository.UserRepository;
import book.service.service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/register")
  @Transactional
  public void register(@RequestBody UserRegisterRequest request) {
    LOGGER.info("username: " + request.username() + " roles: " + request.roles());
    Set<Role> roles = new HashSet<>();
    for (String role : request.roles().split(", ")) {
      roles.add(Role.valueOf(role));
    }
    userRepository.save(
        // Создаем здесь нашу сущность User
        new User(
            request.username(),
            // не забываем, что пароли хранятся в кодированном виде
            passwordEncoder.encode(request.password()),
            roles
        )
    );
  }

}

