package service2.service2;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Service
public class Service2Impl implements Service2 {
    private static final int SERVICE_ONE_PORT_NUM = 8080;
    private static final int SERVICE_THREE_PORT_NUM = 8083;

    @Override
    public String getShips() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:" + SERVICE_ONE_PORT_NUM + "/service1/generate";
        ResponseEntity<String> answer = restTemplate.getForEntity(url, String.class);
        String resourceName = ".\\src\\main\\resources\\arrivalList.json";
        try (FileWriter writer = new FileWriter(resourceName)) {
            writer.write(answer.getBody());
            System.out.println("Service 1 done");
        } catch (Exception e) {
            System.out.println(e);
        }
        return answer.getBody();
    }

    @Override
    public String getShips(String str) throws FileNotFoundException {
        String fileName = ".\\src\\main\\resources\\" + str + ".json";
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File doesn't exist!");
        }
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getReport() throws FileNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:" + SERVICE_THREE_PORT_NUM + "/service3/report";
        ResponseEntity<String> answer = restTemplate.getForEntity(url, String.class);
        String fileName = ".\\src\\main\\resources\\statistics.json";
        try (FileWriter fWriter = new FileWriter(fileName)) {
            fWriter.write(answer.getBody());
            System.out.println("Service 3 was written in JSON");
        } catch (IOException e) {
            throw new FileNotFoundException("No file was found with this name");
        }
        return answer.getBody();
    }
}

