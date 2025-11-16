package ru.vsu.cs.cg.pronin_s_v.task2_triangle_filling.rasterization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RasterizationMathTest {

    @Test
    void computeDenominator_ForNonDegenerateTriangle_NotZero() {
        // Прямоугольный треугольник (0,0), (2,0), (0,2)
        double denom = Rasterization.computeDenominator(
                0, 0,
                2, 0,
                0, 2
        );

        assertTrue(Math.abs(denom) > 1e-8,
                "Для невырожденного треугольника знаменатель не должен быть близок к нулю");
    }

    @Test
    void computeDenominator_ForDegenerateTriangle_Zero() {
        // Вырожденный треугольник: точки на одной прямой (0,0), (1,1), (2,2)
        double denom = Rasterization.computeDenominator(
                0, 0,
                1, 1,
                2, 2
        );

        assertTrue(Math.abs(denom) < 1e-8,
                "Для вырожденного треугольника знаменатель должен быть близок к нулю");
    }

    @Test
    void computeBarycentric_AtVertices_GivesCorrectCoordinates() {
        // Треугольник (0,0), (2,0), (0,2)
        double x0 = 0, y0 = 0;
        double x1 = 2, y1 = 0;
        double x2 = 0, y2 = 2;

        // Вершина A
        double[] bcA = Rasterization.computeBarycentric(
                x0, y0, x0, y0, x1, y1, x2, y2
        );
        assertArrayEquals(
                new double[]{1.0, 0.0, 0.0},
                bcA,
                1e-9,
                "В вершине A барицентрические координаты должны быть (1,0,0)"
        );

        // Вершина B
        double[] bcB = Rasterization.computeBarycentric(
                x1, y1, x0, y0, x1, y1, x2, y2
        );
        assertArrayEquals(
                new double[]{0.0, 1.0, 0.0},
                bcB,
                1e-9,
                "В вершине B барицентрические координаты должны быть (0,1,0)"
        );

        // Вершина C
        double[] bcC = Rasterization.computeBarycentric(
                x2, y2, x0, y0, x1, y1, x2, y2
        );
        assertArrayEquals(
                new double[]{0.0, 0.0, 1.0},
                bcC,
                1e-9,
                "В вершине C барицентрические координаты должны быть (0,0,1)"
        );
    }

    @Test
    void computeBarycentric_InsideTriangle_SumIsOneAndAllNonNegative() {
        // Треугольник (0,0), (2,0), (0,2)
        double x0 = 0, y0 = 0;
        double x1 = 2, y1 = 0;
        double x2 = 0, y2 = 2;

        // Точка внутри треугольника, например (1,1)
        double px = 1, py = 1;

        double[] bc = Rasterization.computeBarycentric(
                px, py, x0, y0, x1, y1, x2, y2
        );

        double alpha = bc[0];
        double beta  = bc[1];
        double gamma = bc[2];

        double sum = alpha + beta + gamma;

        assertEquals(1.0, sum, 1e-9, "Сумма барицентрических координат должна быть равна 1");
        assertTrue(alpha >= 0 && beta >= 0 && gamma >= 0,
                "Для точки внутри треугольника все барицентрические координаты должны быть неотрицательны");
    }
}