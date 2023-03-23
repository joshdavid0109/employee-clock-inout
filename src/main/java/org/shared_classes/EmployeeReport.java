package org.shared_classes;

public class EmployeeReport {
    private String timeIn;
    private String timeOut;
    private String date;


    public EmployeeReport( String timeIn, String timeOut) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public EmployeeReport( String timeIn, String timeOut, String date) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.date = date;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
