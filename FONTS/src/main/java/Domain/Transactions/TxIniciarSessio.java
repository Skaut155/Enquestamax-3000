package Domain.Transactions;

import Domain.Controllers.IAdminCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Admin;
import Domain.Exceptions.AdminNoExisteix;
import Domain.Exceptions.ContrasenyaIncorrecta;

public class TxIniciarSessio implements  Transaction {
    public String nomUsuari;
    public String contrasenya;

    public TxIniciarSessio(String nomUsuari, String contrasenya) {
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
    }

    public void execute() throws ContrasenyaIncorrecta, AdminNoExisteix {
        IAdminCtrl adminCtrl = CtrlFactory.getInstance().GetAdminCtrl();
        Admin admin = adminCtrl.GetAdmin(nomUsuari);

        if (!admin.iniciarSessio(contrasenya)) {
            throw new ContrasenyaIncorrecta("Contrasenya incorrecta per a l'usuari: " + nomUsuari);
        }
    }
}
