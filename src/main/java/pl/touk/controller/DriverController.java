package pl.touk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.touk.service.DriverService;
import pl.touk.service.dto.DriverDto;
import pl.touk.service.dto.NewDriverDto;
import pl.touk.service.dto.UpdateDriverDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/new")
    public NewDriverDto startParkingMeter(@RequestBody DriverDto driverDto) {
        log.info("Insert object [ " + driverDto.getRegistrationNumber() + "]");
        return driverService.startParkingMeter(driverDto.getRegistrationNumber());
    }

    @PostMapping("/update")
    public UpdateDriverDto endParkingMeter(@RequestBody DriverDto driverDto) {
        log.info("Update object [" + driverDto.getRegistrationNumber() + "]");
        return driverService.endParkingMeter(driverDto.getRegistrationNumber());
    }
}