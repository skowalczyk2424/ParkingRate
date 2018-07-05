package pl.touk.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Driver {

    private Long id;
    private String registrationNumber;
    private Date startParkTime;
    private Date endParkTime;
    private Integer parkDuration;
    private Boolean vip;
    private BigDecimal payment;

    public Driver(String registrationNumber, Date startParkTime) {
        this.startParkTime = startParkTime;
        this.registrationNumber = registrationNumber;
    }
}