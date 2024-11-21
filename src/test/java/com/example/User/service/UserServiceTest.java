package com.example.User.service;

import com.example.User.dto.UserDto;
import com.example.User.entity.Name;
import com.example.User.entity.RandomUserResponse;
import com.example.User.entity.User;
import com.example.User.entity.Registered;
import com.example.User.entity.Dob; // Assuming Dob is a separate class
import com.example.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:application.properties")
class UserServiceTest {

    @InjectMocks
    @Spy
    private UserService userService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    private User existingUser;
    private User fetchedUser;
    private RandomUserResponse mockApiResponse;

    @BeforeEach
    void setUp()
    {
        String apiUrl = "https://randomuser.me/api/";
        MockitoAnnotations.openMocks(this);

        // Prepare existing user data with nested objects
        existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setName(new Name("Mr.", "John", "Doe"));
        existingUser.setGender("Male");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setPhone("1234567890");
        existingUser.setCell("9876543210");

        // Existing Dob and Registered data
        Dob existingDob = new Dob("1990-01-01", 34);  // date and age
        Registered existingRegistered = new Registered("2020-01-01", 4);  // date and age

        existingUser.setDob(existingDob);
        existingUser.setRegistered(existingRegistered);

        existingUser.setNat("US");

        // Prepare fetched user data with differences
        fetchedUser = new User();
        fetchedUser.setUserId(1L);
        fetchedUser.setName(new Name("Mr.", "John", "Doe"));
        fetchedUser.setGender("Female");  // Gender has changed
        fetchedUser.setEmail("john.doe@example.com");
        fetchedUser.setPhone("1234567890");
        fetchedUser.setCell("9876543210");

        // Fetched Dob and Registered data (with differences)
        Dob fetchedDob = new Dob("1990-01-01", 35);  // Age has changed
        Registered fetchedRegistered = new Registered("2020-01-01", 5);  // Age has changed

        fetchedUser.setDob(fetchedDob);
        fetchedUser.setRegistered(fetchedRegistered);

        fetchedUser.setNat("US");

        // Mock API response
        mockApiResponse = new RandomUserResponse();
        mockApiResponse.setResults(List.of(fetchedUser));

        // Mock behavior for repository
        when(userRepository.findByPhoneAndCell(fetchedUser.getPhone(), fetchedUser.getCell()))
                .thenReturn(Optional.of(existingUser));

        when(restTemplate.getForObject(eq(apiUrl), eq(RandomUserResponse.class)))
                .thenReturn(mockApiResponse);
    }

    /**
     * Tests the fetchUserFromAPI method when there is a data difference.
     */
    @Test
    void testFetchUserFromAPI_withDataDifference() {
        RandomUserResponse response = userService.fetchUserFromAPI();
        verify(userRepository, times(0)).save(fetchedUser);
        verify(emailService, times(0)).sendEmail(anyString(), anyString());
    }

    /**
     * Tests the getUserDataDifference method to ensure it detects differences correctly.
     */
    @Test
    void testGetUserDataDifference() {
        List<String> differences = userService.getUserDataDifference(existingUser, fetchedUser);
        assertEquals(3, differences.size());
        assertTrue(differences.contains("gender: Male -> Female"));
        assertFalse(differences.contains("dobAge: 34 -> 35"));
        assertFalse(differences.contains("registeredAge: 4 -> 5"));
    }

    /**
     * Tests the constructEmailSubject method with empty name fields.
     */
    @Test
    void constructEmailSubject_EmptyNameFields() {
        Name name = new Name();
        String subject = userService.constructEmailSubject(name);
        assertEquals("Data Updated For User", subject);
    }

    /**
     * Tests the constructEmailSubject method with a null name.
     */
    @Test
    void constructEmailSubject_NullName() {
        String subject = userService.constructEmailSubject(null);
        assertEquals("Data Updated For User", subject);
    }

    /**
     * Tests the constructEmailSubject method with a full name.
     */
    @Test
    void constructEmailSubject_FullName() {
        Name name = new Name();
        name.setTitle("Mr.");
        name.setFirst("John");
        name.setLast("Doe");
        String subject = userService.constructEmailSubject(name);
        assertEquals("Data Updated For User Mr. John Doe", subject);
    }

    /**
     * Tests the fetchUserFromAPI method when no results are returned from the API.
     */
    @Test
    void fetchUserFromAPI_NoResultsFromAPI() {
        RandomUserResponse apiResponse = new RandomUserResponse();
        apiResponse.setResults(Collections.emptyList());
        when(restTemplate.getForObject(eq("https://randomuser.me/api/"), eq(RandomUserResponse.class)))
                .thenReturn(apiResponse);
        RandomUserResponse response = userService.fetchUserFromAPI();
        assertNotNull(response);
        assertTrue(response.getResults().isEmpty());
        System.out.println("Results: " + response.getResults());
    }

