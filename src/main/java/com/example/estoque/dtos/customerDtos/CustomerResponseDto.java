package com.example.estoque.dtos.customerDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerResponseDto {

    @NotNull
    private Long codcus;

    @NotNull
    private String cusname;

    @NotNull
    private String cusaddr;

    @NotNull
    private String cuscity;

    @NotNull
    private String cusstate;

    @NotNull
    private String cuszip;

    @NotNull
    private String cusphone;

    @NotNull
    private String cusemail;

    @NotNull
    private String updatedBy;

    @NotNull
    private String updatedAt;
}
