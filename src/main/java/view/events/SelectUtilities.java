package view.events;

import controller.Controller;
import model.Point;
import model.Shape;
import view.SettingsSingleton;
import view.ShapeType;

public class SelectUtilities {
	private static double selectedX, selectedY;

	public static void selectHoveredShape(Controller controller, double x, double y) {
		Shape selectedShape = SettingsSingleton.getHoveredShape();
		SettingsSingleton.setSelectedShape(selectedShape);
		selectedX = x;
		selectedY = y;

		if (selectedShape.getType() == ShapeType.LINE) {
			controller.transferSingleShapeTo(selectedShape, Controller.SingletonType.PREVIEW);
			selectedShape.getPoints().forEach(point -> transferPoints(controller, point));
		} else if (selectedShape.getType() == ShapeType.POINT) transferPoints(controller, selectedShape);
	}

	public static void moveSelectedArea(Controller controller, double x, double y) {
		Shape selectedShape = SettingsSingleton.getSelectedShape();
		double deltaX = x - selectedX;
		double deltaY = y - selectedY;

		if (selectedShape.getType() == ShapeType.POINT)
			selectedShape.setCoordinates(x, y);
		else if (selectedShape.getType() == ShapeType.LINE) {
			for (Point point : selectedShape.getPoints()) {
				point.setCoordinates(point.getX() + deltaX, point.getY() + deltaY);
			}
		}

		selectedX = x;
		selectedY = y;
	}

	private static void transferPoints(Controller controller, Shape point) {
		if (canTransferShape(controller, point)) controller.transferSingleShapeTo(point, Controller.SingletonType.PREVIEW);
		for (Shape childShape : point.getChildren()) {
			if (canTransferShape(controller, childShape)) controller.transferSingleShapeTo(childShape, Controller.SingletonType.PREVIEW);
		}
	}

	private static boolean canTransferShape(Controller controller, Shape shape) {
		return !controller.getShapeContainer(Controller.SingletonType.PREVIEW).getShapes().contains(shape);
	}
}
