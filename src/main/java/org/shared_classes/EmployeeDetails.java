package org.shared_classes;

import java.io.Serializable;

public class EmployeeDetails implements Serializable {

    String firstName;
    String lastName;
    int age;
    String gender;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public EmployeeDetails() {}

    public EmployeeDetails(String fn, String ln, int a, String g) {
        this.firstName = fn;
        this.lastName = ln;
        this.age = a;
        this.gender = g;
    }

    public EmployeeDetails(String fn, String ln, int a) {
        this.firstName = fn;
        this.lastName = ln;
        this.age = a;
    }

    @Override
    public String toString() {
        return "employee " + firstName.toUpperCase() + " " + lastName.toUpperCase() + " who is " + age +
                " years old, and is of " + gender.toUpperCase() + " gender.";
    }
}
