package Domain.Controllers;

import Domain.Model.Admin;
import Domain.Exceptions.AdminJaExisteix;
import Domain.Exceptions.AdminNoExisteix;

/**
 * Interface defining operations for managing Admin instances.
 * Implementations of this interface handle the persistence and retrieval of Admin objects.
 */
public interface IAdminCtrl {
    /**
     * Retrieves an Admin instance by name.
     *
     * @param nom the name of the Admin to retrieve
     * @return the Admin instance with the specified name
     * @throws AdminNoExisteix if no Admin with the given name exists
     */
    Admin GetAdmin(String nom) throws AdminNoExisteix;

    /**
     * Adds a new Admin instance to the system.
     *
     * @param admin the Admin instance to add
     * @throws AdminJaExisteix if an Admin with the same name already exists
     */
    void AddAdmin(Admin admin) throws AdminJaExisteix;

    /**
     * Updates an existing Admin instance in the system to persistence.
     *
     * @param admin the Admin instance to update
     * @throws AdminNoExisteix if no Admin with the given name exists
     */
    void updateAdmin(Admin admin) throws AdminNoExisteix;

    /**
     * Deletes an Admin instance from the system.
     *
     * @param nom the name of the Admin to delete
     * @throws AdminNoExisteix if no Admin with the given name exists
     */
    void deleteAdmin(String nom) throws AdminNoExisteix;
}
