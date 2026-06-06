package Data.Controllers.NoDBCtrl;

import Domain.Controllers.IAdminCtrl;
import Domain.Model.Admin;
import Domain.Exceptions.AdminJaExisteix;
import Domain.Exceptions.AdminNoExisteix;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * In-memory implementation of the Admin controller without database persistence.
 * Manages Admin instances using a Dictionary data structure.
 */
public class AdminCtrlNoDB implements IAdminCtrl {

    /**
     * Dictionary storing Admin instances indexed by their name in memory.
     */
    private Dictionary<String, Admin> adminsMemoria = new Hashtable<String, Admin>();

    /**
     * Retrieves an Admin instance by name.
     *
     * @param nom the name of the Admin to retrieve
     * @return the Admin instance with the specified name
     * @throws AdminNoExisteix if no Admin with the given name exists
     */
    public Admin GetAdmin(String nom) throws AdminNoExisteix{
        if (nom == null) {
            throw new AdminNoExisteix("El nom de l'admin no pot ser null");
        }

        Admin a;
        if ((a = adminsMemoria.get(nom)) != null) {
            return a;
        }

        throw new AdminNoExisteix("No existeix una instància de admin amb nom " + nom);
    }

    /**
     * Adds a new Admin instance to the system.
     *
     * @param new_admin the Admin instance to add
     * @throws AdminJaExisteix if an Admin with the same name already exists
     */
    public void AddAdmin(Admin new_admin) throws AdminJaExisteix{
        if (adminsMemoria.get(new_admin.getNom()) != null) {
            throw new AdminJaExisteix("Ja existeix una instància de admin amb nom " + new_admin.getNom());
        }

        adminsMemoria.put(new_admin.getNom(), new_admin);
    }

    /**
     * Does nothing because Admin instances are stored in memory
     *
     * @param admin the Admin instance to update
     * @throws AdminNoExisteix if no Admin with the given name exists
     */
    @Override
    public void updateAdmin(Admin admin) throws AdminNoExisteix {
        String nom = admin.getNom();

        if (adminsMemoria.get(nom) == null) {
            throw new AdminNoExisteix("No existeix una instància de admin amb nom " + nom + " a la memòria");
        }
    }

    /**
     * Deletes an Admin instance from the system.
     *
     * @param nom the name of the Admin to delete
     * @throws AdminNoExisteix if no Admin with the given name exists
     */
    @Override
    public void deleteAdmin(String nom) throws AdminNoExisteix {
        if (adminsMemoria.get(nom) != null) {
            adminsMemoria.remove(nom);
        }
        else
        {
            throw new AdminNoExisteix("No existeix una instància de admin amb nom " + nom);
        }
    }
}
