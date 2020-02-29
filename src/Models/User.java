/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;

/**
 *
 * @author vinta
 */
public class User implements Serializable{
    private int id;
    private String name;
    private String pwd;
    private String phoneNumber;
    private String address;
    private int age;
    private byte rol;
    
    public User(){}

    public User(int id, String name, String pwd, String phoneNumber, String address, int age, byte rol) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.age = age;
        this.rol = rol;
    }

    public User(String name, String pwd, String phoneNumber, String address, int age) {
        this.name = name;
        this.pwd = pwd;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.age = age;
    }
    
    
    
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", pwd=" + pwd + ", address=" + address + ", age=" + age + ", rol=" + rol + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public byte getRol() {
        return rol;
    }

    public void setRol(byte rol) {
        this.rol = rol;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    
}
