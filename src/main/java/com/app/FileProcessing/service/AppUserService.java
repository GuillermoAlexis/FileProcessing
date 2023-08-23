package com.app.FileProcessing.service;

import com.app.FileProcessing.dto.AppUserDTO;

import java.util.List;

public interface AppUserService {
    AppUserDTO createAppUser(AppUserDTO appUserDTO);

    List<AppUserDTO> listAppUsers();

    AppUserDTO getAppUserById(Long id);

    AppUserDTO updateAppUser(Long id, AppUserDTO updatedAppUserDTO);

    void deleteAppUser(Long id);

    AppUserDTO login(String email, String password);
}
