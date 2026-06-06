package Domain.Transactions;

import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Exceptions.AdminNoTeEnquesta;
import Domain.Factories.CtrlFactory;
import Domain.Exceptions.EnquestaNoExisteix;
import Domain.Model.Admin;

public class TxEliminarEnquesta implements Transaction {
    private Integer id;
    private String nomAdmin;

    public TxEliminarEnquesta(String nomAdmin, Integer id) {
        this.id = id;
        this.nomAdmin = nomAdmin;
    }

    public void execute() throws EnquestaNoExisteix, AdminNoTeEnquesta
    {
        CtrlFactory f = CtrlFactory.getInstance();
        IEnquestaCtrl ECtrl = f.GetEnquestaCtrl();
        IAdminCtrl ADCtrl = f.GetAdminCtrl();


        Admin admin = ADCtrl.GetAdmin(nomAdmin);
        try{
            admin.removeEnquesta(id);
            ADCtrl.updateAdmin(admin);
        }
        catch (AdminNoTeEnquesta e){
            throw new EnquestaNoExisteix("L'enquesta no existeix o no pertany a l'admin.");
        }
        finally{
            ECtrl.DeleteEnquesta(id);
        }
    }
}
