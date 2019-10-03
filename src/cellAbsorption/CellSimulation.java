package cellAbsorption;

import comp127graphics.CanvasWindow;
import comp127graphics.Ellipse;
import comp127graphics.Point;

import java.awt.Color;
import java.util.Random;

@SuppressWarnings("SameParameterValue")
public class CellSimulation {
    private static final double
        WIGGLINESS = 0.2,
        WANDER_FROM_CENTER = 60000;

    private CanvasWindow canvas;
    private Random rand = new Random();
    private Ellipse shape;
    private double radius;
    private double direction;

    public static void main(String[] args) {
        new CellSimulation();
    }

    public CellSimulation() {
        canvas = new CanvasWindow("Cell Absorption", 800, 800);
        populateCells(400);

        //noinspection InfiniteLoopStatement
        while (true) {
            canvas.draw();
            canvas.pause(10);
            moveCell();
        }
    }

    private void populateCells(int count) {
        double size = rand.nextInt(5) + 2;
        createCell(
            rand.nextDouble() * (canvas.getWidth() - size),
            rand.nextDouble() * (canvas.getWidth() - size),
            size,
            Color.getHSBColor(rand.nextFloat(), rand.nextFloat() * 0.5f + 0.1f, 1));
        canvas.add(shape);
    }

    public void createCell(double x, double y, double radius, Color color) {
        shape = new Ellipse(x, y, radius * 2, radius * 2);
        shape.setFillColor(color);
        this.radius = radius;
        direction = normalizeRadians(Math.random() * Math.PI * 2);
    }

    private void moveCell() {
        Point canvasCenter = new Point(canvas.getWidth() / 2.0, canvas.getHeight() / 2.0);
        move(canvasCenter);
        setRadius(radius + 0.02);
    }

    private void setRadius(double newRadius) {
        if (newRadius < 0) {
            newRadius = 0;
        }
        radius = newRadius;
        Point previousCenter = shape.getCenter();
        shape.setWidthAndHeight(newRadius * 2, newRadius * 2);
        shape.setCenter(previousCenter);
    }

    public void move(Point centerOfGravity) {
        shape.moveBy(Math.cos(direction), Math.sin(direction));

        double distToCenter = shape.getCenter().distance(centerOfGravity);
        double angleToCenter = centerOfGravity.subtract(shape.getCenter()).angle();
        double turnTowardCenter = normalizeRadians(angleToCenter - direction);

        direction = normalizeRadians(
            direction
                + (Math.random() - 0.5) * WIGGLINESS
                + turnTowardCenter * Math.tanh(distToCenter / WANDER_FROM_CENTER));
    }

    private static double sqr(double x) {
        return x * x;
    }

    private static double normalizeRadians(double theta) {
        double pi2 = Math.PI * 2;
        return ((theta + Math.PI) % pi2 + pi2) % pi2 - Math.PI;
    }
}
