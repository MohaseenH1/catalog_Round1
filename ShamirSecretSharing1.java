import org.json.JSONObject;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShamirSecretSharing1 {

    private static BigInteger decodeValue(String base, String value) {
        int baseInt = Integer.parseInt(base);
        return new BigInteger(value, baseInt);
    }

    private static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger result = BigInteger.ZERO;
        Integer[] xValues = points.keySet().toArray(new Integer[0]);
        BigInteger[] yValues = points.values().toArray(new BigInteger[0]);

        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(xValues[i]);
            BigInteger yi = yValues[i];
            BigInteger term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = BigInteger.valueOf(xValues[j]);
                    term = term.multiply(BigInteger.ZERO.subtract(xj))
                            .divide(xi.subtract(xj));
                }
            }

            result = result.add(term);
        }

        return result;
    }

    public static void main(String[] args) {
        // Replace these file paths with the paths to your JSON files
        String filePath1 = "/Users/mohaseen/Desktop/accenture/testcase1.json";
        String filePath2 = "/Users/mohaseen/Downloads/testcase2.json";

        System.out.println("First Test Case:");
        processTestCase(filePath1);

        System.out.println("\nSecond Test Case:");
        processTestCase(filePath2);
    }

    private static void processTestCase(String filePath) {
        try {
            // Read the JSON file content into a String
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON string
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject keys = jsonObject.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            Map<Integer, BigInteger> points = new HashMap<>();

            // Decode the points (x, y) from the JSON object
            for (String key : jsonObject.keySet()) {
                if (!key.equals("keys")) {
                    JSONObject point = jsonObject.getJSONObject(key);
                    int x = Integer.parseInt(key);
                    BigInteger y = decodeValue(point.getString("base"), point.getString("value"));
                    points.put(x, y);
                }
            }

            if (points.size() < k) {
                System.out.println("Not enough points to determine the polynomial.");
                return;
            }

            BigInteger constantTerm = lagrangeInterpolation(points, k);
            System.out.println("Constant term (c): " + constantTerm);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error processing file: " + filePath);
        }
    }
}
