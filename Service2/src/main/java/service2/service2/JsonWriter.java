package service2.service2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import service2.entity.Record;
import service2.entity.Ship;
import service2.entity.StringToCalendar;
import service2.entity.TypeOfCargo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonWriter {

    private List<Ship> liquidShipList = new ArrayList<>();
    private List<Ship> looseShipList = new ArrayList<>();
    private List<Ship> containerShipList = new ArrayList<>();

    public void readS1(String string) {
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
}
