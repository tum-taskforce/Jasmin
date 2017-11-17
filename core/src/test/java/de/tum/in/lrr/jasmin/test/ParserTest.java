package de.tum.in.lrr.jasmin.test;

import de.tum.in.lrr.jasmin.core.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ParserTest {
    String[] code = new String[]{
            "mov eax,1",
            "mov ebx,2",
            "add eax,ebx"
    };

    @Test
    void testParser() {
        DataSpace data = new DataSpace(4096, 0);
        CommandLoader cmdLoader = new CommandLoader(data, "de.tum.in.lrr.jasmin.commands", JasminCommand.class);
        System.out.println("Loaded commands: " + Arrays.toString(cmdLoader.getMnemoList()));
        Parser parser = new Parser(data, cmdLoader);

        data.setParser(parser);
        RegisterSet[] registers = data.getRegisterSets();
        printRegisters(registers);

        parser.clearCache(code.length);
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
        printRegisters(registers);
    }

    private void printRegisters(RegisterSet[] registers) {
        System.out.println("=== Registers ===");
        for (RegisterSet reg : registers) {
            System.out.println(reg.E + ": " + reg.aE.getShortcut());
        }
    }
}
