package org.lordrose.nvhackassist.nv;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Option {

    final int[] correctCharOptions;
    final String word;
    final int index;

    public Option(int index, String word, int[] correctCharOptions) {
        this.index = index;
        this.word = word;
        this.correctCharOptions = correctCharOptions;
    }
}
