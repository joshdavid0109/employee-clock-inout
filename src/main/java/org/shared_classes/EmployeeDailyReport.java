package org.shared_classes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeDailyReport implements Serializable {
    private Date timeIn;
    private Date timeOut;
    private String status;


    private List<Date> listofTimeIns;
    private List<Date> listofTimeOuts;
//    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMMM-dd");

    public EmployeeDailyReport() {;
        listofTimeIns = new ArrayList<>();
        listofTimeOuts= new ArrayList<>();
    }

    public Date getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(Date timeIn) {
        this.timeIn = timeIn;
        listofTimeIns.add(timeIn);

    }

    public Date getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Date timeOutFromServer) {
        this.timeOut = timeOutFromServer;
        listofTimeOuts.add(timeOutFromServer);
        listofTimeOuts.add(timeOutFromServer);

    }

    public List<Date> getListofTimeIns() {
        return this.listofTimeIns;
    }

    public List<Date> getListofTimeOuts() {
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
        return listofTimeIns + "\n" + listofTimeOuts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
