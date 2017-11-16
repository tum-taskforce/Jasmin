package de.tum.in.lrr.jasmin.commands;

import de.tum.in.lrr.jasmin.core.JasminCommand;
import de.tum.in.lrr.jasmin.core.Op;
import de.tum.in.lrr.jasmin.core.Parameters;
import de.tum.in.lrr.jasmin.core.ParseError;

public class Nop extends JasminCommand {

    @Override
    public String[] getID() {
        return new String[]{"NOP", "FNOP"};
    }

    @Override
    public ParseError validate(Parameters p) {
        return p.validate(0, Op.NULL);
    }

    @SuppressWarnings("unused")
    @Override
    public void execute(Parameters p) {
    }

}
