package pl.touk.service

import pl.touk.repository.InMemoryDriverRepository
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.ZoneId

class InMemoryDriverRepositorySec extends Specification {

    @Unroll
    def "should check two dates is same: '#execNr'"() {
        def first = LocalDateTime.ofInstant((new Date(year1, month1, day1)).toInstant(), ZoneId.systemDefault());
        def second = LocalDateTime.ofInstant((new Date(year2, month2, day2)).toInstant(), ZoneId.systemDefault());

        expect:
        InMemoryDriverRepository.isSameDay(first, second) == expectedHours

        where:
        execNr | year1 | month1 | day1 | year2 | month2 | day2 | expectedHours
        1      | 2018  | 06     | 01   | 2018  | 06     | 01   | true
        2      | 2017  | 06     | 01   | 2018  | 06     | 01   | false
        3      | 2018  | 05     | 01   | 2018  | 06     | 01   | false
        4      | 2018  | 06     | 02   | 2018  | 06     | 01   | false
        5      | 2018  | 06     | 01   | 2016  | 06     | 01   | false
        6      | 2018  | 06     | 01   | 2018  | 04     | 01   | false
        7      | 2018  | 06     | 01   | 2018  | 06     | 02   | false
    }
}
