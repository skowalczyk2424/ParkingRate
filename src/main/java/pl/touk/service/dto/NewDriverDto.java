package pl.touk.service.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewDriverDto {

    private String registrationNumber;
    private Date start;
}
