package blib.util;

import java.awt.geom.Line2D;
public class Line {

    public Position start, end;
    
    public Line(double x1, double y1, double x2, double y2){
        start = new Position(x1, y1);
        end = new Position(x2, y2);
    }

    public Line(Position s, Position e){
        start = s;
        end = e;
    }

    public static Line ray(Position pos, double dir, double length){
        Position vector = BTools.angleToVector(dir);
        vector.x *= length;
        vector.y *= length;
        vector.x += pos.x;
        vector.y += pos.y;
        return new Line(pos.copy(), vector);
    }

    public boolean intersects(Line line){
        return Line2D.linesIntersect(start.x, start.y, end.x, end.y, line.start.x, line.start.y, line.end.x, line.end.y);
    }

    public Position intersection(Line line) {
        Line a = this;
        double x1 = a.start.x, y1 = a.start.y, x2 = a.end.x, y2 = a.end.y, x3 = line.start.x, y3 = line.start.y,
                x4 = line.end.x, y4 = line.end.y;
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) {
            return null;
        }

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Position(xi, yi);
    }
}
