package sender;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSenderImpl;
import java.util.*;
import java.util.stream.Stream;

public class MessageSenderTest {

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
    public void messageSeng_Language_test(String ip, Location location, String message) {

        GeoService geoServiceMock = Mockito.mock(GeoService.class);
        Mockito.when(geoServiceMock.byIp(ip))
                .thenReturn(location);

        LocalizationService localizationServiceMock = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationServiceMock.locale(location.getCountry()))
                .thenReturn((message));

        MessageSenderImpl messageSender = new MessageSenderImpl(geoServiceMock, localizationServiceMock);

        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        String language = messageSender.send(headers);

        List<String> list = Arrays.asList(ip.split("\\."));
        String ipRegion = list.stream()
                .findFirst()
                .map(Object::toString)
                .orElse(null);

        String expected = "";
        if (ipRegion.equals("172"))
            expected = "Добро пожаловать";
        else if (ipRegion.equals("96"))
            expected = "Welcome";

        Assertions.assertEquals(expected, language);
    }

    private static Stream<Arguments> source() {
        return Stream.of(Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15), "Добро пожаловать"),
                Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32), "Welcome"));
    }


}
