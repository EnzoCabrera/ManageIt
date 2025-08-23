package com.example.estoque.dtos.authDtos;

import com.example.estoque.entities.userEntities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {

    @NotNull
    private String email;

    @NotNull
    private UserRole role;

    @NotNull
    private String password;

    @NotNull
    private Boolean isDeleted;
}
