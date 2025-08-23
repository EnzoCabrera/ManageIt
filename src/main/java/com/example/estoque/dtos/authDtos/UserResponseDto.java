package com.example.estoque.dtos.authDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {

    @NotNull
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String role;

    @NotNull
    private Boolean isDeleted;
}
