package Domain.Transactions;

import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Admin;
import Domain.Model.Enquesta;
import Domain.Exceptions.AdminNoExisteix;
import Domain.Exceptions.EnquestaJaExisteix;
import Domain.Exceptions.EnquestaNoExisteix;

public class TxCrearEnquesta implements Transaction{
    private String nomE;
    public int result;

    public TxCrearEnquesta(String nomE) {
        this.nomE = nomE;
    }

    public void execute() throws EnquestaNoExisteix
    {
        CtrlFactory f = CtrlFactory.getInstance();
        IEnquestaCtrl ECtrl = f.GetEnquestaCtrl();

        Enquesta e = new Enquesta(nomE);

        ECtrl.AddEnquesta(e);

        result = e.getId();
    }
}
