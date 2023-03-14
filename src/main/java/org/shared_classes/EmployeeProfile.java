package org.shared_classes;

import java.io.Serializable;
import java.time.*;
import java.util.*;

public class EmployeeProfile implements Serializable {
    String empID;
    String userName;
    String passWord;
    EmployeeDetails personalDetails;
    List<Date> totalDates = new ArrayList<>();

    public EmployeeProfile() {}

    public EmployeeProfile(String ei, String un, String pw) {
        this.empID = ei;
        this.userName = un;
        this.passWord = pw;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setPersonalDetails(EmployeeDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public void setTotalDates(Date totalDates) {
        this.totalDates.add(totalDates);
    }

    public String getEmpID() {
        return empID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public EmployeeDetails getPersonalDetails() {
        return personalDetails;
    }

    public List<Date> getTotalDates() {
        return totalDates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EMPLOYEE ID: ").append(empID).append("\n");
        sb.append("USERNAME: ").append(userName).append("\n");
        sb.append("PASSWORD: ").append(passWord).append("\n");
        sb.append("PROFILE: \n").append(personalDetails).append("\n");
        sb.append("DATES: \n");
        for (Date d : totalDates) {
            sb.append(d).append("\n");
        }
        return sb.toString();
    }
}
