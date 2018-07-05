package pl.touk.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.touk.model.VipCustomer;
import pl.touk.repository.ParkingRateRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Slf4j
@Component("vipCustomerInitialValues")
@RequiredArgsConstructor
public class VipCustomerInitialValues {

    private final ParkingRateRepository parkingRateRepository;

    @PostConstruct
    public void init() {
        parkingRateRepository.saveVip(Arrays.asList(
                new VipCustomer("AA 11111"),
                new VipCustomer("BB 22222"),
                new VipCustomer("CC 33333"),
                new VipCustomer("DD 44444"),
                new VipCustomer("EE 55555"))
        );
    }
}
