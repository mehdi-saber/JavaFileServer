package ir.ac.aut.ceit.ap.fileserver.util.icon;

import java.util.Objects;

class IconKey {
    private final int w, h;
    private final String name;

    public IconKey(int w, int h, String name) {
        this.w = w;
        this.h = h;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IconKey that = (IconKey) o;
        return w == that.w &&
                h == that.h &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(w, h, name);
    }
}
