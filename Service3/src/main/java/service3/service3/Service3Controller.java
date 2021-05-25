package service3.service3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service3.entity.IntToTime;
import service3.entity.Ship;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

    @RestController
    @RequestMapping("/service3")
    public class Service3Controller {

        private Service3 serviceThree;

        public Service3Controller() throws IOException {
        }

        @Autowired
        public void setServiceThree(Service3 service3) {
            this.serviceThree = service3;
        }

        @GetMapping("/report")
        public String getReport() throws InterruptedException, IOException {
            Port port = new Port(serviceThree.getList());
            return port.simulate();
        }
    }
