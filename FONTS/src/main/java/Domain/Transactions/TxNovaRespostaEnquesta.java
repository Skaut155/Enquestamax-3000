package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Exceptions.RespostaNoValida;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Model.RespostaAPregunta;

public class TxNovaRespostaEnquesta implements Transaction{
    private int idEnquesta;
    private String[] respostes;
    public int result;

    public TxNovaRespostaEnquesta(int idEnquesta, String[] respostes) {
        this.idEnquesta = idEnquesta;
        this.respostes = respostes;
    }

    public void execute()
    {
        IEnquestaCtrl ECtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = ECtrl.GetEnquesta(idEnquesta);

        int idResposta = e.afegirNovaResposta();
        int nPreguntes = e.numPreguntes();

        for (int i = 0; i < nPreguntes; ++i)
        {
            String resposta = respostes[i];
            RespostaAPregunta<?> rap = null;
            try{
                rap = e.getPregunta(i).generaResposta(resposta);
            }
            catch (RespostaNoValida ex){
                e.eliminaResposta(idResposta);
                throw new RespostaNoValida("Resposta no vàlida per la pregunta " + i);
            }
            finally {
                e.getResposta(idResposta).addRespostaAPregunta(rap);
            }
        }

        ECtrl.updateEnquesta(idEnquesta);
    }
}
