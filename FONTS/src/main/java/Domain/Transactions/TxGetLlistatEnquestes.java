package Domain.Transactions;

import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Admin;
import Domain.Model.Enquesta;
import Domain.Exceptions.AdminNoExisteix;

import java.util.ArrayList;

public class TxGetLlistatEnquestes implements Transaction{
    private String nomAdmin;
    public String result;

    public TxGetLlistatEnquestes(String nomAdmin) {
        this.nomAdmin = nomAdmin;
        result = "";
    }

    public void execute() throws AdminNoExisteix
    {
        IAdminCtrl adminCtrl = CtrlFactory.getInstance().GetAdminCtrl();
        IEnquestaCtrl enquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Admin admin = adminCtrl.GetAdmin(nomAdmin);

        ArrayList<Integer> ids = admin.getEnquestesIds();

        for (Integer i : ids) {
            Enquesta e = enquestaCtrl.GetEnquesta(i);

            result +=("ID: " + e.getId() + ", Nom: " + e.getNom() + "\n");
        }
    }
}
