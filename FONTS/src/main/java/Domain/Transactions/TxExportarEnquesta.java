package Domain.Transactions;

import Domain.Adapters.IEnquestaPersistance;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.AdapterFactory;
import Domain.Factories.CtrlFactory;

import Domain.Model.Enquesta;

/**
 * Transaction class to export a survey (enquesta) to a JSON file and move it to a specified path.
 */
public class TxExportarEnquesta implements Transaction {
    private final int idEnquesta;
    public String finalPath;

    public TxExportarEnquesta(int idEnquesta, String finalPath) {
        this.idEnquesta = idEnquesta;
        this.finalPath = finalPath;
    }

    public void execute(){
        IEnquestaPersistance enquestaPersistance = AdapterFactory.getInstance().getEnquestaPersistance();
        IEnquestaCtrl enquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        // get enquestea instsance from controller
        CtrlFactory ctrlFactory = CtrlFactory.getInstance();
        Enquesta e = enquestaCtrl.GetEnquesta(idEnquesta);
        String og_path = enquestaPersistance.getPathOfEnquesta(e.getId());
        // copy fucking file using OS
        try {
            java.nio.file.Files.copy(java.nio.file.Paths.get(og_path),
                    java.nio.file.Paths.get(finalPath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (java.io.IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

}
