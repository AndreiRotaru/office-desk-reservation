package reservation_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reservation_service.dto.AuthResponse;
import reservation_service.dto.LoginRequest;
import reservation_service.dto.RegisterRequest;
import reservation_service.entity.User;
import reservation_service.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/retrieveAll")
    public ResponseEntity<List<User>> getAll(){
        List<User> userList = authService.allUsersR();
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }


}
