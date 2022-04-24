package geo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GeoServiceImplTest {

    private static long suiteStartTime;
    private long testStartTime;

    @BeforeAll
    public static void initSuite() {
        System.out.println("Running StringTest");
        suiteStartTime = System.nanoTime();
    }

    @AfterAll
    public static void completeSuite() {
        System.out.println("StringTest complete: " + (System.nanoTime() - suiteStartTime));
    }

    @BeforeEach
    public void initTest() {
        System.out.println("Starting new nest");
        testStartTime = System.nanoTime();
    }

    @AfterEach
    public void finalizeTest() {
        System.out.println("\nTest complete:" + (System.nanoTime() - testStartTime));
    }

    @ParameterizedTest
    @MethodSource("source")
    public void byIp_Test(String ip) {
        Location result = new GeoServiceImpl().byIp(ip);

        List<String> list = Arrays.asList(ip.split("\\."));
        String ipRegion = list.stream()
                .findFirst()
                .map(Object::toString)
                .orElse(null);

        Location expected = null;
        switch (ipRegion) {
            case "172":
                expected = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
                break;
            case "96":
                expected = new Location("New York", Country.USA, " 10th Avenue", 32);
                break;
            case "127":
                expected = new Location(null, null, null, 0);
                break;
        }

        Assertions.assertEquals(expected, result);
    }


    private static Stream<Arguments> source() {
        return Stream.of(Arguments.of("172.0.32.11"), Arguments.of("96.44.183.149"), Arguments.of("127.0.0.1"));
    }
}



