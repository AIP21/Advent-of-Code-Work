import java.beans.DesignMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.TreeMap;

public class Day7 extends AbstractDay {
    public static void main(String[] args) {
        new Day7();
    }

    public Day7() {
        super(true);
    }

    private class File {
        public File parent;
        public String name;
        public ArrayList<File> contained = new ArrayList<>();
        public int size = 0;
        public boolean isDir = false;
        public int depth = 0;

        public File() {
            this.parent = null;
            this.name = "/";
            this.isDir = true;
            this.depth = 0;
        }

        public File(File parent, String name) {
            this.parent = parent;
            this.name = name;
            this.isDir = true;
            this.depth = parent.depth + 1;
        }

        public File(File parent, String name, int size) {
            this.parent = parent;
            this.name = name;
            this.size = size;
            this.depth = parent.depth + 1;
        }
    }

    public class FileComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            return o1.size - o2.size;
        }
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        File rootFile = new File();
        File currentDir = rootFile;

        boolean isListing = false;

        for (String line : lines) {
            boolean isCommand = line.indexOf("$") == 0;

            if (isListing && !isCommand) {
                String[] split = line.split(" ");

                if (split[0].contains("dir")) {
                    currentDir.contained.add(new File(currentDir, split[1]));
                } else {
                    String name = split[1];
                    int size = Integer.parseInt(split[0]);
                    currentDir.contained.add(new File(currentDir, name, size));
                }
            } else if (isCommand) {
                isListing = false;

                String fixedCommand = line.substring(2);
                String[] command = fixedCommand.split(" ");

                if (command[0].contains("cd")) {
                    String arg = command[1];

                    if (arg.contains("..")) {
                        currentDir = currentDir.parent;
                    } else if (arg.contains("/")) {
                        currentDir = rootFile;
                    } else {
                        for (File file : currentDir.contained) {
                            if (file.name.equals(arg)) {
                                currentDir = file;
                                break;
                            }
                        }
                    }
                } else if (command[0].contains("ls")) {
                    isListing = true;
                }
            }
        }

        printFile(rootFile);

        print("");

        setDirSizes(rootFile);

        print("");

        ArrayList<File> largeFiles = getLargeFiles(100000, rootFile, new ArrayList<File>());

        int totalSize = 0;
        for (File file : largeFiles) {
            print(file.size + ", " + file.name);
            totalSize += file.size;
        }

        print(totalSize);
    }

    private void printFile(File file) {
        String spaces = "";
        for (int i = 0; i < file.depth; i++) {
            spaces += "-";
        }
        print(spaces + file.size + ", " + file.name);

        for (File contained : file.contained) {
            printFile(contained);
        }
    }

    private int setDirSizes(File file) {
        if (file.isDir) {
            int dirSize = 0;

            for (File contained : file.contained) {
                if (contained.isDir) {
                    dirSize += setDirSizes(contained);
                } else {
                    dirSize += contained.size;
                }
            }

            file.size = dirSize;
        }

        return file.size;
    }

    private ArrayList<File> getLargeFiles(int mustBeBelow, File currentFile, ArrayList<File> files) {
        for (File file : currentFile.contained) {
            if (file.isDir) {
                getLargeFiles(mustBeBelow, file, files);
            }
        }

        if (currentFile.isDir && currentFile.size < mustBeBelow)
            files.add(currentFile);

        return files;
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        File rootFile = new File();
        File currentDir = rootFile;

        boolean isListing = false;

        for (String line : lines) {
            boolean isCommand = line.indexOf("$") == 0;

            if (isListing && !isCommand) {
                String[] split = line.split(" ");

                if (split[0].contains("dir")) {
                    currentDir.contained.add(new File(currentDir, split[1]));
                } else {
                    String name = split[1];
                    int size = Integer.parseInt(split[0]);
                    currentDir.contained.add(new File(currentDir, name, size));
                }
            } else if (isCommand) {
                isListing = false;

                String fixedCommand = line.substring(2);
                String[] command = fixedCommand.split(" ");

                if (command[0].contains("cd")) {
                    String arg = command[1];

                    if (arg.contains("..")) {
                        currentDir = currentDir.parent;
                    } else if (arg.contains("/")) {
                        currentDir = rootFile;
                    } else {
                        for (File file : currentDir.contained) {
                            if (file.name.equals(arg)) {
                                currentDir = file;
                                break;
                            }
                        }
                    }
                } else if (command[0].contains("ls")) {
                    isListing = true;
                }
            }
        }

        printFile(rootFile);

        print("");

        setDirSizes(rootFile);

        print("");

        ArrayList<File> directories = getDirectories(rootFile, new ArrayList<File>());
        Collections.sort(directories, new FileComparator());

        int totalDisk = 70000000;
        int totalUsed = rootFile.size;
        int sizeNeeded = 30000000;
        int usedSpace = totalDisk - totalUsed;
        int toDelete = sizeNeeded - usedSpace;

        print("Total disk: " + totalDisk);
        print("Total used: " + totalUsed);
        print("Size needed: " + sizeNeeded);
        print("Used space: " + usedSpace);
        print("To delete: " + toDelete);

        for (File file : directories) {
            print(file.size + ", " + file.name);
            if (file.size > toDelete) {
                print("Deleted " + file.name);
                break;
            }
        }

    }

    private ArrayList<File> getDirectories(File currentFile, ArrayList<File> files) {
        for (File file : currentFile.contained) {
            if (file.isDir) {
                getDirectories(file, files);
            }
        }

        if (currentFile.isDir)
            files.add(currentFile);

        return files;
    }
}