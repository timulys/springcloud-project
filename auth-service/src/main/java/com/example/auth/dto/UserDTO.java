package com.example.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class UserDTO {
    private String email;
    private String name;
    private String role;

    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("email", email);
        dataMap.put("name", name);
        dataMap.put("role", role);

        return dataMap;
    }
}
