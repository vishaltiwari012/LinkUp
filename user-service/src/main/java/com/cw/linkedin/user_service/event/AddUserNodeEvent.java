package com.cw.linkedin.user_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddUserNodeEvent {
    private Long userId;
    private String name;
    private String email;
}
