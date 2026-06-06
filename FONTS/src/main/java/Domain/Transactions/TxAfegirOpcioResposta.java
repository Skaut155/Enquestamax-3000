package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;
import Domain.Exceptions.EnquestaNoExisteix;
import Domain.Exceptions.OpcioNoExisteix;
import Domain.Exceptions.PreguntaNoExisteix;

public class TxAfegirOpcioResposta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;
    private String textOpcio;

    public TxAfegirOpcioResposta(int idEnquesta, int ordrePregunta, String textOpcio) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
        this.textOpcio = textOpcio;
    }

    public void execute() throws OpcioNoExisteix, PreguntaNoExisteix, EnquestaNoExisteix {
        IEnquestaCtrl iectrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iectrl.GetEnquesta(idEnquesta);
        Pregunta p = e.getPregunta(ordrePregunta);
        p.afegirOpcio(textOpcio);
    }
}
