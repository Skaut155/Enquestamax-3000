package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

/**
 * Vista per mostrar un gràfic de punts 2D agrupats
 * amb informació addicional nombre de punts per grup i coeficient de clustering.
 *
 */
public class VistaPlotGroups extends VistaBase {

    private final int idEnquesta;
    private Integer k;
    private final Double threshold;
    private final String algorithmName; 
    private final String distanceName;
    private final String coefficientName;

    private double[][] points;
    private double coefficient;
    private static String[] perfils;

    private JPanel contentPanel;
    private PointPanel pointPanel;
    private GroupInfoPanel infoPanel;

    private static class Point {
        final int index;      // index in points[][] + 1
        final double x;
        final double y;

        Point(int index, double[] p) {
            this.index = index;
            this.x = p[0];
            this.y = p[1];
        }
    }

    public VistaPlotGroups(int idEnquesta, Integer k, Double threshold, String algorithmName, String distanceName, String coefficientName, CtrlPresentation ctrl) {
        super("Visualització de Grups", ctrl);
        this.idEnquesta = idEnquesta;
        this.k = k;
        this.threshold = threshold;
        this.algorithmName = algorithmName;
        this.distanceName = distanceName;
        this.coefficientName = coefficientName;

        initComponents();
    }

    private void initComponents() {
        contentPanel = VistaHelpers.createContentPanel();
        contentPanel.setLayout(new BorderLayout(15, 15));

        //Cridar clustering
        if(k != null) iCtrlPresentation.calcPlot2DGivenK(idEnquesta, algorithmName, k, distanceName, coefficientName);
        else{
            iCtrlPresentation.calcPlot2DOptimalK(idEnquesta, algorithmName, threshold, distanceName, coefficientName);
            k = iCtrlPresentation.getKOptimal();
        }

        points = iCtrlPresentation.getPlotPoints();
        coefficient = iCtrlPresentation.getPlotCoefficient();
        perfils = iCtrlPresentation.getCentroidsPlot2D();

        ArrayList<Point>[] groupedPoints = groupPoints(points, k);

        int[] counts = countGroups(points, k);
        Color[] colors = createColors(k);

        pointPanel = new PointPanel(points, colors);
        infoPanel = new GroupInfoPanel(counts, coefficient, colors, groupedPoints, k, threshold, algorithmName, distanceName, coefficientName);

        contentPanel.add(pointPanel, BorderLayout.CENTER);
        
        JScrollPane scrollInfoPanel = new JScrollPane(infoPanel);
        scrollInfoPanel.setPreferredSize(new Dimension(350, 400));
        scrollInfoPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollInfoPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.add(scrollInfoPanel, BorderLayout.EAST);


        frame.add(contentPanel, BorderLayout.CENTER);
        frame.pack();
    }

    // Utils
    private static ArrayList<Point>[] groupPoints(double[][] points, int numGroups) {
        @SuppressWarnings("unchecked")
        ArrayList<Point>[] grouped = new ArrayList[numGroups];

        for (int i = 0; i < numGroups; i++) {
            grouped[i] = new java.util.ArrayList<>();
        }

        for (int i = 0; i < points.length; i++) {
            int g = (int) points[i][2];
            if (g >= 0 && g < numGroups) {
                grouped[g].add(new Point(i+1, points[i]));
            }
        }
        return grouped;
    }


    private static int[] countGroups(double[][] points, int numGroups) {
        int[] counts = new int[numGroups];
        for (double[] p : points) {
            int g = (int) p[2];
            if (g >= 0 && g < numGroups) {
                counts[g]++;
            }
        }
        return counts;
    }

    private static Color[] createColors(int numGroups) {
        Color[] palette = {
            new Color(220, 53, 69),   // red
            new Color(13, 110, 253),  // blue
            new Color(25, 135, 84),   // green
            new Color(255, 193, 7),   // yellow
            new Color(111, 66, 193),  // purple
            new Color(255, 87, 51),   // orange
            new Color(0, 204, 204),   // cyan
            new Color(153, 102, 255), // violet
            new Color(255, 102, 204), // pink
            new Color(102, 255, 102), // light green
            new Color(255, 204, 0),   // gold
            new Color(0, 153, 153),   // teal
            new Color(204, 0, 102),   // magenta
            new Color(102, 51, 0),    // brown
            new Color(255, 153, 153), // salmon
            new Color(153, 255, 204), // mint
            new Color(204, 204, 255), // lavender
            new Color(102, 0, 204),   // deep purple
            new Color(255, 102, 0),   // bright orange
            new Color(0, 102, 51)     // dark green
        };

        Color[] colors = new Color[numGroups];
        for (int i = 0; i < numGroups; i++) {
            colors[i] = palette[i % palette.length];
        }
        return colors;
    }

    // Panell de punts

    private static class PointPanel extends JPanel {
        private final double[][] points;
        private final Color[] colors;
        private static final int MARGIN = 40;

        public PointPanel(double[][] points, Color[] colors) {
            this.points = points;
            this.colors = colors;
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(400, 400));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            int plotW = w - 2 * MARGIN;
            int plotH = h - 2 * MARGIN;

            // Draw axes
            g2.setColor(Color.BLACK);

