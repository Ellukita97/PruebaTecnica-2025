package com.lucas.customers_service.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "client")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Client extends Person{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    private String password;
    private Boolean status;

    public Client(String name, String gender, Integer age, String identification, String address, String phoneNumber, Long clientId, String password, Boolean status) {
        super(name, gender, age, identification, address, phoneNumber);
        this.clientId = clientId;
        this.password = password;
        this.status = status;
    }


}
