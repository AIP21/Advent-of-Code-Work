import utils.InputUtils;

/**
 * This is a Day class used for Advent of Code
 */
public abstract class AbstractDay {
    private InputUtils inputUtils;

    /**
     * 
     * @param skipPart1 Whether or not to skip part 1 (in the common case that you
     *                  brute force part 1 and it takes like 5 min to run and now
     *                  you need to test part 2)
     */
    public AbstractDay(boolean skipPart1) {
        inputUtils = new InputUtils(System.getProperty("user.dir") + "/inputs/"
                + (this.getClass().toString().replace("class ", "")) + ".txt");

        if (!skipPart1) {
            part1();
        }

        part2();
    }

    /**
     * Code for part one of this day
     */
    public abstract void part1();

    /**
     * Code for part two of this day
     */
    public abstract void part2();
}