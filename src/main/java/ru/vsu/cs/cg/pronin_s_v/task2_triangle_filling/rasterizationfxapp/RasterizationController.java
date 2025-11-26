package ru.vsu.cs.cg.pronin_s_v.task2_triangle_filling.rasterizationfxapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ru.vsu.cs.cg.pronin_s_v.task2_triangle_filling.rasterization.Rasterization;

import java.util.ArrayList;
import java.util.List;

public class RasterizationController {

  private static class Vertex {
    double x, y;
    Color color;

    Vertex(double x, double y, Color color) {
      this.x = x;
      this.y = y;
      this.color = color;
    }
  }

  private final List<Vertex> vertices = new ArrayList<>();
  private Vertex dragged = null;
  private static final double RADIUS = 10;

  @FXML AnchorPane anchorPane;

  @FXML private Canvas canvas;

  @FXML
  private void initialize() {

    anchorPane
        .widthProperty()
        .addListener(
            (o, oldV, newV) -> {
              canvas.setWidth(newV.doubleValue());
              redraw();
            });

    anchorPane
        .heightProperty()
        .addListener(
            (o, oldV, newV) -> {
              canvas.setHeight(newV.doubleValue());
              redraw();
            });

    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onPress);
    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onDrag);
    canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> dragged = null);

    redraw();
  }

  private void onPress(MouseEvent e) {
    double x = e.getX();
    double y = e.getY();

    Vertex v = findNear(x, y);

    // если нашли существующую вершину
    if (v != null) {
      dragged = v;
      return;
    }

    // если меньше трёх точек — добавить новую
    if (vertices.size() < 3) {
      Color c =
          switch (vertices.size()) {
            case 0 -> Color.RED;
            case 1 -> Color.GREEN;
            default -> Color.BLUE;
          };

      vertices.add(new Vertex(x, y, c));
      redraw();
    }
  }

  private void onDrag(MouseEvent e) {
    if (dragged != null) {
      dragged.x = e.getX();
      dragged.y = e.getY();
      redraw();
    }
  }

  private Vertex findNear(double x, double y) {
    for (Vertex v : vertices) {
      double dx = x - v.x;
      double dy = y - v.y;
      if (dx * dx + dy * dy <= RADIUS * RADIUS) return v;
    }
    return null;
  }

  private void redraw() {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    int w = (int) canvas.getWidth();
    int h = (int) canvas.getHeight();

    Rasterization.drawRectangle(gc, 0, 0, w, h, Color.BLACK);

    if (vertices.size() == 3) {
      Vertex a = vertices.get(0);
      Vertex b = vertices.get(1);
      Vertex c = vertices.get(2);

      Rasterization.fillTriangle(gc, a.x, a.y, a.color, b.x, b.y, b.color, c.x, c.y, c.color);
    }

    for (Vertex v : vertices) {
      Rasterization.drawRectangle(gc, (int) v.x - 3, (int) v.y - 3, 6, 6, Color.WHITE);
    }
  }
}
