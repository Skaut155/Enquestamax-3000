package Domain.Factories;

import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;

import java.lang.Deprecated;

/**
 * Abstract factory for creating controller instances.
 * Implements the Singleton pattern to ensure a single factory instance.
 * By default, uses NoDBCtrlFactory for in-memory storage.
 */
public abstract class CtrlFactory {
    /**
     * Singleton instance of the factory.
     */
    private static CtrlFactory instance;

    /**
     * Returns the singleton instance of the factory.
     * If no instance exists, creates a new NoDBCtrlFactory.
     *
     * @return the singleton CtrlFactory instance
     */
    public static CtrlFactory getInstance(){
        if(instance == null){
            instance = new LocalPersistenceCtrlFactory();
        }

        return instance;
    }

    /**
     * Sets the factory instance.
     * This method is deprecated and should not be used in production code.
     *
     * @param factory the CtrlFactory instance to set
     * @deprecated Use getInstance() instead
     */
    @Deprecated
    public static void setInstance(CtrlFactory factory){
        instance = factory;
    }

    /**
     * Creates and returns an Admin controller instance.
     *
     * @return an implementation of IAdminCtrl
     */
    public abstract IAdminCtrl GetAdminCtrl();

    /**
     * Creates and returns an Enquesta controller instance.
     *
     * @return an implementation of IEnquestaCtrl
     */
    public abstract IEnquestaCtrl GetEnquestaCtrl();
}
