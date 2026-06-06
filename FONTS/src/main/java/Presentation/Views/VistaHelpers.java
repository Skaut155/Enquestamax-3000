package Presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe utilitària per estilitzar components de la interfície d'usuari
 * amb un estil coherent a tota l'aplicació.
 */
public class VistaHelpers {

    /** Color de fons estàndard de l'aplicació */
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    /** Color principal per a botons d'acció */
    public static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    /** Color d'èxit per a accions positives */
    public static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    /** Color de perill per a accions destructives */
    public static final Color DANGER_COLOR = new Color(220, 20, 60);
    /** Color secundari per a botons cancel·lar */
    public static final Color SECONDARY_COLOR = new Color(180, 180, 180);
    /** Color de text principal */
    public static final Color TEXT_COLOR = new Color(60, 60, 60);
    /** Color de títols */
    public static final Color TITLE_COLOR = new Color(60, 60, 120);
    /** Color de vores */
    public static final Color BORDER_COLOR = new Color(200, 200, 200);

    /** Color del borde de focus */
    public static final Color FOCUS_BORDER_COLOR = new Color(0, 120, 215);

    /**
     * Aplica un estil coherent a un botó amb colors personalitzats.
     *
     * @param button Botó a estilitzar
     * @param baseColor Color de fons del botó
     * @param foregroundColor Color del text del botó
     */
    public static void styleButton(JButton button, Color baseColor, Color foregroundColor) {
        button.setBackground(baseColor);
        button.setForeground(foregroundColor);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Afegir indicador visual de focus
        button.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(FOCUS_BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(6, 18, 6, 18)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(baseColor.brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(baseColor);
                }
            }
        });

        // Afegir navegació amb fletxes
        addArrowKeyNavigation(button);
    }

    /**
     * Aplica un estil coherent a un botó amb color de fons personalitzat
     * i text blanc per defecte.
     *
     * @param button Botó a estilitzar
     * @param baseColor Color de fons del botó
     */
    public static void styleButton(JButton button, Color baseColor) {
        styleButton(button, baseColor, Color.WHITE);
    }

    /**
     * Crea un panel de títol amb estil coherent.
     *
     * @param titleText Text del títol
     * @return Panel amb el títol estilitzat
     */
    public static JPanel createTitlePanel(String titleText) {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(245, 245, 250));
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(60, 60, 120));
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    /**
     * Crea un panel principal amb marges i estil coherent.
     *
     * @return Panel principal estilitzat
     */
    public static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 250));
        return mainPanel;
    }

    /**
     * Crea un panel de formulari amb estil coherent.
     *
     * @return Panel de formulari estilitzat amb GridBagLayout
     */
    public static JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return formPanel;
    }

    /**
     * Crea un camp de text amb estil coherent.
     *
     * @return Camp de text estilitzat
     */
    public static JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Afegir indicador visual de focus
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(FOCUS_BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
        });

        return textField;
    }

    /**
     * Crea un camp de contrasenya amb estil coherent.
     *
     * @return Camp de contrasenya estilitzat
     */
    public static JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Afegir indicador visual de focus
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(FOCUS_BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
        });

        return passwordField;
    }

    /**
     * Afegeix un camp de formulari (etiqueta + component) a un panel.
     *
     * @param panel Panel al qual cal afegir el camp
     * @param label Etiqueta del camp
     * @param component Component del camp (TextField, etc.)
     * @param gbc Constrains de GridBagLayout
     * @param y Posició Y (fila) del camp
     */
    public static void addFormField(JPanel panel, JLabel label, JComponent component, GridBagConstraints gbc, int y) {
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.3;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(component, gbc);
    }

    /**
     * Crea un panel de botons centrat amb estil coherent.
     *
     * @return Panel de botons estilitzat
     */
    public static JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        return buttonPanel;
    }

    /**
     * Crea un botó d'acció principal (confirmar, acceptar, etc.)
     *
     * @param text Text del botó
     * @return Botó estilitzat
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, PRIMARY_COLOR);
        return button;
    }

    /**
     * Crea un botó d'èxit (desar, crear, etc.)
     *
     * @param text Text del botó
     * @return Botó estilitzat
     */
    public static JButton createSuccessButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, SUCCESS_COLOR);
        return button;
    }

    /**
     * Crea un botó de cancel·lar amb estil coherent.
     *
     * @param text Text del botó
     * @return Botó estilitzat
     */
    public static JButton createCancelButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, SECONDARY_COLOR, Color.BLACK);
        return button;
    }

    /**
     * Crea un botó de perill (esborrar, eliminar, etc.)
     *
     * @param text Text del botó
     * @return Botó estilitzat
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, DANGER_COLOR);
        return button;
    }

    /**
     * Crea un panell amb camp de contrasenya i botó per mostrar/ocultar.
     *
     * @param passwordField Camp de contrasenya
     * @return Panel amb el camp i el botó de toggle
     */
    public static JPanel createPasswordPanelWithToggle(JPasswordField passwordField) {
        JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
        passwordPanel.setBackground(BACKGROUND_COLOR);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        JButton toggleButton = new JButton("•••");
        toggleButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        toggleButton.setPreferredSize(new Dimension(40, passwordField.getPreferredSize().height));
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.setFocusPainted(false);
        toggleButton.setBackground(new Color(220, 220, 220));
        toggleButton.setToolTipText("Mostrar/ocultar contrasenya");

        toggleButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == (char) 0) {
                passwordField.setEchoChar('•');
                toggleButton.setText("•••");
            } else {
                passwordField.setEchoChar((char) 0);
                toggleButton.setText("ABC");
            }
        });
        passwordPanel.add(toggleButton, BorderLayout.EAST);

        return passwordPanel;
    }

    /**
     * Crea un panell de títol amb subtítol opcional.
     *
     * @param titleText Text del títol
     * @param subtitleText Text del subtítol (pot ser null)
     * @return Panel amb títol i subtítol estilitzats
     */
    public static JPanel createTitlePanelWithSubtitle(String titleText, String subtitleText) {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(TITLE_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        if (subtitleText != null) {
            JLabel subtitleLabel = new JLabel(subtitleText);
            subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        }

        return titlePanel;
    }

    /**
     * Estilitza un botó d'acció per a llistes (icones).
     *
     * @param button Botó a estilitzar
     */
    public static void styleActionButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setMargin(new Insets(5, 8, 5, 8));
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setForeground(new Color(100, 100, 100));

        // Afegir indicador visual de focus
        button.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(FOCUS_BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(0, 2, 0, 2)
                ));
                button.setForeground(FOCUS_BORDER_COLOR);
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
                button.setForeground(new Color(100, 100, 100));
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isFocusOwner()) {
                    button.setForeground(new Color(100, 100, 100));
                } else {
                    button.setForeground(FOCUS_BORDER_COLOR);
                }
            }
        });

        // Afegir navegació amb fletxes
        addArrowKeyNavigation(button);
    }

    /**
     * Crea un JScrollPane amb estil coherent.
     *
     * @param component Component a embolcallar
     * @return JScrollPane estilitzat
     */
    public static JScrollPane createScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    /**
     * Crea un panell de contingut amb vora i fons blanc.
     *
     * @return Panel de contingut estilitzat
     */
    public static JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    /**
     * Configura la navegació per teclat en una finestra.
     * Permet usar Tab/Shift+Tab per navegar, Enter per activar botons,
     * i Escape per tancar la finestra.
     *
     * @param frame La finestra a configurar
     * @param defaultButton El botó per defecte que s'activarà amb Enter (pot ser null)
     */
    public static void setupKeyboardNavigation(JFrame frame, JButton defaultButton) {
        // Configure Tab traversal
        Set<AWTKeyStroke> forwardKeys = new HashSet<>(frame.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
        frame.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

        Set<AWTKeyStroke> backwardKeys = new HashSet<>(frame.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        backwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK));
        frame.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

        // Configure Escape to close the window
        frame.getRootPane().registerKeyboardAction(
            e -> frame.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Configure default button for Enter key
        if (defaultButton != null) {
            frame.getRootPane().setDefaultButton(defaultButton);
        }
    }

    /**
     * Configura la navegació per teclat en una finestra de diàleg.
     *
     * @param dialog El diàleg a configurar
     * @param defaultButton El botó per defecte (pot ser null)
     */
    public static void setupKeyboardNavigation(JDialog dialog, JButton defaultButton) {
        // Configure Tab traversal
        Set<AWTKeyStroke> forwardKeys = new HashSet<>(dialog.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
        dialog.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

        Set<AWTKeyStroke> backwardKeys = new HashSet<>(dialog.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        backwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK));
        dialog.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

        // Configure Escape to close the dialog
        dialog.getRootPane().registerKeyboardAction(
            e -> dialog.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Configure default button for Enter key
        if (defaultButton != null) {
            dialog.getRootPane().setDefaultButton(defaultButton);
        }
    }

    /**
     * Configura les tecles de fletxa per navegar entre components d'un panell.
     *
     * @param panel El panell que conté els components
     */
    public static void setupArrowKeyNavigation(JPanel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                }
            }
        });
    }

    /**
     * Fa que un botó respongui a la tecla Enter quan té el focus.
     *
     * @param button El botó a configurar
     */
    public static void makeButtonRespondToEnter(JButton button) {
        button.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "press"
        );
        button.getActionMap().put("press", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        });
        // També afegir navegació amb fletxes
        addArrowKeyNavigation(button);
    }

    /**
     * Afegeix navegació amb fletxes a un component.
     * Les fletxes permeten navegar al següent/anterior component amb focus.
     *
     * @param component El component a configurar
     */
    public static void addArrowKeyNavigation(JComponent component) {
        component.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "focusNext"
        );
        component.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "focusNext"
        );
        component.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "focusPrev"
        );
        component.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "focusPrev"
        );

        component.getActionMap().put("focusNext", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
            }
        });
        component.getActionMap().put("focusPrev", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
            }
        });
    }

    /**
     * Configura navegació per fletxes per a un grup de radio buttons.
     *
     * @param radioButtons Array de radio buttons del grup
     */
    public static void setupRadioButtonNavigation(JRadioButton[] radioButtons) {
        for (int i = 0; i < radioButtons.length; i++) {
            final int index = i;
            radioButtons[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        int next = (index + 1) % radioButtons.length;
                        radioButtons[next].requestFocus();
                        radioButtons[next].setSelected(true);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
                        int prev = (index - 1 + radioButtons.length) % radioButtons.length;
                        radioButtons[prev].requestFocus();
                        radioButtons[prev].setSelected(true);
                    }
                }
            });
        }
    }

    /**
     * Configura navegació per fletxes per a un grup de checkboxes.
     *
     * @param checkboxes Array de checkboxes
     */
    public static void setupCheckboxNavigation(JCheckBox[] checkboxes) {
        for (int i = 0; i < checkboxes.length; i++) {
            final int index = i;
            checkboxes[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        int next = (index + 1) % checkboxes.length;
                        checkboxes[next].requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
                        int prev = (index - 1 + checkboxes.length) % checkboxes.length;
                        checkboxes[prev].requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                        checkboxes[index].setSelected(!checkboxes[index].isSelected());
                    }
                }
            });
        }
    }
}
