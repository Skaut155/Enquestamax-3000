package Domain.Adapters;

import Domain.Model.Admin;

/**
 * Interface for admin persistence operations.
 */
public interface IAdminPersistance {

    /**
     * Saves an admin.
     * @param admin the Admin object to save.
     */
    public void saveAdmin(Admin admin);

    /**
     * Loads an admin by their name.
     * @param nomAdmin the name of the admin to load.
     * @return the Admin object if found, null otherwise.
     */
    public Admin loadAdmin(String nomAdmin);

    /**
     * Checks if an admin with the given name exists.
     * @param nomAdmin the name of the admin to check.
     * @return true if the admin exists, false otherwise.
     */
    public boolean existsAdmin(String nomAdmin);

    /**
     * Deletes an admin by their name.
     * @param nomAdmin the name of the admin to delete.
     * @return true if the admin was successfully deleted, false otherwise.
     */
    public boolean deleteAdmin(String nomAdmin);
}
