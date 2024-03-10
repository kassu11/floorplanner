package view.events;

import controller.Controller;
import model.Point;
import model.Shape;
import view.GUIElements.CustomCanvas;
import view.SettingsSingleton;
import view.ShapeType;

import java.util.List;
import java.util.function.Consumer;

public class SelectUtilities {
	private static double selectedX, selectedY, startX, startY;

	public static void selectHoveredShape(Controller controller, double x, double y) {
		selectHoveredShape(controller, x, y, true);
	}

	public static void selectHoveredShape(Controller controller, double x, double y, boolean history) {
		Shape selectedShape = SettingsSingleton.getHoveredShape();
		SettingsSingleton.setSelectedShape(selectedShape);
		startX = x;
		startY = y;
		selectedX = x;
		selectedY = y;
		Shape[] oldSelection = controller.getShapes(Controller.SingletonType.PREVIEW).toArray(new Shape[0]);



		Consumer<Shape> transferPoint = point -> {
			transferPoints(controller, point, Controller.SingletonType.PREVIEW);
			point.setSelectedCoordinates(point.getX() - x, point.getY() - y);
		};

		if (selectedShape.getType() == ShapeType.LINE) {
			controller.transferSingleShapeTo(selectedShape, Controller.SingletonType.PREVIEW);
			selectedShape.getPoints().forEach(transferPoint);
		} else if (selectedShape.getType() == ShapeType.POINT) transferPoint.accept(selectedShape);

		if (history) controller.getHistoryManager().selectShape(oldSelection, selectedShape);
	}

	public static void updateSelectionCoordinates(Controller controller, double x, double y) {
		for (Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
			shape.setSelectedCoordinates(shape.getX() - x, shape.getY() - y);
		}
	}

	public static void unselectMissingShape(Controller controller, Shape[] points) {
		controller.transferAllShapesTo(Controller.SingletonType.FINAL);
		for (Shape point : points) {
			if (point.getType() == ShapeType.POINT) transferPoints(controller, point, Controller.SingletonType.PREVIEW);
		}
		if (controller.getShapes(Controller.SingletonType.PREVIEW).isEmpty()) SettingsSingleton.setSelectedShape(null);
	}

	public static void moveSelectedArea(Controller controller, double x, double y) {
		for (Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
			if (shape.getType() != ShapeType.POINT) continue;
			shape.setCoordinates(shape.getSelectedX() + x, shape.getSelectedY() + y);
		}
	}

	public static void finalizeSelectedShapes(Controller controller, CustomCanvas canvas, double x, double y) {
		finalizeSelectedShapes(controller, canvas, x, y, true);
	}

	public static void finalizeSelectedShapes(Controller controller, CustomCanvas canvas, double x, double y, boolean history) {
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
		if (history) controller.getHistoryManager().finalizeSelection(selectedShape, x, y, startX, startY, canvas);
	}

	public static void rotateSelectedShape(Controller controller, double x, double y) {
		double sumX = 0, sumY = 0, totalPoints = 0;

		for (Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
			if (shape.getType() == ShapeType.POINT) {
				sumX += shape.getX();
				sumY += shape.getY();
				totalPoints++;
			}
		}

		if (totalPoints > 1) {
			double centroidX = sumX / totalPoints;
			double centroidY = sumY / totalPoints;
			double vectorX1 = selectedX - centroidX;
			double vectorY1 = selectedY - centroidY;
			double vectorX2 = x - centroidX;
			double vectorY2 = y - centroidY;
			double dotProduct = vectorX1 * vectorX2 + vectorY1 * vectorY2;
			double determinant = vectorX1 * vectorY2 - vectorY1 * vectorX2;
			double angle = Math.atan2(determinant, dotProduct);

			for (Shape point : controller.getShapes(Controller.SingletonType.PREVIEW)) {
				if (point.getType() == ShapeType.POINT) {
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
			}

			selectedX = x;
			selectedY = y;
		}
	}

	public static void finalizeSelectedRotation(Controller controller, double x, double y) {
		rotateSelectedShape(controller, x, y);
		SettingsSingleton.setSelectedShape(null);
		controller.transferAllShapesTo(Controller.SingletonType.FINAL);

	}

	private static void transferPoints(Controller controller, Shape point, Controller.SingletonType type) {
		if (canTransferShape(controller, point, type)) controller.transferSingleShapeTo(point, type);
		for (Shape childShape : point.getChildren()) {
			if (canTransferShape(controller, childShape, type)) controller.transferSingleShapeTo(childShape, type);
		}
	}

	private static boolean canTransferShape(Controller controller, Shape shape, Controller.SingletonType type) {
		return !controller.getShapeContainer(type).getShapes().contains(shape);
	}
}
