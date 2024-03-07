package utils;

import java.util.HashMap;

public class CmdParser {
    private HashMap<String, String> options;
    private HashMap<String, Boolean> switches;
    private HashMap<String, Boolean> optionRequired;

    private String helpMessage = "Syntax: \n" +
            "java -jar alignment.jar [--go <gapopen>] [--ge <gapextend>]\n" +
            "        [--dpmatrices <dir>] [--check]\n" +
            "        --pairs <pairfile> --seqlib <seqlibfile>\n" +
            "        -m <matrixname>\n" +
            "        --mode <local|global|freeshift> [--nw]\n" +
            "        --format <scores|ali|html>\n" +
            "Options:\n" +
            "    --pairs <pairfile>         path to pairs file\n" +
            "    --seqlib <seqlibfile>      path to sequence library file\n" +
            "    -m <matrixname>            path to substitution matrix file\n" +
            "    --go <gapopen>             gap open penalty (default -12)\n" +
            "    --ge <gapextend>           gap extend penalty (default -1)\n" +
            "    --mode <local|global|freeshift>\n" +
            "    --nw                       use NW/SW algorithms\n" +
            "    --format <scores|ali|html> output format\n" +
            "           scores:     scores only\n" +
            "           ali:        scores + alignment in plaintext\n" +
            "           html:       scores + alignment in html";


    public CmdParser() {
        options = new HashMap<>();
        switches = new HashMap<>();
        optionRequired = new HashMap<>();
    }

    // Method to add option with optional or required flag
    public void addOption(String name, boolean isRequired) {
        optionRequired.put(name, isRequired);
    }

    // Method to add switch
    public void addSwitch(String name) {
        switches.put(name, false);
    }

    //TODO: should be able to recognize -m option
    //TODO: distinguish between required and optional options

    // Method to parse command line arguments
    public void parse(String[] args) throws IllegalArgumentException {
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            if (arg.startsWith("--")) { // Handle -- syntax
                String name = arg.substring(2); // Remove --
                if (optionRequired.containsKey(name)) { // Check if the option is required
                    if ((i == args.length - 1) || (args[i+1].startsWith("--") && optionRequired.containsKey(args[i+1].substring(2))) || (args[i+1].startsWith("-") && optionRequired.containsKey(args[i+1].substring(1))) ) {
                        throw new IllegalArgumentException(helpMessage);
                    }
                    options.put(name, args[i+1]);
                    i += 2;
                } else if (switches.containsKey(name)) {
                    switches.put(name, true);
                    i++;
                } else {
                    throw new IllegalArgumentException(helpMessage);
                }
            } else if (arg.startsWith("-")) { // Handle - syntax
                String name = arg.substring(1); // Remove -
                if (optionRequired.containsKey(name)) { // Check if the option is required
                    if (i == args.length - 1 || args[i+1].startsWith("-")) {
                        throw new IllegalArgumentException(helpMessage);
                    }
                    options.put(name, args[i+1]);
                    i += 2;
                } else if (switches.containsKey(name)) {
                    switches.put(name, true);
                    i++;
                } else {
                    throw new IllegalArgumentException(helpMessage);
                }
            } else {
                throw new IllegalArgumentException(helpMessage);
            }
        }

        // Check for missing required options
        for (HashMap.Entry<String, Boolean> entry : optionRequired.entrySet()) {
            if (entry.getValue() && !options.containsKey(entry.getKey())) {
                throw new IllegalArgumentException("Missing required option: --" + entry.getKey() + "\n" + helpMessage);
            }
        }
    }




    // Method to get value of an option
    public String getOptionValue(String name) {
        return options.get(name);
    }

    // Method to get value of a switch
    public boolean getSwitchValue(String name) {
        return switches.get(name);
    }
}
