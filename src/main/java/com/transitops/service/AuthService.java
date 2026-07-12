package com.transitops.service;

import com.transitops.dto.AuthRequestDTO;
import com.transitops.dto.AuthResponseDTO;
import com.transitops.dto.RegisterRequestDTO;

public interface AuthService {

    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO login(AuthRequestDTO request);
}
