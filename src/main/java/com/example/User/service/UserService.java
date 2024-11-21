package com.example.User.service;

import com.example.User.dto.UserDto;
import com.example.User.entity.Name;
import com.example.User.entity.RandomUserResponse;
import com.example.User.entity.User;
import com.example.User.exceptions.CustomException;
import com.example.User.repository.UserRepository;
import com.example.User.utility.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired EmailService emailService;

    /**
     * Schedules fetchUserFromAPI call every minute.
     *
     * <p>Fetches a user from the API and compares it to the existing user in the database. If the data
     *  is different, an email is sent to the user.
     */
   @Scheduled(fixedRate = 60000) // 60 seconds in milliseconds
    public void scheduledFetchUserFromAPI() {
        try {
            log.info("Running scheduled task: fetchUserFromAPI");
            RandomUserResponse apiResponse = fetchUserFromAPI();
            if(apiResponse != null) {
              compareUserObject(apiResponse);
            }
        } catch (Exception e) {
            log.error("Error during scheduled task execution: {}", e.getMessage(), e);
        }
    }

    /**
     * Fetches User from random API.
     *
     * <p>Fetches a user from the API and compares it to the existing user in the database. If the data
     *  is different, an email is sent to the user.
     *
     * @throws RuntimeException if failed to fetch user data from API
     */
    public RandomUserResponse fetchUserFromAPI() {
        log.info("inside UserService method --> fetchUserFromAPI");
        String API_URL = "https://randomuser.me/api/";
        return restTemplate.getForObject(API_URL, RandomUserResponse.class);
    }

    /**
     * Compares the fetched user object with the existing user object.
     *
     * @param response The response containing the fetched user data.
     * @throws RuntimeException if the fetched user data is null or empty.
     */
    void compareUserObject(RandomUserResponse response) {
        if (response != null && !response.getResults().isEmpty()) {
            for (User fetchedUser : response.getResults()) {
               Optional<User> existingUser = getUserFromDB(fetchedUser);

                if(existingUser.isEmpty()){
                    User savedUser = userRepository.save(fetchedUser);
                    log.info("User saved successfully");
                }
                else {
                    boolean isSame = existingUser
                            .filter(user -> compareUsers(user, fetchedUser))
                            .isPresent();
                    if (isSame) {
                        log.info("No email send as data is same");
                    } else {
                        //save Updated data to database
                        fetchedUser.setUserId(existingUser.get().getUserId());
                        userRepository.save(fetchedUser);

                        //compare data and send email
                        compareDataAndSendEmail(fetchedUser, existingUser.get());
                    }
                }
            }
        }
        else{
            throw new RuntimeException("Failed to fetch user data from API");
        }
    }

    /**
     * Compares the fetched user object with the existing user object.
     *
     * @throws RuntimeException if the fetched user data is null or empty.
     */
    public void compareDataAndSendEmail(User fetchedUser, User existingUser) {
        List<String> changedUserData = getUserDataDifference(existingUser, fetchedUser);
        if (!changedUserData.isEmpty()) {
            String subject = constructEmailSubject(existingUser.getName());
            String emailBody = "The following fields have changed:\n" + String.join("\n", changedUserData);
            emailService.sendEmail(subject, emailBody);
            log.info("Email sent successfully");
        }
    }

    private Optional<User> getUserFromDB(User fetchedUser) {
        return userRepository.findByPhoneAndCell(fetchedUser.getPhone(), fetchedUser.getCell());
    }


    /**
     * Constructs an email subject based on the user's name.
     *
     * @param name The Name object containing title, first, and last name.
     * @return The constructed email subject.
     */
    public String constructEmailSubject(Name name) {
        String baseSubject = "Data Updated For User";

        if (name != null) {
            StringBuilder nameBuilder = new StringBuilder();

            if (name.getTitle() != null) {
                nameBuilder.append(name.getTitle()).append(" ");
            }
            if (name.getFirst() != null) {
                nameBuilder.append(name.getFirst()).append(" ");
            }
            if (name.getLast() != null) {
                nameBuilder.append(name.getLast());
            }

            String fullName = nameBuilder.toString().trim();

           if (!fullName.isEmpty()) {
                return baseSubject + " " + fullName;
            }
        }

        return baseSubject;
    }

    /**
     * Calculates the difference in user data between the existing user and the fetched user.
     *
     * @param existingUser The existing user fetched from the database.
     * @param fetchedUser The user data fetched from the API.
     * @return List of strings containing the differences in data.
     */
    List<String> getUserDataDifference(User existingUser, User fetchedUser) {
        List<String> changedFields = new ArrayList<>();
        if ( null != existingUser ) {
            if (!Objects.equals(existingUser.getGender(), fetchedUser.getGender())) {
                changedFields.add("gender: " + existingUser.getGender() + " -> " + fetchedUser.getGender());
            }
            if (!Objects.equals(existingUser.getEmail(), fetchedUser.getEmail())) {
                changedFields.add("email: " + existingUser.getEmail() + " -> " + fetchedUser.getEmail());
            }
            if (!Objects.equals(existingUser.getPhone(), fetchedUser.getPhone())) {
                changedFields.add("phone: " + existingUser.getPhone() + " -> " + fetchedUser.getPhone());
            }
            if (!Objects.equals(existingUser.getCell(), fetchedUser.getCell())) {
                changedFields.add("cell: " + existingUser.getCell() + " -> " + fetchedUser.getCell());
            }
            if (!Objects.equals(existingUser.getDob(), fetchedUser.getDob())) {
                changedFields.add("dob: " + existingUser.getDob() + " -> " + fetchedUser.getDob());
            }

            if (!Objects.equals(existingUser.getRegistered(), fetchedUser.getRegistered())) {
                changedFields.add("registered: " + existingUser.getRegistered() + " -> " + fetchedUser.getRegistered());
            }
            if (!Objects.equals(existingUser.getNat(), fetchedUser.getNat())) {
                changedFields.add("nat: " + existingUser.getNat() + " -> " + fetchedUser.getNat());
            }

            if (!Objects.equals(existingUser.getName(), fetchedUser.getName())) {
                changedFields.add("name: " + existingUser.getName() + " -> " + fetchedUser.getName());
            }

            if (!Objects.equals(existingUser.getLocation(), fetchedUser.getLocation())) {
                changedFields.add("location: " + existingUser.getLocation() + " -> " + fetchedUser.getLocation());
            }

            if (!Objects.equals(existingUser.getPicture(), fetchedUser.getPicture())) {
                changedFields.add("picture: " + existingUser.getPicture() + " -> " + fetchedUser.getPicture());
            }

            if (!Objects.equals(existingUser.getLogin(), fetchedUser.getLogin())) {
                changedFields.add("login: " + existingUser.getLogin() + " -> " + fetchedUser.getLogin());
            }

            if (!changedFields.isEmpty()) {
                 log.info("User data differences detected:");
                for (String field : changedFields) {
                    log.info(field);
                }
            } else {
                log.info("No differences detected.");
            }
        } else {
            log.info("Existing user not found in the database.");
        }

        return changedFields;
    }

    /**
     * Compares two users.
     *
     * @param oldUser The existing user.
     * @param newUser The new user.
     * @return boolean value after comparing both objects.
     */
    public boolean compareUsers(User oldUser, User newUser) {
        return oldUser.equals(newUser);
    }


    /**
     * Retrieves a paginated list of users.
     *
     * @param page the page number (zero-based)
     * @param size the number of records per page
     * @return a Page of User entities
     */
    @Transactional(readOnly = true)
    public Page<UserDto> getPaginatedUsers(int page, int size) {
        log.info("Inside UserService -> getPaginatedUsers");
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userList = userRepository.findAll(pageable);
        log.info("User fetched successfully are  {}", userList.getSize());
        return userList.map(UserMapper::toUserDto);
    }

    /**
     * Retrieves a user by their ID.
     *
     * <p>This method fetches a user from the database using their unique ID. If the user is found,
     * it is converted to a UserDto and returned. If the user is not found, a RuntimeException is thrown.
     *
     * @param userId The unique ID of the user to be retrieved.
     * @return UserDto The data transfer object representing the user.
     * @throws RuntimeException if the user is not found with the given ID.
     */
    public UserDto getUserById(Long userId) {
        log.info("Inside UserService -> getUserById");
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return UserMapper.toUserDto(userOptional.get());
        } else {
            throw new CustomException("User not found with id: " + userId);
        }
    }
}
