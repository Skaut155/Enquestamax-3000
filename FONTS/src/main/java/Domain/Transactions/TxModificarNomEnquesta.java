package Domain.Transactions;

import Domain.Factories.CtrlFactory;

public class TxModificarNomEnquesta implements Transaction {
    private int enquestaId;
    private String text;

    public TxModificarNomEnquesta(int enquestaId, String text) {
        this.enquestaId = enquestaId;
        this.text = text;
    }

    @Override
    public void execute() {
        CtrlFactory.getInstance().GetEnquestaCtrl().GetEnquesta(enquestaId).setNom(text);
    }
}
