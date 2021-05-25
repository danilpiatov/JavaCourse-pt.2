package service2.service2;

import java.io.FileNotFoundException;

public interface Service2 {
    String getShips();

    String getShips(String str) throws FileNotFoundException;

    String getReport() throws FileNotFoundException;
}
