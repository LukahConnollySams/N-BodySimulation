package org.lukah.visualisation.input;

public class MultiKey {

    private final int key;
    private final int mods;

    public MultiKey(int key, int mods) {

        this.key = key;
        this.mods = mods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiKey other)) return false;
        return key == other.key && mods == other.mods;
    }

    @Override
    public int hashCode() {
        return 31 * key + mods;
    }
}
