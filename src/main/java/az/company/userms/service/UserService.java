package az.company.userms.service;

import az.company.userms.config.JwtTokenUtil;
import az.company.userms.entity.Account;
import az.company.userms.entity.User;
import az.company.userms.exception.UserAlreadyExistsException;
import az.company.userms.mapper.AccountMapper;
import az.company.userms.model.AccountDto;
import az.company.userms.model.JwtRequest;
import az.company.userms.model.JwtResponse;
import az.company.userms.model.UserDTO;
import az.company.userms.repository.AccountRepository;
import az.company.userms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder bcryptEncoder;


    public Optional<User> getUserById(Long id) {
        Optional<User> user = Optional.ofNullable(userRepository.findByIdAndStatus(id, '1'));
        return user;
    }


    public ResponseEntity<?> createAuthenticationToken(JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        Long userId = userRepository.findByUsername(authenticationRequest.getUsername()).getId();

        final String token = jwtTokenUtil.generateToken(userDetails, userId);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username:-" + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    public User save(UserDTO user) throws UserAlreadyExistsException {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        User oldUser = userRepository.findByUsername(user.getUsername());
        if (oldUser != null) {
            throw new UserAlreadyExistsException("User already registered");
        }
        return userRepository.save(newUser);
    }

    public List<AccountDto> getActiveAccounts(Long userId) {
        List<Account> accounts = accountRepository.findByUserIdAndStatus(userId, '1');
        return accounts.stream().map(accountMapper::entitiyToDto).collect(Collectors.toList());
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}