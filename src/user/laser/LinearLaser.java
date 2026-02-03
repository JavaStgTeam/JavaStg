package user.laser;

import java.awt.*;
import stg.game.laser.Laser;

public class LinearLaser extends Laser {
    private float rotationSpeed;
    private boolean moving;
    private float moveSpeed;
    private float moveAngle;

    public LinearLaser(float x, float y, float angle, float length, float width, Color color) {
        super(x, y, angle, length, width, color);
        this.rotationSpeed = 0;
        this.moving = false;
        this.moveSpeed = 0;
        this.moveAngle = 0;
    }

    public LinearLaser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage, float rotationSpeed) {
        super(x, y, angle, length, width, color, warningTime, damage);
        this.rotationSpeed = rotationSpeed;
        this.moving = false;
        this.moveSpeed = 0;
        this.moveAngle = 0;
    }

    @Override
    protected void initBehavior() {
    }

    @Override
    protected void onUpdate() {
    }

    @Override
    protected void onMove() {
        if (active) {
            angle += rotationSpeed;

            if (moving) {
                x += (float)Math.cos(moveAngle) * moveSpeed;
                y += (float)Math.sin(moveAngle) * moveSpeed;
            }
        }
    }

    @Override
    public void update() {
        super.update();
    }

    public void setMovement(float moveSpeed, float moveAngle) {
        this.moveSpeed = moveSpeed;
        this.moveAngle = moveAngle;
        this.moving = moveSpeed != 0;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public boolean isOutOfBounds(int width, int height) {
        float leftBound = -width / 2.0f - length;
        float rightBound = width / 2.0f + length;
        float topBound = -height / 2.0f - length;
        float bottomBound = height / 2.0f + length;
        return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
    }

    @Override
    protected void onTaskStart() {
    }

    @Override
    protected void onTaskEnd() {
    }
}
