package pl.touk.repository;

import pl.touk.exception.RecordNotFoundException;
import pl.touk.model.Driver;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DriverRepository {

    Driver save(Driver driver);

    default Driver getDriver(String registrationNumber) {
        return findDriver(registrationNumber)
                .orElseThrow(() -> new RecordNotFoundException("Driver [" + registrationNumber + "] not exist"));
    }

    Optional<Driver> findDriver(String registrationNumber);

    List<Driver> getAllForDay(Date date);
}
