import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Day4 {
    String input = "iwrupvqb";

    MessageDigest md;

    public Day4() {
        try {
            md = MessageDigest.getInstance("MD5");

            boolean found = false;

            for (long number = 0; !found; number++) {
                String newInput = input + String.valueOf(number);

                byte[] bytes = newInput.getBytes("UTF-8");
                byte[] hash = md.digest(bytes);

                String output = String.format("%032x", new BigInteger(1, hash));

                if (output.substring(0, 6).equals("000000")) {
                    found = true;
                    System.out.println("FOUND ONE: input: " + newInput + ", output: " + output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Day4();
    }
}