package com.lucas.customers_service.model.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClientResponse {

    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String identification;
    private String address;
    private String phoneNumber;
    private String password;
    private Boolean status;
}
