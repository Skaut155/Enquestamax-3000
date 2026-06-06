package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Vista per respondre una enquesta.
 * Mostra les preguntes una per una amb navegació entre elles,
 * i permet introduir respostes segons el tipus de pregunta.
 */
public class VistaRespondre extends VistaBase {
    /* Identificador de l'enquesta */
    private final int idEnquesta;
    /* Nom de l'enquesta */
    private final String nomEnquesta;
    /* Preguntes de l'enquesta */
    private String[] preguntes;
    /* Indica si cada pregunta és obligatòria */
    private boolean[] preguntesObligatories;
    /* Respostes de l'usuari */
    private final ArrayList<String> respostes;
    /* Índex de la pregunta actual */
    private int preguntaActual;
    /* Label de la pregunta */
    private JLabel preguntaLabel;
    /* Panell d'input per a la resposta */
    private JPanel inputPanel;
    /* Botons de navegació */
    private JButton nextButton;
    /* Botó de navegació anterior */
    private JButton prevButton;
    /* Botó d'enviament de respostes */
    private JButton submitButton;
    /* Label de progrés */
    private JLabel progressLabel;

    /**
     * Constructor de la vista per respondre enquestes.
     *
     * @param ctrl Controlador de presentació
     * @param idEnquesta Identificador de l'enquesta
     * @param nomEnquesta Nom de l'enquesta
     */
    public VistaRespondre(CtrlPresentation ctrl, int idEnquesta, String nomEnquesta) {
        super("Respondre - " + nomEnquesta, ctrl);
        this.idEnquesta = idEnquesta;
        this.nomEnquesta = nomEnquesta;
        this.preguntaActual = 0;
        this.respostes = new ArrayList<>();

        cargarPreguntes();
        configurarVista();
    }

    /**
     * Fa visible la finestra i mostra la primera pregunta.
     */
    @Override
    public void hacerVisible() {
        super.hacerVisible();
        mostrarPregunta();
    }


    /**
     * Carrega les preguntes de l'enquesta des del controlador.
     */
    private void cargarPreguntes() {
        preguntes = iCtrlPresentation.getPreguntesEnquesta(idEnquesta);
        preguntesObligatories = new boolean[preguntes.length];

        // Initialize all responses and detect mandatory questions
        for (int i = 0; i < preguntes.length; i++) {
            // Check if question is mandatory (has asterisk *)
            preguntesObligatories[i] = preguntes[i].contains("*");

            // Initialize numeric questions with "0" so they're not considered empty
            if (preguntes[i].contains("NUMERICA")) {
                respostes.add("0");
            } else {
                respostes.add("");
            }
        }
    }

