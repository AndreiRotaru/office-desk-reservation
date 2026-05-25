package reservation_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reservation_service.dto.LoginRequest;
import reservation_service.dto.RegisterRequest;
import reservation_service.entity.User;
import reservation_service.exception.UserCustomException;
import reservation_service.repository.UserRepository;
import reservation_service.security.JwtService;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request){
      Optional<User> dbUser = userRepository.findByUsername(request.getUsername());
        if(dbUser.isPresent()){
            throw new UserCustomException("Username already exists!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role("USER")
                .build();

        userRepository.save(user);
    }

    public List<User> allUsersR(){
        return userRepository.findAll();
    }

    public String login (LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UserCustomException("User not found!"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatches){
            throw new UserCustomException("Invalid Credentials!");
        }

        return jwtService.generateToken(user.getUsername());
    }
}
