package pl.touk.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class VipCustomer {

    private Long id;
    private String registrationNumber;

    public VipCustomer(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
