package com.example.User.dto;

import com.example.User.entity.Coordinates;
import com.example.User.entity.Street;
import com.example.User.entity.Timezone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {

    private StreetDto street;
    private String city;
    private String state;
    private String country;
    private String postcode;
    private CoordinatesDto coordinates;
    private TimeZoneDto timezone;
}
