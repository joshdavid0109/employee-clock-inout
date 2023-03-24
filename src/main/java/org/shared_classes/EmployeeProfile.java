package org.shared_classes;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EmployeeProfile implements Serializable {
    private String empID;
    private String userName;
    private String passWord;
    private EmployeeDetails personalDetails;
    private boolean isLoggedIn;
    public String status; // working or break
    private static String note;

    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
    private EmployeeDailyReport employeeDailyReport; // Daily time in/out
    public List<SummaryReport> summaryReport;

    public EmployeeProfile() {}

    public EmployeeProfile(String un) {
        this.userName = un;
    }

    public EmployeeProfile(String un, String pw) {
        this.userName = un;
        this.passWord = pw;
    }

    public EmployeeProfile(String ei, String un, String pw) {
        this.empID = ei;
        this.userName = un;
        this.passWord = pw;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SummaryReport> getSummaryReport() {
        return summaryReport;
    }

    public void setSummaryReport(List<SummaryReport> summaryReport) {
        this.summaryReport = summaryReport;
    }

    public void addSummaryReport(SummaryReport summaryReport)    {
        this.summaryReport.add(summaryReport);
    }

    /**
     * Computes total working hours for each day
     * @throws ParseException
     */
    public void computeWorkingHours() throws ParseException {
        String day;
        for (int i =0 ;i< employeeDailyReport.getListofTimeOuts().size();i++) {
            day = employeeDailyReport.getListofTimeOuts().get(employeeDailyReport.getListofTimeOuts().size()-1).split(", ")[1];
            String timeIn = employeeDailyReport.getListofTimeIns().get(i).split(", ")[1];
            String timeOut = employeeDailyReport.getListofTimeOuts().get(i).split(", ")[1];

            Date d = timeFormat.parse(timeIn);
            Date d2 = timeFormat.parse(timeOut);

            long diff = d2.getTime() - d.getTime();

            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(diff);

            if (minutes<0)
                minutes+=1440;


        }
//        WorkingDays.computeWorkingHours(employeeDailyReport);
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



    public String getFullName() {
        return this.personalDetails.firstName + " " + this.personalDetails.getLastName();
    }

    @Override
    public String toString() {
        return "EMPLOYEE ID: " + empID + "\n" +
                "USERNAME: " + userName + "\n" +
                "PASSWORD: " + passWord + "\n" +
                "PROFILE: \n" + personalDetails + "\n" +
                "DATES: \n" +
                employeeDailyReport + "\n";
    }

    public EmployeeDailyReport getEmployeeDailyReport() {
        return employeeDailyReport;
    }

    public void setEmployeeDailyReport(EmployeeDailyReport employeeDailyReport) {
        this.employeeDailyReport = employeeDailyReport;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    //for table value only
    public String getIsLoggedIn() {
        if (isLoggedIn)
            return "online";
        else
            return "offline";
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
