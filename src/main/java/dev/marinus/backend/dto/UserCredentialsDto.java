package dev.marinus.backend.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.marinus.backend.model.Credentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonTypeName("user")
public class UserCredentialsDto extends Credentials {

    private String password;

    public UserCredentialsDto(String userName, String password) {
        super(userName);
        this.password = password;
    }

}
