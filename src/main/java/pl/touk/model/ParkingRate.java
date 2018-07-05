package pl.touk.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ParkingRate {

    private Long id;
    private CustomerType customerType;
    private BigDecimal firstHour;
    private BigDecimal secondHour;
    private BigDecimal factor;
    private CurrencyType currencyType;

}
