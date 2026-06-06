package Domain.Transactions;

import Domain.Adapters.IRespostesIO;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Exceptions.ArxiuCorrupte;
import Domain.Exceptions.RespostaNoValida;
import Domain.Factories.AdapterFactory;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.RespostaEnquesta;

public class TxImportarResposta implements Transaction {
    private int idEnquesta;
    private String rutaFitxer;

    public TxImportarResposta(int idEnquesta, String rutaFitxer) {
        this.idEnquesta = idEnquesta;
        this.rutaFitxer = rutaFitxer;
    }

    public void execute() {
        IEnquestaCtrl ec = CtrlFactory.getInstance().GetEnquestaCtrl();
        IRespostesIO rIO = AdapterFactory.getInstance().getRespostesIO();

        RespostaEnquesta re;

        Enquesta e = ec.GetEnquesta(idEnquesta);

        int idr = -1;

        try {
            re = rIO.importResposta(rutaFitxer);


            var raps_import = re.getRespostes();

            idr = e.afegirNovaResposta();
            RespostaEnquesta res = e.getResposta(idr);

            for (int i = 0; i < raps_import.size(); i++) {
                var rap = raps_import.get(i);
                rap.setRespostaEnquesta(res);
                res.addRespostaAPregunta(rap);
            }

            ec.updateEnquesta(idEnquesta);

            TxConsultaRespostaAEnquesta txConsultaRespostaAEnquesta = new TxConsultaRespostaAEnquesta(e.getId(), res.getId());
            txConsultaRespostaAEnquesta.execute();

        } catch (RuntimeException ex) {

            try{
                e.eliminaResposta(idr);
                ec.updateEnquesta(idEnquesta);
            }
            catch (RuntimeException rnv){
                // No s'ha pogut eliminar la resposta afegida
            }

            throw new ArxiuCorrupte("La resposta que s'està intentant importar està corrupta o no és de la enquesta corresponent.");
        }
    }
}
