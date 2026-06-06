package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;

public class TxGetCodiTipusPregunta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;
    public String result;

    public TxGetCodiTipusPregunta(int idEnquesta, int ordrePregunta) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
    }

    public void execute() {
        IEnquestaCtrl iEnquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iEnquestaCtrl.GetEnquesta(idEnquesta);
        Pregunta p = e.getPregunta(ordrePregunta);
        result = p.getTipusPregunta().getCode();
    }
}
