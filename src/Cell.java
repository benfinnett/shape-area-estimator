import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Represents one pixel within the drawing plane (aka {@link Grid}).
 */
class Cell extends JPanel {
    /**
     * The position of the Cell in the grid.
     */
    private final int gridPosition;
    /**
     * Size of the square cell in pixels.
     */
    private final int size;
    /**
     * The state of the cell.
     *
     * @see CellStates
     */
    private CellStates state = CellStates.DEFAULT;
    /**
     * Tracks whether the cell has been counted by the algorithm or not.
     */
    private boolean beenCounted = false;

    // Colors
    public final Color defaultColor = new Color(230, 230, 230);
    public final Color paintedColor = new Color(0, 0, 0);
    public final Color startPointColor = new Color(200, 175, 70);
    public final Color areaColor = new Color(200, 100, 115);
    public final Color hoverColor = new Color(150, 150, 150);

    // Mouse information
    /**
     * Tracks whether a cell has changed state since the last time a mouse button was pressed.
     */
    public boolean changedSinceClick = false;
    /**
     * Tracks whether the mouse is currently hovering over the cell.
     */
    private boolean isMouseHovering = false;

    /**
     * Initialises the {@link Cell} object and adds necessary {@link MouseAdapter} controls.
     *
     * @param parentGrid The {@link Grid} object of which the cell belongs to.
     * @param size The size of the cell (square) in pixels.
     */
    public Cell(Grid parentGrid, int gridPosition, int size) {
        this.gridPosition = gridPosition;
        this.size = size;
        // Initially colour the cell
        colorCell();

        // Add listener for mouse clicking and location events
        addMouseListener(new MouseAdapter() {
            // Triggered when the mouse enters the bounds of the cell
            @Override
            public void mouseEntered(MouseEvent e) {
                setMouseHovering(true);
                // If the left mouse button is clicked, the cell isn't a start point and hasn't changed state yet
                // Aka when the user is drag-painting
                if (parentGrid.getMouseButton() == MouseEvent.BUTTON1
                        &&  !changedSinceClick
                        &&  state != CellStates.START_POINT) {

                    // Change cell state to the current paint colour
                    state = parentGrid.getDragPaintState();
                    changedSinceClick = true;
                    colorCell();
                }
                // Show the hover cursor only if the cell state is default
                if (state == CellStates.DEFAULT) {
                    colorCell(hoverColor);
                }
            }

            // Triggered when the mouse leaves the bounds of the cell
            @Override
            public void mouseExited(MouseEvent e) {
                setMouseHovering(false);
                // Update cell to remove hover cursor colour
                colorCell();
            }

            // Triggered when any button on the mouse is pressed down
            @Override
            public void mousePressed(MouseEvent e) {
                // Update parent grid properties to reflect this
                parentGrid.setMouseButton(e.getButton());
                // If the mouse is hovering over this cell
                if (isMouseHovering()) {
                    // Left click on Cell
                    if (e.getButton() == MouseEvent.BUTTON1
                            &&  state != CellStates.START_POINT) {
                        switch (state) {
                            case DEFAULT -> state = CellStates.PAINTED;
                            case PAINTED -> state = CellStates.DEFAULT;
                        }
                        parentGrid.setDragPaintState(state);
                        // Always set the drag paint state to painted when starting on a start point for
                        // convenience
                    } else if (e.getButton() == MouseEvent.BUTTON1
                            &&  state == CellStates.START_POINT) {
                        parentGrid.setDragPaintState(CellStates.PAINTED);
                        // Right click on Cell for start point
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        switch (state) {
                            case DEFAULT:
                                // Only add a start point cell if there are no others
                                if (parentGrid.getStartPoint() == null) {
                                    state = CellStates.START_POINT;
                                    parentGrid.setStartPoint(getThis());
                                }
                                break;
                            // Remove the start point cell
                            case START_POINT:
                                state = CellStates.DEFAULT;
                                parentGrid.setStartPoint(null);
                                break;
                        }
                    }
                    // Update the cell colour to reflect any changes
                    colorCell();
                    changedSinceClick = true;
                }
                // Color back to hover if necessary
                mouseEntered(e);
            }

            // Triggered when a pressed mouse button is released
            @Override
            public void mouseReleased(MouseEvent e) {
                // Update necessary parent grid properties
                parentGrid.setMouseButton(0);
                parentGrid.setAllChangedSinceClick(false);
            }
        });
    }

    /**
     * Handles setting the size of the cell.
     *
     * @return {@link Dimension} object.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }

    /**
     * Colour the cell depending on the cell state.
     *
     * @see Cell#state
     * @see CellStates
     */
    public void colorCell() {
        switch (state) {
            case PAINTED -> setBackground(paintedColor);
            case START_POINT -> setBackground(startPointColor);
            case AREA -> setBackground(areaColor);
            default -> setBackground(defaultColor);
        }
    }

    /**
     * Color the cell based on a given color.
     *
     * @param color {@link Color} object.
     */
    public void colorCell(Color color) {
        setBackground(color);
    }

    public Cell getThis() {
        return this;
    }
    public CellStates getState() {
        return state;
    }
    public void setState(CellStates state) {
        this.state = state;
        colorCell();
    }
    public void setChangedSinceClick(boolean changedSinceClick) {
        this.changedSinceClick = changedSinceClick;
    }
    public boolean isMouseHovering() {
        return isMouseHovering;
    }
    public void setMouseHovering(boolean mouseHovering) {
        isMouseHovering = mouseHovering;
    }
    public int getGridPosition() {
        return gridPosition;
    }
    public boolean hasBeenCounted() {
        return beenCounted;
    }
    public void setBeenCounted(boolean beenCounted) {
        this.beenCounted = beenCounted;
    }
}