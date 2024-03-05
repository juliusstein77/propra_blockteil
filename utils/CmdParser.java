package utils;

import java.util.HashMap;
import java.util.Map;

public class CmdParser {
    private HashMap<String, String> options;
    private HashMap<String, Boolean> switches;
    private HashMap<String, Boolean> optionRequired;

    public CmdParser() {
        options = new HashMap<>();
        switches = new HashMap<>();
        optionRequired = new HashMap<>();
    }

    public void addOption(String name, boolean isRequired) {
        optionRequired.put(name, isRequired);
    }

    public void addSwitch(String name) {
        switches.put(name, false);
    }

    public void parse(String[] args) throws IllegalArgumentException {
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                String name = arg.substring(1);
                if (optionRequired.containsKey(name)) { //check if the option is a required option
                    if (i == args.length - 1 || args[i+1].startsWith("-")) { //check if the current argument is the last argument, or the next argument starts with a hyphen, it means that the current argument does not have a corresponding value
                        throw new IllegalArgumentException("Missing value for option: " + name);
                    }
                    options.put(name, args[i+1]);
                    i += 2;
                } else if (switches.containsKey(name)) { //check if the option is a switch
                    switches.put(name, true);
                    i++;
                } else {
                    throw new IllegalArgumentException("Unknown option: " + name);
                }
            } else {
                throw new IllegalArgumentException("Unexpected argument: " + arg);
            }
        }

        // check for missing required options
        for (HashMap.Entry<String, Boolean> entry : optionRequired.entrySet()) {
            if (entry.getValue() && !options.containsKey(entry.getKey())) {
                throw new IllegalArgumentException("Missing required option: -" + entry.getKey());
            }
        }
    }

    public String getOptionValue(String name) {
        return options.get(name);
    }

    public boolean getSwitchValue(String name) {
        return switches.get(name);
    }
}
