package org.shared_classes;

import org.w3c.dom.ls.LSOutput;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkingDays {
    //    HashMap<Date, Integer> workingDays; // Date - Working hours
    static SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
    public static HashMap<String, Integer> workingHoursPerDay;
    private String day;
    private int hours;
    EmployeeDailyReport employeeDailyReport;
    public static SimpleDateFormat format1 = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss ");

    public WorkingDays() {
        workingHoursPerDay = new HashMap<>();
    }

    public static void computeWorkingHours(EmployeeDailyReport edr) throws ParseException {
        for (int i = 0; i < edr.getListofTimeIns().size(); i++) {
            Date d = format2.parse(String.valueOf(edr.getListofTimeIns().get(i)));
            Date d1 = format2.parse(String.valueOf(edr.getListofTimeOuts().get(i)));

            long diff = d1.getTime() - d.getTime();

//            int minutes =
        }
    }

    public static void main(String[] args) {
//        computeWorkingHours();
    }
}

  /*  @Override
    public String toString() {
        *//*for (Map.Entry<Date, Integer> map:
        workingDays.entrySet()){
            System.out.println("[" +format1.format(map.getKey()) + "] " +  map.getValue());
        }*//*
    }
}*/
