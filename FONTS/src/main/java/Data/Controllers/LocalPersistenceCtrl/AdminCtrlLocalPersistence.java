package Data.Controllers.LocalPersistenceCtrl;

import Domain.Adapters.IAdminPersistance;
import Domain.Controllers.IAdminCtrl;
import Domain.Exceptions.AdminJaExisteix;
import Domain.Exceptions.AdminNoExisteix;
import Domain.Factories.AdapterFactory;
import Domain.Model.Admin;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * In-memory implementation of the Admin controller without database persistence.
 * Manages Admin instances using a Dictionary data structure.
 */
public class AdminCtrlLocalPersistence implements IAdminCtrl {

    /**
     * Dictionary storing Admin instances indexed by their name.
     */
    private Dictionary<String, Admin> admins = new Hashtable<String, Admin>();

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
        if ((a = admins.get(nom)) != null) {
            return a;
        }
        else{
            IAdminPersistance ap = AdapterFactory.getInstance().getAdminPersistance();
            if (ap.existsAdmin(nom)) {
                Admin adminFromPersistence = ap.loadAdmin(nom);
                admins.put(nom, adminFromPersistence);
                return adminFromPersistence;
            }
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
        if (admins.get(new_admin.getNom()) != null) {
            throw new AdminJaExisteix("Ja existeix una instància de admin amb nom " + new_admin.getNom());
        }
        IAdminPersistance ap = AdapterFactory.getInstance().getAdminPersistance();
        if (ap.existsAdmin(new_admin.getNom())) {
            throw new AdminJaExisteix("Ja existeix una instància de admin amb nom " + new_admin.getNom());
        }

        admins.put(new_admin.getNom(), new_admin);
        ap.saveAdmin(new_admin);
    }

    /**
     * Updates an existing Admin instance in the system.
     *
     * @param admin the Admin instance to update
     * @throws AdminNoExisteix if no Admin with the given name exists
     */
    public void updateAdmin(Admin admin) throws AdminNoExisteix {
        if (admins.get(admin.getNom()) == null) {
            throw new AdminNoExisteix("Aquesta instància de admin no és present al controlador");
        }

        IAdminPersistance ap = AdapterFactory.getInstance().getAdminPersistance();

        ap.saveAdmin(admin);
    }

    /**
     * Deletes an Admin instance from the system.
     *
     * @param nom the name of the Admin to delete
     * @throws AdminNoExisteix if no Admin with the given name exists
     */
    public void deleteAdmin(String nom) throws AdminNoExisteix {
        IAdminPersistance ap = AdapterFactory.getInstance().getAdminPersistance();
        if (!ap.existsAdmin(nom))
            throw new AdminNoExisteix("No existeix una instància de admin amb nom " + nom);

        admins.remove(nom);
        ap.deleteAdmin(nom);
    }
}
