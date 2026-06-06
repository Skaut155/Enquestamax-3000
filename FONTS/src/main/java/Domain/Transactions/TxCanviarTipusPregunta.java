package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.*;

public class TxCanviarTipusPregunta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;
    private String nouTipus;

    public TxCanviarTipusPregunta(int idEnquesta, int ordrePregunta, String nouTipus) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
        this.nouTipus = nouTipus;
    }

    public void execute() throws IllegalArgumentException {
        IEnquestaCtrl iectrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iectrl.GetEnquesta(idEnquesta);
        Pregunta p = e.getPregunta(ordrePregunta);
        TipusPregunta tp = p.getTipusPregunta();
        switch (nouTipus) {
            case "NUMERICA":
                tp.setPreguntaRespostaObertaNumerica(p);
                break;
            case "OBERTA":
                tp.setPreguntaRespostaOberta(p);
                break;
            case "MULTIPLE":
                tp.setPreguntaSeleccioMultiple(p);
                break;
            case "UNICA_NO_ORDENADA":
                tp.setPreguntaSeleccioUnicaNoOrdenada(p);
                break;
            case "UNICA_ORDENADA":
                tp.setPreguntaSeleccioUnicaOrdenada(p);
                break;
            default:
                throw new IllegalArgumentException("Tipus de pregunta desconegut: " + nouTipus);
        }
    }
}

