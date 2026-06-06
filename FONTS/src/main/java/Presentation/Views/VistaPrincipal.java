package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Vista principal de l'aplicació.
 * Permet als usuaris registrar-se, iniciar sessió,
 * i gestionar enquestes un cop autenticats.
 */
public class VistaPrincipal {
    /* Controlador de presentació */
    private final CtrlPresentation iCtrlPresentacion;
    /* Finestra principal de la vista */
    private final JFrame frameVista = new JFrame("Enquestamax 3000");

    /* Botó de registre */
    private final JButton buttonRegister = new JButton("Registrar-se");
    /* Botó d'inici de sessió */
    private final JButton buttonLogin = new JButton("Iniciar sessió");
    /* Botó del menú d'usuari */
    private final JButton buttonUserMenu = new JButton("≡");
    /* Botó per afegir enquesta */
    private final JButton buttonAddSurvey = new JButton("Crear enquesta");
    /* Botó per importar enquestes */
    private final JButton buttonImportSurvey = new JButton("Importar enquesta");
    /* Label per al títol del panel d'informació */
    private final JLabel labelPanelInformacion1 = new JLabel("Llistat d'enquestes:");
    /* Label per mostrar l'usuari actual */
    private final JLabel labelUsuario = new JLabel("");

    /* Vista de registre d'usuari */
    private VistaRegister vistaRegister = null;
    /* Vista d'inici de sessió */
    private VistaLogin vistaLogin = null;
    /* Vista per afegir enquestes */
    private VistaAddSurvey vistaAddSurvey = null;
    /* Vista d'edició d'enquesta */
    private VistaEditarEnquesta vistaEditarEnquesta = null;

    /* Panel principal de la vista */
    private JPanel mainPanel;
    /* Panel de botons d'inici de sessió i registre */
    private JPanel buttonPanel;
    /* Panel d'informació amb la llista d'enquestes */
    private JPanel infoPanel;
    /* Panel per al botó d'afegir enquesta */
    private JPanel addSurveyPanel;

    /**
     * Constructor de la vista principal.
     * @param ctrl Controlador de presentació.
     */
    public VistaPrincipal(CtrlPresentation ctrl) {
        iCtrlPresentacion = ctrl;
        configurarVista();
        inicialitzarComponents();
    }

    /**
     * Fa visible la vista principal.
     */
    public void hacerVisible(){
        frameVista.setVisible(true);
    }

    /**
     * Obre la vista d'edició d'enquestes per a una enquesta específica.
     * Només permet una finestra d'edició oberta a la vegada.
     * @param id ID de l'enquesta a editar.
     */
    public void openEditSurvey(int id) {
        if (vistaEditarEnquesta != null && vistaEditarEnquesta.isVisible()) {
            vistaEditarEnquesta.toFront();
            vistaEditarEnquesta.requestFocus();
            JOptionPane.showMessageDialog(frameVista,
                "Ja hi ha una enquesta oberta en mode edició. Si us plau, tanca-la primer.",
                "Avís",
                JOptionPane.WARNING_MESSAGE);
        } else {
            vistaEditarEnquesta = new VistaEditarEnquesta(iCtrlPresentacion, this, id);
            vistaEditarEnquesta.hacerVisible();
        }
    }

