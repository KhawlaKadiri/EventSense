package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstname;

    private String lastname;

    private LocalDate birthdaydate;

    @Column(name = "pass_word")
    private String password;

    public Person() {}

    public Person(String firstname, String lastname, LocalDate birthdaydate, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdaydate = birthdaydate;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthdaydate() {
        return birthdaydate;
    }

    public void setBirthdaydate(LocalDate birthdaydate) {
        this.birthdaydate = birthdaydate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
