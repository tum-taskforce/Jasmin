package de.tum.in.lrr.jasmin.commands;

import de.tum.in.lrr.jasmin.core.JasminCommand;
import de.tum.in.lrr.jasmin.core.Op;
import de.tum.in.lrr.jasmin.core.Parameters;
import de.tum.in.lrr.jasmin.core.ParseError;

/**
 * @author Yang Guo
 */

public class Ret extends JasminCommand {

    public String[] getID() {
        return new String[]{"RET"};
    }

    public ParseError validate(Parameters p) {
        return p.validate(0, Op.NULL);
    }

    public void execute(Parameters p) {
        p.pop(dataspace.EIP);
    }

}
