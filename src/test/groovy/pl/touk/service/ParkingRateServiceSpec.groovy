package pl.touk.service

import pl.touk.model.Driver
import pl.touk.model.ParkingRate
import pl.touk.repository.DriverRepository
import pl.touk.repository.ParkingRateRepository
import spock.lang.Specification
import spock.lang.Unroll

import static pl.touk.model.CurrencyType.PLN
import static pl.touk.model.CustomerType.REGULAR
import static pl.touk.model.CustomerType.VIP

class ParkingRateServiceSpec extends Specification {

    def vipRate = new ParkingRate(null, VIP, 0d, 2d, 1.2d, PLN)
    def regularRate = new ParkingRate(null, REGULAR, 1d, 2d, 1.5d, PLN)
    def rates = [vipRate, regularRate]

    def driverRepository = Mock(DriverRepository)
    def parkingRateRepository = Mock(ParkingRateRepository)

    def service = new ParkingRateService(driverRepository, parkingRateRepository)

    def setup() {
        parkingRateRepository.findAll() >> rates
        service.init()
    }

    @Unroll
    def "should count fee for vip and regular: '#execNr'"() {
        def id = 1L
        def registrationNumber = 'WE 526'
        parkingRateRepository.isVip(registrationNumber) >> isVip
        def driver = new Driver(id: id,
                registrationNumber: registrationNumber,
                parkDuration: parkDuration)

        expect:
        service.calculateFee(driver) == expectedFee

        where:
        execNr | isVip | parkDuration | expectedFee
        1      | true  | 1            | BigDecimal.valueOf(0)
        2      | true  | 2            | BigDecimal.valueOf(2)
        3      | true  | 3            | BigDecimal.valueOf(2 + 2 * 1.2d)
        4      | true  | 4            | BigDecimal.valueOf((2 + 2 * 1.2d) + 2 * 1.2d * 1.2d)
        5      | false | 1            | BigDecimal.valueOf(1)
        6      | false | 2            | BigDecimal.valueOf(1 + 2)
        7      | false | 3            | BigDecimal.valueOf(1 + 2 + 2 * 1.5d)
        8      | false | 4            | BigDecimal.valueOf((1 + 2 + 2 * 1.5d) + 2 * 1.5d * 1.5d)

    }

    @Unroll
    def 'should check driver if he turned on the meter'() {
        given:
        def registrationNumber = "WE A"

        driverRepository.findDriver(registrationNumber) >> Optional.of(new Driver(startParkTime: start, endParkTime: end))

        when:
        def started = service.checkStartMeterForDriver(registrationNumber)

        then:
        started == expected

        where:
        start      | end        | expected
        new Date() | null       | true
        new Date() | new Date() | false
    }

    @Unroll
    def 'should counted fee for all day'() {
        given:
        def date = "12-12-1234"

        when:
        def earnings = service.allDayEarnings(date)

        then:
        1 * driverRepository.getAllForDay(_) >> drivers
        earnings == expected

        where:
        drivers                     | expected
        []                          | BigDecimal.valueOf(0.0d)
        [new Driver(payment: 1.0d)] | BigDecimal.valueOf(1.0d)
        [new Driver(payment: 1.0d),
         new Driver(payment: 2.2d)] | BigDecimal.valueOf(3.2d)
        [new Driver(payment: 1.0d),
         new Driver(payment: 2.2d),
         new Driver(payment: 0.0d)] | BigDecimal.valueOf(3.2d)
        [new Driver(payment: 1.0d),
         new Driver(payment: 2.2d),
         new Driver(payment: 0.0d),
         new Driver(payment: 5.0d)] | BigDecimal.valueOf(8.2d)
    }
}
