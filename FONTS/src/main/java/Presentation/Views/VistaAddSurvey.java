package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;

import javax.swing.*;
import java.awt.*;

/**
 * Vista per crear una nova enquesta.
 * Permet introduir el nom de l'enquesta i redirigeix
 * a la vista d'edició després de crear-la.
 */
public class VistaAddSurvey extends VistaBase {
    private final VistaPrincipal vistaPrincipal;

    /**
     * Constructor de la vista de creació d'enquesta.
     *
     * @param ctrl Controlador de presentació
     * @param vistaPrincipal Vista principal per redirigir després de crear
     */
    public VistaAddSurvey(CtrlPresentation ctrl, VistaPrincipal vistaPrincipal) {
        super("Crear enquesta", ctrl);
        this.vistaPrincipal = vistaPrincipal;
        configurarVista();
    }

    /**
     * Configura els components de la vista. Es crida automàticament en el constructor.
     */
    private void configurarVista() {
        JPanel mainPanel = VistaHelpers.createMainPanel();
        mainPanel.add(VistaHelpers.createTitlePanel("Crear Nova Enquesta"), BorderLayout.NORTH);

        JPanel inputPanel = VistaHelpers.createFormPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField surveyNameField = VistaHelpers.createTextField();
        surveyNameField.addActionListener(e -> handleConfirmar(surveyNameField.getText()));
        VistaHelpers.addFormField(inputPanel, new JLabel("Nom de l'enquesta:"), surveyNameField, gbc, 0);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = VistaHelpers.createButtonPanel();

        JButton confirmButton = VistaHelpers.createSuccessButton("Confirmar");
        confirmButton.addActionListener(e -> handleConfirmar(surveyNameField.getText()));
        VistaHelpers.makeButtonRespondToEnter(confirmButton);

        JButton cancelButton = VistaHelpers.createCancelButton("Cancel·lar");
        cancelButton.addActionListener(e -> frame.dispose());
        VistaHelpers.makeButtonRespondToEnter(cancelButton);

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.pack();

        // Setup keyboard navigation
        VistaHelpers.setupKeyboardNavigation(frame, confirmButton);
    }

    /**
     * Gestiona l'acció de confirmar la creació de l'enquesta.
     *
     * @param surveyName Nom de l'enquesta introduït per l'usuari
     */
    private void handleConfirmar(String surveyName) {
        if (surveyName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "El nom de l'enquesta no pot estar buit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = iCtrlPresentation.crearEnquesta(surveyName);

        JOptionPane.showMessageDialog(frame, "Obrint menú d'edició de \"" + surveyName + "\"", "Èxit", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        vistaPrincipal.openEditSurvey(id);
    }
}

