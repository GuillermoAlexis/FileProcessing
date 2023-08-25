package com.app.FileProcessing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.FileProcessing.model.ValidationDetail;

public interface ValidationDetailRepository extends JpaRepository<ValidationDetail, Long> {

	List<ValidationDetail> findByFile_IdAndErrorCode(Long fileId, Integer errorCodeInt);

}
