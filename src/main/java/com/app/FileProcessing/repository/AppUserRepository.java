package com.app.FileProcessing.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.app.FileProcessing.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}
