package com.aws.account.entity;

import com.aws.client.entity.Client;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private double balance;
    private int agency;
    private int number;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Client owner;
    private static int total = 0;

}
