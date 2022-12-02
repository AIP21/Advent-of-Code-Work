import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProgrammingChallenge {

    public ProgrammingChallenge() {
        File file = null;
        boolean validFile = false;

        while (!validFile) {
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Please enter a filename (in .java file directory): ");
            String fileName = (System.getProperty("java.class.path").split(";")[0]) + "/" + keyboard.next();
            file = new File(fileName);
            validFile = file.exists();
            if (!validFile) {
                System.out.println("Invalid filename: " + fileName);
            }
        }

        Scanner input;
        try {
            input = new Scanner(file);

            HashMap<Integer, Integer> wordCounts = new HashMap<>();
            ArrayList<Integer> keys = new ArrayList<>();

            int wordCount = 0;
            while (input.hasNext()) {
                String word = input.next();
                wordCount = wordCount + 1;
                word = word.replace(".", "");
                word = word.replace(",", "");
                word = word.replace("!", "");
                word = word.replace("?", "");
                word = word.replace(":", "");
                word = word.replace(";", "");
                word = word.replace("-", "");
                word = word.replace("_", "");
                word = word.replace(" ", "");

                wordCounts.put(word.length(), wordCounts.getOrDefault(word.length(), 0) + 1);
                if (!keys.contains(word.length())) {
                    keys.add(word.length());
                }
            }

            Collections.sort(keys);
            Collections.reverse(keys);

            System.out.println(String.format("There are %d words in total and their lengths are", wordCount));
            for (int i = 0; i < keys.size(); i++) {
                System.out.println(String.format("length %d: %d", keys.get(i), wordCounts.get(keys.get(i))));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String str = """
                Please enter a filename: sample.txt
                There are 24 words in total and their lengths are
                length 9: 1
                length 7: 3
                length 6: 1
                length 5: 2
                length 4: 2
                length 3: 10
                length 2: 4
                length 1: 1
                """;
    }

    public static void main(String[] args) {
        new ProgrammingChallenge();
    }
}