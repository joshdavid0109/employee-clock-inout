package org.shared_classes;

public class EmployeeReport {
    private String timeIn;
    private String timeOut;


    public EmployeeReport( String timeIn, String timeOut) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
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

}
