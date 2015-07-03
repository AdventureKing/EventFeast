package com.example.daddyz.turtleboys.subclasses;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * Created by Gregory Hooks Jr on 7/3/2015.
 */
/**
 * Rotation animation that rotates a 2D view 90 degrees on either X or Y axis in 3D space. The
 * rotation is performed around the view center point by its defined rotation direction and position
 * it will start from or end to. The rotation duration can be specified as well as whether the
 * rotation should be accelerated or decelerated in time.
 */
public class User_Icon_Animation extends Animation {
    public enum Rotate {
        UP, DOWN, LEFT, RIGHT
    }
    public enum Angle {
        FROM_DEGREES_0, TO_DEGREES_0
    }
    private static final float DEPTH_Z = 250.0f;
    private static final float ROTATE_DEGREES = -90f;
    private final Rotate rotate;
    private final Angle angle;
    private final int duration;
    private final boolean accelerate;
    private Camera camera;
    private float centerX;
    private float centerY;
    private float fromDegrees;
    private float toDegrees;
    /**
     * Gets animations that flip the view by 3D rotations.
     */
    public AnimationPair getFlipAnimations(Rotate rotate, int duration) {
        return new AnimationPair(
                new User_Icon_Animation(rotate, Angle.FROM_DEGREES_0, duration, true),
                new User_Icon_Animation(rotate, Angle.TO_DEGREES_0, duration, false));
    }
    public User_Icon_Animation(Rotate rotate, Angle angle, int duration, boolean accelerate) {
        this.rotate = rotate;
        this.angle = angle;
        this.duration = duration;
        this.accelerate = accelerate;
    }
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        super.setDuration(duration);
        super.setInterpolator(
                accelerate ? new AccelerateInterpolator() : new DecelerateInterpolator());
        camera = new Camera();
        centerX = width / 2.0f;
        centerY = height / 2.0f;
        if (angle == Angle.FROM_DEGREES_0) {
            fromDegrees = 0;
            switch (rotate) {
                case RIGHT:
                case UP:
                    toDegrees = ROTATE_DEGREES;
                    break;
                case LEFT:
                case DOWN:
                    toDegrees = -ROTATE_DEGREES;
                    break;
            }
        } else if (angle == Angle.TO_DEGREES_0) {
            toDegrees = 0;
            switch (rotate) {
                case RIGHT:
                case UP:
                    fromDegrees = -ROTATE_DEGREES;
                    break;
                case LEFT:
                case DOWN:
                    fromDegrees = ROTATE_DEGREES;
                    break;
            }
        }
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        camera.save();
        if (angle == Angle.FROM_DEGREES_0) {
            camera.translate(0.0f, 0.0f, DEPTH_Z * interpolatedTime);
        } else if (angle == Angle.TO_DEGREES_0) {
            camera.translate(0.0f, 0.0f, DEPTH_Z * (1.0f - interpolatedTime));
        }
        float degrees = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime);
        switch (rotate) {
            case RIGHT:
            case LEFT:
                camera.rotateY(degrees);
                break;
            case UP:
            case DOWN:
                camera.rotateX(degrees);
                break;
        }
        final Matrix matrix = t.getMatrix();
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

    public class AnimationPair {

        private final Animation first;
        private final Animation second;

        AnimationPair(Animation first, Animation second) {
            this.first = first;
            this.second = second;
        }

        public Animation first() {
            return first;
        }

        public Animation second() {
            return second;
        }
    }
}
