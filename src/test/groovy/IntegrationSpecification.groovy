import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ActiveProfiles
import pl.touk.Application
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT


@SpringBootTest(classes = [Application], webEnvironment = RANDOM_PORT)
@ActiveProfiles(["dev", "test"])
abstract class IntegrationSpecification extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    TestRestTemplate entryPoint
}
