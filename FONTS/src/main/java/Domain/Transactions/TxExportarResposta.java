package Domain.Transactions;

import Domain.Adapters.IRespostesIO;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.AdapterFactory;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.RespostaEnquesta;

public class TxExportarResposta implements Transaction {
    private int idEnquesta;
    private int idResposta;
    private String rutaFitxer;

    public TxExportarResposta(int idEnquesta, String rutaFitxer, int idResposta) {
        this.idEnquesta = idEnquesta;
        this.rutaFitxer = rutaFitxer;
        this.idResposta = idResposta;
    }

    public void execute() {
        IEnquestaCtrl ec = CtrlFactory.getInstance().GetEnquestaCtrl();
        IRespostesIO rIO = AdapterFactory.getInstance().getRespostesIO();

        Enquesta e = ec.GetEnquesta(idEnquesta);
        RespostaEnquesta re = e.getResposta(idResposta);

        rIO.exportResposta(re, rutaFitxer);
    }
}
