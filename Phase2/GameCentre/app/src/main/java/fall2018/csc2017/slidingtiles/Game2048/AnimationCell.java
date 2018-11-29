package fall2018.csc2017.slidingtiles.Game2048;

/**
 * An animated cell - store data here to be used by the bitmap
 */
class AnimationCell extends Cell {
    /**
     * Extra info, such as previous position of the cell during a move animation
     */
    public final int[] extras;

    /**
     * Type of animation.
     */
    private final int animationType;

    /**
     * The time the animation will take
     */
    private final long animationTime;

    /**
     * The time delayed before animation starts
     */
    private final long delayTime;

    /**
     * Time elapsed during the whole animation, including delay time
     */
    private long timeElapsed;

    /**
     * Create new animation cell
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param animationType type of animation
     * @param length length of animation
     * @param delay delay before start of animation
     * @param extras extra info needed for the information
     */
    public AnimationCell(int x, int y, int animationType, long length, long delay, int[] extras) {
        super(x, y);
        this.animationType = animationType;
        animationTime = length;
        delayTime = delay;
        this.extras = extras;
    }

    /**
     * Return the animation type
     * @return the animation type
     */
    public int getAnimationType() {
        return animationType;
    }

    /**
     * Accumulate the this.timeElapsed by timeElapsed
     * @param timeElapsed the time elapsed
     */
    public void tick(long timeElapsed) {
        this.timeElapsed = this.timeElapsed + timeElapsed;
    }

    /**
     * Return true iff the animation is done
     * @return true iff the animation is done
     */
    public boolean animationDone() {
        return animationTime + delayTime < timeElapsed;
    }

    /**
     * Get the percent done - 0% meaning starting, 100% means done
     * @return the percentage completed of the animation
     */
    public double getPercentageDone() {
        return Math.max(0, 1.0 * (timeElapsed - delayTime) / animationTime);
    }

    /**
     * Return true iff the animation is happening
     * @return true iff the animation is happening
     */
    public boolean isActive() {
        return (timeElapsed >= delayTime);
    }

}
