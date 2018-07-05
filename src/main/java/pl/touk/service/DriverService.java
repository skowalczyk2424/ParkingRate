package pl.touk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.touk.model.Driver;
import pl.touk.repository.DriverRepository;
import pl.touk.service.dto.NewDriverConverter;
import pl.touk.service.dto.NewDriverDto;
import pl.touk.service.dto.UpdateDriverConverter;
import pl.touk.service.dto.UpdateDriverDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class
DriverService {

    private final DriverRepository driverRepository;
    private final NewDriverConverter newDriverConverter;
    private final UpdateDriverConverter updateDriverConverter;
    private final CurrentTimeService currentTimeService;
    private final ParkingRateService parkingRateService;

    public NewDriverDto startParkingMeter(String registrationNumber) {
        if (StringUtils.isEmpty(registrationNumber)) {
            throw new IllegalArgumentException("Registration number is required");
        }
        Driver driver = new Driver(registrationNumber, currentTimeService.now());
        Driver driverEntity = driverRepository.save(driver);
        log.debug("Insert object with parameters: " + driverEntity.getId() + ", " + driverEntity.getRegistrationNumber() + ", "
                + driverEntity.getStartParkTime());
        return newDriverConverter.convert(driverEntity);
    }

    public UpdateDriverDto endParkingMeter(String registrationNumber) {
        if (StringUtils.isEmpty(registrationNumber)) {
            throw new IllegalArgumentException("Registration number is required");
        }
        Driver driver = driverRepository.getDriver(registrationNumber);
        driver.setEndParkTime((currentTimeService.now()));
        driver.setParkDuration(parkDuration(driver));
        driver.setPayment(parkingRateService.calculateFee(driver));
        driver.setVip(parkingRateService.isVip(registrationNumber));

        Driver driverEntity = driverRepository.save(driver);
        log.debug("Update object with parameters: " + driverEntity.getId() + "," + driverEntity.getRegistrationNumber() + ", "
                + driverEntity.getEndParkTime());
        return updateDriverConverter.convert(driverEntity);
    }

    static Integer parkDuration(Driver driver) {
        long parkDuration = (driver.getEndParkTime().getTime() - driver.getStartParkTime().getTime());
        int temp = (int) (parkDuration / 3600000.);
        log.debug("tart: [" +
                driver.getStartParkTime().getTime() + "], end: [" +
                driver.getEndParkTime().getTime() + "]");
        if (parkDuration % 3600000L != 0) {
            return temp += 1;
        } else {
            return temp;
        }
    }
}