    /**
     * Configura la interfície gràfica de la vista.
     */
    private void configurarVista() {
        frame.setSize(700, 500);

        JPanel mainPanel = VistaHelpers.createMainPanel();

        // Title panel
        JPanel titlePanel = VistaHelpers.createTitlePanelWithSubtitle("Respondre: " + nomEnquesta, null);
        progressLabel = new JLabel();
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titlePanel.add(progressLabel, BorderLayout.SOUTH);

        // Content panel
        JPanel contentPanel = VistaHelpers.createContentPanel();

        preguntaLabel = new JLabel();
        preguntaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        preguntaLabel.setForeground(VistaHelpers.TEXT_COLOR);
        contentPanel.add(preguntaLabel, BorderLayout.NORTH);

        inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        contentPanel.add(inputPanel, BorderLayout.CENTER);

        // Navigation buttons
        JPanel buttonPanel = VistaHelpers.createButtonPanel();

        prevButton = VistaHelpers.createCancelButton("← Anterior");
        prevButton.addActionListener(e -> anteriorPregunta());
        VistaHelpers.makeButtonRespondToEnter(prevButton);

        nextButton = VistaHelpers.createPrimaryButton("Següent →");
        nextButton.addActionListener(e -> seguentPregunta());
        VistaHelpers.makeButtonRespondToEnter(nextButton);

        submitButton = VistaHelpers.createSuccessButton("Enviar respostes");
        submitButton.addActionListener(e -> enviarRespuestas());
        submitButton.setVisible(false);
        VistaHelpers.makeButtonRespondToEnter(submitButton);

        JButton cancelButton = VistaHelpers.createDangerButton("Cancel·lar");
        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame,
                "¿Segur que vols cancel·lar? Les respostes es perdran.",
                "Confirmar cancel·lació",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
            }
        });
        VistaHelpers.makeButtonRespondToEnter(cancelButton);

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);

        // Setup keyboard navigation
        VistaHelpers.setupKeyboardNavigation(frame, nextButton);
    }

    /**
     * Mostra la pregunta actual amb el seu tipus d'input corresponent.
     */
    private void mostrarPregunta() {
        if (preguntes.length == 0) {
            JOptionPane.showMessageDialog(frame,
                "Aquesta enquesta no té preguntes",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            frame.dispose();
            return;
        }

        String preguntaInfo = preguntes[preguntaActual];
        String[] parts = parsePregunta(preguntaInfo);
        String tipus = parts[0];
        String textPregunta = parts[1];

        // Mostrar indicador de pregunta obligatòria
        String labelText = "Pregunta " + (preguntaActual + 1) + ": " + textPregunta;
        if (preguntesObligatories[preguntaActual]) {
            labelText += " <span style='color: red;'>*</span>";
            preguntaLabel.setText("<html>" + labelText + "</html>");
        } else {
            preguntaLabel.setText(labelText);
        }

        progressLabel.setText("Pregunta " + (preguntaActual + 1) + " de " + preguntes.length);

        inputPanel.removeAll();
        inputPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        switch (tipus) {
            case "NUMERICA":
                crearInputNumeric(formPanel);
                break;
            case "MULTIPLE":
                crearInputMulti(formPanel, parts);
                break;
                case "UNICA_NO_ORDENADA":
                crearInputUnicaNoOrdenada(formPanel, parts);
                break;
            case "UNICA_ORDENADA":
                crearInputUnicaOrdenada(formPanel, parts);
                break;
            case "OBERTA":
                crearInputOberta(formPanel);
                break;
            default:
                JLabel errorLabel = new JLabel("Tipus de pregunta no reconegut: " + tipus);
                formPanel.add(errorLabel);
        }

        inputPanel.add(formPanel, BorderLayout.NORTH);
        inputPanel.revalidate();
        inputPanel.repaint();

        // Update navigation buttons
        prevButton.setEnabled(preguntaActual > 0);
        nextButton.setVisible(preguntaActual < preguntes.length - 1);
        submitButton.setVisible(preguntaActual == preguntes.length - 1);

        // Update default button based on current state
        if (preguntaActual == preguntes.length - 1) {
            frame.getRootPane().setDefaultButton(submitButton);
        } else {
            frame.getRootPane().setDefaultButton(nextButton);
        }
    }

    /**
     * Parsea la informació d'una pregunta en les seves parts components.
     *
     * @param preguntaInfo String amb la informació completa de la pregunta
     * @return Array amb [tipus, textPregunta, opcions...]
     */
    private String[] parsePregunta(String preguntaInfo) {
        // Format real: "[#1] Título* - tipus: TIPUS\n\tOpción 1\n\tOpción 2\n..."
        String[] lines = preguntaInfo.split("\n");
        String tipus = "";
        String text = "";
        ArrayList<String> opcions = new ArrayList<>();

        if (lines.length > 0) {
            // First line: "[#1] Título* - tipus: TIPUS"
            String firstLine = lines[0];

            // Extract type
            int tipusIndex = firstLine.indexOf("- tipus: ");
            if (tipusIndex != -1) {
                tipus = firstLine.substring(tipusIndex + 9).trim();
            }

            // Extract text (between "] " and " - tipus:")
            int startText = firstLine.indexOf("] ") + 2;
            int endText = firstLine.indexOf(" - tipus:");
            if (startText > 1 && endText != -1) {
                text = firstLine.substring(startText, endText).trim();
                // Remove asterisk if present (mandatory indicator)
                if (text.endsWith("*")) {
                    text = text.substring(0, text.length() - 1).trim();
                }
            }
        }

        // Remaining lines are options (start with tab)
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (!line.isEmpty()) {
                opcions.add(line);
            }
        }

        String[] result = new String[2 + opcions.size()];
        result[0] = tipus;
        result[1] = text;
        for (int i = 0; i < opcions.size(); i++) {
            result[2 + i] = opcions.get(i);
        }

        return result;
    }

    /**
     * Crea un input de tipus numèrica amb un JSpinner que accepta decimals.
     *
     * @param panel Panell on afegir el JSpinner
     */
    private void crearInputNumeric(JPanel panel) {
        // Obtenir valor inicial si existeix
        double valorInicial = 0.0;
        String respostaActual = respostes.get(preguntaActual);
        if (!respostaActual.isEmpty()) {
            try {
                valorInicial = Double.parseDouble(respostaActual);
            } catch (NumberFormatException ignored) {}
        }

        SpinnerNumberModel model = new SpinnerNumberModel(valorInicial, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(new Font("Arial", Font.PLAIN, 14));
        spinner.setPreferredSize(new Dimension(150, 30));

        // Configurar el editor per mostrar decimals correctament
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#0.##");
        spinner.setEditor(editor);

        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.add(new JLabel("Resposta numèrica: "));
        fieldPanel.add(spinner);

        panel.add(fieldPanel);

        // Guardar el valor inicial immediatament (per si l'usuari deixa 0 com a resposta)
        guardarRespostaActual(String.valueOf(spinner.getValue()));

        // Save on change
        spinner.addChangeListener(e -> guardarRespostaActual(String.valueOf(spinner.getValue())));

        // Allow Enter key to go to next question
        JTextField tf = editor.getTextField();
        tf.addActionListener(e -> seguentPregunta());
    }

    /**
     * Crea un input de tipus oberta amb un JTextArea.
     *
     * @param panel Panell on afegir el JTextArea
     */
    private void crearInputOberta(JPanel panel) {
        JTextArea textArea = new JTextArea(5, 30);
        textArea.setText(respostes.get(preguntaActual));
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);

        textArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                guardarRespostaActual(textArea.getText());
            }
        });
    }

    /**
     * Crea un input de tipus única no ordenada amb radiobuttons.
     *
     * @param panel Panell on afegir els radiobuttons
     * @param parts Parts de la pregunta (tipus, text, opcions)
     */
    private void crearInputUnicaNoOrdenada(JPanel panel, String[] parts) {
        inputButtonCreation(panel, parts);
    }

    /**
     * Crea els radiobuttons per a preguntes d'elecció única.
     *
     * @param panel Panell on afegir els radiobuttons
     * @param parts Parts de la pregunta (tipus, text, opcions)
     */
    private void inputButtonCreation(JPanel panel, String[] parts) {
        ButtonGroup group = new ButtonGroup();
        String respostaActual = respostes.get(preguntaActual);
        java.util.List<JRadioButton> radioList = new java.util.ArrayList<>();

        for (int i = 2; i < parts.length; i++) {
            JRadioButton radio = new JRadioButton(parts[i]);
            radio.setBackground(Color.WHITE);
            radio.setFont(new Font("Arial", Font.PLAIN, 13));

            if (respostaActual.equals(String.valueOf(i - 1))) {
                radio.setSelected(true);
            }

            int opcio = i - 1;
            radio.addActionListener(e -> guardarRespostaActual(String.valueOf(opcio)));

            group.add(radio);
            panel.add(radio);
            radioList.add(radio);
        }

        // Setup arrow key navigation for radio buttons
        if (!radioList.isEmpty()) {
            JRadioButton[] radioArray = radioList.toArray(new JRadioButton[0]);
            VistaHelpers.setupRadioButtonNavigation(radioArray);
        }
    }

    /**
     * Crea un input de tipus única ordenada amb radiobuttons.
     *
     * @param panel Panell on afegir els radiobuttons
     * @param parts Parts de la pregunta (tipus, text, opcions)
     */
    private void crearInputUnicaOrdenada(JPanel panel, String[] parts) {
        inputButtonCreation(panel, parts);
    }

    /**
     * Crea un input de tipus múltiple amb checkboxes.
     *
     * @param panel Panell on afegir els checkboxes
     * @param parts Parts de la pregunta (tipus, text, opcions)
     */
    private void crearInputMulti(JPanel panel, String[] parts) {
        ArrayList<JCheckBox> checkboxes = new ArrayList<>();
        String respostaActual = respostes.get(preguntaActual);
        String[] seleccionades = respostaActual.isEmpty() ? new String[0] : respostaActual.split(" ");

        for (int i = 2; i < parts.length; i++) {
            JCheckBox checkbox = new JCheckBox(parts[i]);
            checkbox.setBackground(Color.WHITE);
            checkbox.setFont(new Font("Arial", Font.PLAIN, 13));

            String opcioStr = String.valueOf(i - 1);
            for (String sel : seleccionades) {
                if (sel.equals(opcioStr)) {
                    checkbox.setSelected(true);
                    break;
                }
            }

            checkbox.addActionListener(e -> {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < checkboxes.size(); j++) {
                    if (checkboxes.get(j).isSelected()) {
                        if (!sb.isEmpty()) sb.append(" ");
                        sb.append(j + 1);
                    }
                }
                guardarRespostaActual(sb.toString());
            });

            checkboxes.add(checkbox);
            panel.add(checkbox);
        }

        // Setup arrow key navigation for checkboxes
        if (!checkboxes.isEmpty()) {
            JCheckBox[] checkboxArray = checkboxes.toArray(new JCheckBox[0]);
            VistaHelpers.setupCheckboxNavigation(checkboxArray);
        }
    }

    /**
     * Guarda la resposta actual a l'array de respostes.
     *
     * @param resposta Resposta a guardar
     */
    private void guardarRespostaActual(String resposta) {
        respostes.set(preguntaActual, resposta);
    }

    /**
     * Mostra la següent pregunta si existeix.
     */
    private void seguentPregunta() {
        if (preguntaActual < preguntes.length - 1) {
            preguntaActual++;
            mostrarPregunta();
        }
    }

    /**
     * Mostra la pregunta anterior si existeix.
     */
    private void anteriorPregunta() {
        if (preguntaActual > 0) {
            preguntaActual--;
            mostrarPregunta();
        }
    }

    /**
     * Envia les respostes al controlador després de validar-les.
     */
    private void enviarRespuestas() {
        // Validate mandatory questions are answered
        for (int i = 0; i < respostes.size(); i++) {
            if (preguntesObligatories[i] && respostes.get(i).trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                    "La pregunta " + (i + 1) + " és obligatòria i no ha estat resposta.",
                    "Error: Pregunta obligatòria sense respondre",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Validate non-mandatory questions
        for (int i = 0; i < respostes.size(); i++) {
            if (!preguntesObligatories[i] && respostes.get(i).trim().isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                    "La pregunta " + (i + 1) + " no ha estat resposta. Vols continuar igualment?",
                    "Pregunta sense respondre",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.NO_OPTION) {
                    return;
                }
            }
        }

        try {
            String[] respostesArray = respostes.toArray(new String[0]);
            iCtrlPresentation.guardarRespostes(idEnquesta, respostesArray);

            JOptionPane.showMessageDialog(frame,
                "Respostes enviades amb èxit!",
                "Èxit",
                JOptionPane.INFORMATION_MESSAGE);

            frame.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Error: Les respostes numèriques només accepten números com a resposta.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
