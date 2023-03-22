package org.shared_classes;

import java.util.ArrayList;
import java.util.List;

public class SummaryReport {
    private List<String> timeIns;
    private List<String> timeOuts ;
    private String date;

    public SummaryReport(List<String> ins, List<String> outs, String date) {
        this.timeIns = ins;
        this.timeOuts = outs;
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
}
