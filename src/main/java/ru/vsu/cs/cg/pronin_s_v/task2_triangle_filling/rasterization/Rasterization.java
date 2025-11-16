package ru.vsu.cs.cg.pronin_s_v.task2_triangle_filling.rasterization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rasterization {

    public static void drawRectangle(
            final GraphicsContext graphicsContext,
            final int x, final int y,
            final int width, final int height,
            final Color color
    ) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        for (int row = y; row < y + height; ++row) {
            for (int col = x; col < x + width; ++col) {
                pixelWriter.setColor(col, row, color);
            }
        }
    }

    public static void fillTriangle(
            GraphicsContext gc,
            double x0, double y0, Color c0,
            double x1, double y1, Color c1,
            double x2, double y2, Color c2
    ) {
        PixelWriter writer = gc.getPixelWriter();

        double denom = (y1 - y2) * (x0 - x2) +
                (x2 - x1) * (y0 - y2);

        if (Math.abs(denom) < 1e-8) return;  // треугольник вырожден

        int minX = (int)Math.floor(Math.min(x0, Math.min(x1, x2)));
        int maxX = (int)Math.ceil(Math.max(x0, Math.max(x1, x2)));
        int minY = (int)Math.floor(Math.min(y0, Math.min(y1, y2)));
        int maxY = (int)Math.ceil(Math.max(y0, Math.max(y1, y2)));

        minX = Math.max(minX, 0);
        minY = Math.max(minY, 0);
        maxX = Math.min(maxX, (int)gc.getCanvas().getWidth() - 1);
        maxY = Math.min(maxY, (int)gc.getCanvas().getHeight() - 1);

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {

                double px = x;
                double py = y;

                double alpha = ((y1 - y2)*(px - x2) + (x2 - x1)*(py - y2)) / denom;
                double beta  = ((y2 - y0)*(px - x2) + (x0 - x2)*(py - y2)) / denom;
                double gamma = 1.0 - alpha - beta;

                if (alpha >= 0 && beta >= 0 && gamma >= 0) {
                    double r = alpha*c0.getRed()   + beta*c1.getRed()   + gamma*c2.getRed();
                    double g = alpha*c0.getGreen() + beta*c1.getGreen() + gamma*c2.getGreen();
                    double b = alpha*c0.getBlue()  + beta*c1.getBlue()  + gamma*c2.getBlue();

                    writer.setColor(x, y, new Color(r, g, b, 1.0));
                }
            }
        }
    }
}