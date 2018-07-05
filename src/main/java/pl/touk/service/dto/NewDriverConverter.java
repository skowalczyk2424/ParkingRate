package pl.touk.service.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.touk.model.Driver;

@Component
public class NewDriverConverter implements Converter<Driver, NewDriverDto> {

    @Override
    public NewDriverDto convert(Driver driver) {
        return new NewDriverDto(driver.getRegistrationNumber(), driver.getStartParkTime());
    }
}
