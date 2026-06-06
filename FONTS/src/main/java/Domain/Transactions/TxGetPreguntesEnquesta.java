package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Model.Enquesta;
import Domain.Factories.CtrlFactory;

public class TxGetPreguntesEnquesta implements Transaction{
    private int idEnquesta;
    public String[] result;

    public TxGetPreguntesEnquesta(int idEnquesta) {
        this.idEnquesta = idEnquesta;
    }

    public void execute() {
        IEnquestaCtrl ectrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = ectrl.GetEnquesta(idEnquesta);

        int nPreguntes = e.numPreguntes();
        result = new String[nPreguntes];

        for (int i = 0; i < nPreguntes; i++) {
            result[i] = e.getPregunta(i).toString();
        }
    }
}
