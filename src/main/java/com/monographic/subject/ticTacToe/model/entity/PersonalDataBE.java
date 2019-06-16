package com.monographic.subject.ticTacToe.model.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "Personal_Data")
public class PersonalDataBE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String surname;

    @Email
    private String email;

    @OneToOne(mappedBy = "personalData")
    private PlayerBE player;

    public PersonalDataBE() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PlayerBE getPlayer() {
        return player;
    }

    public void setPlayer(PlayerBE player) {
        this.player = player;
    }
}
