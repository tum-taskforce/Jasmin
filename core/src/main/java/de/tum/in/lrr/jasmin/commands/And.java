package de.tum.in.lrr.jasmin.commands;

import de.tum.in.lrr.jasmin.core.JasminCommand;
import de.tum.in.lrr.jasmin.core.Op;
import de.tum.in.lrr.jasmin.core.Parameters;
import de.tum.in.lrr.jasmin.core.ParseError;

/**
 * @author Jakob Kummerow
 */

public class And extends JasminCommand {

    public String[] getID() {
        return new String[]{"AND", "OR", "XOR", "TEST"};
    }

    public ParseError validate(Parameters p) {
        ParseError e = p.numericDestOK();
        if (e != null) {
            return e;
        }
        e = p.numericSrcOK();
        if (e != null) {
            return e;
        }
        e = p.validate(2, Op.NULL);
        return e;
    }

    public void execute(Parameters p) {
        p.prepareAB();
        switch (p.mnemo) {
            case "AND":
            case "TEST":
                p.result = p.a & p.b;
                break;
            case "OR":
                p.result = p.a | p.b;
                break;
            case "XOR":
                p.result = p.a ^ p.b;
                break;
        }
        setFlags(p, SF + ZF + PF);
        dataspace.fOverflow = false;
        dataspace.fCarry = false;
        if (!p.mnemo.equals("TEST")) {
            p.put(0, p.result, null);
        }
    }

}
