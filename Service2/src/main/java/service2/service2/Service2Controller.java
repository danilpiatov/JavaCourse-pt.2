package service2.service2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("/service2")
public class Service2Controller {

    private Service2 service2;

    @Autowired
    public void setService2(Service2 service2) {
        this.service2 = service2;
    }

    @GetMapping("/get")
    public String getList() {
        return service2.getShips();
    }

    @GetMapping("/get/{filename}")
    public String getList(@PathVariable("filename") String fileName) {
        try {
            String str = service2.getShips(fileName);
            if (str.isEmpty())
            {
                return "File is empty";
            }
            return str;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found!");
        }
    }

    @PostMapping("/getReport")
    public String getReport(@RequestBody String string) {
        String str = null;
        try {
            str = service2.getReport();

        }
            /*if (string.equals("\"report\""))
            {
                return service2.getReport();
            }
            else {
                FileWriter writer = new FileWriter(".\\src\\main\\resources\\statistics.json");
                writer.write("");
                return null;
            }
        }         */
        catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found!");
        }
        return str;
    }
}