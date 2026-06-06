package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;
import Domain.Exceptions.EnquestaNoExisteix;
import Domain.Exceptions.OpcioNoExisteix;
import Domain.Exceptions.PreguntaNoExisteix;

public class TxModificarOpcioResposta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;
    private int ordreOpcio;
    private String textOpcio;

    public TxModificarOpcioResposta(int idEnquesta, int ordrePregunta, int ordreOpcio, String textOpcio) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
        this.ordreOpcio = ordreOpcio;
        this.textOpcio = textOpcio;
    }

    public void execute() throws EnquestaNoExisteix, PreguntaNoExisteix, OpcioNoExisteix {
        IEnquestaCtrl iectrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iectrl.GetEnquesta(idEnquesta);
        Pregunta p = e.getPregunta(ordrePregunta);
        p.modificarOpcio(ordreOpcio, textOpcio);
    }
}
