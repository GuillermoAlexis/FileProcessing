package com.app.FileProcessing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserWithoutRolesDTO {
	private Long id;
	private String userName;
	private String email;
	private String password;
}
