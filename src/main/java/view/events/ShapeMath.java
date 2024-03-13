package view.events;

public class ShapeMath {
    private ShapeMath() {
    }

    public static double getSnapAngle(double startX, double startY, double endX, double endY) {
        double originalAngle = Math.atan2(endY - startY, endX - startX);
        double snappingAngle = Math.PI / 12;
        return (originalAngle >= 0 ? Math.round(originalAngle / snappingAngle) : Math.ceil(originalAngle / snappingAngle - 0.5)) * snappingAngle;
    }

    public static double getRadius(double startX, double startY, double endX, double endY) {
        return Math.hypot(endX - startX, endY - startY);
    }

    public static double getSnapAngleX(double x, double radius, double angle) {
        return x + Math.cos(angle) * radius;
    }

    public static double getSnapAngleY(double y, double radius, double angle) {
        return y + Math.sin(angle) * radius;
    }
}
