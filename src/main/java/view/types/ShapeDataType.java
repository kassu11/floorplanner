package view.types;

public final class ShapeDataType {
    private ShapeDataType() {
    }
    public static final int NORMAL = 1<<0;
    public static final int HOVER = 1<<1;
    public static final int SELECTED = 1<<2;
    public static final int DISABLED = 1<<3;
    public static final int AREA = 1<<4;
}
