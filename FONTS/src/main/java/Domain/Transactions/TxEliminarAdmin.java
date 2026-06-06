package Domain.Transactions;

import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Admin;

public class TxEliminarAdmin implements Transaction {
    private String nomAdmin;

    public TxEliminarAdmin(String nomAdmin) {
        this.nomAdmin = nomAdmin;
    }

    public void execute() {
        IEnquestaCtrl ec = CtrlFactory.getInstance().GetEnquestaCtrl();
        IAdminCtrl ac = CtrlFactory.getInstance().GetAdminCtrl();

        Admin a = ac.GetAdmin(nomAdmin);

        var ids = a.getEnquestesIds();
        for (Integer id : ids) {
            ec.DeleteEnquesta(id);
        }

        ac.deleteAdmin(nomAdmin);
    }

}
