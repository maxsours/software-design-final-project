package fall2018.csc2017.slidingtiles.Game2048;

import org.junit.Test;

import static org.junit.Assert.*;

public class AnimationGridAndCellTest {

    AnimationGrid aGrid;

    /**
     * Create a 4x4 blank animation grid.
     */
    private void createBlankGrid(){
        aGrid = new AnimationGrid(4, 4);
    }

    /**
     * Create an animation grid with some animations in it.
     */
    private void createGrid(){
        createBlankGrid();
        aGrid.startAnimation(0, 0, MainGame.SPAWN_ANIMATION, 2, 1, null);
        aGrid.startAnimation(0, 0, MainGame.SPAWN_ANIMATION, 4, 0, null);
        aGrid.startAnimation(3, 3, MainGame.MERGE_ANIMATION, 1, 2, null);
        aGrid.startAnimation(-1, -1, MainGame.FADE_GLOBAL_ANIMATION, 1, 1, null);
    }

    /**
     * Test whether startAnimation works
     */
    @Test
    public void testStartAnimation() {
        createBlankGrid();
        aGrid.startAnimation(0, 0, MainGame.SPAWN_ANIMATION, 2, 1, null);
        assertEquals(MainGame.SPAWN_ANIMATION, aGrid.getAnimationCell(0, 0).get(0).getAnimationType());
    }

    /**
     * Test tickAll method
     */
    @Test
    public void testTickAll() {
        createGrid();
        aGrid.tickAll(1);
        assertTrue(aGrid.getAnimationCell(0, 0).get(0).isActive());
        assertFalse(aGrid.getAnimationCell(3, 3).get(0).isActive());
        assertTrue(0.25 == aGrid.getAnimationCell(0, 0).get(1).getPercentageDone());
        aGrid.tickAll(3);
        assertTrue(aGrid.isAnimationActive());
        aGrid.tickAll(1);
        assertTrue(aGrid.isAnimationActive());
        assertFalse(aGrid.isAnimationActive());
    }

    /**
     * Test whether cancelAnimations works
     */
    @Test
    public void testCancelAnimations() {
        createGrid();
        aGrid.cancelAnimations();
        assertEquals(0, aGrid.getAnimationCell(0, 0).size());
        assertFalse(aGrid.isAnimationActive());
    }

    @Test
    public void doSomeTesting(){

    }
}