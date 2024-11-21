package com.example.User.controller;

import com.example.User.dto.UserDto;
import com.example.User.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing User-related operations.
 */
@Tag(name = "User", description = "Operations related to User management")
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Fetches a page of users based on the given page number and size.
     *
     * @param page The page number to fetch.
     * @param size The number of users per page.
     * @return ResponseEntity containing the page of users.
     */
    @Operation(
            summary = "Get paginated users",
            description = "Fetches a page of users based on the given page number and size",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users fetched successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid page or size"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request")
            }
    )
    @GetMapping("/pagination")
    public ResponseEntity<Page<UserDto>> getPaginatedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Inside Controller -> getPaginatedUsers");
        Page<UserDto> users = userService.getPaginatedUsers(page, size);
        return ResponseEntity.ok(users);
    }

    /**
     * Fetches a user by their ID.
     *
     * @param userId The ID of the user to fetch.
     * @return ResponseEntity containing the user details.
     */
    @Operation(
            summary = "Get user by ID",
            description = "Fetches a user by their ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request")
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        log.info("Inside Controller -> getUserById");
        UserDto users = userService.getUserById(userId);
        return ResponseEntity.ok(users);
    }
}