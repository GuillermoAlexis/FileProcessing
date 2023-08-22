package com.app.FileProcessing.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
	private Long id;
	private String name;
	private Set<FeatureDTO> features;
}
