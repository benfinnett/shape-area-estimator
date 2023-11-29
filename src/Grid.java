import com.sun.jdi.InternalException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Represents a drawing plane allowing shapes to be drawn.
 * <p>
 * The denoted data structure for the algorithm!
 */
public class Grid {
    /**
     * Array of {@link Cell} objects for data manipulation within {@link Grid}.
     */
    private Cell[] cells;
    /**
     * The number of {@link Cell} objects that make up each row and column in the grid.
     */
    private int numberOfCells;
    /**
     * The starting cell for where the algorithm will begin.
     */
    private Cell startPoint = null;

    // Content panel
    /**
     * Constructs {@link JPanel} object.
     */
    private JPanel panel;

    // Mouse information
    /**
     * Represents which mouse button is pressed down, if any.
     * <p>
     * @see MouseEvent#getButton()
     *
     */
    private int mouseButton;
    /**
     * Represents the cell state {@link CellStates} that the grid should be painting
     * when the user is drag-painting as opposed to single-cell painting.
     */
    private CellStates dragPaintState = CellStates.PAINTED;

    /**
     * Initialises {@link Grid} object, creating all {@link Cell} objects in the grid.
     * <p>
     * The grid will always be square for simplicity.
     *
     * @param numberOfCells The number of cells that make up each row and column.
     */
    public Grid(int numberOfCells) {
        // Validation
        final int maxNumberOfCells = 128;
        final int minCellSize = 2;

        int cellSize = (int) Math.floor((float) Toolkit.getDefaultToolkit().getScreenSize().height/numberOfCells);
        // Validation of args
        if (numberOfCells > maxNumberOfCells) {
            throw new InternalException("Having that many cells in the grid will cause the program to hang! Please try a number less than " + maxNumberOfCells+1);
        } else if (cellSize < minCellSize) {
            throw new InternalException("Cell size must be greater than " + minCellSize);
        }

        // Create the grid layout formatting and build the grid
        buildGrid(numberOfCells);
    }

    /**
     * Builds a grid of {@link Cell} objects of row and column size equal to
     * numberOfCells.
     *
     * @param numberOfCells The number of cells that make up each row and column.
     */
    public void buildGrid(int numberOfCells) {
        // Panel setup
        panel = new JPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());

        // Variables
        this.numberOfCells = numberOfCells;
        int cellSize = (int) Math.floor((float) (Toolkit.getDefaultToolkit().getScreenSize().height-100)/numberOfCells);
        cells = new Cell[numberOfCells * numberOfCells];

        // For each row in the grid
        for (int i = 0; i < numberOfCells; i++) {
            // For each column in the grid
            for (int j = 0; j < numberOfCells; j++) {
                // Format the grid
                gridBagConstraints.gridx = j;
                gridBagConstraints.gridy = i;

                // Create a Cell object in each position on the grid
                int cellGridPosition = (i*numberOfCells) + j;
                Cell cell = new Cell(this, cellGridPosition, cellSize);
                // Add it to cells array and to the JPanel
                cells[cellGridPosition] = cell;
                panel.add(cell, gridBagConstraints);
            }
        }
    }

    /**
     * Set the changedSinceClick property for every {@link Cell} to the same boolean value.
     *
     * @param bool Arbitrary boolean value
     */
    public void setAllChangedSinceClick(boolean bool) {
        for (Cell cell : cells) {
            cell.setChangedSinceClick(bool);
        }
    }

    /**
     * Helper function to get the {@link Cell} neighbourhood the given cell.
     *
     * @param id ID of a given cell.
     * @return Array of {@link Cell} {@code null} or {@code null} values.
     */
    public ArrayList<Cell> getNeighbouringCells(int id) {
        ArrayList<Cell> neighbourhood = new ArrayList<>();

        // Cell to the left (-1,0)
        // Add if cell is not in the first column
        if (id % numberOfCells > 0) {
            neighbourhood.add(cells[id - 1]);
        }

        // Cell above (0,+1)
        // Add if cell is not in the first row
        // floor(id/numberOfCells) gives the row index of the id
        if ((int) Math.floor((float) id/numberOfCells) > 0) {
            neighbourhood.add(cells[id - numberOfCells]);
        }

        // Cell to the right (+1,0)
        // Add if cell is not in the last column
        if (id % numberOfCells < numberOfCells-1) {
            neighbourhood.add(cells[id + 1]);
        }

        // Cell below (0,-1)
        // Add if cell is not in the last row
        // floor(id/numberOfCells) == floor(cells[-1].id/numberOfCells)
        if ((int) Math.floor((float) id/numberOfCells) < (int) Math.floor((float) cells[cells.length-1].getGridPosition()/numberOfCells)) {
            neighbourhood.add(cells[id + numberOfCells]);
        }

        return neighbourhood;
    }

    public JPanel getPanel() {
        return panel;
    }
    public int getMouseButton() {
        return mouseButton;
    }
    public void setMouseButton(int mouseButton) {
        this.mouseButton = mouseButton;
    }
    public CellStates getDragPaintState() {
        return dragPaintState;
    }
    public void setDragPaintState(CellStates dragPaintState) {
        this.dragPaintState = dragPaintState;
    }
    public Cell getStartPoint() {
        return startPoint;
    }
    public void setStartPoint(Cell cell) {
        startPoint = cell;
    }
    public Cell[] getCells() {
        return cells;
    }
}