package view.events;

import controller.Controller;
import model.Point;
import model.Shape;
import view.GUIElements.CustomCanvas;
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
		double deltaX = x - selectedX;
		double deltaY = y - selectedY;

		for (Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
			if (shape.getType() == ShapeType.POINT) {
				shape.setCoordinates(shape.getX() + deltaX, shape.getY() + deltaY);
			}
		}
		selectedX = x;
		selectedY = y;
	}

	public static void finalizeSelectedShapes(Controller controller, CustomCanvas canvas, double x, double y) {
		Shape selectedShape = SettingsSingleton.getSelectedShape();
		Shape hoveredShape = SettingsSingleton.getHoveredShape();

		if (hoveredShape != null && selectedShape.getType() == ShapeType.POINT && hoveredShape.getType() == ShapeType.POINT) {
			for (int i = 0; i < selectedShape.getChildren().size(); i++) {
				Shape childShape = selectedShape.getChildren().get(i);
				if (childShape.getPoints().contains(hoveredShape)) {
					controller.deleteShape(childShape, Controller.SingletonType.PREVIEW);
					i--;
				} else {
					childShape.getPoints().remove(selectedShape);
					childShape.getPoints().add((Point) hoveredShape);
					hoveredShape.addChild(childShape);
				}
			}

			controller.removeShape(selectedShape, Controller.SingletonType.PREVIEW);
		} else
			moveSelectedArea(controller, x, y);

		SettingsSingleton.setSelectedShape(null);
		controller.drawAllShapes(canvas, Controller.SingletonType.PREVIEW);
		controller.transferAllShapesTo(Controller.SingletonType.FINAL);
	}

	public static void rotateSelectedShape(double x, double y) {
		Shape selectedShape = SettingsSingleton.getSelectedShape();
		if (selectedShape != null && selectedShape.getType() != ShapeType.POINT) {
			double centroidX = selectedShape.getCentroidX();
			double centroidY = selectedShape.getCentroidY();
			double vectorX1 = selectedX - centroidX;
			double vectorY1 = selectedY - centroidY;
			double vectorX2 = x - centroidX;
			double vectorY2 = y - centroidY;
			double dotProduct = vectorX1 * vectorX2 + vectorY1 * vectorY2;
			double determinant = vectorX1 * vectorY2 - vectorY1 * vectorX2;
			double angle = Math.atan2(determinant, dotProduct);

			for (Point point : selectedShape.getPoints()) {
				double radians = Math.sqrt(Math.pow(point.getX() - centroidX, 2) + Math.pow(point.getY() - centroidY, 2));
				if (centroidX == point.getX()) {
					if (point.getY() - centroidY < 0)
						point.setCoordinates(centroidX, centroidY - radians);
					else
						point.setCoordinates(centroidX, centroidY + radians);
				} else {
					double pointAngle = Math.atan2(point.getY() - centroidY, point.getX() - centroidX);
					double newAngle = (pointAngle + angle) % (2 * Math.PI);
					point.setCoordinates(centroidX + radians * Math.cos(newAngle), centroidY + radians * Math.sin(newAngle));
				}
			}

			selectedX = x;
			selectedY = y;
		}
	}

	public static void finalizeSelectedRotation(Controller controller, double x, double y) {
		rotateSelectedShape(x, y);
		SettingsSingleton.setSelectedShape(null);
		controller.transferAllShapesTo(Controller.SingletonType.FINAL);

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
