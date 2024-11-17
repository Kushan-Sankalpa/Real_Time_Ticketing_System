package org.example.server.Dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
