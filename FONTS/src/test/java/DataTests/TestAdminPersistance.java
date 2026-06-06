package DataTests;

import Data.Persistance.AdminPersistance;
import Domain.Model.Admin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class TestAdminPersistance {

    private AdminPersistance adminPersistance;
    private static final String TEST_ADMIN_NAME = "testAdmin";
    private static final String TEST_ADMIN_PASSWORD = "testPassword123";

    @Before
    public void setUp() {
        adminPersistance = new AdminPersistance();
    }

    @After
    public void tearDown() {
        // Clean up test files after each test
        File testFile = new File(TEST_ADMIN_NAME + ".json");
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testSaveAdmin_CreatesJsonFile() {
        // Given
        Admin admin = new Admin(TEST_ADMIN_NAME, TEST_ADMIN_PASSWORD);

        // When
        adminPersistance.saveAdmin(admin);

        // Then
        assertTrue("Admin JSON file should exist after saving",
                   adminPersistance.existsAdmin(TEST_ADMIN_NAME));
    }

    @Test
    public void testSaveAndLoadAdmin_PreservesBasicInfo() {
        // Given
        Admin originalAdmin = new Admin(TEST_ADMIN_NAME, TEST_ADMIN_PASSWORD);

        // When
        adminPersistance.saveAdmin(originalAdmin);
        Admin loadedAdmin = adminPersistance.loadAdmin(TEST_ADMIN_NAME);

        // Then
        assertNotNull("Loaded admin should not be null", loadedAdmin);
        assertEquals("Admin name should be preserved",
                     TEST_ADMIN_NAME, loadedAdmin.getNom());
        assertTrue("Admin password should authenticate correctly",
                   loadedAdmin.iniciarSessio(TEST_ADMIN_PASSWORD));
        assertFalse("Admin should not authenticate with wrong password",
                    loadedAdmin.iniciarSessio("wrongPassword"));
    }

    @Test
    public void testSaveAndLoadAdmin_PreservesEnquestesIds() {
        // Given
        Admin originalAdmin = new Admin(TEST_ADMIN_NAME, TEST_ADMIN_PASSWORD);
        originalAdmin.addEnquesta(1);
        originalAdmin.addEnquesta(2);
        originalAdmin.addEnquesta(5);

        // When
        adminPersistance.saveAdmin(originalAdmin);
        Admin loadedAdmin = adminPersistance.loadAdmin(TEST_ADMIN_NAME);

        // Then
        assertNotNull("Loaded admin should not be null", loadedAdmin);
        assertEquals("Number of enquestes should be preserved",
                     3, loadedAdmin.getEnquestesIds().size());
        assertTrue("Admin should have enquesta with id 1",
                   loadedAdmin.hasEnquesta(1));
        assertTrue("Admin should have enquesta with id 2",
                   loadedAdmin.hasEnquesta(2));
        assertTrue("Admin should have enquesta with id 5",
                   loadedAdmin.hasEnquesta(5));
        assertFalse("Admin should not have enquesta with id 3",
                    loadedAdmin.hasEnquesta(3));
    }

    @Test
    public void testLoadAdmin_ReturnsNullForNonExistentAdmin() {
        // When
        Admin loadedAdmin = adminPersistance.loadAdmin("nonExistentAdmin");

        // Then
        assertNull("Loading non-existent admin should return null", loadedAdmin);
    }

    @Test
    public void testExistsAdmin_ReturnsTrueForExistingAdmin() {
        // Given
        Admin admin = new Admin(TEST_ADMIN_NAME, TEST_ADMIN_PASSWORD);
        adminPersistance.saveAdmin(admin);

        // When
        boolean exists = adminPersistance.existsAdmin(TEST_ADMIN_NAME);

        // Then
        assertTrue("existsAdmin should return true for saved admin", exists);
    }

    @Test
    public void testExistsAdmin_ReturnsFalseForNonExistentAdmin() {
        // When
        boolean exists = adminPersistance.existsAdmin("nonExistentAdmin");

        // Then
        assertFalse("existsAdmin should return false for non-existent admin", exists);
    }

    @Test
    public void testSaveAdmin_WithEmptyEnquestesList() {
        // Given
        Admin admin = new Admin(TEST_ADMIN_NAME, TEST_ADMIN_PASSWORD);

        // When
        adminPersistance.saveAdmin(admin);
        Admin loadedAdmin = adminPersistance.loadAdmin(TEST_ADMIN_NAME);

        // Then
        assertNotNull("Loaded admin should not be null", loadedAdmin);
        assertNotNull("Enquestes list should not be null",
                      loadedAdmin.getEnquestesIds());
        assertEquals("Enquestes list should be empty",
                     0, loadedAdmin.getEnquestesIds().size());

        // cleanup after test using delete from AdminPersistance
        adminPersistance.deleteAdmin(TEST_ADMIN_NAME);
    }
}
