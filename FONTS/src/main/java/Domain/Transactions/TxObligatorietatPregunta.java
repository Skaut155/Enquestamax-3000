package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;
import Domain.Exceptions.EnquestaNoExisteix;
import Domain.Exceptions.PreguntaNoExisteix;

public class TxObligatorietatPregunta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;
    private boolean isObligatoria;

    public TxObligatorietatPregunta(int idEnquesta, int ordrePregunta, boolean obligatoria) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
        this.isObligatoria = obligatoria;
    }

    public void execute() throws EnquestaNoExisteix, PreguntaNoExisteix {
        IEnquestaCtrl iEnquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iEnquestaCtrl.GetEnquesta(idEnquesta);
        Pregunta p = e.getPregunta(ordrePregunta);
        p.setObligatoria(isObligatoria);
    }
}
