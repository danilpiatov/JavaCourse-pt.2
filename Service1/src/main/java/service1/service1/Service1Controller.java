package service1.service1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service1.entity.Record;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/service1")
public class Service1Controller {
    private Service1 service1;

    @Autowired
    public void setServiceOne(Service1 service) {
        this.service1 = service;
    }

    @GetMapping("/generate")
    public String schedule() {
        List<Record> records = service1.getList();
        JSONArray service1JSON = new JSONArray();
        for (Record record : records) {
            JSONObject recordJSON = new JSONObject();
            SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd yyyy HH:mm");
            recordJSON.put("Time of arrival", formatter.format(record.getTimeOfArrival().getTime()));
            recordJSON.put("Name of the ship", record.getNameOfShip());
            recordJSON.put("Type of cargo", record.getTypeOfCargo().toString());
            recordJSON.put("Amount of cargo", record.getAmountOfCargo());
            recordJSON.put("Estimated time of staying", record.getEstimatedTimeOfStaying());
            service1JSON.add(recordJSON);
        }
        JSONObject out = new JSONObject();
        out.put("schedule", service1JSON);
        return out.toString();
    }
}
