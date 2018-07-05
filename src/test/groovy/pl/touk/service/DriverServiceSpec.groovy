package pl.touk.service

import pl.touk.model.Driver
import pl.touk.repository.DriverRepository
import pl.touk.service.dto.NewDriverConverter
import pl.touk.service.dto.NewDriverDto
import pl.touk.service.dto.UpdateDriverConverter
import pl.touk.service.dto.UpdateDriverDto
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class DriverServiceSpec extends Specification {

    @Shared
    def millisecondsInHour = 1 * 60 * 60 * 1000

    def driverRepository = Mock(DriverRepository)
    def newDriverConverter = Mock(NewDriverConverter)
    def updateDriverConverter = Mock(UpdateDriverConverter)
    def currentTimeService = Mock(CurrentTimeService)
    def parkingRateService = Mock(ParkingRateService)

    def service = new DriverService(driverRepository,
            newDriverConverter, updateDriverConverter, currentTimeService, parkingRateService)

    def 'should start meter for driver'() {
        given:
        def registrationNumber = 'WE 526'
        def now = new Date()
        def id = 1L
        newDriverConverter.convert(_) >> new NewDriverDto(registrationNumber: registrationNumber, start: now)

        when:
        def newDriverDto = service.startParkingMeter(registrationNumber)

        then:
        1 * currentTimeService.now() >> now
        1 * driverRepository.save(_) >> new Driver(id: id, startParkTime: now, registrationNumber: registrationNumber)
        newDriverDto.registrationNumber == registrationNumber
        newDriverDto.start == now
    }

    @Unroll
    def "should reject registration number when driver start park: '#registrationNumber'"() {
        when:
        service.startParkingMeter(registrationNumber)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.getMessage() != null

        where:
        registrationNumber << [null, ""]
    }

    def 'should end meter and count park duration for driver'() {
        given:
        def id = 1L
        def registrationNumber = 'WE 526'
        def start = new Date()
        def driver = new Driver(
                id: id,
                registrationNumber: registrationNumber,
                startParkTime: start)
        def end = new Date(start.getTime() + millisecondsInHour)
        def parkDuration = millisecondsInHour
        def payment = 2
        def vip = true
        def driver1 = new Driver(id: id,
                registrationNumber: registrationNumber,
                startParkTime: start,
                endParkTime: end,
                parkDuration: parkDuration,
                payment: payment,
                vip: vip)

        currentTimeService.now() >> end
        updateDriverConverter.convert(_) >> new UpdateDriverDto(
                registrationNumber: registrationNumber,
                startParkTime: start,
                endParkTime: end,
                parkDuration: parkDuration,
                payment: payment,
                vip: vip)

        when:
        def updateDriverDto = service.endParkingMeter(registrationNumber)

        then:
        1 * driverRepository.getDriver(registrationNumber) >> driver
        1 * parkingRateService.calculateFee(driver) >> payment
        1 * parkingRateService.isVip(registrationNumber) >> vip
        1 * driverRepository.save(_) >> driver1


        updateDriverDto.equals(updateDriverConverter.convert(driver1))
    }

    @Unroll
    def "should reject registration number when driver end park: '#registrationNumber'"() {
        when:
        service.endParkingMeter(registrationNumber)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.getMessage() != null

        where:
        registrationNumber << [null, ""]
    }

    @Unroll
    def 'should count park duration'() {
        given:
        def start = new Date(startLong)
        def end = new Date(endLong)
        def driver = new Driver(startParkTime: start, endParkTime: end)

        when:
        def parkDuration = DriverService.parkDuration(driver)

        then:
        parkDuration == expectedHours

        where:
        startLong          | endLong                    | expectedHours
        millisecondsInHour | millisecondsInHour * 2     | 1
        millisecondsInHour | millisecondsInHour * 2 + 1 | 2
        millisecondsInHour | millisecondsInHour + 1     | 1
        millisecondsInHour | millisecondsInHour * 3 + 1 | 3
        0L                 | 1L                         | 1
    }
}
