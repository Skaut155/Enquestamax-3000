package Domain.Transactions;

import Domain.Controllers.IAdminCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Admin;

public class TxRegistrarAdmin implements  Transaction {
    public String nomUsuari;
    public String contrasenya;

    public TxRegistrarAdmin(String nomUsuari, String contrasenya) {
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
    }

    public void execute() {
        Admin a = new Admin(nomUsuari, contrasenya);
        IAdminCtrl ACtrl = CtrlFactory.getInstance().GetAdminCtrl();
        ACtrl.AddAdmin(a);
    }
}
