package com.transitops.service.impl;

import com.transitops.dto.AuthRequestDTO;
import com.transitops.dto.AuthResponseDTO;
import com.transitops.dto.RegisterRequestDTO;
import com.transitops.entity.Role;
import com.transitops.entity.User;
import com.transitops.exception.BusinessException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.RoleRepository;
import com.transitops.repository.UserRepository;
import com.transitops.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Email is already registered");
        }

        Role role = roleRepository.findByRoleName(request.getRole())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(request.getRole());
                    newRole.setDescription(request.getRole().name());
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setActive(true);

        User saved = userRepository.save(user);
        log.info("Registered user {}", saved.getEmail());
        return toResponse(saved);
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("User logged in {}", user.getEmail());
        return toResponse(user);
    }

    private AuthResponseDTO toResponse(User user) {
        return AuthResponseDTO.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole() == null ? null : user.getRole().getRoleName())
                .authenticationType("HTTP_BASIC")
                .build();
    }
}
