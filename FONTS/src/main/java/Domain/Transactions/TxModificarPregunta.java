package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;
import Domain.Exceptions.EnquestaNoExisteix;
import Domain.Exceptions.PreguntaNoExisteix;

public class TxModificarPregunta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;
    private String nouText;

    public TxModificarPregunta(int idEnquesta, int ordrePregunta, String nouText) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
        this.nouText = nouText;
    }

    public void execute() throws EnquestaNoExisteix, PreguntaNoExisteix, IllegalArgumentException {
        IEnquestaCtrl iectrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iectrl.GetEnquesta(idEnquesta);
        Pregunta p = e.getPregunta(ordrePregunta);
        p.setTitol(nouText);
    }
}
