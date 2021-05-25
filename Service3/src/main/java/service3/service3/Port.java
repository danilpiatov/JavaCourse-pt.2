package service3.service3;

import service3.entity.Ship;
import service3.entity.StringToCalendar;
import service3.entity.TypeOfCargo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Port {
    public static Integer speedOfLiquidCrane = 20; //Tons per minute
    public static Integer speedOfLooseCrane = 15; //Tons per minute
    public static Integer speedOfContainerCrane = 10; //containers per minute
    public static final Integer CRANE_FINE = 30000;
    public static final Integer WAIT_FINE = 100;

    private List<Ship> liquidShipList = new ArrayList<>();
    private List<Ship> looseShipList = new ArrayList<>();
    private List<Ship> containerShipList = new ArrayList<>();

    private void readFromJSON() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray schedule = (JSONArray) parser.parse(new FileReader(System.getProperty("user.dir") + "/arrivalList.json"));

            for (Object o : schedule)
            {
                JSONObject record = (JSONObject) o;
                Ship ship = new Ship();
                ship.setNameOfUnloadedShip((String) record.get("Name of the ship"));
                String type = record.get("Type of cargo").toString();
                switch (type) {
                    case "LIQUID" -> ship.setTypeOfCargo(TypeOfCargo.LIQUID);
                    case "LOOSE" -> ship.setTypeOfCargo(TypeOfCargo.LOOSE);
                    case "CONTAINER" -> ship.setTypeOfCargo(TypeOfCargo.CONTAINER);
                }
                GregorianCalendar timeOfArrival =  StringToCalendar.convert(record.get("Time of arrival").toString());
                timeOfArrival.add(Calendar.MINUTE, -10080 + (int) (Math.random() * 20160));
                ship.setRealTimeOfArrival(timeOfArrival);
                ship.setEstimatedTimeOfUnloading(Math.toIntExact((Long) record.get("Estimated time of staying")));
                ship.setTimeOfUnloading(ship.getEstimatedTimeOfUnloading());
                ship.amountOfCargo = Math.toIntExact((Long) record.get("Amount of cargo"));
                switch (ship.getTypeOfCargo()) {
                    case LIQUID -> liquidShipList.add(ship);
                    case LOOSE -> looseShipList.add(ship);
                    case CONTAINER -> containerShipList.add(ship);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public Port(List<Ship> list) {
        for (Ship ship: list) {
            switch (ship.getTypeOfCargo()) {
                case LIQUID -> liquidShipList.add(ship);
                case LOOSE -> looseShipList.add(ship);
                case CONTAINER -> containerShipList.add(ship);
            }
        }
        liquidShipList.sort(Comparator.comparing(Ship::getRealTimeOfArrival));
        looseShipList.sort(Comparator.comparing(Ship::getRealTimeOfArrival));
        containerShipList.sort(Comparator.comparing(Ship::getRealTimeOfArrival));
    }

    public String simulate() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Unloader unloader1 = new Unloader(looseShipList);
        Unloader unloader2 = new Unloader(containerShipList);
        Unloader unloader3 = new Unloader(liquidShipList);
        Future future1 = executor.submit(unloader1);
        Future future2 = executor.submit(unloader2);
        Future future3 = executor.submit(unloader3);

        List<Ship> unloadedShipList = new ArrayList<Ship>();
        try {
            unloadedShipList.addAll((Collection<Ship>) future1.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try {
            unloadedShipList.addAll((Collection<Ship>) future2.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try {
            unloadedShipList.addAll((Collection<Ship>) future3.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        JSONObject stats = new JSONObject();
        stats.put("Number of unloaded ships", unloadedShipList.size());
        stats.put("Average queue length", (unloader1.getAvgQueueLength() + unloader2.getAvgQueueLength() + unloader3.getAvgQueueLength())/3);
        stats.put("Final fine", unloader1.getFinalCost()+unloader2.getFinalCost()+unloader3.getFinalCost());
        Double avgWaitingTime = 0.0;
        Integer maxWaitingTime = 0;
        for (Ship s: unloadedShipList) {
            avgWaitingTime += s.getTimeOfWaiting();
            maxWaitingTime = Math.max(maxWaitingTime, s.getTimeOfWaiting());
        }
        stats.put("Maximum waiting time", maxWaitingTime);
        stats.put("Average waiting time", avgWaitingTime / unloadedShipList.size());
        stats.put("Loose cranes needed", unloader1.getFinalNumOfCranes());
        stats.put("Container cranes needed", unloader2.getFinalNumOfCranes());
        stats.put("Liquid cranes needed", unloader3.getFinalNumOfCranes());

        JSONArray ships = StatisticsJSON.writeS3(unloadedShipList);
        JSONObject out = new JSONObject();
        out.put("Statistics", stats);
        out.put("Ships", ships);
        return out.toString();
    }
}