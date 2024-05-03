package view.GUIElements;

import javafx.geometry.Orientation;
import javafx.scene.control.Slider;
/**
 * Ruler class
 */
public class Ruler extends Slider {
    /**
     * Constructor for the ruler
     * @param isVertical is vertical
     */
    public Ruler(boolean isVertical){
        super();
        if(isVertical)
            this.setOrientation(Orientation.VERTICAL);

        else
            this.setOrientation(Orientation.HORIZONTAL);
        this.setMin(0);
        this.setMax(750);
        this.setValue(0);
        this.setShowTickLabels(true);
        this.setShowTickMarks(true);
        this.setMajorTickUnit(75);
        this.setMinorTickCount(25);
        this.setBlockIncrement(1);
    }
    /**
     * Updates the ruler
     * @param value value
     */
    public void updateRuler(double value){
        this.setMax(Math.round(750 / value));
        this.setMajorTickUnit(Math.round(75 / value));
        this.setMinorTickCount((int) (25 / value));
        this.setBlockIncrement(Math.round(1 / value));
    }


}
