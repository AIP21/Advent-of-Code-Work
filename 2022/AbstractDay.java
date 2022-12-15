import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import utils.InputUtils;

/**
 * This is a Day class used for Advent of Code
 */
public abstract class AbstractDay {
    public InputUtils inputUtils;
    public PrintStream printFileWriter = null;

    /**
     * 
     * @param skipPart1 Whether or not to skip part 1 (in the common case that you
     *                  brute force part 1 and it takes like 5 min to run and now
     *                  you need to test part 2)
     */
    public AbstractDay(boolean skipPart1) {
        inputUtils = new InputUtils(System.getProperty("user.dir") + "/inputs/"
                + (this.getClass().toString().replace("class ", "")) + ".txt");

        try {
            File dir = new File(
                    System.getProperty("user.dir") + "/files/" + this.getClass().toString().replace("class ", ""));
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(
                    System.getProperty("user.dir") + "/files/" + (this.getClass().toString().replace("class ", ""))
                            + "/LOG" + System.currentTimeMillis() + ".txt");
            file.createNewFile();

            printFileWriter = new PrintStream(new BufferedOutputStream(new FileOutputStream(file, true)), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long start = System.currentTimeMillis();
        if (!skipPart1) {
            part1();
        }

        part2();
        print("Total time: " + (System.currentTimeMillis() - start) + "ms");

        if (printFileWriter != null) {
            try {
                printFileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Code for part one of this day
     */
    public abstract void part1();

    /**
     * Code for part two of this day
     */
    public abstract void part2();

    public void print(Object o) {
        System.out.println(o);
        printFileWriter.println(o);
    }

    public void print(Object o, boolean newLine) {
        if (newLine) {
            System.out.println(o);
            printFileWriter.println(o);
        } else {
            System.out.print(o);
            printFileWriter.print(o);
        }
    }

    public void log(Object o) {
        printFileWriter.println(o);
    }

    public void log(Object o, boolean newLine) {
        if (newLine) {
            printFileWriter.println(o);
        } else {
            printFileWriter.print(o);
        }
    }

}