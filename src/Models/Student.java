/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author vinta
 */
public class Student extends User{
    private int idgrade;

    public Student() {
    }

    public Student(int idgrade, int id, String name, String pwd, String phoneNumber, String address, int age, byte rol) {
        super(id, name, pwd, phoneNumber, address, age, rol);
        this.idgrade = idgrade;
    }

    public int getIdgrade() {
        return idgrade;
    }

    public void setIdgrade(int idgrade) {
        this.idgrade = idgrade;
    }
    
    
}
