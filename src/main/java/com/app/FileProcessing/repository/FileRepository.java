package com.app.FileProcessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.FileProcessing.model.File;

public interface FileRepository extends JpaRepository<File, Long> {

	Object findByFileName(String fileName);

}
