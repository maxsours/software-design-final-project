package fall2018.csc2017.slidingtiles.Game2048;

import java.util.ArrayList;

/**
 * A grid of animation cells
 */
public class AnimationGrid {
    public final ArrayList<AnimationCell> globalAnimation = new ArrayList<>(); //list of global animations
    private final ArrayList<AnimationCell>[][] field; //2-D array of lists of cell animation
    private int activeAnimations = 0; // number of active animations
    private boolean oneMoreFrame = false; // true iff there is at least one more frame in the animation

    /**
     * Create a new AnimationGrid
     * @param x num rows in field
     * @param y num cols in field
     */
    public AnimationGrid(int x, int y) {
        field = new ArrayList[x][y];

        for (int xx = 0; xx < x; xx++) {
            for (int yy = 0; yy < y; yy++) {
                field[xx][yy] = new ArrayList<>();
            }
        }
    }

    /**
     * Start the animation
     * @param x x-coordinate where the animation is (or -1 for global)
     * @param y y-coordinate where the animation is (or -1 for global)
     * @param animationType type of animation
     * @param length length of animation
     * @param delay delay before animation start
     * @param extras extra info needed for specific information
     */
    public void startAnimation(int x, int y, int animationType, long length, long delay, int[] extras) {
        AnimationCell animationToAdd = new AnimationCell(x, y, animationType, length, delay, extras);
        if (x == -1 && y == -1) {
            globalAnimation.add(animationToAdd);
        } else {
            field[x][y].add(animationToAdd);
        }
        activeAnimations = activeAnimations + 1;
    }

    /**
     * Move all animations forward
     * @param timeElapsed the timeElapsed
     */
    public void tickAll(long timeElapsed) {
        ArrayList<AnimationCell> cancelledAnimations = new ArrayList<>();
        for (AnimationCell animation : globalAnimation) {
            animation.tick(timeElapsed);
            if (animation.animationDone()) {
                cancelledAnimations.add(animation);
                activeAnimations = activeAnimations - 1;
            }
        }

        for (ArrayList<AnimationCell>[] array : field) {
            for (ArrayList<AnimationCell> list : array) {
                for (AnimationCell animation : list) {
                    animation.tick(timeElapsed);
                    if (animation.animationDone()) {
                        cancelledAnimations.add(animation);
                        activeAnimations = activeAnimations - 1;
                    }
                }
            }
        }

        for (AnimationCell animation : cancelledAnimations) {
            cancelAnimation(animation);
        }
    }

    /**
     * Return true iff there is an active animation and update oneMoreFrame
     * @return true iff there is an active animation
     */
    public boolean isAnimationActive() {
        if (activeAnimations != 0) {
            oneMoreFrame = true;
            return true;
        } else if (oneMoreFrame) {
            oneMoreFrame = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the animation cell at position (x,y)
     * @param x the x-position
     * @param y the y-position
     * @return the cell at field[x][y]
     */
    public ArrayList<AnimationCell> getAnimationCell(int x, int y) {
        return field[x][y];
    }

    /**
     * Cancel all animations
     */
    public void cancelAnimations() {
        for (ArrayList<AnimationCell>[] array : field) {
            for (ArrayList<AnimationCell> list : array) {
                list.clear();
            }
        }
        globalAnimation.clear();
        activeAnimations = 0;
    }

    /**
     * Cancel a specific animation
     * @param animation the animation cell to be cancelled
     */
    private void cancelAnimation(AnimationCell animation) {
        if (animation.getX() == -1 && animation.getY() == -1) {
            globalAnimation.remove(animation);
        } else {
            field[animation.getX()][animation.getY()].remove(animation);
        }
    }

}
