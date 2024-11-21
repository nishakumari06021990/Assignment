package com.example.User.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDto {

    private Long userId;
    private String gender;
    private IdDto id;
    private NameDto name;
    private LocationDto location;
    private String email;
    private LoginDto login;
    private DobDto dob;
    private RegisteredDto registered;
    private String phone;
    private String cell;
    private PictureDto picture;
    private String nat;

}

