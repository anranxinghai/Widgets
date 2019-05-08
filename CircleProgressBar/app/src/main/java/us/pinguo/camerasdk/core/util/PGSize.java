//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package us.pinguo.camerasdk.core.util;

public final class PGSize implements Comparable<PGSize> {
    private final int mWidth;
    private final int mHeight;

    public PGSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof PGSize)) {
            return false;
        } else {
            PGSize other = (PGSize)obj;
            return this.mWidth == other.mWidth && this.mHeight == other.mHeight;
        }
    }

    public String toString() {
        return this.mWidth + "x" + this.mHeight;
    }

    private static NumberFormatException invalidSize(String s) {
        throw new NumberFormatException("Invalid Size: \"" + s + "\"");
    }

    public static PGSize parseSize(String string) throws NumberFormatException {
        int sep_ix = string.indexOf(42);
        if (sep_ix < 0) {
            sep_ix = string.indexOf(120);
        }

        if (sep_ix < 0) {
            throw invalidSize(string);
        } else {
            try {
                return new PGSize(Integer.parseInt(string.substring(0, sep_ix)), Integer.parseInt(string.substring(sep_ix + 1)));
            } catch (NumberFormatException var3) {
                throw invalidSize(string);
            }
        }
    }

    public int hashCode() {
        return this.mHeight ^ (this.mWidth << 16 | this.mWidth >>> 16);
    }

    public int compareTo(PGSize another) {
        return Long.signum((long)this.getWidth() * (long)this.getHeight() - (long)another.getWidth() * (long)another.getHeight());
    }
}
