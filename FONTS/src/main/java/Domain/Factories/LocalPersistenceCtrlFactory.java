package Domain.Factories;

import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import Data.Controllers.LocalPersistenceCtrl.AdminCtrlLocalPersistence;
import Data.Controllers.LocalPersistenceCtrl.EnquestaCtrlLocalPersistence;

/*
 * Concrete factory implementation for creating persistence controller instances.
 * This factory creates AdminCtrlLocalPersistence and EnquestaCtrlLocalPersistence instances that store data
 * locally and persistently.
 * Follows the Singleton pattern through the parent CtrlFactory class.
 */
public class LocalPersistenceCtrlFactory extends CtrlFactory{

    /**
     * Singleton instance of the Admin controller for in-memory storage.
     */
    private final IAdminCtrl adminCtrl;

    /**
     * Singleton instance of the Enquesta controller for in-memory storage.
     */
    private final IEnquestaCtrl enquestaCtrl;

    /**
     * Constructs a new NoDBCtrlFactory and initializes the in-memory controllers.
     * Creates instances of AdminCtrlNoDB and EnquestaCtrlNoDB.
     */
    public LocalPersistenceCtrlFactory() {
        this.adminCtrl = new AdminCtrlLocalPersistence();
        this.enquestaCtrl = new EnquestaCtrlLocalPersistence();
    }

    /**
     * Returns the Admin controller instance for in-memory operations.
     *
     * @return the AdminCtrlNoDB instance
     */
    @Override
    public IAdminCtrl GetAdminCtrl() {
        return adminCtrl;
    }

    /**
     * Returns the Enquesta controller instance for in-memory operations.
     *
     * @return the EnquestaCtrlNoDB instance
     */
    @Override
    public IEnquestaCtrl GetEnquestaCtrl() {
        return enquestaCtrl;
    }
}
