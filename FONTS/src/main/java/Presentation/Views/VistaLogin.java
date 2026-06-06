package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;

import javax.swing.*;
import java.awt.*;

/**
 * Vista per a l'inici de sessió d'usuaris.
 * Proporciona camps per introduir nom d'usuari i contrasenya,
 * amb opció de mostrar/ocultar la contrasenya.
 */
public class VistaLogin extends VistaBase {
    private JTextField usernameField;
    private JPasswordField passwordField;

    /**
     * Constructor de la vista d'inici de sessió.
     *
     * @param ctrl Controlador de presentació
     */
    public VistaLogin(CtrlPresentation ctrl){
        super("Inici de sessió", ctrl);
        configurarVista();
    }

    /**
     * Configura els components de la vista. Es crida automàticament en el constructor.
     */
    private void configurarVista(){
        frame.setSize(450, 400);

        JPanel mainPanel = VistaHelpers.createMainPanel();
        JPanel titlePanel = VistaHelpers.createTitlePanel("Introduir dades de l'usuari");
        JPanel formPanel = VistaHelpers.createFormPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        usernameField = VistaHelpers.createTextField();
        usernameField.addActionListener(e -> handleLogin());
        VistaHelpers.addFormField(formPanel, new JLabel("Usuari:"), usernameField, gbc, 0);

        passwordField = VistaHelpers.createPasswordField();
        passwordField.addActionListener(e -> handleLogin());

        JPanel passwordPanel = VistaHelpers.createPasswordPanelWithToggle(passwordField);
        VistaHelpers.addFormField(formPanel, new JLabel("Contrasenya:"), passwordPanel, gbc, 1);

        JPanel buttonPanel = VistaHelpers.createButtonPanel();

        JButton loginButton = VistaHelpers.createPrimaryButton("Iniciar sessió");
        loginButton.addActionListener(e -> handleLogin());
        VistaHelpers.makeButtonRespondToEnter(loginButton);

        JButton cancelButton = VistaHelpers.createCancelButton("Cancel·lar");
        cancelButton.addActionListener(e -> frame.dispose());
        VistaHelpers.makeButtonRespondToEnter(cancelButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        // Setup keyboard navigation
        VistaHelpers.setupKeyboardNavigation(frame, loginButton);
    }

    /**
     * Gestiona l'acció d'iniciar sessió.
     * Verifica les credencials i mostra missatges d'error o èxit segons correspongui.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
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

        if (!iCtrlPresentation.iniciarSessio(username.toLowerCase(), password)) {
            JOptionPane.showMessageDialog(frame,
                    "Contrasenya o usuari incorrecte.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(frame,
                "Inici de sessió amb èxit. Benvingut, " + username,
                "Èxit",
                JOptionPane.INFORMATION_MESSAGE);

        frame.dispose();
    }
}


