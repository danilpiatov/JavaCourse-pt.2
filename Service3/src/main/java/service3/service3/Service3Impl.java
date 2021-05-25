package service3.service3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import service3.entity.Ship;
import service3.entity.StringToCalendar;
import service3.entity.TypeOfCargo;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class Service3Impl implements Service3{
    private static final int SERVICE_TWO_PORT_NUM = 8082;

    @Override
    public List<Ship> getList ()   {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/service2/get/arrivalList";
        String responseEntity = restTemplate.getForObject(url, String.class);
        String str = responseEntity;
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(str);
            JSONArray shipsArray = (JSONArray) jsonObject.get("schedule");
            List<Ship> shipList = new ArrayList<>();

            for (Object o : shipsArray)
            {
                org.json.simple.JSONObject record = (JSONObject) o;
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
                shipList.add(ship);
            }
            return shipList;
        } catch ( ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
