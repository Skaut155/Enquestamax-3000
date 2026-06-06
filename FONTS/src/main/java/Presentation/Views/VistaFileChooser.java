package Presentation.Views;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

/**
 * Vista reutilizable para seleccionar archivos (importación/exportación).
 * Puede configurarse para modo de apertura (importar) o guardado (exportar).
 */
public class VistaFileChooser extends VistaBase {

    /**
     * Mode d'operació de la vista.
     */
    public enum Mode {
        IMPORT,
        EXPORT
    }

    private final Mode mode;
    private final Consumer<String> onAccept;
    private JTextField pathField;
    private String selectedPath = "";

    /**
     * Constructor de la vista de selecció de fitxers.
     *
     * @param mode Mode d'operació (IMPORT o EXPORT)
     * @param entityName Nom de l'entitat (ex: "Enquesta", "Resposta") per personalitzar títols
     * @param onAccept Callback que s'executarà en acceptar amb el path seleccionat
     */
    public VistaFileChooser(Mode mode, String entityName, Consumer<String> onAccept) {
        super(mode == Mode.IMPORT ? "Importar " + entityName : "Exportar " + entityName);
        this.mode = mode;
        this.onAccept = onAccept;
        configurarVista(entityName);
    }

    /**
     * Configura els components de la vista segons el mode i el nom de l'enquesta.
     *
     * @param entityName Nom de l'enquesta
     */
    private void configurarVista(String entityName) {
        frame.setSize(500, 250);
        frame.setResizable(false);

        JPanel mainPanel = VistaHelpers.createMainPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel titlePanel = VistaHelpers.createTitlePanel(getModeText(entityName));
        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Obté el text segons el mode d'operació.
     *
     * @param entityName Nom de l'entitat
     * @return Text per al mode actual
     */
    private String getModeText(String entityName) {
        return mode == Mode.IMPORT ? "Importar " + entityName : "Exportar " + entityName;
    }

    /**
     * Crea el panell de formulari amb la selecció de ruta.
     *
     * @return Panel del formulari
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(VistaHelpers.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel pathLabel = new JLabel(getPathLabelText());
        pathLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel pathPanel = createPathPanel();

        formPanel.add(pathLabel, BorderLayout.NORTH);
        formPanel.add(pathPanel, BorderLayout.CENTER);

        return formPanel;
    }

    /**
     * Obté el text de l'etiqueta de ruta segons el mode.
     *
     * @return Text de l'etiqueta
     */
    private String getPathLabelText() {
        return mode == Mode.IMPORT ? "Fitxer a importar:" : "Desar com:";
    }

    /**
     * Crea el panell amb el camp de ruta i botó d'examinar.
     *
     * @return Panel de ruta
     */
    private JPanel createPathPanel() {
        JPanel pathPanel = new JPanel(new BorderLayout(5, 0));
        pathPanel.setBackground(VistaHelpers.BACKGROUND_COLOR);

        pathField = VistaHelpers.createTextField();
        pathField.setEditable(false);
        pathField.setBackground(Color.WHITE);

        JButton browseButton = VistaHelpers.createCancelButton("Examinar...");
        browseButton.setPreferredSize(new Dimension(125, 30));
        browseButton.addActionListener(e -> openFileChooser());

        pathPanel.add(pathField, BorderLayout.CENTER);
        pathPanel.add(browseButton, BorderLayout.EAST);

        return pathPanel;
    }

    /**
     * Crea el panell de botons d'acceptar i cancel·lar.
     *
     * @return Panel de botons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = VistaHelpers.createButtonPanel();

        JButton acceptButton = VistaHelpers.createPrimaryButton(getAcceptButtonText());
        acceptButton.addActionListener(e -> handleAccept());
        VistaHelpers.makeButtonRespondToEnter(acceptButton);

        JButton cancelButton = VistaHelpers.createCancelButton("Cancel·lar");
        cancelButton.addActionListener(e -> frame.dispose());
        VistaHelpers.makeButtonRespondToEnter(cancelButton);

        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);

        // Setup keyboard navigation
        VistaHelpers.setupKeyboardNavigation(frame, acceptButton);

        return buttonPanel;
    }

    /**
     * Obté el text del botó d'acceptar segons el mode.
     *
     * @return Text del botó
     */
    private String getAcceptButtonText() {
        return mode == Mode.IMPORT ? "Importar" : "Exportar";
    }

    /**
     * Obre el diàleg de selecció de fitxers segons el mode actual.
     */
    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fitxers JSON (*.json)", "json"));

        int result;
        if (mode == Mode.IMPORT) {
            fileChooser.setDialogTitle("Seleccionar fitxer a importar");
            result = fileChooser.showOpenDialog(frame);
        } else {
            fileChooser.setDialogTitle("Seleccionar ubicació per desar");
            result = fileChooser.showSaveDialog(frame);
        }

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedPath = selectedFile.getAbsolutePath();

            // Add .json extension if exporting and not already present
            if (mode == Mode.EXPORT && !selectedPath.toLowerCase().endsWith(".json")) {
                selectedPath += ".json";
            }

            pathField.setText(selectedPath);
        }
    }

    /**
     * Gestiona l'acció d'acceptar la selecció de fitxer.
     * Valida la selecció i crida el callback si és vàlida.
     */
    private void handleAccept() {
        if (selectedPath.isEmpty()) {
            showError(getEmptyPathErrorMessage());
            return;
        }

        if (mode == Mode.IMPORT && !validateFileExists()) {
            showError("El fitxer seleccionat no existeix.");
            return;
        }

        // Call the callback and close
        if (onAccept != null) {
            onAccept.accept(selectedPath);
        }
        frame.dispose();
    }

    /**
     * Obté el missatge d'error quan no s'ha seleccionat cap ruta.
     *
     * @return Missatge d'error
     */
    private String getEmptyPathErrorMessage() {
        return mode == Mode.IMPORT
            ? "Si us plau, selecciona un fitxer per importar."
            : "Si us plau, selecciona una ubicació per desar.";
    }

    /**
     * Valida que el fitxer existeix (només per mode IMPORT).
     *
     * @return true si el fitxer existeix, false altrament
     */
    private boolean validateFileExists() {
        File file = new File(selectedPath);
        return file.exists();
    }

    /**
     * Mostra un missatge d'error a l'usuari.
     *
     * @param message Missatge a mostrar
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

