package com.cw.linkedin.user_service.service.impl;

import com.cw.linkedin.user_service.dto.LoginRequestDto;
import com.cw.linkedin.user_service.dto.SignupRequestDto;
import com.cw.linkedin.user_service.dto.UserDto;
import com.cw.linkedin.user_service.entity.User;
import com.cw.linkedin.user_service.event.AddUserNodeEvent;
import com.cw.linkedin.user_service.exceptions.BadRequestException;
import com.cw.linkedin.user_service.exceptions.ResourceNotFoundException;
import com.cw.linkedin.user_service.repository.UserRepository;
import com.cw.linkedin.user_service.security.JWTService;
import com.cw.linkedin.user_service.service.AuthService;
import com.cw.linkedin.user_service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;
    private final KafkaTemplate<Long, AddUserNodeEvent> kafkaTemplate;

    @Override
    public UserDto signup(SignupRequestDto signupRequestDto) {
        boolean isUserExist = userRepository.existsByEmail(signupRequestDto.getEmail());
        if (isUserExist) {
            throw new BadRequestException("User already exists, cannot signup again!");
        }
        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(PasswordUtil.hasPassword(signupRequestDto.getPassword()));
        User savedUser = userRepository.save(user);

        AddUserNodeEvent addUserNodeEvent = AddUserNodeEvent.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .build();
        kafkaTemplate.send("add-user-node-topic", addUserNodeEvent);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email : " + loginRequestDto.getEmail()));

        boolean isPasswordMatched = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());
        if(!isPasswordMatched) {
            throw new BadRequestException("Incorrect password");
        }
        return jwtService.generateAccessToken(user);
    }
}
