package com.swasthyamitra.healthportal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("java:S1068")
public class CommonDto {

    private String message;
    private String data;

    public CommonDto(String m){
        message = m;
    }

    public CommonDto(String m, String d){
        message = m;
        data = d;
    }
}
