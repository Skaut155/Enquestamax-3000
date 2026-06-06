package Domain.Factories;

import Data.Persistance.AdminPersistance;
import Data.Persistance.EnquestaPersistance;
import Domain.Adapters.IAdminPersistance;
import Domain.Adapters.IEnquestaPersistance;
import Domain.Adapters.IRespostesIO;
import Data.Persistance.RespostesIO;

/**
 * Factory class to provide singleton instances of persistence adapters.
 */
public class AdapterFactory {

    /**
     * Singleton instance of the AdapterFactory.
     */
    private static AdapterFactory instance;

    /**
     * Persistence adapters for admin functionalities.
     */
    private IAdminPersistance adminPersistance;
    /**
     * Persistence adapter for surveys (enquestes).
     */
    private IEnquestaPersistance enquestaPersistance;

    /**
     * Persistence adapter for responses (respostes).
     */
    private IRespostesIO respostesIO;

    /**
     * Private constructor to prevent external instantiation.
     * @return The singleton instance of the AdapterFactory.
     */
    public static AdapterFactory getInstance() {
        if (instance == null) {
            instance = new AdapterFactory();
        }

        return instance;
    }

    /**
     * Get the admin persistence adapter.
     * @return The admin persistence adapter.
     */
    public IAdminPersistance getAdminPersistance() {
        if(adminPersistance == null) {
            adminPersistance = new AdminPersistance();
        }
        return adminPersistance;
    }

    /**
     * Get the survey (enquesta) persistence adapter.
     * @return The survey persistence adapter.
     */
    public IEnquestaPersistance getEnquestaPersistance() {
        if(enquestaPersistance == null) {
            enquestaPersistance = new EnquestaPersistance();
        }
        return enquestaPersistance;
    }

    /**
     * Get the responses (respostes) IO adapter.
     * @return The responses IO adapter.
     */
    public IRespostesIO getRespostesIO(){
        if(respostesIO == null){
            respostesIO = new RespostesIO();
        }
        return respostesIO;
    }
}
