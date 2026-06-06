package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;
import javax.swing.*;
import java.awt.*;

/**
 * Vista per configurar i executar l'anàlisi de clustering d'una enquesta.
 * Permet seleccionar l'algoritme, el nombre de clusters (k fix o òptim)
 * i altres paràmetres necessaris per a l'anàlisi.
 */
public class VistaAnalitzar extends VistaBase {
    private final int idEnquesta;
    private final String nomEnquesta;

    private JComboBox<String> algorithmComboBox;
    private JComboBox<String> distanceComboBox;
    private JComboBox<String> coefficientComboBox;
    private JRadioButton fixedKRadioButton;
    private JRadioButton optimalKRadioButton;
    private JSpinner kSpinner;
    private JSpinner thresholdSpinner;
    private JPanel fixedKPanel;
    private JPanel optimalKPanel;

    /**
     * Constructor de la vista d'anàlisi.
     *
     * @param ctrl Controlador de presentació
     * @param idEnquesta Identificador de l'enquesta a analitzar
     * @param nomEnquesta Nom de l'enquesta
     */
    public VistaAnalitzar(CtrlPresentation ctrl, int idEnquesta, String nomEnquesta) {
        super("Analitzar Enquesta", ctrl);
        this.idEnquesta = idEnquesta;
        this.nomEnquesta = nomEnquesta;
        configurarVista();
    }

