package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;
import javax.swing.*;
import java.awt.*;

/**
 * Vista per al registre de nous usuaris.
 * Permet crear un compte introduint nom d'usuari, contrasenya
 * i confirmació de la contrasenya.
 */
public class VistaRegister extends VistaBase {
    /* Camps de text per a l'entrada de l'usuari */
    private JTextField usernameField;
    /* Camps de contrasenya */
    private JPasswordField passwordField;
    /* Camps de confirmació de contrasenya */
    private JPasswordField confirmPasswordField;

    /**
     * Constructor de la vista de registre.
     *
     * @param ctrl Controlador de presentació
     */
    public VistaRegister(CtrlPresentation ctrl) {
        super("Registre d'Usuari", ctrl);
        configurarVista();
    }

    /**
     * Configura la vista de registre amb els camps i botons necessaris.
     */
    private void configurarVista(){
        frame.setSize(450, 400);

        JPanel mainPanel = VistaHelpers.createMainPanel();
        JPanel titlePanel = VistaHelpers.createTitlePanel("Crear nou compte");
        JPanel formPanel = VistaHelpers.createFormPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        usernameField = VistaHelpers.createTextField();
        usernameField.addActionListener(e -> passwordField.requestFocus());
        VistaHelpers.addFormField(formPanel, new JLabel("Usuari:"), usernameField, gbc, 0);

        passwordField = VistaHelpers.createPasswordField();
        passwordField.addActionListener(e -> confirmPasswordField.requestFocus());
        JPanel passwordPanel = VistaHelpers.createPasswordPanelWithToggle(passwordField);
        VistaHelpers.addFormField(formPanel, new JLabel("Contrasenya:"), passwordPanel, gbc, 1);

        confirmPasswordField = VistaHelpers.createPasswordField();
        confirmPasswordField.addActionListener(e -> handleRegister());
        JPanel confirmPasswordPanel = VistaHelpers.createPasswordPanelWithToggle(confirmPasswordField);
        VistaHelpers.addFormField(formPanel, new JLabel("Confirmar:"), confirmPasswordPanel, gbc, 2);

        JPanel buttonPanel = VistaHelpers.createButtonPanel();

        JButton registerButton = VistaHelpers.createPrimaryButton("Registrar-se");
        registerButton.addActionListener(e -> handleRegister());
        VistaHelpers.makeButtonRespondToEnter(registerButton);

        JButton cancelButton = VistaHelpers.createCancelButton("Cancel·lar");
        cancelButton.addActionListener(e -> frame.dispose());
        VistaHelpers.makeButtonRespondToEnter(cancelButton);

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        // Setup keyboard navigation
        VistaHelpers.setupKeyboardNavigation(frame, registerButton);
    }

    /**
     * Gestiona l'acció de registre quan l'usuari prem el botó de registrar-se.
     * Verifica les entrades i mostra missatges d'error o èxit segons correspongui.
     */
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                "Si us plau, emplena tots els camps.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!username.chars().allMatch(Character::isLetterOrDigit)) {
            JOptionPane.showMessageDialog(frame,
                    "L'usuari només pot contenir lletres i dígits.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame,
                "Les contrasenyes no coincideixen.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!iCtrlPresentation.registrarAdmin(username.toLowerCase(), password)){
            JOptionPane.showMessageDialog(frame,
                    "L'usuari ja existeix",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(frame,
            "Registre amb èxit per a l'usuari: " + username,
            "Èxit",
            JOptionPane.INFORMATION_MESSAGE);

        frame.dispose();
    }
}




