package view.GUIElements;

import javafx.scene.layout.GridPane;

public class CanvasContainer extends GridPane {
  private final CustomCanvas[] layers = new CustomCanvas[2];

  public CanvasContainer(double width, double height) {
    super();

    for (int i = 0; i < layers.length; i++) {
      DrawingCanvas canvas = new DrawingCanvas(width, height);
      layers[i] = canvas;
      add(canvas, 0, 0);
    }
  }

  public CustomCanvas getLayer(int index) {
    return layers[index];
  }

  public void resizeCanvas(double width, double height) {
    for (CustomCanvas layer : layers) {
      layer.resize(width, height);
    }
  }

  public void clear() {
    for (CustomCanvas layer : layers) {
      layer.clear();
    }
  }
}