    /**
     * Configura els components de la vista. Es crida automàticament des del constructor.
     */
    private void configurarVista() {
        frame.setSize(500, 600);

        JPanel mainPanel = VistaHelpers.createMainPanel();
        JPanel titlePanel = VistaHelpers.createTitlePanel("Anàlisi de Clustering: " + nomEnquesta);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(VistaHelpers.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Algorithm selection
        JPanel algorithmPanel = createAlgorithmPanel();
        formPanel.add(algorithmPanel);
        formPanel.add(Box.createVerticalStrut(15));

        // Distance selection
        JPanel distancePanel = createDistancePanel();
        formPanel.add(distancePanel);
        formPanel.add(Box.createVerticalStrut(15));

        // Coefficient selection
        JPanel coefficientPanel = createCoefficientPanel();
        formPanel.add(coefficientPanel);
        formPanel.add(Box.createVerticalStrut(15));

        // Mode selection with integrated inputs
        JPanel modePanel = createModePanel();
        formPanel.add(modePanel);
        formPanel.add(Box.createVerticalStrut(15));


        // Button panel
        JPanel buttonPanel = VistaHelpers.createButtonPanel();

        JButton analyzeButton = VistaHelpers.createPrimaryButton("Analitzar");
        analyzeButton.addActionListener(e -> handleAnalyze());
        VistaHelpers.makeButtonRespondToEnter(analyzeButton);

        JButton cancelButton = VistaHelpers.createCancelButton("Cancel·lar");
        cancelButton.addActionListener(e -> frame.dispose());
        VistaHelpers.makeButtonRespondToEnter(cancelButton);

        buttonPanel.add(analyzeButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        // Setup keyboard navigation
        VistaHelpers.setupKeyboardNavigation(frame, analyzeButton);
    }

    /**
     * Crea el panell de selecció d'algoritme.
     *
     * @return Panell amb el combo box d'algoritmes
     */
    private JPanel createAlgorithmPanel() {
        JPanel algorithmPanel = new JPanel(new BorderLayout(10, 5));
        algorithmPanel.setBackground(VistaHelpers.BACKGROUND_COLOR);
        algorithmPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel algorithmLabel = new JLabel("Algoritme de Clustering:");
        algorithmLabel.setFont(new Font("Arial", Font.BOLD, 12));

        String[] algorithms = {"K-means Random", "K-means++", "K-medoids"};
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setFont(new Font("Arial", Font.PLAIN, 12));

        algorithmPanel.add(algorithmLabel, BorderLayout.NORTH);
        algorithmPanel.add(algorithmComboBox, BorderLayout.CENTER);

        return algorithmPanel;
    }

    /**
     * Crea el panell de selecció de mètode de distància.
     *
     * @return Panell amb el combo box de distàncies
     */
    private JPanel createDistancePanel() {
        JPanel distancePanel = new JPanel(new BorderLayout(10, 5));
        distancePanel.setBackground(VistaHelpers.BACKGROUND_COLOR);
        distancePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel distanceLabel = new JLabel("Mètode de càlcul de distància:");
        distanceLabel.setFont(new Font("Arial", Font.BOLD, 12));

        String[] distances = {"Manhattan", "Euclidiana"};
        distanceComboBox = new JComboBox<>(distances);
        distanceComboBox.setFont(new Font("Arial", Font.PLAIN, 12));

        distancePanel.add(distanceLabel, BorderLayout.NORTH);
        distancePanel.add(distanceComboBox, BorderLayout.CENTER);

        return distancePanel;
    }

    /**
     * Crea el panell de selecció d'índex de clustering.
     *
     * @return Panell amb el combo box d'índexs
     */
    private JPanel createCoefficientPanel() {
        JPanel coefficientPanel = new JPanel(new BorderLayout(10, 5));
        coefficientPanel.setBackground(VistaHelpers.BACKGROUND_COLOR);
        coefficientPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel coefficientLabel = new JLabel("Índex de qualitat del clustering:");
        coefficientLabel.setFont(new Font("Arial", Font.BOLD, 12));

        String[] coefficients = {"Silhouette", "Calinski-Harabasz", "Dunn"};
        coefficientComboBox = new JComboBox<>(coefficients);
        coefficientComboBox.setFont(new Font("Arial", Font.PLAIN, 12));

        coefficientPanel.add(coefficientLabel, BorderLayout.NORTH);
        coefficientPanel.add(coefficientComboBox, BorderLayout.CENTER);

        return coefficientPanel;
    }

    /**
     * Crea el panell de selecció de mode (k fix o òptim).
     *
     * @return Panell amb els radio buttons de mode
     */
    private JPanel createModePanel() {
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.Y_AXIS));
        modePanel.setBackground(VistaHelpers.BACKGROUND_COLOR);
        modePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(VistaHelpers.PRIMARY_COLOR, 1),
            "Mètode de Clustering",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            VistaHelpers.PRIMARY_COLOR
        ));

        ButtonGroup modeGroup = new ButtonGroup();
        fixedKRadioButton = new JRadioButton("Nombre fix de clústers (k):");
        optimalKRadioButton = new JRadioButton("Calcular k òptim (threshold):");
        fixedKRadioButton.setSelected(true);
        fixedKRadioButton.setBackground(VistaHelpers.BACKGROUND_COLOR);
        optimalKRadioButton.setBackground(VistaHelpers.BACKGROUND_COLOR);

        modeGroup.add(fixedKRadioButton);
        modeGroup.add(optimalKRadioButton);

        // Fixed K row
        JPanel fixedKRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        fixedKRow.setBackground(VistaHelpers.BACKGROUND_COLOR);
        fixedKRow.add(fixedKRadioButton);

        fixedKPanel = createFixedKPanel();
        fixedKRow.add(fixedKPanel);

        // Optimal K row
        JPanel optimalKRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        optimalKRow.setBackground(VistaHelpers.BACKGROUND_COLOR);
        optimalKRow.add(optimalKRadioButton);

        optimalKPanel = createOptimalKPanel();
        optimalKRow.add(optimalKPanel);

        modePanel.add(fixedKRow);
        modePanel.add(Box.createVerticalStrut(5));
        modePanel.add(optimalKRow);

        // Radio button listeners to toggle panels
        fixedKRadioButton.addActionListener(e -> {
            fixedKPanel.setEnabled(true);
            kSpinner.setEnabled(true);
            optimalKPanel.setEnabled(false);
            thresholdSpinner.setEnabled(false);
        });

        optimalKRadioButton.addActionListener(e -> {
            fixedKPanel.setEnabled(false);
            kSpinner.setEnabled(false);
            optimalKPanel.setEnabled(true);
            thresholdSpinner.setEnabled(true);
        });

        // Initial state
        optimalKPanel.setEnabled(false);
        thresholdSpinner.setEnabled(false);

        return modePanel;
    }

    /**
     * Crea el panell per a k fix.
     *
     * @return Panell amb el spinner de k
     */
    private JPanel createFixedKPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(VistaHelpers.BACKGROUND_COLOR);

        SpinnerNumberModel kModel = new SpinnerNumberModel(3, 1, 999, 1);
        kSpinner = new JSpinner(kModel);
        kSpinner.setFont(new Font("Arial", Font.PLAIN, 12));
        kSpinner.setPreferredSize(new Dimension(60, 25));

        panel.add(kSpinner);

        return panel;
    }

    /**
     * Crea el panell per a k òptim.
     *
     * @return Panell amb el spinner de threshold
     */
    private JPanel createOptimalKPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(VistaHelpers.BACKGROUND_COLOR);

        SpinnerNumberModel thresholdModel = new SpinnerNumberModel(0.85, 0.01, 0.99, 0.05);
        thresholdSpinner = new JSpinner(thresholdModel);
        thresholdSpinner.setFont(new Font("Arial", Font.PLAIN, 12));
        thresholdSpinner.setPreferredSize(new Dimension(60, 25));

        panel.add(thresholdSpinner);

        return panel;
    }

    private void handleAnalyze() {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        String distance = (String) distanceComboBox.getSelectedItem();
        String coefficient = (String) coefficientComboBox.getSelectedItem();

        // Convert names to format expected by domain
        assert algorithm != null;
        String algorithmCode = convertAlgorithmName(algorithm);
        String distanceCode = convertDistanceName(distance);
        String coefficientCode = convertCoefficientName(coefficient);

        try {
            if (fixedKRadioButton.isSelected()) {
                int k = (Integer) kSpinner.getValue();

                // Mostrar resultados en VistaPlotGroups
                VistaPlotGroups vistaPlot = new VistaPlotGroups(
                    idEnquesta, k, 0.0, algorithmCode, distanceCode, coefficientCode, iCtrlPresentation
                );
                vistaPlot.hacerVisible();

            } else {
                double threshold = (Double) thresholdSpinner.getValue();

                // Mostrar resultados en VistaPlotGroups (k = null para óptimo)
                VistaPlotGroups vistaPlot = new VistaPlotGroups(
                    idEnquesta, null, threshold, algorithmCode, distanceCode, coefficientCode, iCtrlPresentation
                );
                vistaPlot.hacerVisible();
            }

            frame.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                "Error en realitzar l'anàlisi: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Converts the user-friendly algorithm name to the format expected by the domain layer
     */
    private String convertAlgorithmName(String displayName) {
        return switch (displayName) {
            case "K-means++" -> "K_MEANS_PLUS_PLUS";
            case "K-medoids" -> "K_MEDOIDS";
            default -> "K_MEANS_RANDOM";
        };
    }

    /**
     * Converts the user-friendly distance name to the format expected by the domain layer
     */
    private String convertDistanceName(String displayName) {
        return switch (displayName) {
            case "Euclidiana" -> "DISTANCIA_EUCLIDEANA";
            default -> "DISTANCIA_MANHATTAN";
        };
    }

    /**
     * Converts the user-friendly coefficient name to the format expected by the domain layer
     */
    private String convertCoefficientName(String displayName) {
        return switch (displayName) {
            case "Calinski-Harabasz" -> "COEFICIENT_DE_CALINSKI";
            case "Dunn" -> "COEFICIENT_DE_DUNN";
            default -> "COEFICIENT_DE_SILHOUETE";
        };
    }
}

