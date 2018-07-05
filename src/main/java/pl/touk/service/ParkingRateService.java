package pl.touk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import pl.touk.model.CustomerType;
import pl.touk.model.Driver;
import pl.touk.model.ParkingRate;
import pl.touk.repository.DriverRepository;
import pl.touk.repository.ParkingRateRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@DependsOn({"parkingRateInitialValues"})
@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingRateService {

    private final DriverRepository driverRepository;
    private final ParkingRateRepository parkingRateRepository;

    private ParkingRate regular;
    private ParkingRate vip;
    private BigDecimal payment = BigDecimal.valueOf(0.0);

    @PostConstruct
    public void init() {
        List<ParkingRate> rates = parkingRateRepository.findAll();
        rates.forEach(rate -> {
            if (rate.getCustomerType() == CustomerType.VIP) {
                vip = rate;
            } else if (rate.getCustomerType() == CustomerType.REGULAR) {
                regular = rate;
            } else {
                throw new IllegalStateException("Inconsistent configuration");
            }
        });
    }

    public BigDecimal calculateFee(Driver driver) {
        int hour = driver.getParkDuration();
        driver.setVip(isVip(driver.getRegistrationNumber()));
        if (driver.getVip()) {
            payment = calculate(hour, vip);
        } else {
            payment = calculate(hour, regular);
        }
        return payment.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculate(int hour, ParkingRate configuration) {
        if (hour <= 1) {
            payment = configuration.getFirstHour();
        } else if (hour <= 2) {
            payment = configuration.getFirstHour().add(configuration.getSecondHour());
        } else {
            payment = configuration.getFirstHour().add(configuration.getSecondHour());
            BigDecimal prevHour = configuration.getSecondHour();
            for (int i = 2; i < hour; i++) {
                prevHour = prevHour.multiply(configuration.getFactor());
                payment = payment.add(prevHour);
            }
        }
        return payment;
    }

    public boolean isVip(String registrationNumber) {
        return parkingRateRepository.isVip(registrationNumber);
    }

    public boolean checkStartMeterForDriver(String registrationNumber) {
        log.debug("Check driver by " + registrationNumber);
        return driverRepository.findDriver(registrationNumber)
                .filter(driver -> driver.getStartParkTime() != null && driver.getEndParkTime() == null)
                .map(driver -> true)
                .orElse(false);
    }

    public BigDecimal allDayEarnings(String date) throws ParseException {
        try {
            BigDecimal earnings = driverRepository.getAllForDay(new SimpleDateFormat("dd-MM-yyyy").parse(date))
                    .stream()
                    .filter(driver -> driver.getPayment() != null)
                    .map(Driver::getPayment)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return earnings.setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (ParseException ex) {
            throw new ParseException("Incorrect date: '" + date + "' format. Correct: [dd-MM-yyyy]", ex.getErrorOffset());
        }
    }
}