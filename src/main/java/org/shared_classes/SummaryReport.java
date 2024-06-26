package org.shared_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SummaryReport implements Serializable {
    private String empID;
    private String date;
    private List<String> timeIns;
    private List<String> timeOuts ;

    public SummaryReport(String date) {
        this.date = date;
    }

    public List<String> getTimeIns() {
        return timeIns;
    }

    public void setTimeIns(List<String> timeIns) {
        this.timeIns = timeIns;
    }

    public List<String> getTimeOuts() {
        return timeOuts;
    }

    public void setTimeOuts(List<String> timeOuts) {
        this.timeOuts = timeOuts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }
}
