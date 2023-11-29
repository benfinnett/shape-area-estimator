/**
 * Stores all possible states a {@link Cell} can be in.
 */
enum CellStates {
    /**
     * The default state.
     *
     * @see Cell#defaultColor
     */
    DEFAULT,
    /**
     * Once the cell has been painted.
     *
     * @see Cell#paintedColor
     */
    PAINTED,
    /**
     * When a cell has been flagged as the starting point for the algorithm.
     *
     * @see Cell#startPointColor
     */
    START_POINT,
    /**
     * When the cell is representing the shape area.
     *
     * @see Cell#areaColor
     */
    AREA
}