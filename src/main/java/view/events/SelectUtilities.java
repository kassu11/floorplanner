package view.events;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.canvas.CustomCanvas;
import view.SettingsSingleton;
import view.types.ShapeType;
import java.util.function.Consumer;

public class SelectUtilities {
	private static double selectedX, selectedY, startX, startY;

	public static void selectHoveredShape(Controller controller, double x, double y) {
		selectHoveredShape(controller, x, y, true);
	}

	public static void selectHoveredShape(Controller controller, double x, double y, boolean history) {
		Shape selectedShape = controller.getHoveredShape();
		controller.setSelectedShape(selectedShape);
		startX = x;
		startY = y;
		selectedX = x;
		selectedY = y;
		boolean isNewSelection = controller.getShapes(Controller.SingletonType.PREVIEW).isEmpty();

		Consumer<Shape> transferPoint = point -> {
			transferPoints(controller, point, Controller.SingletonType.PREVIEW);
			point.setSelectedCoordinates(point.getX() - x, point.getY() - y);
		};

		if (selectedShape.getType() == ShapeType.LINE) {
			controller.transferSingleShapeTo(selectedShape, Controller.SingletonType.PREVIEW);
			selectedShape.getPoints().forEach(transferPoint);
		} else if (selectedShape.getType() == ShapeType.POINT) transferPoint.accept(selectedShape);

		if (history) {
			if (isNewSelection) controller.getHistoryManager().startSelection(controller.getShapes(Controller.SingletonType.PREVIEW));
			else controller.getHistoryManager().addToSelection(controller.getShapes(Controller.SingletonType.PREVIEW));
		}
	}

	public static void updateSelectionCoordinates(Controller controller, double x, double y) {
		for (Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
			shape.setSelectedCoordinates(shape.getX() - x, shape.getY() - y);
		}
	}

	public static void moveSelectedArea(Controller controller, double x, double y) {
		Point hoveredShape = controller.getHoveredPoint();
		if(hoveredShape == null) {
			for (Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
				if (shape.getType() != ShapeType.POINT) continue;
				shape.setCoordinates(shape.getSelectedX() + x, shape.getSelectedY() + y);
			}
		} else {
			for (Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
				if (shape.getType() != ShapeType.POINT) continue;
				shape.setCoordinates(hoveredShape.getX(), hoveredShape.getY());
			}
		}
	}

	public static void finalizeSelectedShapes(Controller controller, CustomCanvas canvas, double x, double y) {
		finalizeSelectedShapes(controller, canvas, x, y, true);
	}

	public static void finalizeSelectedShapes(Controller controller, CustomCanvas canvas, double x, double y, boolean history) {
		Shape selectedShape = controller.getSelectedShape();
		Shape hoveredShape = controller.getHoveredShape();

		if (hoveredShape != null && selectedShape.getType() == ShapeType.POINT && hoveredShape.getType() == ShapeType.POINT) {
			if (history) controller.getHistoryManager().finalizeSelectionMerge(controller.getShapes(Controller.SingletonType.PREVIEW), (Point) selectedShape, (Point) hoveredShape);
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
			if(hoveredShape.getChildren().isEmpty()) controller.removeShape(hoveredShape, Controller.SingletonType.FINAL);
		} else {
			moveSelectedArea(controller, x, y);
			if (history) controller.getHistoryManager().finalizeSelection(controller.getShapes(Controller.SingletonType.PREVIEW));
		}

		controller.setSelectedShape(null);
		if(canvas != null) controller.drawAllShapes(canvas, Controller.SingletonType.PREVIEW);
		controller.transferAllShapesTo(Controller.SingletonType.FINAL);
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

		double centroidX = sumX / totalPoints;
		double centroidY = sumY / totalPoints;
		Shape selectedShape = controller.getSelectedShape();
		if (totalPoints > 1) {
			double angle = Math.atan2(y - centroidY, x - centroidX) - Math.atan2(selectedY - centroidY, selectedX - centroidX);

			// Check for the wrap around
			angle = angle < -Math.PI ? angle + 2 * Math.PI : angle > Math.PI ? angle - 2 * Math.PI : angle;

			// Temporary snapping on/off
			boolean snapping = controller.isCtrlDown();
			double snappingAngle = Math.PI / 12;
			angle = snapping ? angle >= snappingAngle || angle <= -snappingAngle ? angle >= 0 ? snappingAngle : -snappingAngle : 0 : angle;

			for (Shape point : controller.getShapes(Controller.SingletonType.PREVIEW)) {
				if(point.getType() != ShapeType.POINT) continue;
				double radius = Math.sqrt(Math.pow(point.getX() - centroidX, 2) + Math.pow(point.getY() - centroidY, 2));
				double pointAngle = Math.atan2(point.getY() - centroidY, point.getX() - centroidX);
				double newAngle = (pointAngle + angle);
				point.setCoordinates(centroidX + radius * Math.cos(newAngle), centroidY + radius * Math.sin(newAngle));
			}

			if (!snapping || angle != 0) {
				selectedX = x;
				selectedY = y;
			}
		}
	}

	public static void finalizeSelectedRotation(Controller controller, double x, double y) {
		rotateSelectedShape(controller, x, y);
		controller.setSelectedShape(null);
		controller.getHistoryManager().finalizeSelection(controller.getShapes(Controller.SingletonType.PREVIEW));
		controller.transferAllShapesTo(Controller.SingletonType.FINAL);
	}

	public static void deleteShape(Controller controller, Shape shape) {
		controller.getHistoryManager().deleteShape(shape);
		controller.deleteShape(shape, Controller.SingletonType.FINAL);
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
