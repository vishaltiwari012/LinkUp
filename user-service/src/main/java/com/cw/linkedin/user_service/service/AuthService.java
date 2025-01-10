package com.cw.linkedin.user_service.service;

import com.cw.linkedin.user_service.dto.LoginRequestDto;
import com.cw.linkedin.user_service.dto.SignupRequestDto;
import com.cw.linkedin.user_service.dto.UserDto;

public interface AuthService {
    UserDto signup(SignupRequestDto signupRequestDto);

    String login(LoginRequestDto loginRequestDto);
}
