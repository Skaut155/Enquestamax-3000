package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;

public class TxGetObligatorietatPregunta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;
    public boolean result;

    public TxGetObligatorietatPregunta(int idEnquesta, int ordrePregunta) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
    }

    public void execute() {
        IEnquestaCtrl iEnquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iEnquestaCtrl.GetEnquesta(idEnquesta);
        Pregunta p = e.getPregunta(ordrePregunta);
        result = !p.isOptional();
    }
}
