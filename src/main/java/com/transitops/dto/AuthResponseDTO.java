package com.transitops.dto;

import com.transitops.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponseDTO {

    private Long userId;
    private String fullName;
    private String email;
    private RoleType role;
    private String authenticationType;
}
