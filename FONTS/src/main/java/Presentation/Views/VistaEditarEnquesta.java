package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class VistaEditarEnquesta extends VistaBase {
    private final CtrlPresentation cp;
    private final VistaPrincipal vistaPrincipal;
    private final int idEnquesta;
    private int preguntesNum = 0;
    private JPanel questionsContainer;
    JTextField titleField;
    private final List<QuestionUI> questions = new ArrayList<>();

    private enum PseudoTipusPregunta {
        OBERTA,
        NUMERICA,
        UNICA_NO_ORDENADA,
        UNICA_ORDENADA,
        MULTIPLE;

        @Override
        public String toString() {
            return switch (this) {
                case OBERTA -> "Oberta";
                case NUMERICA -> "Numèrica";
                case UNICA_NO_ORDENADA -> "Única No Ordenada";
                case UNICA_ORDENADA -> "Única Ordenada";
                case MULTIPLE -> "Resposta Mútliple";
            };
        }

        public String toTxString() {
            return switch (this) {
                case OBERTA -> "OBERTA";
                case NUMERICA -> "NUMERICA";
                case UNICA_NO_ORDENADA -> "UNICA_NO_ORDENADA";
                case UNICA_ORDENADA -> "UNICA_ORDENADA";
                case MULTIPLE -> "MULTIPLE";
            };
        }
        static PseudoTipusPregunta fromStringCode(String s) {
            return switch (s) {
                case "OBERTA" -> OBERTA;
                case "NUMERICA" -> NUMERICA;
                case "UNICA_NO_ORDENADA" -> UNICA_NO_ORDENADA;
                case "UNICA_ORDENADA" -> UNICA_ORDENADA;
                case "MULTIPLE" -> MULTIPLE;
                default -> throw new IllegalStateException("Unexpected value: " + s);
            };
        }
    }

    private static class PseudoOptions {
        public JTextField optionField;
        public int ordre;
    }

    private static class QuestionUI {
        JTextField textField;
        PseudoTipusPregunta tipus;
        JPanel rootPanel;
        JPanel optionsPanel;
        JComboBox<PseudoTipusPregunta> changeTypeComboBox;
        JButton deleteButton;
        JButton addOptionButton;
        int ordre;
        boolean isObl;
        List<PseudoOptions> optionFields = new ArrayList<>();
    }

    public VistaEditarEnquesta(CtrlPresentation ctrl, VistaPrincipal vistaPrincipal, int idEnquesta) {
        super("Editar enquesta");
        this.cp = ctrl;
        this.vistaPrincipal = vistaPrincipal;
        this.idEnquesta = idEnquesta;
        configurarVista();
        carregarDadesInicials();
    }

    private void configurarVista() {
        JPanel mainPanel = VistaHelpers.createMainPanel();
        mainPanel.add(VistaHelpers.createTitlePanel("Editant:"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 245, 250));

        // Title field
        titleField = VistaHelpers.createTextField();
        titleField.setText(cp.getNomEnquesta(idEnquesta));
        titleField.setFont(titleField.getFont().deriveFont(Font.BOLD, 20f));
        titleField.setColumns(40);
        titleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                if(!titleField.getText().trim().isEmpty())
                cp.canviarNomEnquesta(idEnquesta, titleField.getText());
                else
                    JOptionPane.showMessageDialog(frame,
                            "El nom de l'enquesta no pot estar buit.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
            }
        });

        centerPanel.add(titleField, BorderLayout.NORTH);

        // Questions scroll area
        questionsContainer = new JPanel();
        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        questionsContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(questionsContainer);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Add question button
        JButton addQuestionButton = new JButton("+ Afegir pregunta");
        VistaHelpers.styleButton(addQuestionButton, new Color(60, 179, 113));
        addQuestionButton.addActionListener(this::afegirPreguntaBuida);

        JPanel addQuestionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addQuestionPanel.setBackground(new Color(245, 245, 250));
        addQuestionPanel.add(addQuestionButton);
        centerPanel.add(addQuestionPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom buttons: Cancel & Save
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton cancelButton = new JButton("Cancel·lar");

        VistaHelpers.styleButton(cancelButton, new Color(180, 180, 180), Color.BLACK);
        cancelButton.addActionListener(this::onCancel);

        JButton saveButton = new JButton("Desar");
        VistaHelpers.styleButton(saveButton, new Color(70, 130, 180));
        saveButton.addActionListener(this::onSave);

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(mainPanel);
        frame.pack();
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
    }

    private void carregarDadesInicials() {
        // For now, we only know how to get question texts as Strings
        preguntesNum = cp.getNumPreguntesEnquesta(idEnquesta);
        for (int i = 0; i < preguntesNum; i++) {
            String p = cp.getTitolPregunta(idEnquesta, i);
            String type = cp.getCodiTipusPregunta(idEnquesta, i);
            PseudoTipusPregunta tipus = PseudoTipusPregunta.fromStringCode(type);
            afegirPreguntaAmbText(p, tipus, i, false);
            int c = 0;
            String[] ops = new String[0];
            try{
                ops = cp.getTextOpcionsPregunta(idEnquesta, i);
            } catch (UnsupportedOperationException e){
                // No options for this question type
            }
            for (String op : ops) {
                afegirOpcio(questions.get(i), op, c++, false);
            }
        }
    }

    private void afegirPreguntaBuida(ActionEvent e) {
        // When adding from UI, also add in DomainController via CtrlPresentation
        afegirPreguntaAmbText("", PseudoTipusPregunta.OBERTA, preguntesNum++, true);
    }

    private void afegirPreguntaAmbText(String text, PseudoTipusPregunta tipusInicial, int ordre, boolean isAdd) {
        QuestionUI q = new QuestionUI();
        q.tipus = tipusInicial;
        q.ordre = ordre;

        if (isAdd) {
            cp.afegirPregunta(idEnquesta);
            text = cp.getTitolPregunta(idEnquesta, ordre);
            String typeCode = cp.getCodiTipusPregunta(idEnquesta, ordre);
            tipusInicial = PseudoTipusPregunta.fromStringCode(typeCode);
            q.tipus = tipusInicial;
        }

        JPanel root = new JPanel();
        root.setLayout(new BorderLayout(5, 5));
        root.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))
        ));

        root.setBackground(Color.WHITE);

        // Top row: label + text + change type + delete
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        topRow.setBackground(Color.WHITE);

        JLabel label = new JLabel("P" + (questions.size() + 1) + ":");
        topRow.add(label);

        JTextField textField = VistaHelpers.createTextField();
        textField.setText(text);
        textField.setColumns(30);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                try {
                    cp.canviarTitolPregunta(idEnquesta, q.ordre, textField.getText());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        q.textField = textField;
        topRow.add(textField);

        JCheckBox isMandatory = new JCheckBox("");
        q.isObl = cp.getObligatorietatPregunta(idEnquesta, q.ordre);
        Font base = isMandatory.getFont();
        isMandatory.setFont(base.deriveFont(Font.BOLD, 16f));
        updateMandatory(q, isMandatory);
        isMandatory.setSelected(q.isObl);
        isMandatory.setEnabled(true);
        isMandatory.setToolTipText("Marcar com a pregunta obligatòria");
        isMandatory.addActionListener(ev -> {
            q.isObl = !q.isObl;
            updateMandatory(q, isMandatory);
            cp.canviarObligatorietatPregunta(idEnquesta, q.ordre, q.isObl);
        });
        topRow.add(isMandatory);


        JComboBox<PseudoTipusPregunta> changeTypeComboBox = new JComboBox<>(PseudoTipusPregunta.values());
        changeTypeComboBox.setSelectedItem(tipusInicial);
        changeTypeComboBox.addActionListener(ev -> {
            PseudoTipusPregunta selectedType = (PseudoTipusPregunta) changeTypeComboBox.getSelectedItem();
            if (selectedType != null) {
                q.tipus = selectedType;
                cp.canviarTipusPregunta(idEnquesta, q.ordre, q.tipus.toTxString());
                actualitzarOpcionsVisible(q);
            }
        });
        q.changeTypeComboBox = changeTypeComboBox;
        topRow.add(changeTypeComboBox);

        JButton deleteButton = new JButton("\uD83D\uDDD1");
        deleteButton.setToolTipText("Esborrar pregunta");
        VistaHelpers.styleButton(deleteButton, new Color(220, 20, 60));
        deleteButton.addActionListener(ev -> eliminarPregunta(q));
        q.deleteButton = deleteButton;
        topRow.add(deleteButton);

        root.add(topRow, BorderLayout.NORTH);

        // Options panel (for single/multiple)
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(new Color(250, 250, 250));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 5));
        q.optionsPanel = optionsPanel;

        // Add-option button
        JButton addOptionButton = new JButton("+ Opció");
        addOptionButton.setToolTipText("Afegir opció");
        VistaHelpers.styleButton(addOptionButton, new Color(210, 210, 210), Color.BLACK);
        addOptionButton.addActionListener(ev -> afegirOpcio(q, "Nova opció", q.optionFields.size(), true));
        q.addOptionButton = addOptionButton;

        JPanel addOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        addOptionPanel.setBackground(new Color(250, 250, 250));
        addOptionPanel.add(addOptionButton);

        JPanel optionsWrapper = new JPanel();
        optionsWrapper.setLayout(new BorderLayout());
        optionsWrapper.setBackground(new Color(250, 250, 250));
        optionsWrapper.add(optionsPanel, BorderLayout.CENTER);
        optionsWrapper.add(addOptionPanel, BorderLayout.SOUTH);

        root.add(optionsWrapper, BorderLayout.CENTER);

        q.rootPanel = root;
        questions.add(q);
        questionsContainer.add(root);

        actualitzarOpcionsVisible(q);
        questionsContainer.revalidate();
        questionsContainer.repaint();
    }

    private static void updateMandatory(QuestionUI q, JCheckBox isMandatory) {
        if(q.isObl) {
            isMandatory.setText(" * ");
            isMandatory.setForeground(Color.RED);
        }
        else {
            isMandatory.setText("   ");
            isMandatory.setForeground(Color.BLACK);
        }
    }

    private void afegirOpcio(QuestionUI q, String text, int ordre, boolean isAdd) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        row.setBackground(new Color(250, 250, 250));

        PseudoOptions option = new PseudoOptions();
        option.ordre = ordre;

        JTextField optionField = VistaHelpers.createTextField();
        optionField.setText(text);
        optionField.setColumns(20);
        optionField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                if(optionField.getText().trim().isEmpty()) {
                    deleteOpt(q, option, row);
                    return;
                }
                cp.modificarOpcioResposta(idEnquesta, q.ordre, option.ordre, optionField.getText());
            }
        });

        option.optionField = optionField;
        q.optionFields.add(option);

        if (isAdd) {
            cp.afegirOpcioResposta(idEnquesta, q.ordre, text);
        }

        JButton deleteOptionButton = new JButton("\uD83D\uDDD1");
        deleteOptionButton.setToolTipText("Esborrar opció");

        VistaHelpers.styleButton(deleteOptionButton, new Color(200, 100, 100));
        deleteOptionButton.addActionListener(e -> deleteOpt(q, option, row));

        row.add(new JLabel("Opció:"));
        row.add(optionField);
        row.add(deleteOptionButton);

        q.optionsPanel.add(row);
        q.optionsPanel.revalidate();
        q.optionsPanel.repaint();
    }

    private void deleteOpt(QuestionUI q, PseudoOptions option, JPanel row) {
        int deletedOrdre = option.ordre;
        q.optionsPanel.remove(row);
        q.optionFields.remove(option);
        q.optionsPanel.revalidate();
        q.optionsPanel.repaint();
        cp.esborrarOpcioResposta(idEnquesta, q.ordre, deletedOrdre);
        // Recalculate ordre for remaining options
        recalcularOrdreOpcions(q);
    }

    private void actualitzarOpcionsVisible(QuestionUI q) {
        boolean visible = (q.tipus == PseudoTipusPregunta.UNICA_NO_ORDENADA
                || q.tipus == PseudoTipusPregunta.UNICA_ORDENADA
                || q.tipus == PseudoTipusPregunta.MULTIPLE);
        q.optionsPanel.setVisible(visible);
        q.addOptionButton.setVisible(visible);
        q.rootPanel.revalidate();
        q.rootPanel.repaint();
    }

    private void eliminarPregunta(QuestionUI q) {
        int deletedOrdre = q.ordre;
        questionsContainer.remove(q.rootPanel);
        questions.remove(q);
        questionsContainer.revalidate();
        questionsContainer.repaint();
        cp.eliminarPregunta(idEnquesta, deletedOrdre);
        // Recalculate ordre for remaining questions
        recalcularOrdrePreguntes();
        preguntesNum--;
    }

    private void recalcularOrdreOpcions(QuestionUI q) {
        // Update the ordre of each option to be sequential (0, 1, 2, ...)
        for (int i = 0; i < q.optionFields.size(); i++) {
            q.optionFields.get(i).ordre = i;
        }
    }

    private void recalcularOrdrePreguntes() {
        // Update the ordre of each question to be sequential (0, 1, 2, ...)
        // Also update the UI labels to reflect new numbering
        for (int i = 0; i < questions.size(); i++) {
            QuestionUI q = questions.get(i);
            q.ordre = i;
            Component north = ((BorderLayout) q.rootPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
            if (north instanceof JPanel topRow) {
                Component firstComponent = topRow.getComponent(0);
                if (firstComponent instanceof JLabel label) {
                    label.setText("P" + (i + 1) + ":");
                }
            }
        }
    }

    private void onCancel(ActionEvent e) {
        cp.cancelarEdicioEnquesta(idEnquesta);
        frame.dispose();
    }

    private void onSave(ActionEvent e) {
        try {
            // check if the title is not empty
            if(titleField.getText().trim().isEmpty()) {
                return;
            }

            // check if there are any questions without options that are required to have them
            for (QuestionUI q : questions) {
                if ((q.tipus == PseudoTipusPregunta.UNICA_NO_ORDENADA
                        || q.tipus == PseudoTipusPregunta.UNICA_ORDENADA
                        || q.tipus == PseudoTipusPregunta.MULTIPLE)
                        && q.optionFields.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "La pregunta \"" + q.textField.getText() + "\" requereix almenys una opció.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // check that there are no empty questions or options
            for (QuestionUI q : questions) {
                if (q.textField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Hi ha una pregunta sense text. Si us plau, completa-la o elimina-la.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for (PseudoOptions op : q.optionFields) {
                    if (op.optionField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame,
                                "Hi ha una opció sense text a la pregunta \"" + q.textField.getText() + "\". Si us plau, completa-la o elimina-la.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }


            // open confirmation dialog
            int result = JOptionPane.showConfirmDialog(frame,
                    "Estàs a punt de guardar els canvis. Perdràs totes les respostes associades, si n'hi han. Vols Continuar?",
                    "Avís",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result != JOptionPane.OK_OPTION) return;

            cp.guardarEdicioEnquesta(idEnquesta);
            JOptionPane.showMessageDialog(frame, "Enquesta desada correctament.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            vistaPrincipal.actualitzarEstatSessio();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error en desar l'enquesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
