package de.tum.in.lrr.jasmin.test;

import de.tum.in.lrr.jasmin.core.Jasmin;
import de.tum.in.lrr.jasmin.core.RegisterSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JasminTest {
    private String[] code = new String[]{
            "mov eax,1",
            "mov ebx,2",
            "add eax,ebx"
    };

    @Test
    void testParser() {
        Jasmin jas = new Jasmin();
        RegisterSet[] registers = jas.getData().getRegisterSets();
        printRegisters(registers);
        jas.parse(code);
        jas.execute(code);
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
