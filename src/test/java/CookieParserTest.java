import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CookieParserTest {

    @Test
    public void mainSuccessfullyReturnsMostActiveCookie() {
        final var outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        final String[] args = {"-f", "src/main/resources/cookies_log.csv", "-d", "2018-12-09"};
        CookieParser.main(args);

        System.setOut(System.out);

        final var expectedOutput = "AtY0laUfhglK3lC7\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void mainSuccessfullyReturnsMultipleMostActiveCookies() {
        final var outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        final String[] args = {"-f", "src/main/resources/cookies_log.csv", "-d", "2018-12-08"};
        CookieParser.main(args);

        System.setOut(System.out);

        final var cookie1 = "SAZuXPGUrfbcn5UA";
        final var cookie2 = "4sMM2LxV07bPJzwf";
        final var cookie3 = "fbcn5UAVanZf6UtG";

        assertTrue(outContent.toString().contains(cookie1));
        assertTrue(outContent.toString().contains(cookie2));
        assertTrue(outContent.toString().contains(cookie3));
    }

    @Test
    public void mainReturnsErrorMessageWhenLogFormatIsIncorrect() {
        final var outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        final String[] args = {"-f", "src/main/resources/incorrect_log.csv", "-d", "2018-12-09"};
        CookieParser.main(args);

        System.setOut(System.out);

        final var expectedOutput = "Csv does not contain a most frequently found cookie\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void mainReturnsErrorMessageWhenCsvFileIsNotPresent() {
        final var outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        final String[] args = {"-f", "src/main/main/main/resources/incorrect_log.csv", "-d", "2018-12-09"};
        CookieParser.main(args);

        System.setOut(System.out);

        final String expectedOutput = """
                Error: File not found: src/main/main/main/resources/incorrect_log.csv
                Program ended with exception java.lang.RuntimeException: java.io.FileNotFoundException: src/main/main/main/resources/incorrect_log.csv (No such file or directory)
                """;
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void mainReturnsErrorMessageWhenDateRequestedIsNotPresentInLog() {
        final var outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        final String[] args = {"-f", "", "-d", "2022-12-09"};
        CookieParser.main(args);

        System.setOut(System.out);

        final String expectedOutput = """
                Error: File not found:\s
                Program ended with exception java.lang.RuntimeException: java.io.FileNotFoundException:  (No such file or directory)
                """;
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void mainReturnsErrorMessageWhenTooFewParametersAreProvided() {
        final var outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        final String[] args = {"-f"};
        CookieParser.main(args);

        System.setOut(System.out);

        final String expectedOutput = "Usage: java CookieParser -f <file_path> -d <date>\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void mainReturnsErrorMessageWhenDateIsIncorrectlyFormatted() {
        final var outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        final String[] args = {"-f", "src/main/resources/incorrect_log.csv", "-d", "12-09-2023"};
        CookieParser.main(args);

        System.setOut(System.out);

        final String expectedOutput = "Csv does not contain a most frequently found cookie\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
