package Presentation.Views;

import Presentation.Controllers.CtrlPresentation;

public class Main {
    public static void main (String[] args) {
        javax.swing.SwingUtilities.invokeLater (() -> {
            CtrlPresentation ctrlPresentacion = new CtrlPresentation();
            ctrlPresentacion.inicializarPresentacion();
        });
    }
}