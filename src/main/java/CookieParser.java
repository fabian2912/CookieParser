import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;

public class CookieParser {
    public static void main(final String[] args) {
        try {
            if (args.length < 4) {
                System.out.println("Usage: java CookieParser -f <file_path> -d <date>");
                return;
            }

            String fileName = null;
            String date = null;

            for (int i = 0; i < args.length; i++) {
                final String arg = args[i];

                if (arg.equals("-f")) {
                    if (i + 1 < args.length) {
                        fileName = args[i + 1];
                    }
                }

                if (arg.equals("-d")) {
                    if (i + 1 < args.length) {
                        date = args[i + 1];
                    }
                }
            }

            final var mostFrequentCookies = retrieveCookies(fileName, date);

            if (!mostFrequentCookies.isEmpty()) {
                outputMostFrequentCookies(mostFrequentCookies);
            } else {
                System.out.println("Csv does not contain a most frequently found cookie");
            }
        } catch (Exception e){
            System.out.println("Program ended with exception " + e);
        }
    }

    private static void outputMostFrequentCookies(final List<String> mostFrequentCookies) {
        for (final String cookie : mostFrequentCookies) {
            System.out.println(cookie);
        }
    }

    private static List<String> retrieveCookies(final String filePath, final String date) {
        try {
            final BufferedReader scanner = new BufferedReader(new FileReader(filePath));
            final Map<String, Integer> cookieCounts = new HashMap<>();

            scanner.readLine();

            String line;
            while ((line = scanner.readLine()) != null) {
                var cookie = extractCookie(line, date);

                if (!isNull(cookie)) {
                        cookieCounts.put(cookie, cookieCounts.getOrDefault(cookie, 0) + 1);
                    }

            }
            scanner.close();

            return getMostFrequentCookies(cookieCounts);
        } catch (Exception e) {
            System.out.println("Error: File not found: " + filePath);
            throw new RuntimeException(e);
        }
    }

    private static List<String> getMostFrequentCookies(final Map<String, Integer> cookieCounts) {
        final List<String> mostFrequentCookies = new ArrayList<>();
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : cookieCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentCookies.clear();
            }
            if (entry.getValue() == maxCount) {
                mostFrequentCookies.add(entry.getKey());
            }
        }
        return mostFrequentCookies;
    }

    private static String extractCookie(final String line, final String date) {
        final String[] parts = line.split(",");

        final var cookieDate = extractCookieDate(parts[1]);

        return filterCookieByDate(parts[0], cookieDate, date);
    }

    private static String extractCookieDate(final String cookieDate) {
        var index = cookieDate.indexOf('T');
        return cookieDate.substring(0, index);
    }

    private static String filterCookieByDate(final String cookie, final String cookieDate, final String date) {
        return cookieDate.equals(date) ? cookie : null;
    }
}