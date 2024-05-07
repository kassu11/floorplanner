package view.events;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.canvas.CustomCanvas;
import view.SettingsSingleton;
import view.types.ShapeDataType;
import view.types.ShapeType;

import java.util.ArrayList;
import java.util.List;
/**
 * Class for handling area utilities
 */
public class AreaUtilities {
    private AreaUtilities() {
    }

    public static Point createAreaPoint(Controller controller) {
        Point point = controller.getHoveredPoint();
        Point areaPoint =
            point == null ?
                controller.createAbsolutePoint(controller.getMouseX(), controller.getMouseY(), Controller.SingletonType.FINAL)
                : controller.createAbsolutePoint(point.getX(), point.getY(), Controller.SingletonType.FINAL);

        areaPoint.setShapeDataType(ShapeDataType.AREA);
        areaPoint.setPriority(2);
        return areaPoint;
    }
    /**
     * Draws the area of shapes in the preview ShapeContainer
     * @param controller controller
     * @param gc custom canvas
     */
    public static void drawArea(Controller controller, List<Point> points, CustomCanvas gc) {
        double x = 0, y = 0;
        gc.beginPath();
        for(Shape point : points) {
            x += point.getX();
            y += point.getY();
            if (points.size() == 1) gc.moveTo(point.getX(), point.getY());
            else gc.lineTo(point.getX(), point.getY());
        }

        if(points.size() < 3) return;

        gc.setStrokeColor("#4269f5d4");
        gc.setFillColor("#4269f54a");
        gc.closePath();
        gc.stroke();
        gc.fill();
        double area = calculateArea(points);
        gc.setFillColor("#000000");
        String unit = SettingsSingleton.getInstance().getMeasurementUnit();
        double modifier = SettingsSingleton.getInstance().getMeasurementModifier();
        gc.fillText(String.format("%.2f %s²", area * Math.pow(modifier, 2), unit), 0, x / points.size(), y / points.size(), 0);
    }
    /**
     * Calculates the area of a list of points
     * @param points list of points
     * @return area
     */
    private static double calculateArea(List<Point> points) {
        if (points.size() < 3) return 0;

        double area = 0;
        for (int i = 0; i < points.size(); i++) {
            int j = (i + 1) % points.size();
            area += (points.get(i).getX() * points.get(j).getY());
            area -= (points.get(j).getX() * points.get(i).getY());
        }

        return Math.abs(area) / 2;
    }
}
