package com.task.fbresult.model;

import java.time.LocalDate;

public class Person {
    private long id;
    private String login;
    private String fio;
    private String telephone;
    private String address;
    private LocalDate birthday;
    private long roleId;

    public Person(long id, String login, String fio, String telephone, String address, LocalDate birthday, long role) {
        this.id = id;
        this.login = login;
        this.fio = fio;
        this.telephone = telephone;
        this.address = address;
        this.birthday = birthday;
        this.roleId = role;
    }

    public Person(String login, String fio, String telephone, String address, LocalDate birthday, long role) {
        this.login = login;
        this.fio = fio;
        this.telephone = telephone;
        this.address = address;
        this.birthday = birthday;
        this.roleId = role;
    }

    public Person(String login, String fio, long role) {
        this.login = login;
        this.fio = fio;
        this.roleId = role;
    }

    //region get/set

    public String getFio() {
        return fio;
    }
    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return fio.split(" ")[0];
    }

    public String getSurname() {
        return fio.split(" ")[1];
    }

    public String getPatronymic() {
        return fio.split(" ")[2];
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public long getRole() {
        return roleId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setRole(long role) {
        this.roleId = role;
    }

    //endregion
}
