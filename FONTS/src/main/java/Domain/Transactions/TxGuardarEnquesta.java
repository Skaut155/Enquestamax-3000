package Domain.Transactions;

import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Admin;
import Domain.Model.Enquesta;

/**
 * Class representing the transaction to save an Enquesta to persistence.
 * The id of the new Enquesta is stored in the result attribute
 */
public class TxGuardarEnquesta implements Transaction {
    private int idEnquesta;
    private String nomAdmin;

    public TxGuardarEnquesta(int idEnquesta, String nomAdmin) {
        this.nomAdmin = nomAdmin;
        this.idEnquesta = idEnquesta;
    }

    public void execute() {
        IEnquestaCtrl enquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        IAdminCtrl ac = CtrlFactory.getInstance().GetAdminCtrl();
        Enquesta enquesta = enquestaCtrl.GetEnquesta(this.idEnquesta);
        Admin a = ac.GetAdmin(nomAdmin);

        enquesta.esborrarRespostes();

        if (!a.getEnquestesIds().contains(enquesta.getId()))
        {
            a.addEnquesta(enquesta.getId());
        }

        ac.updateAdmin(a);

        enquestaCtrl.updateEnquesta(this.idEnquesta);
    }
}
