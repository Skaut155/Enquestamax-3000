package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Exceptions.EnquestaNoExisteix;
import Domain.Exceptions.PreguntaNoExisteix;

public class TxEliminarPregunta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;

    public TxEliminarPregunta(int idEnquesta, int ordrePregunta) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
    }

    public void execute() throws EnquestaNoExisteix, PreguntaNoExisteix {
        IEnquestaCtrl iectrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iectrl.GetEnquesta(idEnquesta);
        e.eliminarPregunta(ordrePregunta);
    }
}
