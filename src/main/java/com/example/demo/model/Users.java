

package com.example.demo.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstname;

    private String lastname;

    @Column(unique = true)
    private String email;

    private String passWord;

    private String telephone;

    private String cin;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;
}