package gregtech.api.util;

public class Vector2 {
    public double x;
    public double y;

    public Vector2() {
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this(v.x, v.y);
    }

    public Vector2 copy() {
        return new Vector2(this);
    }

    public Vector2 copy(Vector2 dst) {
        return dst.set(this);
    }

    public Vector2 set(double vx, double vy) {
        this.x = vx;
        this.y = vy;
        return this;
    }

    public Vector2 set(Vector2 v) {
        return this.set(v.x, v.y);
    }

    public Vector2 add(double vx, double vy) {
        this.x += vx;
        this.y += vy;
        return this;
    }

    public Vector2 add(Vector2 v) {
        return this.add(v.x, v.y);
    }

    public Vector2 sub(double vx, double vy) {
        this.x -= vx;
        this.y -= vy;
        return this;
    }

    public Vector2 sub(Vector2 v) {
        return this.sub(v.x, v.y);
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public double distanceSquared(double vx, double vy) {
        double dx = vx - this.x;
        double dy = vy - this.y;
        return dx * dx + dy * dy;
    }

    public double distance(double vx, double vy) {
        return Math.sqrt(this.distanceSquared(vx, vy));
    }

    public double distance(Vector2 v) {
        return this.distance(v.x, v.y);
    }

}
