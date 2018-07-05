package pl.touk.service.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.touk.model.Driver;

@Component
public class UpdateDriverConverter implements Converter<Driver, UpdateDriverDto> {

    @Override
    public UpdateDriverDto convert(Driver driver) {
        return new UpdateDriverDto(
                driver.getRegistrationNumber(),
                driver.getStartParkTime(),
                driver.getEndParkTime(),
                driver.getParkDuration(),
                driver.getPayment(),
                driver.getVip());
    }
}
