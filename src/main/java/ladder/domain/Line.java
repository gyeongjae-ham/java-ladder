package ladder.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;
import static ladder.domain.Direction.*;

public class Line {

    private static final Random random = new Random();
    private final List<Point> points;

    private Line(List<Point> points) {
        for (int i = 0 ; i < points.size() - 1 ; i++) {
            checkValidation(points.get(i), points.get(i + 1));
        }
        this.points = Collections.unmodifiableList(points);
    }

    private void checkValidation(Point point, Point nextPoint) {
        if (point.isConnectedTo(LEFT) && nextPoint.isConnectedTo(LEFT)) {
            throw new IllegalArgumentException("바로 옆에 있는 점들은 같은 방향으로 연결될 수 없다");
        }

        if (point.isConnectedTo(RIGHT) && nextPoint.isConnectedTo(RIGHT)) {
            throw new IllegalArgumentException("바로 옆에 있는 점들은 같은 방향으로 연결될 수 없다");
        }
    }

    public static Line of(List<Point> points) {
        return new Line(points);
    }

    public static Line ofLength(int length) {
        List<Point> points = new ArrayList<>();
        points.add(Point.in(0));

        for (int i = 1; i < length; i++) {
            Point right = Point.in(i);
            Point left = points.get(i - 1);
            connectPointRandomly(left, right);
            points.add(right);
        }

        return new Line(points);
    }

    private static void connectPointRandomly(Point left, Point right) {
        if (!random.nextBoolean() || left.existConnect() || right.existConnect()) {
            return;
        }
        left.connect(right);
    }

    public Point at(int idx) {
        return points.get(idx);
    }

    public List<Point> getPoints() {
        return points;
    }

    public int width() {
        return points.size();
    }

    public int move(int col) {
        if (moveLeft(col)) {
            return col - 1;
        }

        if (moveRight(col)) {
            return col + 1;
        }

        return col;
    }

    private boolean moveLeft(int col) {
        if (col <= 0) {
            return false;
        }

        Point point = points.get(col);
        return point.isConnectedTo(LEFT);
    }

    private boolean moveRight(int col) {
        if (col >= points.size() - 1) {
            return false;
        }

        Point point = points.get(col);
        return point.isConnectedTo(RIGHT);
    }
}