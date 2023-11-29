import javax.swing.*;
import java.util.ArrayList;

/**
 *  The algorithm for estimating any 2D shape's area using a cellular automaton approach.
*/
class AreaEstimator {
    /**
     * {@link Grid} object for use with the algorithm.
     */
    private final Grid grid;
    /**
     * Tracks the number of {@link Cell} objects that have been counted by the algorithm
     */
    private int totalCountedCells;
    /**
     * How long to wait between each step for visualisation in milliseconds.
     */
    private boolean delaySteps = false;
    /**
     * The label to display the results to.
     */
    private JLabel areaCounterLabel;

    public AreaEstimator(Grid grid) {
        this.grid = grid;
    }

    /**
     * Check to see what state a cell is in and count it if appropriate, before looking at its
     * neighbours.
     *
     * @param cell A {@link Cell} object.
     */
    private void checkCell(Cell cell) {
        if (cell.hasBeenCounted()) {
            return;
        }
        areaCounterLabel.setText("Area: " + totalCountedCells + " units^2");
        ArrayList<Cell> neighbours = new ArrayList<>();
        // Implementation of the rule space f
        switch (cell.getState()) {
            case DEFAULT -> {
                totalCountedCells++;
                cell.setState(CellStates.AREA);
                cell.setBeenCounted(true);
                neighbours.addAll(grid.getNeighbouringCells(cell.getGridPosition()));
            }
            case PAINTED -> {
                totalCountedCells++;
                cell.setBeenCounted(true);
            }
        }
        // Check the cell's neighbours using recursion
        for (Cell neighbour : neighbours) {
            if (delaySteps) {
                Timer timer = new Timer(0, e -> checkCell(neighbour));
                timer.setRepeats(false);
                timer.start();
            } else {
                checkCell(neighbour);
            }
        }
    }

    /**
     * Checks for any boundary cells that are corners and thus not counted by the
     * von Neumann neighbourhood check.
     *
     * @param cells An array of {@link Cell} objects.
     */
    private void checkForCorners(Cell[] cells) {
        for (Cell cell : cells) {
            // If the cell isn't a border cell or has already been counted, continue
            if (cell.getState() != CellStates.PAINTED
                    ||  cell.hasBeenCounted()) {
                continue;
            }
            // Otherwise, find its neighbours
            ArrayList<Cell> neighbours = grid.getNeighbouringCells(cell.getGridPosition());
            // Loop over the neighbours
            for (Cell neighbour : neighbours) {
                if (neighbour.getState() == CellStates.PAINTED
                        &&  neighbour.hasBeenCounted()) {
                    totalCountedCells++;
                    areaCounterLabel.setText("Area: " + totalCountedCells + " units^2");
                    cell.setBeenCounted(true);
                    break;
                }
            }
        }
    }

    public void runAlgorithm() {
        // Start on one to account for the starting point
        totalCountedCells = 1;
        // Get the start point's neighbours
        ArrayList<Cell> startPointNeighbours = new ArrayList<>(){};
        Cell startCell = grid.getStartPoint();
        startPointNeighbours.addAll(grid.getNeighbouringCells(startCell.getGridPosition()));
        // Iterate through them
        for (Cell startPointNeighbour : startPointNeighbours) {
            checkCell(startPointNeighbour);
        }
        // Lastly, check for corner cells
        checkForCorners(grid.getCells());
    }

    public boolean getDelaySteps() {
        return delaySteps;
    }
    public void setDelaySteps(boolean b) {
        this.delaySteps = b;
    }
    public void setLabel(JLabel areaCounterLabel) {
        this.areaCounterLabel = areaCounterLabel;
    }
}
