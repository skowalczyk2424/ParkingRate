package pl.touk.service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDriverDto {

    private String registrationNumber;
    private Date startParkTime;
    private Date endParkTime;
    private Integer parkDuration;
    private BigDecimal payment;
    private Boolean vip;

}
