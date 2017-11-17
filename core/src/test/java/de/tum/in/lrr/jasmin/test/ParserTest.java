package de.tum.in.lrr.jasmin.test;

import de.tum.in.lrr.jasmin.core.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {
    private String[] code = new String[]{
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

        assertRegisterEquals(registers, "eax", 3L);
        assertRegisterEquals(registers, "ebx", 2L);
    }

    private void printRegisters(RegisterSet[] registers) {
        System.out.println("=== Registers ===");
        for (RegisterSet reg : registers) {
            System.out.println(reg.E + ": " + reg.aE.getShortcut());
        }
    }

    private void assertRegisterEquals(RegisterSet[] registers, String name, long value) {
        for (RegisterSet reg : registers) {
            if (reg.E.equalsIgnoreCase(name)) {
                assertEquals(reg.aE.getShortcut(), value);
                return;
            } else if (reg.X.equalsIgnoreCase(name)) {
                assertEquals(reg.aX.getShortcut(), value);
                return;
            } else if (reg.H.equalsIgnoreCase(name)) {
                assertEquals(reg.aH.getShortcut(), value);
                return;
            } else if (reg.L.equalsIgnoreCase(name)) {
                assertEquals(reg.aL.getShortcut(), value);
                return;
            }
        }
    }
}
