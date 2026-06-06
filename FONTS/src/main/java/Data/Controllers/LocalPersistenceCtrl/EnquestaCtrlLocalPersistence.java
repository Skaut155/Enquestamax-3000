package Data.Controllers.LocalPersistenceCtrl;

import Domain.Adapters.IEnquestaPersistance;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Exceptions.EnquestaJaExisteix;
import Domain.Exceptions.EnquestaNoExisteix;
import Domain.Factories.AdapterFactory;
import Domain.Model.Enquesta;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * In-memory implementation of the Enquesta controller without database persistence.
 * Manages Enquesta instances using a Dictionary data structure.
 */
public class EnquestaCtrlLocalPersistence implements IEnquestaCtrl {

    /**
     * Dictionary storing Enquesta instances indexed by their ID.
     */
    private Dictionary<Integer, Enquesta> enquestes = new Hashtable<Integer, Enquesta>();

    private int lastId = 0;

    public EnquestaCtrlLocalPersistence() {
        IEnquestaPersistance ep = AdapterFactory.getInstance().getEnquestaPersistance();
        lastId = ep.getLastId() + 1;
    }

    /**
     * Retrieves an Enquesta instance by its ID.
     *
     * @param id the ID of the Enquesta to retrieve
     * @return the Enquesta instance with the specified ID
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    @Override
    public Enquesta GetEnquesta(int id) {

        Enquesta e;
        if ((e = enquestes.get(id)) != null) {
            return e;
        }
        else{
            IEnquestaPersistance ep = AdapterFactory.getInstance().getEnquestaPersistance();
            if (ep.existsEnquesta(id)) {
                Enquesta enquestaFromPersistence = ep.loadEnquesta(id);
                enquestes.put(id, enquestaFromPersistence);
                return enquestaFromPersistence;
            }
        }

        throw new EnquestaNoExisteix("No existeix una instància de Enquesta amb id " + id);
    }

    /**
     * Adds a new Enquesta instance to the system.
     *
     * @param new_enquesta the Enquesta instance to add
     * @throws EnquestaNoExisteix if an Enquesta with the same ID already exists
     */
    @Override
    public void AddEnquesta(Enquesta new_enquesta) {

        new_enquesta.setId(lastId++);

        enquestes.put(new_enquesta.getId(), new_enquesta);
    }

    /**
     * Deletes an Enquesta instance from the system.
     *
     * @param id the ID of the Enquesta to delete
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    @Override
    public void DeleteEnquesta(int id) {
        IEnquestaPersistance ep = AdapterFactory.getInstance().getEnquestaPersistance();
        if (!ep.existsEnquesta(id)) {
            throw new EnquestaNoExisteix("No existeix una instància de Enquesta amb id " + id);
        }

        ep.deleteEnquesta(id);
        if (enquestes.get(id) != null) {
            enquestes.remove(id);
        }

    }

    /**
     * Updates an existing Enquesta instance from the system to persistence.
     *
     * @param idEnquesta the id of the Enquesta to update
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    @Override
    public void updateEnquesta(int idEnquesta) {
        if (enquestes.get(idEnquesta) == null) {
            throw new EnquestaNoExisteix("Aquesta instància de Enquesta no és present al controlador");
        }
        IEnquestaPersistance ep = AdapterFactory.getInstance().getEnquestaPersistance();

        ep.saveEnquesta(enquestes.get(idEnquesta));
    }

    /**
     * Reloads an existing Enquesta instance from persistence to the system.
     *
     * @param idEnquesta the id of the Enquesta to reload
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    @Override
    public void reloadEnquesta(int idEnquesta) {
        if (enquestes.get(idEnquesta) == null) {
            throw new EnquestaNoExisteix("No existeix una instància de Enquesta amb id " + idEnquesta);
        }

        IEnquestaPersistance ep = AdapterFactory.getInstance().getEnquestaPersistance();
        if (!ep.existsEnquesta(idEnquesta)) {
            throw new EnquestaNoExisteix("No existeix una instància de Enquesta amb id " + idEnquesta);
        }
        Enquesta enquestaFromPersistence = ep.loadEnquesta(idEnquesta);
        enquestes.remove(idEnquesta);
        enquestes.put(idEnquesta, enquestaFromPersistence);
    }
}
