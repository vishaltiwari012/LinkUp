package com.cw.linkedin.user_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AddUserNodeEvent {
    private Long userId;
    private String name;
    private String email;
}
