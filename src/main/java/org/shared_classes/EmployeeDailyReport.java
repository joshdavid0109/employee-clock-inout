package org.shared_classes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeDailyReport implements Serializable {
    private String timeIn;
    private String timeOut;
    private String status;
    private String date;


    private List<String> listofTimeIns = new ArrayList<>();;
    private final List<String> listofTimeOuts= new ArrayList<>();;
//    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMMM-dd");

    public EmployeeDailyReport(String date) {
        this.date = date;
    }

    public String getTimeIn() {
        return timeIn == null ? "" :timeIn.split(",")[1];
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
        if (!(timeIn == null))
            listofTimeIns.add(timeIn);
    }

    public String getTimeOut() {
        return timeOut == null ? "" :timeOut.split(",")[1];
    }

    public void setTimeOut(String timeOutFromServer) {
        this.timeOut = timeOutFromServer;
        if (!(timeOutFromServer == null))
            listofTimeOuts.add(timeOutFromServer);

    }

    public void setListofTimeIns(List<String> listofTimeIns) {
        this.listofTimeIns = listofTimeIns;
    }

    public List<String> getListofTimeIns() {
        return this.listofTimeIns;
    }

    public List<String> getListofTimeOuts() {
        return this.listofTimeOuts;
    }

/*
    @Override
    public String toString() {
        return "[" + dateFormat.format(date) + "] : " + "\nTime in: " + timeFormat.format(timeIn) + "\nTime out: " + timeFormat.format(timeOut);
    }
*/

    @Override
    public String toString() {
        return timeIn + "\n" +
                timeOut + "\n" +
                listofTimeIns + "\n" +
                listofTimeOuts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
