package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstname;
    private String lastname;

    private LocalDate birthdaydate;

    private String email;
    private String cin;
    private String role;
    private String telephone;

    @Column(name = "pass_word")
    private String password;

    @Column(name = "preferred_categ")
    private String preferredCategories;

    @Column(name = "preferred_locati")
    private String preferredLocations;

    @Column(name = "budget_max")
    private Integer budgetMax;

    @Column(name = "preferred_actors")
    private String preferredActors;



    // GETTERS + SETTERS

    public Integer getId() { return id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public LocalDate getBirthdaydate() { return birthdaydate; }
    public void setBirthdaydate(LocalDate birthdaydate) { this.birthdaydate = birthdaydate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPreferredCategories() { return preferredCategories; }
    public void setPreferredCategories(String preferredCategories) { this.preferredCategories = preferredCategories; }

    public String getPreferredLocations() { return preferredLocations; }
    public void setPreferredLocations(String preferredLocations) { this.preferredLocations = preferredLocations; }

    public Integer getBudgetMax() { return budgetMax; }
    public void setBudgetMax(Integer budgetMax) { this.budgetMax = budgetMax; }

    public String getPreferredActors() { return preferredActors; }
    public void setPreferredActors(String preferredActors) { this.preferredActors = preferredActors; }

    public int getAge() {
        if (birthdaydate == null) return 0;
        return LocalDate.now().getYear() - birthdaydate.getYear();
    }
    
}