    /**
     * Tests the constructEmailSubject method with a partial name.
     */
    @Test
    void constructEmailSubject_PartialName() {
        Name name = new Name();
        name.setFirst("Jane");
        String subject = userService.constructEmailSubject(name);
        assertEquals("Data Updated For User Jane", subject);
    }

    /**
     * Tests the getPaginatedUsers method to ensure it returns the correct paginated users.
     */
    @Test
    void testGetPaginatedUsers() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<User> users = List.of(existingUser);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        Page<UserDto> result = userService.getPaginatedUsers(page, size);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(pageable);
    }

    /**
     * Tests the getUserById method to ensure it returns the correct user by ID.
     */
    @Test
    void testGetUserById() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        UserDto userDto = userService.getUserById(userId);

        assertNotNull(userDto);
        assertEquals(existingUser.getUserId(), userDto.getUserId());
        verify(userRepository, times(1)).findById(userId);
    }

    /**
     * Tests the getUserById method when the user is not found.
     */
    @Test
    void testGetUserById_UserNotFound() {
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        });

        String expectedMessage = "User not found with id: " + userId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(userRepository, times(1)).findById(userId);
    }


    /**
     * Tests the compareUserObject method when the response is null.
     */
    @Test
    void testCompareUserObject_NullResponse() {
        assertThrows(RuntimeException.class, () -> {
            userService.compareUserObject(null);
        });
    }

    /**
     * Tests the compareUserObject method when the response has no results.
     */
    @Test
    void testCompareUserObject_EmptyResponse() {
        RandomUserResponse emptyResponse = new RandomUserResponse();
        emptyResponse.setResults(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> {
            userService.compareUserObject(emptyResponse);
        });
    }

    /**
     * Tests the compareUserObject method when the fetched user is new.
     */
    @Test
    void testCompareUserObject_NewUser() {
        RandomUserResponse response = new RandomUserResponse();
        response.setResults(List.of(fetchedUser));

        when(userRepository.findByPhoneAndCell(fetchedUser.getPhone(), fetchedUser.getCell()))
                .thenReturn(Optional.empty());

        userService.compareUserObject(response);

        verify(userRepository, times(1)).save(fetchedUser);
        verify(emailService, times(0)).sendEmail(anyString(), anyString());
    }

    /**
     * Tests the compareUserObject method when the fetched user data is the same as the existing user.
     */
    @Test
    void testCompareUserObject_SameUser() {
        RandomUserResponse response = new RandomUserResponse();
        response.setResults(List.of(existingUser));

        when(userRepository.findByPhoneAndCell(existingUser.getPhone(), existingUser.getCell()))
                .thenReturn(Optional.of(existingUser));

        userService.compareUserObject(response);

        verify(userRepository, times(0)).save(existingUser);
        verify(emailService, times(0)).sendEmail(anyString(), anyString());
    }

    /**
     * Tests the compareUserObject method when the fetched user data is different from the existing user.
     */
    @Test
    void testCompareUserObject_DifferentUser() {
        RandomUserResponse response = new RandomUserResponse();
        response.setResults(List.of(fetchedUser));

        when(userRepository.findByPhoneAndCell(fetchedUser.getPhone(), fetchedUser.getCell()))
                .thenReturn(Optional.of(existingUser));

        userService.compareUserObject(response);

        verify(userRepository, times(1)).save(fetchedUser);
        verify(emailService, times(1)).sendEmail(anyString(), anyString());
    }

    /**
     * Tests the compareDataAndSendEmail method when there are differences in user data.
     */
    @Test
    void testCompareDataAndSendEmail_WithDifferences() {
        List<String> differences = List.of("gender: Male -> Female");

        when(userService.getUserDataDifference(any(User.class), any(User.class))).thenReturn(differences);

        userService.compareDataAndSendEmail(fetchedUser, existingUser);

        verify(emailService, times(1)).sendEmail(anyString(), anyString());
    }

    /**
     * Tests the compareDataAndSendEmail method when there are no differences in user data.
     */
    @Test
    void testCompareDataAndSendEmail_NoDifferences() {
        List<String> differences = Collections.emptyList();

        when(userService.getUserDataDifference(any(User.class), any(User.class))).thenReturn(differences);

        userService.compareDataAndSendEmail(fetchedUser, existingUser);

        verify(emailService, times(0)).sendEmail(anyString(), anyString());
    }

}
