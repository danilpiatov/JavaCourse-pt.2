package service1.service1;

import service1.entity.Record;
import service1.entity.TypeOfCargo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Generator {
    public static final int LOOSE_CRANE_SPEED = 15;
    public static final int LIQUID_CRANE_SPEED = 20;
    public static final int CONTAINER_CRANE_SPEED = 10;


    public static List<Record> generateRecords(int numOfRecords) {
        List<Record> recordList = new ArrayList<>();
        for (int i = 0; i < numOfRecords; i++) {
            Record record = new Record();
            record.setTimeOfArrival(generateTimeOfArrival());
            record.setNameOfShip(generateNameOfShip());
            record.setTypeOfCargo(generateType());
            record.setAmountOfCargo(generateAmount());
            switch (record.getTypeOfCargo()) {
                case LOOSE -> record.setEstimatedTimeOfStaying(record.getAmountOfCargo() / LOOSE_CRANE_SPEED);
                case LIQUID -> record.setEstimatedTimeOfStaying(record.getAmountOfCargo() / LIQUID_CRANE_SPEED);
                case CONTAINER -> record.setEstimatedTimeOfStaying(record.getAmountOfCargo() / CONTAINER_CRANE_SPEED);
            }
            recordList.add(record);
        }
        return recordList;
    }

    public static GregorianCalendar generateTimeOfArrival() {
        GregorianCalendar time = new GregorianCalendar();
        time.set(GregorianCalendar.YEAR, 2021);
        time.set(GregorianCalendar.MONTH, GregorianCalendar.APRIL);
        time.set(GregorianCalendar.DAY_OF_MONTH, 1 + (int) (Math.random() * 29));
        time.set(GregorianCalendar.HOUR_OF_DAY, (int) (Math.random() * 24));
        time.set(GregorianCalendar.MINUTE, (int) (Math.random() * 60));
        return time;
    }

    public static String generateNameOfShip() {
        String name = "";
        try (FileReader reader = new FileReader(System.getProperty("user.dir") + "/Names")) {
            int stringNumber = 1 + (int) (Math.random() * 2699);
            BufferedReader bufferedReader = new BufferedReader(reader);
            for (int i = 0; i < stringNumber - 1; i++) {
                bufferedReader.readLine();
            }
            name = bufferedReader.readLine();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
        return name;
    }

    public static TypeOfCargo generateType() {
        return TypeOfCargo.values()[(int) (Math.random() * 3)];
    }

    public static Integer generateAmount() {
        return 10000 + (int) (Math.random() * 4901);
    }

}