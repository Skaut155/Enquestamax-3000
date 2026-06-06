package DataTests;

import Data.Controllers.NoDBCtrl.AdminCtrlNoDB;
import Domain.Model.Admin;
import Domain.Exceptions.AdminJaExisteix;
import Domain.Exceptions.AdminNoExisteix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the AdminCtrlNoDB class.
 * Tests in-memory management of Admin instances.
 */
public class TestAdminCtrlNoDB {

    private AdminCtrlNoDB adminCtrl;

    @Before
    public void setUp() {
        adminCtrl = new AdminCtrlNoDB();
    }

    @Test
    public void testAddAdminSuccessfully() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("testUser", "password");
        adminCtrl.AddAdmin(admin);

        Admin retrieved = adminCtrl.GetAdmin("testUser");
        assertNotNull("Retrieved admin should not be null", retrieved);
        assertEquals("Retrieved admin should have same name", "testUser", retrieved.getNom());
    }

    @Test
    public void testAddAdminWithDuplicateName() throws AdminJaExisteix {
        Admin admin1 = new Admin("testUser", "password1");
        Admin admin2 = new Admin("testUser", "password2");

        adminCtrl.AddAdmin(admin1);
        assertThrows(AdminJaExisteix.class, () -> adminCtrl.AddAdmin(admin2));
    }

    @Test
    public void testGetAdminSuccessfully() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("testUser", "password");
        adminCtrl.AddAdmin(admin);

        Admin retrieved = adminCtrl.GetAdmin("testUser");
        assertSame("Retrieved admin should be the same instance", admin, retrieved);
    }

    @Test
    public void testGetAdminNonExistent() {
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.GetAdmin("nonExistentUser"));
    }

    @Test
    public void testAddMultipleAdmins() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin1 = new Admin("user1", "pass1");
        Admin admin2 = new Admin("user2", "pass2");
        Admin admin3 = new Admin("user3", "pass3");

        adminCtrl.AddAdmin(admin1);
        adminCtrl.AddAdmin(admin2);
        adminCtrl.AddAdmin(admin3);

        assertEquals("user1", adminCtrl.GetAdmin("user1").getNom());
        assertEquals("user2", adminCtrl.GetAdmin("user2").getNom());
        assertEquals("user3", adminCtrl.GetAdmin("user3").getNom());
    }

    @Test
    public void testAddAdminWithEmptyName() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("", "password");
        adminCtrl.AddAdmin(admin);

        Admin retrieved = adminCtrl.GetAdmin("");
        assertNotNull("Should be able to add and retrieve admin with empty name", retrieved);
    }

    @Test
    public void testAddAdminWithNullPassword() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("testUser", null);
        adminCtrl.AddAdmin(admin);

        Admin retrieved = adminCtrl.GetAdmin("testUser");
        assertNotNull("Should be able to add admin with null password", retrieved);
    }

    @Test
    public void testAddSameAdminTwice() throws AdminJaExisteix {
        Admin admin = new Admin("testUser", "password");
        adminCtrl.AddAdmin(admin);
        assertThrows(AdminJaExisteix.class, () -> adminCtrl.AddAdmin(admin));
    }

    @Test
    public void testGetAdminReturnsCorrectInstance() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin1 = new Admin("user1", "pass1");
        Admin admin2 = new Admin("user2", "pass2");

        adminCtrl.AddAdmin(admin1);
        adminCtrl.AddAdmin(admin2);

        Admin retrieved1 = adminCtrl.GetAdmin("user1");
        Admin retrieved2 = adminCtrl.GetAdmin("user2");

        assertSame("Should return exact instance of admin1", admin1, retrieved1);
        assertSame("Should return exact instance of admin2", admin2, retrieved2);
        assertNotSame("Different admins should be different instances", retrieved1, retrieved2);
    }

    @Test
    public void testGetAdminWithNull() {
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.GetAdmin(null));
    }

    @Test
    public void testAddAdminCaseSensitive() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin1 = new Admin("TestUser", "password");
        Admin admin2 = new Admin("testuser", "password");

        adminCtrl.AddAdmin(admin1);
        adminCtrl.AddAdmin(admin2); // Should not throw - different names

        Admin retrieved1 = adminCtrl.GetAdmin("TestUser");
        Admin retrieved2 = adminCtrl.GetAdmin("testuser");

        assertNotSame("Case-sensitive names should be different admins", retrieved1, retrieved2);
    }

    @Test
    public void testGetAdminCaseSensitive() throws AdminJaExisteix {
        Admin admin = new Admin("TestUser", "password");
        adminCtrl.AddAdmin(admin);

        // This should throw because case doesn't match
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.GetAdmin("testuser"));
    }

    @Test
    public void testAddAdminWithSpecialCharacters() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("test@user#123", "password");
        adminCtrl.AddAdmin(admin);

        Admin retrieved = adminCtrl.GetAdmin("test@user#123");
        assertNotNull("Should handle special characters in name", retrieved);
    }

    @Test
    public void testAddAdminWithLongName() throws AdminJaExisteix, AdminNoExisteix {
        String longName = "a".repeat(1000);
        Admin admin = new Admin(longName, "password");
        adminCtrl.AddAdmin(admin);

        Admin retrieved = adminCtrl.GetAdmin(longName);
        assertNotNull("Should handle very long names", retrieved);
    }

    @Test
    public void testMultipleAdminControllers() throws AdminJaExisteix, AdminNoExisteix {
        AdminCtrlNoDB ctrl1 = new AdminCtrlNoDB();
        AdminCtrlNoDB ctrl2 = new AdminCtrlNoDB();

        Admin admin1 = new Admin("user1", "pass1");
        Admin admin2 = new Admin("user2", "pass2");

        ctrl1.AddAdmin(admin1);
        ctrl2.AddAdmin(admin2);

        // ctrl1 should only have admin1
        assertEquals("user1", ctrl1.GetAdmin("user1").getNom());

        // ctrl2 should only have admin2
        assertEquals("user2", ctrl2.GetAdmin("user2").getNom());
    }

    @Test
    public void testControllerIsolation() throws AdminJaExisteix {
        AdminCtrlNoDB ctrl1 = new AdminCtrlNoDB();
        AdminCtrlNoDB ctrl2 = new AdminCtrlNoDB();

        Admin admin = new Admin("testUser", "password");
        ctrl1.AddAdmin(admin);

        // Should throw - admin not in ctrl2
        assertThrows(AdminNoExisteix.class, () -> ctrl2.GetAdmin("testUser"));
    }

    @Test
    public void testAddManyAdmins() throws AdminJaExisteix, AdminNoExisteix {
        for (int i = 0; i < 100; i++) {
            Admin admin = new Admin("user" + i, "pass" + i);
            adminCtrl.AddAdmin(admin);
        }

        // Verify all were added
        for (int i = 0; i < 100; i++) {
            Admin retrieved = adminCtrl.GetAdmin("user" + i);
            assertNotNull("Admin " + i + " should exist", retrieved);
            assertEquals("user" + i, retrieved.getNom());
        }
    }

    @Test
    public void testExceptionMessageForDuplicateAdmin() throws AdminJaExisteix {
        Admin admin = new Admin("testUser", "password");

        adminCtrl.AddAdmin(admin);
        AdminJaExisteix exception = assertThrows(AdminJaExisteix.class,
            () -> adminCtrl.AddAdmin(admin));
        assertTrue("Exception message should contain admin name",
                  exception.getMessage().contains("testUser"));
    }

    @Test
    public void testExceptionMessageForNonExistentAdmin() {
        AdminNoExisteix exception = assertThrows(AdminNoExisteix.class,
            () -> adminCtrl.GetAdmin("nonExistentUser"));
        assertTrue("Exception message should contain admin name",
                  exception.getMessage().contains("nonExistentUser"));
    }

    // ==================== Tests for updateAdmin ====================

    @Test
    public void testUpdateAdminSuccessfully() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("testUser", "password");
        try{
            adminCtrl.AddAdmin(admin);
        }
        catch (AdminJaExisteix exception){
            // Admin already exists, proceed
        }

        // updateAdmin should not throw when admin exists
        adminCtrl.updateAdmin(admin);
    }

    @Test
    public void testUpdateAdminNonExistent() {
        Admin admin = new Admin("nonExistentUser", "password");
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.updateAdmin(admin));
    }

    @Test
    public void testUpdateAdminExceptionMessage() {
        Admin admin = new Admin("testUser", "password");
        AdminNoExisteix exception = assertThrows(AdminNoExisteix.class,
            () -> adminCtrl.updateAdmin(admin));
        assertTrue("Exception message should contain admin name",
                  exception.getMessage().contains("testUser"));
    }

    @Test
    public void testUpdateAdminMultipleTimes() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("testUser", "password");
        adminCtrl.AddAdmin(admin);

        // Multiple updates should work
        adminCtrl.updateAdmin(admin);
        adminCtrl.updateAdmin(admin);
        adminCtrl.updateAdmin(admin);
    }

    @Test
    public void testUpdateDifferentAdmins() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin1 = new Admin("user1", "pass1");
        Admin admin2 = new Admin("user2", "pass2");

        adminCtrl.AddAdmin(admin1);
        adminCtrl.AddAdmin(admin2);

        adminCtrl.updateAdmin(admin1);
        adminCtrl.updateAdmin(admin2);
    }

    // ==================== Tests for deleteAdmin ====================

    @Test
    public void testDeleteAdminSuccessfully() throws AdminJaExisteix, AdminNoExisteix {
        try{
            Admin admin = new Admin("testUser", "password");
            adminCtrl.AddAdmin(admin);
        } catch (AdminJaExisteix exception){
            // Admin already exists, proceed
        }

        adminCtrl.deleteAdmin("testUser");

        // Should throw because admin was deleted
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.GetAdmin("testUser"));
    }

    @Test
    public void testDeleteAdminNonExistent() {
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.deleteAdmin("nonExistentUser"));
    }

    @Test
    public void testDeleteAdminTwice() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("testUser", "password");
        adminCtrl.AddAdmin(admin);

        adminCtrl.deleteAdmin("testUser");

        // Second delete should throw
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.deleteAdmin("testUser"));
    }

    @Test
    public void testDeleteAdminExceptionMessage() {
        AdminNoExisteix exception = assertThrows(AdminNoExisteix.class,
            () -> adminCtrl.deleteAdmin("nonExistentUser"));
        assertTrue("Exception message should contain admin name",
                  exception.getMessage().contains("nonExistentUser"));
    }

    @Test
    public void testDeleteAndReAddAdmin() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin1 = new Admin("testUser", "password1");
        adminCtrl.AddAdmin(admin1);

        adminCtrl.deleteAdmin("testUser");

        // Should be able to add admin with same name again
        Admin admin2 = new Admin("testUser", "password2");
        adminCtrl.AddAdmin(admin2);

        Admin retrieved = adminCtrl.GetAdmin("testUser");
        assertSame(admin2, retrieved);
    }

    @Test
    public void testDeleteMultipleAdmins() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin1 = new Admin("user1", "pass1");
        Admin admin2 = new Admin("user2", "pass2");
        Admin admin3 = new Admin("user3", "pass3");

        adminCtrl.AddAdmin(admin1);
        adminCtrl.AddAdmin(admin2);
        adminCtrl.AddAdmin(admin3);

        adminCtrl.deleteAdmin("user2");

        // user1 and user3 should still exist
        assertNotNull(adminCtrl.GetAdmin("user1"));
        assertNotNull(adminCtrl.GetAdmin("user3"));

        // user2 should not exist
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.GetAdmin("user2"));
    }

    @Test
    public void testDeleteAdminWithEmptyName() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("", "password");
        adminCtrl.AddAdmin(admin);

        adminCtrl.deleteAdmin("");

        assertThrows(AdminNoExisteix.class, () -> adminCtrl.GetAdmin(""));
    }

    @Test
    public void testDeleteAdminWithSpecialCharacters() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("test@user#123", "password");
        adminCtrl.AddAdmin(admin);

        adminCtrl.deleteAdmin("test@user#123");

        assertThrows(AdminNoExisteix.class, () -> adminCtrl.GetAdmin("test@user#123"));
    }

    @Test
    public void testDeleteAdminCaseSensitive() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("TestUser", "password");
        adminCtrl.AddAdmin(admin);

        // Should throw because case doesn't match
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.deleteAdmin("testuser"));

        // Original should still exist
        assertNotNull(adminCtrl.GetAdmin("TestUser"));
    }

    @Test
    public void testUpdateAfterDelete() throws AdminJaExisteix, AdminNoExisteix {
        Admin admin = new Admin("testUser", "password");
        adminCtrl.AddAdmin(admin);

        adminCtrl.deleteAdmin("testUser");

        // Update should throw after delete
        assertThrows(AdminNoExisteix.class, () -> adminCtrl.updateAdmin(admin));
    }

    @Test
    public void testDeleteAllAdmins() throws AdminJaExisteix, AdminNoExisteix {
        for (int i = 0; i < 10; i++) {
            Admin admin = new Admin("user" + i, "pass" + i);
            adminCtrl.AddAdmin(admin);
        }

        for (int i = 0; i < 10; i++) {
            adminCtrl.deleteAdmin("user" + i);
        }

        // All should be deleted
        for (int i = 0; i < 10; i++) {
            final int index = i;
            assertThrows(AdminNoExisteix.class, () -> adminCtrl.GetAdmin("user" + index));
        }
    }
}
