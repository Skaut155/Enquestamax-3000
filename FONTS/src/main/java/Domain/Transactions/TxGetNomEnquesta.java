package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;

public class TxGetNomEnquesta implements Transaction {
    private int idEnquesta;
    public String result;

    public TxGetNomEnquesta(int idEnquesta) {
        this.idEnquesta = idEnquesta;
    }

    public void execute() {
        IEnquestaCtrl iEnquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        result = iEnquestaCtrl.GetEnquesta(idEnquesta).getNom();
    }
}
