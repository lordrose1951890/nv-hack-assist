package org.lordrose.nvhackassist.nv;

import java.util.List;

public class MasterMind {

    private NewVegasMasterMind internal;

    private MasterMind(NewVegasMasterMind internal) {
        this.internal = internal;
    }

    public static MasterMind from(String... words) {
        return new MasterMind(NewVegasMasterMind.from(words));
    }

    public void evaluate(int index, int numOfCorrect) {
        internal = internal.evaluate(index, numOfCorrect);
        if (internal.getOptions().size() == 1) {
            System.out.println("Correct word is: " + internal.getOptions().get(0).word);
        }
    }

    public void dud(int index) {
        internal = internal.dud(index);
        if (internal.getOptions().size() == 1) {
            System.out.println("Correct word is: " + internal.getOptions().get(0).word);
        }
    }

    public List<Option> getOptions() {
        return internal.getOptions();
    }
}
