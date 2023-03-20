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
    String workingDays;

    EmployeeDailyReport employeeDailyReport;
    private SimpleDateFormat format1 = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss ");

    public WorkingDays() {
    }
/*
    public static void computeWorkingHours(EmployeeDailyReport edr) throws ParseException {
        for (int i = 0; i < edr.getListofTimeIns().size(); i++) {
            Date d = format2.parse(String.valueOf(edr.getListofTimeIns().get(i)));
            Date d1 = format2.parse(String.valueOf(edr.getListofTimeOuts().get(i)));

            long diff = d1.getTime() - d.getTime();



            int t1 = Integer.parseInt(format2.format(d).split(":")[0]);
            int t2 = Integer.parseInt(format2.format(1).split(":")[0]);
            int workingHours = t2 - t1;
        }
    }*/
}

  /*  @Override
    public String toString() {
        *//*for (Map.Entry<Date, Integer> map:
        workingDays.entrySet()){
            System.out.println("[" +format1.format(map.getKey()) + "] " +  map.getValue());
        }*//*
    }
}*/
