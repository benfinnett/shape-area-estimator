import java.awt.*;
import javax.swing.*;

/**
 * Initially based on a grid design from StackOverflow.
 * <p>
 * Source: <a href="https://stackoverflow.com/a/15422801/6739015">View Swing graphics source.</a>
 */
public class Main {
    /**
     * Entry point to program. Sets up {@link JFrame} window and adds a {@link Grid} object
     * as well as all necessary buttons.
     *
     * @param args Command line args, should be ignored.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
            }

            // Construct Grid and Algorithm objects
            JPanel gridPanel = new JPanel(new BorderLayout());
            final int[] gridSizeValue = {32};
            Grid grid = new Grid(gridSizeValue[0]);
            gridPanel.add(grid.getPanel());
            AreaEstimator algorithm = new AreaEstimator(grid);

            // Setup JFrame window
            JFrame window = new JFrame("Main");
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.X_AXIS));

            // UI elements
            JPanel UIPanel = new JPanel();
            UIPanel.setLayout(new BoxLayout(UIPanel, BoxLayout.Y_AXIS));
            // Cell counter
            JLabel areaCounterLabel = new JLabel();
            areaCounterLabel.setText("Area: 0 units^2");
            algorithm.setLabel(areaCounterLabel);

            // Run button
            JButton runButton = new JButton("Run algorithm");
            runButton.addActionListener(e -> {
                if (grid.getStartPoint() == null) {
                    JOptionPane.showMessageDialog(window, "You must set a start location by right-clicking on an appropriate cell!");
                } else {
                    runButton.setEnabled(false);
                    algorithm.runAlgorithm();
                }
            });
            // Delay
            JButton toggleDelayButton = new JButton("Enable animation delay");
            toggleDelayButton.addActionListener(e -> {
                if (algorithm.getDelaySteps()) {
                    toggleDelayButton.setText("Enable animation delay");
                    algorithm.setDelaySteps(false);
                    if (runButton.isEnabled()) {
                        areaCounterLabel.setVisible(true);
                    }
                } else {
                    toggleDelayButton.setText("Disable animation delay");
                    algorithm.setDelaySteps(true);
                    areaCounterLabel.setVisible(false);
                }
                window.pack();
            });
            // Reset button
            JButton resetButton = new JButton("Reset grid");
            resetButton.addActionListener(e -> {
                gridPanel.remove(grid.getPanel());

                grid.buildGrid(gridSizeValue[0]);
                grid.setStartPoint(null);
                areaCounterLabel.setText("Area: 0 units^2");
                runButton.setEnabled(true);
                if (!algorithm.getDelaySteps()) {
                    areaCounterLabel.setVisible(true);
                }

                gridPanel.add(grid.getPanel());
                gridPanel.revalidate();
                gridPanel.repaint();
                window.pack();
            });
            // Grid size
            JButton gridSize = new JButton("Change to grid size: " + gridSizeValue[0]/2);
            gridSize.addActionListener(e -> {
                gridPanel.remove(grid.getPanel());
                gridSizeValue[0] = gridSizeValue[0]/2;
                if (gridSizeValue[0] < 8) {
                    gridSizeValue[0] = 128;
                }
                if (gridSizeValue[0]/2 < 8) {
                    gridSize.setText("Change to grid size: 128");
                } else {
                    gridSize.setText("Change to grid size: " + gridSizeValue[0]/2);
                }

                grid.buildGrid(gridSizeValue[0]);
                grid.setStartPoint(null);
                areaCounterLabel.setText("Area: 0 units^2");
                runButton.setEnabled(true);
                if (!algorithm.getDelaySteps()) {
                    areaCounterLabel.setVisible(true);
                }

                gridPanel.add(grid.getPanel());
                gridPanel.revalidate();
                gridPanel.repaint();
                window.pack();
            });

            // Add all UI elements to the UI JPanel
            UIPanel.add(areaCounterLabel);
            UIPanel.add(Box.createHorizontalStrut(25));
            UIPanel.add(runButton);
            UIPanel.add(toggleDelayButton);
            UIPanel.add(resetButton);
            UIPanel.add(gridSize);

            // Add the grid to the window
            window.add(UIPanel);
            window.add(gridPanel);

            // Finalise window settings and display
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}