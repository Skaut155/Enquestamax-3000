package Domain.Transactions;

import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Exceptions.EnquestaNoExisteix;

import java.util.ArrayList;

public class TxGetRespostesEnquesta implements Transaction {
    private int id;
    public ArrayList<Integer> result;

    public TxGetRespostesEnquesta(int id) {
        this.id = id;
    }

    public void execute() throws EnquestaNoExisteix
    {
        IEnquestaCtrl enq_ctrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta enq = enq_ctrl.GetEnquesta(id);

        result = enq.getIdsRespostes();
    }
}
