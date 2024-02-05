package dev.marinus.backend.dto;

import dev.marinus.backend.model.UserListRequestType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserListRequestDto {

    private UserListRequestType type;

}
