package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;
import Domain.Exceptions.OrdreIncorrecte;

public class TxAfegirPregunta implements Transaction {
    private int idEnquesta;

    public TxAfegirPregunta(int idEnquesta){
        this.idEnquesta = idEnquesta;
    }

    public void execute() throws OrdreIncorrecte {
        IEnquestaCtrl IECtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = IECtrl.GetEnquesta(idEnquesta);
        Pregunta p = new Pregunta();
        e.afegirPregunta(p);
    }
}
