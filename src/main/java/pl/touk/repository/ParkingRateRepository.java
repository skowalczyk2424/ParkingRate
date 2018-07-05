package pl.touk.repository;


import pl.touk.model.ParkingRate;
import pl.touk.model.VipCustomer;

import java.util.List;

public interface ParkingRateRepository {

    void save(List<ParkingRate> parkingRates);

    void saveVip(List<VipCustomer> vipCustomers);

    List<ParkingRate> findAll();

    boolean isVip(String registrationNumber);
}
