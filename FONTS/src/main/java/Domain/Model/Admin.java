package Domain.Model;

import Domain.Exceptions.AdminJaTeEnquesta;
import Domain.Exceptions.AdminNoTeEnquesta;
import Domain.Exceptions.EnquestaJaExisteix;
import Domain.Exceptions.EnquestaNoExisteix;

import java.util.ArrayList;

/**
 * Represents an administrator who manages a set of survey IDs ("enquestes").
 * The Admin holds its name, password and a list of managed survey ids.
 */
public class Admin {
    /**
     * Admin name / identifier.
     */
    private String nom;

    /**
     * Admin password used for simple authentication.
     */
    private String contrasenya;

    /**
     * List of IDs of surveys the admin manages.
     * Note: this reference is returned directly by getEnquestesIds() (modifiable).
     */
    private ArrayList<Integer> enquestes_ids;

    /**
     * Create a new Admin with the given name and password.
     *
     * @param nom the admin's name
     * @param contrasenya the admin's password
     */
    public Admin(String nom, String contrasenya) {
        this.nom = nom;
        this.contrasenya = contrasenya;
        this.enquestes_ids = new ArrayList<>();
    }

    /**
     * Return the admin's name.
     *
     * @return the name of the admin
     */
    public String getNom() {
        return nom;
    }

    /**
     * Simple authentication: check if the provided password matches the stored one.
     *
     * @param contrasenya the password to verify
     * @return true if the password matches, false otherwise
     */
    public boolean iniciarSessio(String contrasenya)
    {
        if (this.contrasenya == null || this.contrasenya.isEmpty()) {
            return contrasenya == null || contrasenya.isEmpty();
        }

        if (contrasenya == null) return false;
        else return this.contrasenya.equals(contrasenya);
    }

    /**
     * Add a survey id to this admin's managed list.
     *
     * @param id the survey id to add
     * @throws EnquestaJaExisteix if the admin already has that survey id
     */
    public void addEnquesta(int id) throws EnquestaJaExisteix {
        if (enquestes_ids.contains(id)) {
            throw new AdminJaTeEnquesta("L'admin ja te una enquesta amb id " + id);
        }
        enquestes_ids.add(id);
    }

    /**
     * Remove a survey id from this admin's managed list.
     *
     * @param id the survey id to remove
     * @throws EnquestaNoExisteix if the admin does not have that survey id
     */
    public void removeEnquesta(int id) throws EnquestaNoExisteix {
        if (!enquestes_ids.contains(id)) {
            throw new AdminNoTeEnquesta("L'admin no te cap enquesta amb id " + id);
        }
        enquestes_ids.remove(Integer.valueOf(id));
    }

    /**
     * Check whether this admin manages the survey with the given id.
     *
     * @param id the survey id to check
     * @return true if the admin manages the survey, false otherwise
     */
    public boolean hasEnquesta(int id) {
        return enquestes_ids.contains(id);
    }

    /**
     * Get the list of survey ids managed by this admin.
     *
     * Note: the returned list is the internal list and can be modified by callers.
     * If immutable access is desired, callers should create an unmodifiable copy.
     *
     * @return the list of survey ids
     */
    public ArrayList<Integer> getEnquestesIds() {
        return enquestes_ids;
    }
}