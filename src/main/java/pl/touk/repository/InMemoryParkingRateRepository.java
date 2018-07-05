package pl.touk.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.touk.model.ParkingRate;
import pl.touk.model.VipCustomer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryParkingRateRepository implements ParkingRateRepository {

    private AtomicLong idGenerator = new AtomicLong();
    private Map<Long, ParkingRate> rates = new ConcurrentHashMap<>();
    private Map<Long, VipCustomer> vips = new ConcurrentHashMap<>();

    @Override
    public void save(List<ParkingRate> parkingRates) {
        log.debug("Save " + parkingRates.toString());
        parkingRates.forEach(parkingRate -> {
            rates.put(idGenerator.getAndIncrement(), parkingRate);
            parkingRate.setId(idGenerator.get());
        });
    }

    @Override
    public void saveVip(List<VipCustomer> vipCustomer) {
        log.debug("Save vip " + vipCustomer.toString());
        vipCustomer.forEach(vip -> {
            vips.put(idGenerator.getAndIncrement(), vip);
            vip.setId(idGenerator.get());
        });
    }

    @Override
    public List<ParkingRate> findAll() {
        return rates.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isVip(String registrationNumber) {
        return vips.entrySet()
                .stream()
                .anyMatch(es -> es.getValue()
                        .equals(registrationNumber));
    }
}
