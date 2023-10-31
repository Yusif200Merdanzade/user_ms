package az.company.userms.controller;

import az.company.userms.entity.User;
import az.company.userms.model.JwtRequest;
import az.company.userms.model.UserDTO;
import az.company.userms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userDetailsService;

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        return userDetailsService.getUserById(id);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {
        return userDetailsService.createAuthenticationToken(authenticationRequest);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    @GetMapping("/userDetails")
    public UserDetails getUserDetails(@RequestParam("username") String username) {
        return userDetailsService.loadUserByUsername(username);

    }

    @GetMapping(value = "/verify")
    public boolean verify() {
        return true;
    }

}
