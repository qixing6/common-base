package com.example.commonbase.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private String username;
    private Long userId;
    private String roles;
}