    /**
     * Configura la vista principal.
     */
    private void configurarVista() {
        frameVista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameVista.setSize(500, 400);

        // Main panel with padding
        mainPanel = VistaHelpers.createMainPanel();

        // Current user panel
        JPanel currentUserPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        currentUserPanel.setBackground(VistaHelpers.BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(VistaHelpers.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Enquestamax 3000");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(VistaHelpers.TITLE_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(currentUserPanel, BorderLayout.EAST);

        // Usuario label
        labelUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        labelUsuario.setForeground(VistaHelpers.TEXT_COLOR);
        currentUserPanel.add(labelUsuario);

        // Style user menu button (three dots)
        buttonUserMenu.setFont(new Font("Arial", Font.BOLD, 15));
        buttonUserMenu.setBackground(VistaHelpers.BACKGROUND_COLOR);
        buttonUserMenu.setForeground(VistaHelpers.TEXT_COLOR);
        buttonUserMenu.setMargin(new Insets(2, 8, 2, 8));
        buttonUserMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonUserMenu.setFocusPainted(false);
        buttonUserMenu.setBorderPainted(false);
        buttonUserMenu.setContentAreaFilled(false);
        currentUserPanel.add(buttonUserMenu);

        // Button panel with better layout
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 15));
        buttonPanel.setBackground(VistaHelpers.BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.add(buttonRegister);
        buttonPanel.add(buttonLogin);

        // Style buttons
        VistaHelpers.styleButton(buttonRegister, VistaHelpers.PRIMARY_COLOR);
        VistaHelpers.styleButton(buttonLogin, VistaHelpers.PRIMARY_COLOR);
        VistaHelpers.styleButton(buttonAddSurvey, VistaHelpers.SUCCESS_COLOR);
        VistaHelpers.styleButton(buttonImportSurvey, VistaHelpers.PRIMARY_COLOR);

        // Make buttons respond to Enter key
        VistaHelpers.makeButtonRespondToEnter(buttonRegister);
        VistaHelpers.makeButtonRespondToEnter(buttonLogin);
        VistaHelpers.makeButtonRespondToEnter(buttonAddSurvey);
        VistaHelpers.makeButtonRespondToEnter(buttonImportSurvey);
        VistaHelpers.makeButtonRespondToEnter(buttonUserMenu);

        // Panel for add survey button (when logged in)
        addSurveyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addSurveyPanel.setBackground(VistaHelpers.BACKGROUND_COLOR);
        addSurveyPanel.add(buttonAddSurvey);
        addSurveyPanel.add(buttonImportSurvey);


        // Information panel
        infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VistaHelpers.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        labelPanelInformacion1.setFont(new Font("Arial", Font.BOLD, 14));
        labelPanelInformacion1.setForeground(VistaHelpers.TEXT_COLOR);
        infoPanel.add(labelPanelInformacion1, BorderLayout.NORTH);

        // Add a placeholder for survey list
        JPanel surveyListContainer = new JPanel(new BorderLayout());
        surveyListContainer.setBackground(Color.WHITE);
        infoPanel.add(surveyListContainer, BorderLayout.CENTER);


        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(addSurveyPanel, BorderLayout.SOUTH); // Add it but keep it invisible initially

        // Initially show login/register buttons
        actualitzarBotons(false);

        frameVista.add(mainPanel);
        frameVista.setLocationRelativeTo(null);

        // Setup keyboard navigation
        setupKeyboardNavigation();
    }

    /**
     * Configura la navegació per teclat de la vista principal.
     */
    private void setupKeyboardNavigation() {
        // Configure Tab traversal
        java.util.Set<AWTKeyStroke> forwardKeys = new java.util.HashSet<>(frameVista.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
        frameVista.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

        java.util.Set<AWTKeyStroke> backwardKeys = new java.util.HashSet<>(frameVista.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        backwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        frameVista.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
    }

    /**
     * Actualitza la llista d'enquestes mostrada a la vista principal.
     */
    private void actualizarListaEncuestas() {
        JPanel surveyListPanel = new JPanel();
        surveyListPanel.setLayout(new BoxLayout(surveyListPanel, BoxLayout.Y_AXIS));
        surveyListPanel.setBackground(Color.WHITE);

        java.util.Map<Integer, String> enquestes = iCtrlPresentacion.getEnquestes();

        if (enquestes.isEmpty()) {
            JLabel noSurveysLabel = new JLabel("No hi ha enquestes disponibles.");
            noSurveysLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            noSurveysLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            surveyListPanel.add(noSurveysLabel);
        } else {
            for (java.util.Map.Entry<Integer, String> entry : enquestes.entrySet()) {
                surveyListPanel.add(createSurveyItemPanel(entry.getKey(), entry.getValue()));
            }
        }

        JPanel surveyListContainer = (JPanel) infoPanel.getComponent(1);
        surveyListContainer.removeAll();
        JScrollPane scrollPane = new JScrollPane(surveyListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        surveyListContainer.add(scrollPane, BorderLayout.CENTER);

        surveyListContainer.revalidate();
        surveyListContainer.repaint();
    }

    /**
     * Crea un panel per a un element de la llista d'enquestes.
     * @param surveyId ID de l'enquesta.
     * @param surveyName Nom de l'enquesta.
     * @return Panel que representa l'element de l'enquesta.
     */
    private JPanel createSurveyItemPanel(int surveyId, String surveyName) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, VistaHelpers.BORDER_COLOR));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        itemPanel.setPreferredSize(new Dimension(400, 50));

        // Panel para el nombre con centrado vertical
        JPanel namePanel = new JPanel(new GridBagLayout());
        namePanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(surveyName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        namePanel.add(nameLabel);
        namePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        itemPanel.add(namePanel, BorderLayout.WEST);

        JButton editButton = new JButton("✎"); // Pencil icon
        JButton deleteButton = new JButton("\uD83D\uDDD1"); // Trash can icon
        JButton viewButton = new JButton("\uD83D\uDCCB"); // Clipboard icon
        JButton respondButton = new JButton("➤"); // Arrow icon
        JButton analyzeButton = new JButton("\uD83D\uDCC8"); // Chart icon
        JButton cloneButton = new JButton("⎘"); // Copy icon
        JButton exportButton = new JButton("⬇"); // Download icon

        VistaHelpers.styleActionButton(editButton);
        VistaHelpers.styleActionButton(deleteButton);
        VistaHelpers.styleActionButton(cloneButton);
        VistaHelpers.styleActionButton(viewButton);
        VistaHelpers.styleActionButton(analyzeButton);
        VistaHelpers.styleActionButton(respondButton);
        VistaHelpers.styleActionButton(exportButton);

        // Make action buttons respond to Enter key
        VistaHelpers.makeButtonRespondToEnter(editButton);
        VistaHelpers.makeButtonRespondToEnter(deleteButton);
        VistaHelpers.makeButtonRespondToEnter(cloneButton);
        VistaHelpers.makeButtonRespondToEnter(viewButton);
        VistaHelpers.makeButtonRespondToEnter(analyzeButton);
        VistaHelpers.makeButtonRespondToEnter(respondButton);
        VistaHelpers.makeButtonRespondToEnter(exportButton);

        // Add tooltips
        editButton.setToolTipText("Editar enquesta");
        deleteButton.setToolTipText("Esborrar enquesta");
        viewButton.setToolTipText("Veure respostes");
        respondButton.setToolTipText("Respondre enquesta");
        analyzeButton.setToolTipText("Analitzar enquesta");
        cloneButton.setToolTipText("Clonar enquesta");
        exportButton.setToolTipText("Exportar enquesta");

        editButton.addActionListener(e -> iCtrlPresentacion.editarEnquesta(surveyId));
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frameVista, "¿Segur que desitja esborrar l'enquesta '" + surveyName + "'?", "Confirmar esborrat", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                iCtrlPresentacion.esborrarEnquesta(surveyId);
                actualizarListaEncuestas();
            }
        });
        viewButton.addActionListener(e -> iCtrlPresentacion.veureRespostesEnquesta(surveyId));
        respondButton.addActionListener(e -> iCtrlPresentacion.respondreEnquesta(surveyId));
        analyzeButton.addActionListener(e -> iCtrlPresentacion.analitzarEnquesta(surveyId));
        cloneButton.addActionListener(e -> {
            iCtrlPresentacion.clonarEnquesta(surveyId);
            actualizarListaEncuestas();
        });
        exportButton.addActionListener(e -> openExportDialog(surveyId, surveyName));

