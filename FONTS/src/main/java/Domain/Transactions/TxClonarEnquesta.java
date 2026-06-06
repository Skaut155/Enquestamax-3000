package Domain.Transactions;

import Domain.Adapters.IRespostesIO;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.AdapterFactory;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;

import java.io.File;

/**
 * Class representing the transaction to clone a Enquesta
 * The id of the new Enquesta is stored in the result attribute
 */
public class TxClonarEnquesta implements Transaction {
    private int idEnquestaAClonar;
    private String nomAdmin;
    public int result;

    public TxClonarEnquesta(int idEnquestaAClonar, String nomAdmin) {
        this.idEnquestaAClonar = idEnquestaAClonar;
        this.nomAdmin = nomAdmin;
    }

    public void execute() {

        ensureDirExists("./.data/cache/temp_enquesta_export.json");

        Transaction exportTx = new TxExportarEnquesta(idEnquestaAClonar, "./.data/cache/temp_enquesta_export.json");
        exportTx.execute();

        Transaction importTx = new TxImportarEnquesta("./.data/cache/temp_enquesta_export.json", nomAdmin);
        importTx.execute();
    }

    private void ensureDirExists(String dirPath) {
        File dir = new File(dirPath).getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
