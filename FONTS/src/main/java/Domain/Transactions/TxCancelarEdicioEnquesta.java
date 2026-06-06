package Domain.Transactions;

import Domain.Adapters.IEnquestaPersistance;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.AdapterFactory;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;

/**
 * Class representing the transaction to cancel the edition of an Enquesta.
 * The id of the new Enquesta is stored in the result attribute
 */
public class TxCancelarEdicioEnquesta implements Transaction {
    private int idEnquesta;

    public TxCancelarEdicioEnquesta(int idEnquesta) {
        this.idEnquesta = idEnquesta;
    }

    public void execute() {
        IEnquestaCtrl enquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        IEnquestaPersistance ep = AdapterFactory.getInstance().getEnquestaPersistance();
        if (ep.existsEnquesta(idEnquesta)) {
            enquestaCtrl.reloadEnquesta(idEnquesta);
        }
    }
}
