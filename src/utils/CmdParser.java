package utils;

import java.util.HashMap;

public class CmdParser {
    private HashMap<String, String> options;
    private HashMap<String, Boolean> switches;
    private HashMap<String, Boolean> optionRequired;

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
                        throw new IllegalArgumentException("Missing value for option: " + name);
                    }
                    options.put(name, args[i+1]);
                    i += 2;
                } else if (switches.containsKey(name)) {
                    switches.put(name, true);
                    i++;
                } else {
                    throw new IllegalArgumentException("Unknown option: " + name);
                }
            } else if (arg.startsWith("-")) { // Handle - syntax
                String name = arg.substring(1); // Remove -
                if (optionRequired.containsKey(name)) { // Check if the option is required
                    if (i == args.length - 1 || args[i+1].startsWith("-")) {
                        throw new IllegalArgumentException("Missing value for option: " + name);
                    }
                    options.put(name, args[i+1]);
                    i += 2;
                } else if (switches.containsKey(name)) {
                    switches.put(name, true);
                    i++;
                } else {
                    throw new IllegalArgumentException("Unknown option: " + name);
                }
            } else {
                throw new IllegalArgumentException("Unexpected argument: " + arg);
            }
        }

        // Check for missing required options
        for (HashMap.Entry<String, Boolean> entry : optionRequired.entrySet()) {
            if (entry.getValue() && !options.containsKey(entry.getKey())) {
                throw new IllegalArgumentException("Missing required option: --" + entry.getKey());
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
