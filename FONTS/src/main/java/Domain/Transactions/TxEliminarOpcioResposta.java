package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;
import Domain.Exceptions.EnquestaNoExisteix;
import Domain.Exceptions.OpcioNoExisteix;
import Domain.Exceptions.PreguntaNoExisteix;

public class TxEliminarOpcioResposta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;
    private int ordreOpcio;

    public TxEliminarOpcioResposta(int idEnquesta, int ordrePregunta, int ordreOpcio) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
        this.ordreOpcio = ordreOpcio;
    }

    public void execute() throws EnquestaNoExisteix, PreguntaNoExisteix, OpcioNoExisteix, UnsupportedOperationException {
        IEnquestaCtrl iectrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iectrl.GetEnquesta(idEnquesta);
        Pregunta p = e.getPregunta(ordrePregunta);
        p.eliminarOpcio(ordreOpcio);
    }
}
