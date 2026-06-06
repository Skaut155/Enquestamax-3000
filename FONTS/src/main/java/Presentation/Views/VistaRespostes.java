package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;
import javax.swing.*;
import java.awt.*;

/**
 * Vista per mostrar i gestionar les respostes d'una enquesta.
 * Permet veure detalls de cada resposta i importar/exportar respostes.
 */
public class VistaRespostes extends VistaBase {
    /* Identificador de l'enquesta */
    private final int idEnquesta;
    /* Nom de l'enquesta */
    private final String nomEnquesta;

    /**
     * Constructor de la vista principal de les respostes.
     *
     * @param ctrl Controlador de presentació
     * @param idEnquesta Identificador de l'enquesta
     * @param nomEnquesta Nom de l'enquesta
     */
    public VistaRespostes(CtrlPresentation ctrl, int idEnquesta, String nomEnquesta) {
        super("Respostes - " + nomEnquesta, ctrl);
        this.idEnquesta = idEnquesta;
        this.nomEnquesta = nomEnquesta;
        configurarVista();
    }

    /**
     * Configura la vista principal de les respostes.
     */
    private void configurarVista() {
        frame.setSize(700, 500);

        JPanel mainPanel = VistaHelpers.createMainPanel();

        // Title panel
        JPanel titlePanel = VistaHelpers.createTitlePanelWithSubtitle("Respostes de: " + nomEnquesta, null);

        // Get response IDs
        int[] idsRespostes = iCtrlPresentation.getIdsRespostes(idEnquesta);

        // Create responses panel
        JPanel responsesPanel = createResponsesPanel(idsRespostes);

        JScrollPane scrollPane = VistaHelpers.createScrollPane(responsesPanel);

        // Button panel
        JPanel buttonPanel = VistaHelpers.createButtonPanel();

        JButton importButton = VistaHelpers.createPrimaryButton("Importar resposta");
        importButton.addActionListener(e -> openImportDialog());
        VistaHelpers.makeButtonRespondToEnter(importButton);

        JButton closeButton = VistaHelpers.createCancelButton("Tancar");
        closeButton.addActionListener(e -> frame.dispose());
        VistaHelpers.makeButtonRespondToEnter(closeButton);

        buttonPanel.add(importButton);
        buttonPanel.add(closeButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);

        // Setup keyboard navigation
        VistaHelpers.setupKeyboardNavigation(frame, closeButton);
    }

    /**
     * Crea el panell amb totes les respostes.
     *
     * @param idsRespostes Array amb els ID de les respostes
     * @return Panell amb les respostes
     */
    private JPanel createResponsesPanel(int[] idsRespostes) {
        JPanel responsesPanel = new JPanel();
        responsesPanel.setLayout(new BoxLayout(responsesPanel, BoxLayout.Y_AXIS));
        responsesPanel.setBackground(Color.WHITE);

        if (idsRespostes.length == 0) {
            JLabel noResponsesLabel = new JLabel("No hi ha respostes per aquesta enquesta.");
            noResponsesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            noResponsesLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            responsesPanel.add(noResponsesLabel);
        } else {
            for (int i = 0; i < idsRespostes.length; i++) {
                responsesPanel.add(createResponsePanel(idsRespostes[i], i + 1));
            }
        }

        return responsesPanel;
    }

    /**
     * Crea un panell que representa una resposta individual.
     *
     * @param idResposta Identificador de la resposta
     * @param numero Número de la resposta (per mostrar al títol)
     * @return JPanel representant la resposta
     */
    private JPanel createResponsePanel(int idResposta, int numero) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, VistaHelpers.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Header with response number
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel numberLabel = new JLabel("Resposta #" + numero);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        numberLabel.setForeground(VistaHelpers.TITLE_COLOR);

        JButton viewDetailsButton = VistaHelpers.createPrimaryButton("+");
        viewDetailsButton.setToolTipText("Veure detalls");
        viewDetailsButton.setPreferredSize(new Dimension(50, 30));
        viewDetailsButton.addActionListener(e -> mostrarDetallsResposta(idResposta, numero));
        VistaHelpers.makeButtonRespondToEnter(viewDetailsButton);

        JButton exportButton = new JButton("⬇");
        exportButton.setToolTipText("Exportar resposta");
        VistaHelpers.styleActionButton(exportButton);
        exportButton.setPreferredSize(new Dimension(40, 30));
        exportButton.addActionListener(e -> openExportDialog(idResposta, numero));
        VistaHelpers.makeButtonRespondToEnter(exportButton);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.add(viewDetailsButton);
        buttonsPanel.add(exportButton);

        headerPanel.add(numberLabel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Mostra un diàleg amb els detalls d'una resposta específica.
     *
     * @param idResposta Identificador de la resposta
     * @param numero Número de la resposta (per mostrar al títol)
     */
    private void mostrarDetallsResposta(int idResposta, int numero) {
        String detalles = iCtrlPresentation.consultarResposta(idEnquesta, idResposta);

        JDialog dialog = new JDialog(frame, "Detalls Resposta #" + numero, true);
        dialog.setSize(600, 400);

        JPanel mainPanel = VistaHelpers.createMainPanel();

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(VistaHelpers.BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Resposta #" + numero);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(VistaHelpers.TITLE_COLOR);
        titlePanel.add(titleLabel);

        // Text area with details
        JTextArea textArea = new JTextArea(detalles);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(VistaHelpers.BORDER_COLOR));

        // Close button
        JPanel buttonPanel = VistaHelpers.createButtonPanel();
        JButton closeButton = VistaHelpers.createCancelButton("Tancar");
        closeButton.addActionListener(e -> dialog.dispose());
        VistaHelpers.makeButtonRespondToEnter(closeButton);
        buttonPanel.add(closeButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setLocationRelativeTo(frame);

        // Setup keyboard navigation for dialog
        VistaHelpers.setupKeyboardNavigation(dialog, closeButton);

        dialog.setVisible(true);
    }

    /**
     * Obre un diàleg per importar una resposta des d'un fitxer.
     * Després d'importar, refresca la vista amb les respostes actualitzades.
     */
    private void openImportDialog() {
        SwingUtilities.invokeLater(() -> {
            VistaFileChooser vistaImport = new VistaFileChooser(
                VistaFileChooser.Mode.IMPORT,
                "Resposta",
                path -> {
                    try {
                        iCtrlPresentation.importarResposta(idEnquesta, path);
                    }
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(frame,
                                "Error en importar l'enquesta:\n" + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(frame,
                        "Resposta importada correctament des de:\n" + path,
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);
                    // Refresh the view
                    frame.dispose();
                    new VistaRespostes(iCtrlPresentation, idEnquesta, nomEnquesta).hacerVisible();
                }
            );
            vistaImport.hacerVisible();
        });
    }

    /**
     * Obre un diàleg per exportar una resposta a un fitxer.
     *
     * @param idResposta Identificador de la resposta a exportar
     * @param numero Número de la resposta (per mostrar als missatges)
     */
    private void openExportDialog(int idResposta, int numero) {
        SwingUtilities.invokeLater(() -> {
            VistaFileChooser vistaExport = new VistaFileChooser(
                VistaFileChooser.Mode.EXPORT,
                "Resposta",
                path -> {
                    try{
                        iCtrlPresentation.exportarResposta(idEnquesta, idResposta, path);
                    }
                    catch (Exception e){
                        JOptionPane.showMessageDialog(frame,
                                "Error en exportar la resposta:\n" + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(frame,
                        "Resposta #" + numero + " exportada correctament a:\n" + path,
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            );
            vistaExport.hacerVisible();
        });
    }
}
