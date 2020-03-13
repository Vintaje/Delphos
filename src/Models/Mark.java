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
public class Mark implements Serializable{
    private int id;
    private int cod_student;
    private int cod_teacher;
    private String mark;
    private byte[] signature;

    public Mark(int id, int cod_student, int cod_teacher, String mark) {
        this.id = id;
        this.cod_student = cod_student;
        this.cod_teacher = cod_teacher;
        this.mark = mark;
    }

    public Mark(int id, int cod_student, int cod_teacher, String mark, byte[] signature) {
        this.id = id;
        this.cod_student = cod_student;
        this.cod_teacher = cod_teacher;
        this.mark = mark;
        this.signature = signature;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    
    
    public Mark() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCod_student() {
        return cod_student;
    }

    public void setCod_student(int cod_student) {
        this.cod_student = cod_student;
    }

    public int getCod_teacher() {
        return cod_teacher;
    }

    public void setCod_teacher(int cod_teacher) {
        this.cod_teacher = cod_teacher;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
    
    
}
