package org.lukah.visualisation.input;

public class MouseButtonEvent {

    int button;
    int mods;

    public MouseButtonEvent(int button, int mods)  {

        this.button = button;
        this.mods = mods;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof MouseButtonEvent other)) return false;

        return button == other.button && mods == other.mods;
    }

    @Override
    public int hashCode() {
        return 31 * button + mods;
    }
}
