package org.mainfolder;

public class Employee {

    String firstName;
    String lastName;
    int age;
    boolean isLoggedIn;

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

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Employee() {}

    public Employee(String fn, String ln, int a, boolean ili) {
        this.firstName = fn;
        this.lastName = ln;
        this.age = a;
        this.isLoggedIn = ili;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", isLoggedIn=" + isLoggedIn +
                '}';
    }
}
