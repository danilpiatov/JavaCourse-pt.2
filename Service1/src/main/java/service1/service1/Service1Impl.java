package service1.service1;

import org.springframework.stereotype.Service;
import service1.entity.Record;

import java.util.List;

@Service
public class Service1Impl implements Service1 {
    private static final int NUM_OF_RECORDS = 100;

    @Override
    public List<Record> getList() {
        return Generator.generateRecords(NUM_OF_RECORDS);
    }

}
