package de.tum.in.lrr.jasmin.commands;

import de.tum.in.lrr.jasmin.core.JasminCommand;
import de.tum.in.lrr.jasmin.core.Op;
import de.tum.in.lrr.jasmin.core.Parameters;
import de.tum.in.lrr.jasmin.core.ParseError;

/**
 * @author Yang Guo
 */

public class Call extends JasminCommand {

    public String[] getID() {
        return new String[]{"CALL"};
    }

    public ParseError validate(Parameters p) {
        ParseError e = p.validate(0, Op.LABEL | Op.MEM | Op.REG | Op.IMM);
        if (e != null) {
            return e;
        }
        e = p.validate(1, Op.NULL);
        return e;
    }

    public void execute(Parameters p) {
        p.a = p.get(0);
        p.push(dataspace.EIP);
        dataspace.setInstructionPointer((int) p.a);
    }

}
