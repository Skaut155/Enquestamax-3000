package Data.Controllers.NoDBCtrl;

import Domain.Controllers.IEnquestaCtrl;

import Domain.Exceptions.EnquestaJaExisteix;
import Domain.Model.Enquesta;
import Domain.Exceptions.EnquestaNoExisteix;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * In-memory implementation of the Enquesta controller without database persistence.
 * Manages Enquesta instances using a Dictionary data structure.
 */
public class EnquestaCtrlNoDB implements IEnquestaCtrl {

    /**
     * Dictionary storing Enquesta instances indexed by their ID.
     */
    private Dictionary<Integer, Enquesta> enquestesMemoria = new Hashtable<Integer, Enquesta>();

    private int lastId = 0;

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

        if ((e = enquestesMemoria.get(id)) != null) {
            return e;
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

        enquestesMemoria.put(new_enquesta.getId(), new_enquesta);
    }

    /**
     * Deletes an Enquesta instance from the system.
     *
     * @param id the ID of the Enquesta to delete
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    @Override
    public void DeleteEnquesta(int id) {
        if (enquestesMemoria.get(id) != null) {
            enquestesMemoria.remove(id);
        }
        else{
            throw new EnquestaNoExisteix("No existeix una instància de Enquesta amb id " + id);
        }
    }

    /**
     * Does nothing because in-memory implementation does not require updating.
     *
     * @param idEnquesta the ID of the Enquesta to update
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    @Override
    public void updateEnquesta(int idEnquesta) {
        if (enquestesMemoria.get(idEnquesta) == null) {
            throw new EnquestaNoExisteix("No existeix una instància de Enquesta amb id " + idEnquesta + " a la memòria");
        }
    }

    /**
     * Does nothing because in-memory implementation does not require reloading.
     *
     * @param idEnquesta the ID of the Enquesta to reload
     * @throws EnquestaNoExisteix if no Enquesta with the given ID exists
     */
    @Override
    public void reloadEnquesta(int idEnquesta) {
        if (enquestesMemoria.get(idEnquesta) == null) {
            throw new EnquestaNoExisteix("No existeix una instància de Enquesta amb id " + idEnquesta + " a la memòria");
        }
    }
}
