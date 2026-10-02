package com.dipdeveloper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


// ═══════════════════════════════════════════════════
// SERVICE TO TEST
// ═══════════════════════════════════════════════════

public interface UserRepository {
    User findById(Long id);
    void save(User user);
}

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void promoteUser(Long userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setRole("ADMIN");
            userRepository.save(user);
        }
    }
}


// ═══════════════════════════════════════════════════
// UNIT TEST WITH MOCKITO
// ═══════════════════════════════════════════════════

class UserServiceTest {

    @Mock
    private UserRepository userRepository;  // Mock dependency

    @InjectMocks
    private UserService userService;  // Service under test with mocks injected

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    void testGetUserById() {
        // ARRANGE
        Long userId = 1L;
        User expectedUser = new User(userId, "John", "john@example.com");
        when(userRepository.findById(userId)).thenReturn(expectedUser);

        // ACT
        User result = userService.getUserById(userId);

        // ASSERT
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());

        // VERIFY that the mock was called exactly once
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testPromoteUser() {
        // ARRANGE
        Long userId = 1L;
        User user = new User(userId, "John", "john@example.com");
        user.setRole("USER");

        when(userRepository.findById(userId)).thenReturn(user);

        // ACT
        userService.promoteUser(userId);

        // ASSERT
        assertEquals("ADMIN", user.getRole());
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void testPromoteNonExistentUser() {
        // ARRANGE
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(null);

        // ACT
        userService.promoteUser(userId);

        // ASSERT
        // save() should never be called
        verify(userRepository, never()).save(any());
    }
}


// ═══════════════════════════════════════════════════
// HELPER CLASS
// ═══════════════════════════════════════════════════

class User {
    private Long id;
    private String name;
    private String email;
    private String role = "USER";

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
