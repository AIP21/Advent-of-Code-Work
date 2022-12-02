package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputUtils {
    private final ArrayList<String> lines;

    public IntParser intParser = new IntParser();
    public FloatParser floatParser = new FloatParser();
    public DoubleParser doubleParser = new DoubleParser();
    public BooleanParser booleanParser = new BooleanParser();

    public InputUtils(String name) {
        File inputFile = new File(name);
        if (!inputFile.exists()) {
            System.out.println("File not found");
        }

        // Read every line in the file and put it into a list
        lines = getLines(inputFile);
    }

    public ArrayList<String> getLines(File inputFile) {
        BufferedReader reader;

        ArrayList<String> lines = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);

                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    // region Util methods
    public ArrayList<String> getLines() {
        return lines;
    }

    public String getLine(int index) {
        return lines.get(index);
    }

    public String getLinePart(int lineIndex, int startIndex) {
        return lines.get(lineIndex).substring(startIndex);
    }

    public String getLinePart(int lineIndex, int startIndex, int endIndex) {
        return lines.get(lineIndex).substring(startIndex, endIndex);
    }

    public int getLineCount() {
        return lines.size();
    }

    public int getLineLength(int index) {
        return lines.get(index).length();
    }

    public char getChar(int lineIndex, int charIndex) {
        return lines.get(lineIndex).charAt(charIndex);
    }

    // region Number parsing
    private abstract class AbstractParser<T> {
        public abstract T parse(int lineIndex);

        public abstract T parse(int lineIndex, int startIndex);

        public abstract T parse(int lineIndex, int startIndex, int endInex);

        public abstract T parseSmart(int lineIndex);

        public abstract T parseSmart(int lineIndex, int startIndex);

        public abstract T parseSmart(int lineIndex, int startIndex, int endInex);
    }

    // int parser
    private class IntParser extends AbstractParser<Integer> {
        @Override
        public Integer parse(int lineIndex) {
            return Integer.parseInt(getLine(lineIndex));
        }

        @Override
        public Integer parse(int lineIndex, int startIndex) {
            return Integer.parseInt(getLinePart(lineIndex, startIndex));
        }

        @Override
        public Integer parse(int lineIndex, int startIndex, int endIndex) {
            return Integer.parseInt(getLinePart(lineIndex, startIndex, endIndex));
        }

        @Override
        public Integer parseSmart(int lineIndex) {
            String line = lines.get(lineIndex);
            int firstNumIndex = line.indexOf("[0-9]");
            for (int i = firstNumIndex; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i))) {
                    return Integer.parseInt(line.substring(firstNumIndex, i));
                }
            }

            return 0;
        }

        @Override
        public Integer parseSmart(int lineIndex, int startIndex) {
            String line = lines.get(lineIndex);
            for (int i = startIndex; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i))) {
                    return Integer.parseInt(line.substring(startIndex, i));
                }
            }

            return 0;
        }

        @Override
        public Integer parseSmart(int lineIndex, int startIndex, int endIndex) {
            String line = lines.get(lineIndex);
            for (int i = startIndex; i < endIndex; i++) {
                if (!Character.isDigit(line.charAt(i))) {
                    return Integer.parseInt(line.substring(startIndex, i));
                }
            }

            return 0;
        }
    }

    // float parser
    private class FloatParser extends AbstractParser<Float> {
        @Override
        public Float parse(int lineIndex) {
            return Float.parseFloat(getLine(lineIndex));
        }

        @Override
        public Float parse(int lineIndex, int startIndex) {
            return Float.parseFloat(getLinePart(lineIndex, startIndex));
        }

        @Override
        public Float parse(int lineIndex, int startIndex, int endIndex) {
            return Float.parseFloat(getLinePart(lineIndex, startIndex, endIndex));
        }

        @Override
        public Float parseSmart(int lineIndex) {
            String line = lines.get(lineIndex);
            int firstNumIndex = line.indexOf("[0-9]");
            for (int i = firstNumIndex; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i)) && line.charAt(i) != '.') {
                    return Float.parseFloat(line.substring(firstNumIndex, i));
                }
            }

            return 0.0f;
        }

        @Override
        public Float parseSmart(int lineIndex, int startIndex) {
            String line = lines.get(lineIndex);
            for (int i = startIndex; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i)) && line.charAt(i) != '.') {
                    return Float.parseFloat(line.substring(startIndex, i));
                }
            }

            return 0.0f;
        }

        @Override
        public Float parseSmart(int lineIndex, int startIndex, int endIndex) {
            String line = lines.get(lineIndex);
            for (int i = startIndex; i < endIndex; i++) {
                if (!Character.isDigit(line.charAt(i)) && line.charAt(i) != '.') {
                    return Float.parseFloat(line.substring(startIndex, i));
                }
            }

            return 0.0f;
        }
    }

    // double parser
    private class DoubleParser extends AbstractParser<Double> {
        @Override
        public Double parse(int lineIndex) {
            return Double.parseDouble(getLine(lineIndex));
        }

        @Override
        public Double parse(int lineIndex, int startIndex) {
            return Double.parseDouble(getLinePart(lineIndex, startIndex));
        }

        @Override
        public Double parse(int lineIndex, int startIndex, int endIndex) {
            return Double.parseDouble(getLinePart(lineIndex, startIndex, endIndex));
        }

        @Override
        public Double parseSmart(int lineIndex) {
            String line = lines.get(lineIndex);
            int firstNumIndex = line.indexOf("[0-9]");
            for (int i = firstNumIndex; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i)) && line.charAt(i) != '.') {
                    return Double.parseDouble(line.substring(firstNumIndex, i));
                }
            }

            return 0.0;
        }

        @Override
        public Double parseSmart(int lineIndex, int startIndex) {
            String line = lines.get(lineIndex);
            for (int i = startIndex; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i)) && line.charAt(i) != '.') {
                    return Double.parseDouble(line.substring(startIndex, i));
                }
            }

            return 0.0;
        }

        @Override
        public Double parseSmart(int lineIndex, int startIndex, int endIndex) {
            String line = lines.get(lineIndex);
            for (int i = startIndex; i < endIndex; i++) {
                if (!Character.isDigit(line.charAt(i)) && line.charAt(i) != '.') {
                    return Double.parseDouble(line.substring(startIndex, i));
                }
            }

            return 0.0;
        }
    }

    // boolean parser
    private class BooleanParser extends AbstractParser<Boolean> {
        @Override
        public Boolean parse(int lineIndex) {
            return Boolean.parseBoolean(getLine(lineIndex));
        }

        @Override
        public Boolean parse(int lineIndex, int startIndex) {
            return Boolean.parseBoolean(getLinePart(lineIndex, startIndex));
        }

        @Override
        public Boolean parse(int lineIndex, int startIndex, int endIndex) {
            return Boolean.parseBoolean(getLinePart(lineIndex, startIndex, endIndex));
        }

        @Override
        public Boolean parseSmart(int lineIndex) {
            String line = lines.get(lineIndex);
            int firstNumIndex = line.indexOf("[0-9]");
            for (int i = firstNumIndex; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i)) && line.charAt(i) != '.') {
                    return Boolean.parseBoolean(line.substring(firstNumIndex, i));
                }
            }

            return false;
        }

        @Override
        public Boolean parseSmart(int lineIndex, int startIndex) {
            String line = lines.get(lineIndex);
            for (int i = startIndex; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i)) && line.charAt(i) != '.') {
                    return Boolean.parseBoolean(line.substring(startIndex, i));
                }
            }

            return false;
        }

        @Override
        public Boolean parseSmart(int lineIndex, int startIndex, int endIndex) {
            String line = lines.get(lineIndex);
            for (int i = startIndex; i < endIndex; i++) {
                if (!Character.isDigit(line.charAt(i)) && line.charAt(i) != '.') {
                    return Boolean.parseBoolean(line.substring(startIndex, i));
                }
            }

            return false;
        }
    }
    // endregion
    // endregion
}