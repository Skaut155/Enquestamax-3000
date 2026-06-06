package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Exceptions.EnquestaNoExisteix;

public class TxMostrarEnquesta implements Transaction{
    private int idEnquesta;
    public String result;

    public TxMostrarEnquesta(int idEnquesta) {
        this.idEnquesta = idEnquesta;
    }

    public void execute() throws EnquestaNoExisteix
    {
        CtrlFactory f = CtrlFactory.getInstance();
        IEnquestaCtrl ECtrl = f.GetEnquestaCtrl();
        Enquesta e = ECtrl.GetEnquesta(idEnquesta);

        result = e.toString();
    }
}
