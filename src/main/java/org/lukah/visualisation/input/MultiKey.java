package org.lukah.visualisation.input;

public class MultiKey {

    public static int hashPrime = 31;

    private final int key;
    private final int mods;

    public MultiKey(int key, int mods) {

        this.key = key;
        this.mods = mods;
    }

    public int getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiKey other)) return false;
        return key == other.key && mods == other.mods;
    }

    @Override
    public int hashCode() {
        return hashPrime * key + mods;
    }
}
