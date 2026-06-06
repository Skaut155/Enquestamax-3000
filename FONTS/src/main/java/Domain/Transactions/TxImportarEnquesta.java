package Domain.Transactions;

import Domain.Adapters.IEnquestaPersistance;
import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Exceptions.ArxiuCorrupte;
import Domain.Factories.AdapterFactory;
import Domain.Factories.CtrlFactory;
import Domain.Model.Admin;
import Domain.Model.Enquesta;

public class TxImportarEnquesta implements Transaction {
    public String pathToFile;
    public String nomAdmin;

    public TxImportarEnquesta(String pathToFile, String nomAdmin) {
        this.pathToFile = pathToFile;
        this.nomAdmin = nomAdmin;
    }

    @Override
    public void execute() {
        IEnquestaPersistance enquestaPersistance = AdapterFactory.getInstance().getEnquestaPersistance();
        Enquesta enquesta;
        try {
            enquesta = enquestaPersistance.loadEnquestaByPath(pathToFile);
            enquesta.toString();
        } catch (RuntimeException e) {
            throw new ArxiuCorrupte("L'enquesta que s'està intentant importar està corrupta o li falten camps vitals.");
        }
        IEnquestaCtrl enquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        enquestaCtrl.AddEnquesta(enquesta);
        enquestaCtrl.updateEnquesta(enquesta.getId());
        IAdminCtrl adminCtrl = CtrlFactory.getInstance().GetAdminCtrl();
        Admin admin = adminCtrl.GetAdmin(nomAdmin);
        admin.addEnquesta(enquesta.getId());
        adminCtrl.updateAdmin(admin);
    }
}