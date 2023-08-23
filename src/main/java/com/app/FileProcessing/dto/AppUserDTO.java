package com.app.FileProcessing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private Long id;
    private String userName;
    private String email;
    private String password;
    private List<RoleDTO> roles;
}
