package pl.touk.service;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CurrentTimeService {

    public Date now() {
        return new Date();
    }
}
