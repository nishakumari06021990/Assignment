package com.example.User.utility;

import com.example.User.dto.CoordinatesDto;
import com.example.User.dto.DobDto;
import com.example.User.dto.IdDto;
import com.example.User.dto.LocationDto;
import com.example.User.dto.LoginDto;
import com.example.User.dto.NameDto;
import com.example.User.dto.PictureDto;
import com.example.User.dto.RegisteredDto;
import com.example.User.dto.StreetDto;
import com.example.User.dto.TimeZoneDto;
import com.example.User.dto.UserDto;
import com.example.User.entity.User;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .userId(user.getUserId())
                .gender(user.getGender())
                .id(user.getId() != null ? IdDto.builder()
                        .name(user.getId().getIdName())
                        .value(user.getId().getValue())
                        .build() : null)
                .name(user.getName() != null ? NameDto.builder()
                        .title(user.getName().getTitle())
                        .first(user.getName().getFirst())
                        .last(user.getName().getLast())
                        .build() : null)
                .location(user.getLocation() != null ? LocationDto.builder()
                        .street(user.getLocation().getStreet() != null ? StreetDto.builder()
                                .number(user.getLocation().getStreet().getNumber())
                                .streetName(user.getLocation().getStreet().getStreetName())
                                .build() : null)
                        .city(user.getLocation().getCity())
                        .state(user.getLocation().getState())
                        .country(user.getLocation().getCountry())
                        .coordinates(user.getLocation().getCoordinates() != null ? CoordinatesDto.builder()
                                .latitude(user.getLocation().getCoordinates().getLatitude())
                                .longitude(user.getLocation().getCoordinates().getLongitude())
                                .build() : null)
                        .timezone(user.getLocation().getTimezone() != null ? TimeZoneDto.builder()
                                .offset(user.getLocation().getTimezone().getOffset())
                                .description(user.getLocation().getTimezone().getDescription())
                                .build() : null)
                        .postcode(user.getLocation().getPostcode())
                        .build() : null)
                .email(user.getEmail())
                .login(user.getLogin() != null ? LoginDto.builder()
                        .username(user.getLogin().getUsername())
                        .uuid(user.getLogin().getUuid())
                        .md5(user.getLogin().getMd5())
                        .salt(user.getLogin().getSalt())
                        .sha1(user.getLogin().getSha1())
                        .sha256(user.getLogin().getSha256())
                        .password(user.getLogin().getPassword())
                        .build() : null)
                .dob(user.getDob() != null ? DobDto.builder()
                        .dobDate(user.getDob().getDobDate())
                        .dobAge(user.getDob().getDobAge())
                        .build() : null)
                .registered(user.getRegistered() != null ? RegisteredDto.builder()
                        .date(user.getRegistered().getDate())
                        .age(user.getRegistered().getAge())
                        .build() : null)
                .phone(user.getPhone())
                .cell(user.getCell())
                .picture(user.getPicture() != null ? PictureDto.builder()
                        .large(user.getPicture().getLarge())
                        .medium(user.getPicture().getMedium())
                        .thumbnail(user.getPicture().getThumbnail())
                        .build() : null)
                .nat(user.getNat())
                .build();
    }
}
