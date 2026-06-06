package Domain.Factories;

import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import Data.Controllers.NoDBCtrl.AdminCtrlNoDB;
import Data.Controllers.NoDBCtrl.EnquestaCtrlNoDB;

/**
 * Concrete factory implementation for creating in-memory (no database) controller instances.
 * This factory creates AdminCtrlNoDB and EnquestaCtrlNoDB instances that store data
 * in memory without any database persistence.
 * Follows the Singleton pattern through the parent CtrlFactory class.
 */
public class NoDBCtrlFactory extends CtrlFactory{

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
    public NoDBCtrlFactory() {
        this.adminCtrl = new AdminCtrlNoDB();
        this.enquestaCtrl = new EnquestaCtrlNoDB();
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
