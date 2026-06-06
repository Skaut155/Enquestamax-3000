package Domain.Controllers;

import Domain.Model.Enquesta;
import Domain.Exceptions.EnquestaJaExisteix;
import Domain.Exceptions.EnquestaNoExisteix;

/**
 * Interface defining operations for managing Enquesta instances.
 * Implementations of this interface handle the persistence, retrieval, and deletion of Enquesta objects.
 */
public interface IEnquestaCtrl {
    /**
     * Retrieves an Enquesta instance by its ID.
     *
     * @param id the ID of the Enquesta to retrieve
     * @return the Enquesta instance with the specified ID
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    Enquesta GetEnquesta(int id) throws EnquestaNoExisteix;

    /**
     * Adds a new Enquesta instance to the system.
     *
     * @param enquesta the Enquesta instance to add
     * @throws EnquestaJaExisteix if an Enquesta with the same ID already exists
     */
    void AddEnquesta(Enquesta enquesta) throws EnquestaJaExisteix;

    /**
     * Deletes an Enquesta instance from the system.
     *
     * @param id the ID of the Enquesta to delete
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    void DeleteEnquesta(int id) throws EnquestaNoExisteix;

    /**
     * Updates an existing Enquesta instance from the system to persistence.
     *
     * @param idEnquesta the ID of the Enquesta to update
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    void updateEnquesta(int idEnquesta) throws EnquestaNoExisteix;

    /**
     * Reloads an existing Enquesta instance from persistence to the system.
     *
     * @param idEnquesta the ID of the Enquesta to reload
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    void reloadEnquesta(int idEnquesta) throws EnquestaNoExisteix;
}
