package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Exceptions.EnquestaNoExisteix;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;
import Domain.Model.RespostaEnquesta;

import java.util.ArrayList;

public class TxConsultaRespostaAEnquesta implements Transaction {
    private int idEnquesta;
    private int idResposta;
    public String result;

    public TxConsultaRespostaAEnquesta(int id, int idResposta) {
        this.idEnquesta = id;
        this.idResposta = idResposta;
        this.result = "";
    }

    public void execute()
    {
        IEnquestaCtrl enq_ctrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta enq = enq_ctrl.GetEnquesta(idEnquesta);
        RespostaEnquesta re = enq.getResposta(idResposta);

        String[] respostes = re.getRespostesString();

        for (int i = 0; i < enq.numPreguntes(); ++i) {
            Pregunta p = enq.getPregunta(i);

            result += p.toString();
            result += "Resposta: " + respostes[i] + "\n";
        }
    }
}
