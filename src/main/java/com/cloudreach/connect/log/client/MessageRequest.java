package com.cloudreach.connect.log.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
class MessageRequest {

    private Long level;
    private String message;

}
