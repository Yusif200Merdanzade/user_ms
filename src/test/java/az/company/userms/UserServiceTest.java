package az.company.userms;

import az.company.userms.entity.User;
import az.company.userms.exception.UserAlreadyExistsException;
import az.company.userms.model.UserDTO;
import az.company.userms.repository.UserRepository;
import az.company.userms.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void registerUser_success() throws UserAlreadyExistsException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("1234");
        userDTO.setPassword("password");

        User user = new User();
        user.setUsername("1234");
        user.setPassword("encodedPassword");

        Mockito.when(bcryptEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        Mockito.when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(null);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User savedUser = userService.save(userDTO);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));

        assertEquals("1234", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test()
    public void registerUser_userAlreadyExists() throws UserAlreadyExistsException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("1234");
        userDTO.setPassword("password");

        User existingUser = new User();
        existingUser.setUsername("1234");
        existingUser.setPassword("encodedPassword");

        Mockito.when(bcryptEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        Mockito.when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(existingUser);

        userService.save(userDTO);

        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(User.class));
    }
}
