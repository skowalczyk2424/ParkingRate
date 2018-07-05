package pl.touk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.touk.service.ParkingRateService;

import java.math.BigDecimal;
import java.text.ParseException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParkingRateController {

    private final ParkingRateService parkingRateService;

    @GetMapping("/checkStartMeterForDriver/{registrationNumber}")
    public boolean checkStartMeterForDriver(@PathVariable String registrationNumber) {
        log.info("Check driver [" + registrationNumber + "]");
        return parkingRateService.checkStartMeterForDriver(registrationNumber);
    }

    @GetMapping("/earning/{date}")
    public BigDecimal operatorEarnings(@PathVariable String date) throws ParseException {
        log.info("Checks earning [" + date + "]");
        return parkingRateService.allDayEarnings(date);
    }
}