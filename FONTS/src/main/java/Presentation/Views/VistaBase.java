package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;
import javax.swing.*;

/**
 * Classe base abstracta per a totes les vistes de l'aplicació.
 * Proporciona funcionalitat comuna per gestionar finestres JFrame.
 */
public abstract class VistaBase {
    /** Finestra principal de la vista */
    protected final JFrame frame;
    /** Controlador de presentació (pot ser null per a vistes que no el necessiten) */
    protected final CtrlPresentation iCtrlPresentation;

    /**
     * Constructor de la classe base amb controlador de presentació.
     *
     * @param title Títol de la finestra
     * @param ctrl Controlador de presentació
     */
    public VistaBase(String title, CtrlPresentation ctrl) {
        this.frame = new JFrame(title);
        this.iCtrlPresentation = ctrl;
        configureFrame();
    }

    /**
     * Constructor de la classe base sense controlador de presentació.
     * Usat per vistes que no necessiten accés al controlador (ex: VistaFileChooser).
     *
     * @param title Títol de la finestra
     */
    public VistaBase(String title) {
        this(title, null);
    }

    /**
     * Fa visible la finestra.
     */
    public void hacerVisible() {
        frame.setVisible(true);
    }

    /**
     * Comprova si la finestra és visible.
     *
     * @return true si la finestra és visible, false altrament
     */
    public boolean isVisible() {
        return frame.isVisible();
    }

    /**
     * Porta la finestra al primer pla.
     */
    public void toFront() {
        frame.toFront();
        frame.setState(JFrame.NORMAL);
    }

    /**
     * Sol·licita el focus per a la finestra.
     */
    public void requestFocus() {
        frame.requestFocus();
    }

    /**
     * Tanca i allibera els recursos de la finestra.
     */
    public void dispose() {
        frame.dispose();
    }

    /**
     * Configura les propietats bàsiques de la finestra.
     */
    private void configureFrame() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }
}

