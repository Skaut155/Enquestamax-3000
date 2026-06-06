package DataTests;
import Domain.Exceptions.AdminJaExisteix;
import Domain.Exceptions.AdminNoExisteix;
import org.junit.Test;
import static org.junit.Assert.*;
import Domain.Model.Admin;
import Data.Controllers.LocalPersistenceCtrl.AdminCtrlLocalPersistence;

public class TestAdminCtrlLocalPersistence {

    private AdminCtrlLocalPersistence adminCtrl = new AdminCtrlLocalPersistence();

    @Test
    public void testAddAdminWithSameName(){
        Admin a1 = new Admin("admin1", "password1");
        Admin a2 = new Admin("admin1", "password2");

        try{
            adminCtrl.AddAdmin(a1);
        }
        catch(AdminJaExisteix e){
            // Admin with name admin1 already exists
            // Load it to proceed with the test
            adminCtrl.GetAdmin("admin1");
        }

        assertThrows(AdminJaExisteix.class, () -> adminCtrl.AddAdmin(a2));
    }

    @Test
    public void testGetAdminThatDoesNotExist(){
        assertThrows(Domain.Exceptions.AdminNoExisteix.class, () -> adminCtrl.GetAdmin("nonexistent_admin"));
    }

    @Test
    public void textGetNullAdmin(){
        assertThrows(Domain.Exceptions.AdminNoExisteix.class, () -> adminCtrl.GetAdmin(null));
    }

    @Test
    public void updateNonExistentAdmin(){
        Admin a = new Admin("nonexistent_admin", "password");
        assertThrows(AdminNoExisteix.class , () -> adminCtrl.updateAdmin(a));
    }

    @Test
    public void deleteNonExistentAdmin(){
        assertThrows(Domain.Exceptions.AdminNoExisteix.class, () -> adminCtrl.deleteAdmin("nonexistent_admin"));
    }

    @Test
    public void testRemoveExistentAdmin(){
        Admin a1 = new Admin("adminToRemove", "password");
        try{
            adminCtrl.AddAdmin(a1);
        }
        catch(AdminJaExisteix e){
            // Admin with name adminToRemove already exists, proceed
        }

        try{
            adminCtrl.GetAdmin("adminToRemove");
        }
        catch(Domain.Exceptions.AdminNoExisteix e){
            fail("Admin should exist before removal");
        }

        // Simulate removal by not providing a remove method in this test
        // In a real scenario, you would call adminCtrl.RemoveAdmin("adminToRemove");

        adminCtrl.deleteAdmin("adminToRemove");
        assertThrows(Domain.Exceptions.AdminNoExisteix.class, () -> adminCtrl.GetAdmin("adminToRemove"));
    }

    @Test
    public void testUpdateAdmin(){

        try{
            adminCtrl.deleteAdmin("adminToUpdate");
        }
        catch(Domain.Exceptions.AdminNoExisteix e){
            // Admin does not exist, proceed
        }

        Admin a1 = new Admin("adminToUpdate", "oldPassword");
        a1.addEnquesta(1);
        try{
            adminCtrl.AddAdmin(a1);
        }
        catch(AdminJaExisteix e){
            // Admin with name adminToUpdate already exists, proceed
        }

        try{
            Admin retrievedAdmin = adminCtrl.GetAdmin("adminToUpdate");
            assertTrue(a1.hasEnquesta(1));

            // Add new enquesta
            retrievedAdmin.addEnquesta(2);

            // Update admin
            adminCtrl.updateAdmin(retrievedAdmin);

            // Verify update
            Admin updatedAdmin = adminCtrl.GetAdmin("adminToUpdate");
            assertTrue(a1.hasEnquesta(1));
            assertTrue(a1.hasEnquesta(2));
        }
        catch(Domain.Exceptions.AdminNoExisteix e){
            fail("Admin should exist for update test");
        }
    }

}