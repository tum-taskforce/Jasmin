package de.tum.in.lrr.jasmin.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Jasmin {
    private final DataSpace data;
    private final Parser parser;

    private Map<String, Integer> labels = new HashMap<>();

    public Jasmin() {
        data = new DataSpace(4096, 0);
        CommandLoader cmdLoader = new CommandLoader(data, "de.tum.in.lrr.jasmin.commands", JasminCommand.class);
        System.out.println("Loaded commands: " + Arrays.toString(cmdLoader.getMnemoList()));
        parser = new Parser(data, cmdLoader, this);
        data.setParser(parser);
    }

    public void reset() {
        data.setInstructionPointer(0);
        data.clear();
    }

    public void parse(String[] code) {
        parser.clearCache(code.length);
        labels.clear();

        String lastLabel = null;
        for (int i = 0; i < code.length; i++) {
            ParseResult res = parser.parse(code[i], lastLabel);
            if (res.label != null) {
                labels.put(res.label, i);
                lastLabel = res.labelOnly ? res.label : null;
            } else if (!res.empty) {
                lastLabel = null;
            }
        }
    }

    public void execute(String[] code) {
        int line = data.getInstructionPointer();
        while (line < code.length) {
            data.setInstructionPointer(line + 1);

            try {
                ParseError parseError = parser.execute(code[line], null, line);
                if (parseError != null) {
                    System.out.println("Error in line " + line + ": " + parseError.errorMsg);
                    break;
                }
            } catch (Exception e) {
                System.out.println("Error in line " + line);
                e.printStackTrace();
                break;
            }

            line = data.getInstructionPointer();
        }

        data.updateDirty();
    }

    public int getLabelLine(String label) {
        return labels.getOrDefault(label, -1);
    }

    public DataSpace getData() {
        return data;
    }
}
