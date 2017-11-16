package de.tum.in.lrr.jasmin.commands;

import de.tum.in.lrr.jasmin.core.JasminCommand;
import de.tum.in.lrr.jasmin.core.Op;
import de.tum.in.lrr.jasmin.core.Parameters;
import de.tum.in.lrr.jasmin.core.ParseError;

/**
 * @author Yang Guo
 */

public class PopA extends JasminCommand {

    public String[] getID() {
        return new String[]{"POPA"};
    }

    public ParseError validate(Parameters p) {
        return p.validate(0, Op.NULL);
    }

    public void execute(Parameters p) {
        p.pop(dataspace.DI);
        p.pop(dataspace.SI);
        p.pop(dataspace.BP);
        p.pop(dataspace.BX); // is overwritten in the next step
        p.pop(dataspace.BX);
        p.pop(dataspace.DX);
        p.pop(dataspace.CX);
        p.pop(dataspace.AX);
    }

}
