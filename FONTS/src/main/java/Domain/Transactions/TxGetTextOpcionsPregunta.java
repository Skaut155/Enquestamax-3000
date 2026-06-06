package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;

public class TxGetTextOpcionsPregunta implements Transaction {
    private int idEnquesta;
    private int ordrePregunta;
    public String[] result;

    public TxGetTextOpcionsPregunta(int idEnquesta, int ordrePregunta) {
        this.idEnquesta = idEnquesta;
        this.ordrePregunta = ordrePregunta;
    }

    public void execute() {
        IEnquestaCtrl iEnquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = iEnquestaCtrl.GetEnquesta(idEnquesta);
        Pregunta p = e.getPregunta(ordrePregunta);

        p.getTipusPregunta().checkOpcions();

        var ops = p.getOpcions();
        result = new String[ops.size()];

        for (int i = 0; i < ops.size(); i++) {
            result[i] = ops.get(i).getText();
        }
    }
}
