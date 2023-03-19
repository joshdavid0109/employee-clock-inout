package org.shared_classes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeProfile implements Serializable {
    private String empID;
    private String userName;
    private String passWord;
    private EmployeeDetails personalDetails;
    private boolean isLoggedIn;
    static String status; // working or break
    static String note;

    private EmployeeDailyReport employeeDailyReport; // Daily time in/out
    private final List<EmployeeDailyReport> totalDates = new ArrayList<>();


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

    public void setTotalDates(EmployeeDailyReport totalDates) {
        this.totalDates.add(totalDates);
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
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

    public List<EmployeeDailyReport> getTotalDates() {
        return totalDates;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getFullName() {
        return this.personalDetails.firstName + " " + this.personalDetails.getLastName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EMPLOYEE ID: ").append(empID).append("\n");
        sb.append("USERNAME: ").append(userName).append("\n");
        sb.append("PASSWORD: ").append(passWord).append("\n");
        sb.append("PROFILE: \n").append(personalDetails).append("\n");
        sb.append("DATES: \n");
        for (EmployeeDailyReport d : totalDates) {
            sb.append(d).append("\n");
        }
        return sb.toString();
    }

    public EmployeeDailyReport getEmployeeDailyReport() {
        return employeeDailyReport;
    }

    public void setEmployeeDailyReport(EmployeeDailyReport employeeDailyReport) {
        this.employeeDailyReport = employeeDailyReport;
    }
}