        // Panel para los botones con centrado vertical
        JPanel buttonGroup = new JPanel(new GridBagLayout());
        buttonGroup.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        buttonGroup.add(editButton, gbc);
        buttonGroup.add(deleteButton, gbc);
        buttonGroup.add(cloneButton, gbc);
        buttonGroup.add(exportButton, gbc);
        buttonGroup.add(viewButton, gbc);
        buttonGroup.add(analyzeButton, gbc);
        buttonGroup.add(respondButton, gbc);

        itemPanel.add(buttonGroup, BorderLayout.EAST);

        return itemPanel;
    }


    /**
     * Gestiona l'acció del botó de registre.
     * Obre la finestra de registre si no està ja oberta.
     * @param event Esdeveniment d'acció que desencadena aquest mètode.
     */
    private void actionPerformed_buttonRegister (ActionEvent event) {
        SwingUtilities.invokeLater(() -> {
            if (vistaRegister == null || !vistaRegister.isVisible()) {
                vistaRegister = new VistaRegister(iCtrlPresentacion);
                vistaRegister.hacerVisible();
            } else {
                vistaRegister.toFront();
                vistaRegister.requestFocus();
            }
        });
    }

    /**
     * Gestiona l'acció del botó d'inici de sessió.
     * Obre la finestra d'inici de sessió si no està ja oberta.
     * @param event Esdeveniment d'acció que desencadena aquest mètode.
     */
    private void actionPerformed_buttonLogin (ActionEvent event) {
        SwingUtilities.invokeLater(() -> {
            if (vistaLogin == null || !vistaLogin.isVisible()) {
                vistaLogin = new VistaLogin(iCtrlPresentacion);
                vistaLogin.hacerVisible();
            } else {
                vistaLogin.toFront();
                vistaLogin.requestFocus();
            }
        });
    }

    /**
     * Gestiona l'acció del botó del menú d'usuari.
     * Mostra un menú emergent amb opcions per tancar sessió o eliminar l'usuari.
     * @param event Esdeveniment d'acció que desencadena aquest mètode.
     */
    private void actionPerformed_buttonUserMenu (ActionEvent event) {
        JPopupMenu popupMenu = new JPopupMenu();

        // Crear el ítem de cerrar sesión
        JMenuItem logoutItem = new JMenuItem("Tancar sessió");
        logoutItem.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutItem.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(frameVista,
                    "Està segur de que desitja tancar la sessió?",
                    "Confirmar tancament de sessió",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                iCtrlPresentacion.tancarSessio();
                JOptionPane.showMessageDialog(frameVista,
                        "Sessió tancada amb èxit",
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Crear el ítem de eliminar usuario
        JMenuItem deleteUserItem = getJMenuItem();

        popupMenu.add(logoutItem);
        popupMenu.addSeparator();
        popupMenu.add(deleteUserItem);

        // Mostrar el menú en la posición del botón
        popupMenu.show(buttonUserMenu, 0, buttonUserMenu.getHeight());
    }

    private JMenuItem getJMenuItem() {
        JMenuItem deleteUserItem = new JMenuItem("Eliminar usuari");
        deleteUserItem.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteUserItem.setForeground(new Color(220, 20, 60));
        deleteUserItem.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(frameVista,
                    "¿Està segur de que desitja eliminar el seu compte d'usuari?\nAquesta acció no es pot desfer.",
                    "Confirmar eliminació de l'usuari",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                iCtrlPresentacion.eliminarUsuariActual();
                JOptionPane.showMessageDialog(frameVista,
                        "Usuari esborrat amb èxit",
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return deleteUserItem;
    }

    /**
     * Gestiona l'acció del botó d'afegir enquesta.
     * @param event Esdeveniment d'acció que desencadena aquest mètode.
     */
    private void actionPerformed_buttonAddSurvey (ActionEvent event) {
        SwingUtilities.invokeLater(() -> {
            if (vistaAddSurvey == null || !vistaAddSurvey.isVisible()) {
                vistaAddSurvey = new VistaAddSurvey(iCtrlPresentacion, this);
                vistaAddSurvey.hacerVisible();
            } else {
                vistaAddSurvey.toFront();
                vistaAddSurvey.requestFocus();
            }
        });
    }

    /**
     * Gestiona l'acció del botó d'importació d'enquestes.
     * @param event Esdeveniment d'acció que desencadena aquest mètode.
     */
    private void actionPerformed_buttonImportSurvey(ActionEvent event) {
        SwingUtilities.invokeLater(() -> {
            VistaFileChooser vistaImport = new VistaFileChooser(
                VistaFileChooser.Mode.IMPORT,
                "Enquesta",
                path -> {
                    try {
                        iCtrlPresentacion.importarEnquesta(path);
                    }
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(frameVista,
                            "Error en importar l'enquesta:\n" + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(frameVista,
                        "Enquesta importada correctament des de:\n" + path,
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);
                    actualizarListaEncuestas();
                }
            );
            vistaImport.hacerVisible();
        });
    }

    /**
     * Obre el diàleg d'exportació per a una enquesta específica.
     * @param surveyId ID de l'enquesta a exportar.
     * @param surveyName Nom de l'enquesta a exportar.
     */
    private void openExportDialog(int surveyId, String surveyName) {
        SwingUtilities.invokeLater(() -> {
            VistaFileChooser vistaExport = new VistaFileChooser(
                VistaFileChooser.Mode.EXPORT,
                "Enquesta",
                path -> {
                    try {
                        iCtrlPresentacion.exportarEnquesta(surveyId, path);
                    }
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(frameVista,
                            "La ruta introduida no existeix",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(frameVista,
                        "Enquesta '" + surveyName + "' exportada correctament a:\n" + path,
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            );
            vistaExport.hacerVisible();
        });
    }

    /**
     * Assigna els actionListeners als botons de la vista principal.
     */
    private void inicialitzarComponents() {
        buttonRegister.addActionListener(this::actionPerformed_buttonRegister);
        buttonLogin.addActionListener(this::actionPerformed_buttonLogin);
        buttonUserMenu.addActionListener(this::actionPerformed_buttonUserMenu);
        buttonAddSurvey.addActionListener(this::actionPerformed_buttonAddSurvey);
        buttonImportSurvey.addActionListener(this::actionPerformed_buttonImportSurvey);
    }

    /**
     * Actualitza l'estat de la sessió a la vista principal.
     * Mostra o amaga els botons i panells segons si hi ha una sessió iniciada.
     */
    public void actualitzarEstatSessio() {
        boolean loggedIn = iCtrlPresentacion.sessioIniciada();
        if (loggedIn) {
            labelUsuario.setText("Usuari: " + iCtrlPresentacion.getUsuariActual());
            actualizarListaEncuestas();
        } else {
            labelUsuario.setText("");
        }
        actualitzarBotons(loggedIn);
    }

    /**
     * Actualitza la visibilitat dels botonss i panells segons l'estat de l'inici de sessió.
     * @param loggedIn true si hi ha una sessió iniciada, false en cas contrari.
     */
    private void actualitzarBotons(boolean loggedIn) {
        // Toggle visibility based on login state
        buttonPanel.setVisible(!loggedIn);
        infoPanel.setVisible(loggedIn);
        addSurveyPanel.setVisible(loggedIn);
        buttonUserMenu.setVisible(loggedIn);
        labelUsuario.setVisible(loggedIn);

        // If logged in, infoPanel should be in the center
        if (loggedIn) {
            mainPanel.add(infoPanel, BorderLayout.CENTER);
        } else {
            mainPanel.remove(infoPanel);
            mainPanel.add(buttonPanel, BorderLayout.CENTER);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}