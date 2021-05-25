package service3.service3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import service3.entity.IntToTime;
import service3.entity.Ship;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StatisticsJSON {

    public static JSONArray writeS3(List<Ship> unloadedShips) throws IOException {

        JSONArray service3JSON = new JSONArray();
        for (Ship s: unloadedShips) {
            JSONObject recordJSON = new JSONObject();
            SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd yyyy HH:mm");
            recordJSON.put("Name of the ship", s.getNameOfUnloadedShip());
            recordJSON.put("Time of arrival", formatter.format(s.getRealTimeOfArrival().getTime()));
            recordJSON.put("Time of waiting", IntToTime.convert(s.getTimeOfWaiting()));
            recordJSON.put("Time of the start of unloading", formatter.format(s.getStartTimeOfUnloading().getTime()));
            recordJSON.put("Time of unloading", s.getRealTimeOfUnloading());
            service3JSON.add(recordJSON);
        }
        try (FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/statistics.json")) {
            fileWriter.write(service3JSON.toJSONString());
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return service3JSON;
    }
}
