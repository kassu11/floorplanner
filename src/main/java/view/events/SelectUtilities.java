package view.events;

import controller.Controller;
import model.shapes.Line;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
/**
 * Class for handling select utilities
 */
public class SelectUtilities {
	private SelectUtilities() {
	}
	/**
	 * X coordinate of the selected shape
	 * Y coordinate of the selected shape
	 */
	private static double selectedX, selectedY;
	/**
	 * Selects the hovered shape
	 * @param controller controller
	 * @param x X coordinate
	 * @param y Y coordinate
	 */

	public static void selectHoveredShape(Controller controller, double x, double y) {
		selectHoveredShape(controller, x, y, true);
	}
	/**
	 * Selects the hovered shape with history
	 * @param controller controller
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param history history
	 */
	public static void selectHoveredShape(Controller controller, double x, double y, boolean history) {
		Shape selectedShape = controller.getHoveredShape();
		controller.setSelectedShape(selectedShape);
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
	/**
	 * Updates the selection coordinates
	 * @param controller controller
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public static void updateSelectionCoordinates(Controller controller, double x, double y) {
		for (Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
			shape.setSelectedCoordinates(shape.getX() - x, shape.getY() - y);
		}
	}
	/**
	 * Moves the selected area
	 * @param controller controller
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public static void moveSelectedArea(Controller controller, double x, double y) {
		Point hoveredPoint = controller.getHoveredPoint();
		Shape selectedShape = controller.getSelectedShape();
		Shape hoveredShape = controller.getHoveredShape();

		Point mousePoint = controller.createAbsolutePoint(x, y);
		if(controller.isCtrlDown() && selectedShape.getType() == ShapeType.POINT && selectedShape.getChildren().size() == 1) {
			Point oppositePoint = selectedShape.getChildren().get(0).getPoints().get(0);
			if (oppositePoint == selectedShape) oppositePoint = selectedShape.getChildren().get(0).getPoints().get(1);

			if(!controller.getShapes(Controller.SingletonType.PREVIEW).contains(oppositePoint)) {
				mousePoint.setCoordinates(ShapeMath.getSnapCoordinates(oppositePoint, x, y));

				if(hoveredShape != null && hoveredShape.getType() == ShapeType.LINE) {
					Shape line = controller.createShape(mousePoint.getX(), mousePoint.getY(), oppositePoint.getX(), oppositePoint.getY(), ShapeType.LINE, null);
					Point intersection = ShapeMath.createIntersectionPoint(controller, line, hoveredShape);
					if (intersection != null) mousePoint = intersection;
				}
				mousePoint.setCoordinates(mousePoint.getX() - selectedShape.getSelectedX(), mousePoint.getY() - selectedShape.getSelectedY());
			}
		}

		if(hoveredPoint != null && selectedShape.getType() == ShapeType.POINT) {
			mousePoint.setCoordinates(hoveredPoint.getX() - selectedShape.getSelectedX(), hoveredPoint.getY() - selectedShape.getSelectedY());
		}
		else if(!controller.isCtrlDown() && hoveredShape != null && hoveredShape.getType() == ShapeType.LINE && selectedShape.getType() == ShapeType.POINT) {
			double[] pointsOnLine = ShapeMath.getPointOnLine(hoveredShape, x, y);
			mousePoint.setCoordinates(pointsOnLine[0] - selectedShape.getSelectedX(), pointsOnLine[1] - selectedShape.getSelectedY());
		}

		moveAllSelectedShapesToCursor(controller, mousePoint.getX(), mousePoint.getY());
		for(Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
			if(shape.getType() == ShapeType.LINE) ((Line)shape).resizeToDimensions();
		}
	}
	/**
	 * Moves all selected shapes to cursor
	 * @param controller controller
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	private static void moveAllSelectedShapesToCursor(Controller controller, double x, double y) {
		for (Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
			if (shape.getType() != ShapeType.POINT) continue;
			shape.setCoordinates(shape.getSelectedX() + x, shape.getSelectedY() + y);
		}
	}
	/**
	 * Finalizes the selected shapes
	 * @param controller controller
	 * @param canvas custom canvas
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public static void finalizeSelectedShapes(Controller controller, CustomCanvas canvas, double x, double y) {
		finalizeSelectedShapes(controller, canvas, x, y, true);
	}
	/**
	 * Finalizes the selected shapes with history
	 * @param controller controller
	 * @param canvas custom canvas
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param history history
	 */
	public static void finalizeSelectedShapes(Controller controller, CustomCanvas canvas, double x, double y, boolean history) {
		Shape selectedShape = controller.getSelectedShape();
		Shape hoveredShape = controller.getHoveredShape();

		if (hoveredShape != null && selectedShape.getType() == ShapeType.POINT && hoveredShape.getType() == ShapeType.POINT) {
			if (history) controller.getHistoryManager().finalizeSelectionMerge(controller.getShapes(Controller.SingletonType.PREVIEW), (Point) selectedShape, (Point) hoveredShape);
			Iterator<Shape> iterator = selectedShape.getChildren().iterator();
			while (iterator.hasNext()) {
				Shape childShape = iterator.next();
				if (childShape.getPoints().contains(hoveredShape)) {
					iterator.remove();
					controller.deleteShape(childShape, Controller.SingletonType.PREVIEW);
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
		controller.getShapes(Controller.SingletonType.PREVIEW).forEach(shape -> shape.setSelected(false));
		controller.transferAllShapesTo(Controller.SingletonType.FINAL);
	}
	/**
	 * Rotates the selected shape
	 * @param controller controller
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
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
	/**
	 * Finalizes the selected rotation
	 * @param controller controller
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public static void finalizeSelectedRotation(Controller controller, double x, double y) {
		rotateSelectedShape(controller, x, y);
		controller.setSelectedShape(null);
		controller.getHistoryManager().finalizeSelection(controller.getShapes(Controller.SingletonType.PREVIEW));
		controller.transferAllShapesTo(Controller.SingletonType.FINAL);
	}
	/**
	 * Deletes the selected shape
	 * @param controller controller
	 */
	public static void deleteShape(Controller controller, Shape shape) {
		controller.getHistoryManager().deleteShape(shape);
		controller.deleteShape(shape, Controller.SingletonType.FINAL);
	}
	/**
	 * Transfers the points
	 * @param controller
	 * @param point
	 * @param type
	 */
	private static void transferPoints(Controller controller, Shape point, Controller.SingletonType type) {
		if (canTransferShape(controller, point, type)) controller.transferSingleShapeTo(point, type);
		for (Shape childShape : point.getChildren()) {
			if (canTransferShape(controller, childShape, type)) controller.transferSingleShapeTo(childShape, type);
		}
	}
	/**
	 * Checks if the shape can be transferred
	 * @param controller controller
	 * @param shape shape
	 * @param type type
	 * @return true if the shape can be transferred
	 */
	private static boolean canTransferShape(Controller controller, Shape shape, Controller.SingletonType type) {
		if (type == Controller.SingletonType.PREVIEW) shape.setSelected(true);
		return !controller.getShapeContainer(type).getShapes().contains(shape);
	}

	public static void drawSelectionBox(Controller controller, CustomCanvas gc, double x1, double y1, double x2, double y2) {
		controller.createShape(x1, y1, x2, y2, ShapeType.RECTANGLE, null).draw(gc);
	}
	/**
	 * Selects the shapes inside the selection box
	 * @param controller controller
	 * @param mouseX X coordinate
	 * @param mouseY Y coordinate
	 * @param x1 X1 coordinate
	 * @param y1 Y1 coordinate
	 * @param x2 X2 coordinate
	 * @param y2 Y2 coordinate
	 */
	public static void selectShapesInsideBox(Controller controller, double mouseX, double mouseY, double x1, double y1, double x2, double y2){
		List<Shape> selectedPoints = new ArrayList<>();
		boolean isNewSelection = controller.getShapes(Controller.SingletonType.PREVIEW).isEmpty();

		for(Shape shape : controller.getShapes(Controller.SingletonType.FINAL)) {
			if(shape.getType() == ShapeType.POINT && isInsideSelectionBox(shape.getX(), shape.getY(), x1, y1, x2, y2)){
				selectedPoints.add(shape);
			}
		}
		if(selectedPoints.isEmpty()) return;
		selectedPoints.forEach(point -> {
			transferPoints(controller, point, Controller.SingletonType.PREVIEW);
			point.setSelectedCoordinates(point.getX() - mouseX, point.getY() - mouseY);
		});
		controller.setSelectedShape(controller.getShapes(Controller.SingletonType.PREVIEW).getFirst());
		controller.setHoveredShape(controller.getShapes(Controller.SingletonType.PREVIEW).getFirst());

		if (isNewSelection) controller.getHistoryManager().startSelection(controller.getShapes(Controller.SingletonType.PREVIEW));
		else controller.getHistoryManager().addToSelection(controller.getShapes(Controller.SingletonType.PREVIEW));
	}
	/**
	 * Checks if the point is inside the selection box
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param x1 X1 coordinate
	 * @param y1 Y1 coordinate
	 * @param x2 X2 coordinate
	 * @param y2 Y2 coordinate
	 * @return true if the point is inside the selection box
	 */
	public static boolean isInsideSelectionBox(double x, double y, double x1, double y1, double x2, double y2) {
		double minX = Math.min(x1, x2);
		double maxX = Math.max(x1, x2);
		double minY = Math.min(y1, y2);
		double maxY = Math.max(y1, y2);
		return(x >= minX && x <= maxX && y >= minY && y <= maxY);
	}
}
