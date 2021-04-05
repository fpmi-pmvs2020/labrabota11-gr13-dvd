package com.task.fbresult.model;

import java.util.Arrays;
import java.util.Objects;

public class Person extends FBModel{
    private String login;
    private String fio;
    private String telephone;
    private String address;
    private String birthday;
    private byte[] avatar;
    private String roleId;

    public Person() {
    }

    public Person(String id, String login, String fio, String telephone, String address, String birthday, byte[] avatar, String role) {
        super(id);
        this.login = login;
        this.fio = fio;
        this.telephone = telephone;
        this.address = address;
        this.birthday = birthday;
        this.roleId = role;
        this.avatar = avatar;
    }

    public Person(String login, String fio, String telephone, String address, String birthday, byte[] avatar, String role) {
        this.login = login;
        this.fio = fio;
        this.telephone = telephone;
        this.address = address;
        this.birthday = birthday;
        this.roleId = role;
        this.avatar = avatar;
    }

    public Person(String login, String fio, String role) {
        this.login = login;
        this.fio = fio;
        this.roleId = role;
    }

    //region get/set

    public String getFio() {
        return fio;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return fio.split(" ")[1];
    }

    public String getSurname() {
        return fio.split(" ")[0];
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

    public String getBirthday() {
        return birthday;
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

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return getFirebaseId().equals(person.getFirebaseId()) &&
                roleId.equals(person.roleId) &&
                Objects.equals(login, person.login) &&
                Objects.equals(fio, person.fio) &&
                Objects.equals(telephone, person.telephone) &&
                Objects.equals(address, person.address) &&
                Objects.equals(birthday, person.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirebaseId(), login, fio, telephone, address, birthday, roleId);
    }

    public String getSurnameWithInitials(){
        return getSurname() + " " + getName().substring(0, 1) + ". " + getPatronymic().substring(0, 1) + ".";
    }

    @Override
    public String toString() {
        return "Person{" +
                "login='" + login + '\'' +
                ", fio='" + fio + '\'' +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                ", avatar=" + Arrays.toString(avatar) +
                ", roleId='" + roleId + '\'' +
                '}';
    }

    //endregion
}
