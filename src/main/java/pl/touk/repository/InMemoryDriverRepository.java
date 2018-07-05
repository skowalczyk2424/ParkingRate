package pl.touk.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.touk.model.Driver;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryDriverRepository implements DriverRepository {

    private AtomicLong idGenerator = new AtomicLong();
    private Map<Long, Driver> drivers = new ConcurrentHashMap<>();

    @Override
    public Driver save(Driver driver) {
        drivers.put(idGenerator.getAndIncrement(), driver);
        driver.setId(idGenerator.getAndDecrement());
        log.info("Save " + driver.toString());
        return driver;
    }

    @Override
    public Optional<Driver> findDriver(String registrationNumber) {
        log.debug("Find driver by " + registrationNumber);
        return drivers.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getRegistrationNumber().equals(registrationNumber))
                .map(Map.Entry::getValue)
                .findAny();
    }

    @Override
    public List<Driver> getAllForDay(Date date) {
        LocalDateTime dateTime = asDateTime(date);
        return drivers.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(driver -> isSameDay(dateTime, asDateTime(driver.getEndParkTime())))
                .collect(Collectors.toList());
    }

    static boolean isSameDay(LocalDateTime first, LocalDateTime second) {
        return first.equals(second);
    }

    private static LocalDateTime asDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }


}