package view.events;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

import java.util.ArrayList;
import java.util.List;

public class AreaUtilities {
//    public static void addToArea(Controller controller, double x, double y, CustomCanvas gc) {
//        SelectUtilities.selectHoveredShape(controller, x, y);
//        controller.drawAllShapes(gc, Controller.SingletonType.PREVIEW);
//        List<Point> points = new ArrayList<>();
//
//        for(Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
//            if(shape.getType() == ShapeType.POINT) points.add((Point) shape);
//        }
//
//        double area = calculateArea(points);
//        gc.fillText(String.valueOf(area), 0, x, y, 0);
//    }

    public static void drawArea(Controller controller, CustomCanvas gc) {
        List<Point> points = new ArrayList<>();
        double x = 0, y = 0;
        gc.beginPath();
        for(Shape point : controller.getShapes(Controller.SingletonType.PREVIEW)) {
            if(point.getType() != ShapeType.POINT) continue;
            points.add((Point) point);
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
        gc.fillText(String.format("%.2f cm2", area), 0, x / points.size(), y / points.size(), 0);
    }

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