            // X axis
            g2.drawLine(MARGIN, h - MARGIN, w - MARGIN, h - MARGIN);
            // Y axis
            g2.drawLine(MARGIN, MARGIN, MARGIN, h - MARGIN);

            g2.drawString("X", w - MARGIN + 10, h - MARGIN + 5);
            g2.drawString("Y", MARGIN - 10, MARGIN - 10);

            // Draw ticks (0.0 to 1.0)
            int ticks = 5;
            for (int i = 0; i <= ticks; i++) {
                double v = i / (double) ticks;

                int x = MARGIN + (int) (v * plotW);
                int y = h - MARGIN - (int) (v * plotH);

                // X ticks
                g2.drawLine(x, h - MARGIN - 4, x, h - MARGIN + 4);
                g2.drawString(String.format("%.1f", v), x - 10, h - MARGIN + 18);

                // Y ticks
                g2.drawLine(MARGIN - 4, y, MARGIN + 4, y);
                g2.drawString(String.format("%.1f", v), 5, y + 4);
            }

            // Draw points
            int radius = 4;
            for (double[] p : points) {
                int group = (int) p[2];
                if (group < 0 || group >= colors.length) continue;

                g2.setColor(colors[group]);

                int x = MARGIN + (int) (p[0] * plotW);
                int y = h - MARGIN - (int) (p[1] * plotH);

                g2.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            }
        }

    }

    //Panell d'informació

    private static class GroupInfoPanel extends JPanel {

        public GroupInfoPanel(int[] counts, double coefficient, Color[] colors, ArrayList<Point>[] groupedPoints,
            Integer k, Double threshold, String algorithmName, String distanceName, String coefficientName) {

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(VistaHelpers.BACKGROUND_COLOR);
            setBorder(BorderFactory.createTitledBorder("Llegenda"));

            // Info algorithm panel
            JPanel dataPanel = new JPanel();
            dataPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
            dataPanel.setOpaque(false);

            dataPanel.add(new JLabel("Algorisme: " + algorithmName ));
            dataPanel.add(Box.createVerticalStrut(4));
            dataPanel.add(new JLabel("Distància: " + distanceName));
            dataPanel.add(Box.createVerticalStrut(4));
            dataPanel.add(new JLabel("Índex: " + coefficientName));
            dataPanel.add(Box.createVerticalStrut(4));
            dataPanel.add(new JLabel("k: " + k));
            dataPanel.add(Box.createVerticalStrut(4));

            if (threshold != 0.0) {
                dataPanel.add(new JLabel(String.format("Threshold: %.4f", threshold)));
                dataPanel.add(Box.createVerticalStrut(4));
            }

            dataPanel.add(new JLabel(String.format("Valor del índex: %.4f", coefficient)));
            dataPanel.add(Box.createVerticalStrut(4));

            add(dataPanel);
            add(Box.createVerticalStrut(12));


            // Add group information
            for (int g = 0; g < counts.length; g++) {
                final int groupIndex = g;
                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
                row.setOpaque(false);

                JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
                header.setOpaque(false);

                JLabel colorBox = new JLabel("  ");
                colorBox.setOpaque(true);
                colorBox.setBackground(colors[groupIndex]);
                colorBox.setPreferredSize(new Dimension(14, 14));

                JLabel label;
                if (counts[groupIndex] == 1) label = new JLabel(String.format("%d punt",counts[groupIndex]));
                else label = new JLabel(String.format("%d punts", counts[groupIndex]));
                header.add(colorBox);
                header.add(Box.createHorizontalStrut(5));
                header.add(label);
                row.add(header);

                // Point list
                int numRows = Math.min(groupedPoints[g].size() + 1, 15); // +1 for extra space or profile
                JTextArea pointList = new JTextArea(numRows, 18);
                pointList.setEditable(false);

                StringBuilder sb = new StringBuilder();
                for (Point p : groupedPoints[g]) {
                    sb.append(String.format("Resposta %d: (%.3f, %.3f)%n", p.index, p.x, p.y));
                }
                pointList.setText(sb.toString());

                JScrollPane scroll = new JScrollPane(pointList);
                int height = 20 * numRows; // 20 pixels per row
                scroll.setPreferredSize(new Dimension(220, height));

                row.add(scroll);
                row.add(Box.createVerticalStrut(10));

                JButton perfilButton = new JButton("Veure perfil del grup");
                perfilButton.setAlignmentX(Component.LEFT_ALIGNMENT);

                perfilButton.addActionListener(e -> {
                    JTextArea perfilText = new JTextArea(perfils[groupIndex]);
                    perfilText.setEditable(false);
                    perfilText.setLineWrap(true);
                    perfilText.setWrapStyleWord(true);

                    JScrollPane perfilScroll = new JScrollPane(perfilText);
                    perfilScroll.setPreferredSize(new Dimension(350, 200));

                    JOptionPane.showMessageDialog(
                        this,
                        perfilScroll,
                        "Perfil del grup " + (groupIndex + 1),
                        JOptionPane.INFORMATION_MESSAGE
                    );
                });

                row.add(Box.createVerticalStrut(5));
                row.add(perfilButton);

                add(row);
            }
        }
    }
}