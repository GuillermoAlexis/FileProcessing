package com.app.FileProcessing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "validation_detail")
public class ValidationDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "file_id")
	private File file;

	@Column(name = "line_number", nullable = false)
	private Integer lineNumber;

	@Column(name = "field_name", nullable = false)
	private String fieldName;

	@Column(name = "error_code", nullable = false)
	private Integer errorCode;

	@Column(name = "error_message", nullable = false)
	private String errorMessage;
}