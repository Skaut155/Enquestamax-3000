package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;

public class TxGetNumPreguntesEnquesta implements Transaction {
    private int idEnquesta;
    public int result;

    public TxGetNumPreguntesEnquesta(int idEnquesta) {
        this.idEnquesta = idEnquesta;
    }

    public void execute() {
        IEnquestaCtrl iEnquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        result = iEnquestaCtrl.GetEnquesta(idEnquesta).numPreguntes();
    }
}
