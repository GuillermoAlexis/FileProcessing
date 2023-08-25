package com.app.FileProcessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.FileProcessing.model.ValidationDetail;

public interface ValidationDetailRepository extends JpaRepository<ValidationDetail, Long> {

}
