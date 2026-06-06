package DataTests;

import Domain.Exceptions.EnquestaNoExisteix;
import org.junit.Test;
import static org.junit.Assert.*;
import Domain.Model.Enquesta;
import Data.Controllers.LocalPersistenceCtrl.EnquestaCtrlLocalPersistence;

public class TestEnquestaCtrlLocalPersistence {

    private EnquestaCtrlLocalPersistence enquestaCtrl = new EnquestaCtrlLocalPersistence();

    @Test
    public void testAddEnquestaSuccessfully() {
        Enquesta e = new Enquesta("TestSurvey");
        enquestaCtrl.AddEnquesta(e);

        Enquesta retrieved = enquestaCtrl.GetEnquesta(e.getId());
        assertNotNull(retrieved);
        assertEquals("TestSurvey", retrieved.getNom());

        // Cleanup
        try {
            enquestaCtrl.DeleteEnquesta(e.getId());
        } catch (EnquestaNoExisteix ex) {
            // Already deleted, proceed
        }
    }

    @Test
    public void testGetEnquestaThatDoesNotExist() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.GetEnquesta(999999));
    }

    @Test
    public void testGetEnquestaWithNegativeId() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.GetEnquesta(-1));
    }

    @Test
    public void testDeleteNonExistentEnquesta() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.DeleteEnquesta(999999));
    }

    @Test
    public void testDeleteExistentEnquesta() {
        Enquesta e = new Enquesta("EnquestaToRemove");
        enquestaCtrl.AddEnquesta(e);
        int id = e.getId();
        enquestaCtrl.updateEnquesta(id);

        // Verify it exists
        try {
            enquestaCtrl.GetEnquesta(id);
        } catch (EnquestaNoExisteix ex) {
            fail("Enquesta should exist before removal");
        }

        // Delete it
        enquestaCtrl.DeleteEnquesta(id);

        // Verify it no longer exists
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.GetEnquesta(id));
    }

    @Test
    public void testUpdateNonExistentEnquesta() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.updateEnquesta(999999));
    }

    @Test
    public void testUpdateEnquesta() {
        Enquesta e = new Enquesta("EnquestaToUpdate");
        enquestaCtrl.AddEnquesta(e);
        int id = e.getId();

        try {
            Enquesta retrieved = enquestaCtrl.GetEnquesta(id);
            assertEquals("EnquestaToUpdate", retrieved.getNom());

            // Modify the enquesta
            retrieved.setNom("UpdatedEnquesta");

            // Update in persistence
            enquestaCtrl.updateEnquesta(id);

            // Verify update persisted (reload from persistence)
            enquestaCtrl.reloadEnquesta(id);
            Enquesta updatedEnquesta = enquestaCtrl.GetEnquesta(id);
            assertEquals("UpdatedEnquesta", updatedEnquesta.getNom());

        } catch (EnquestaNoExisteix ex) {
            fail("Enquesta should exist for update test");
        } finally {
            // Cleanup
            try {
                enquestaCtrl.DeleteEnquesta(id);
            } catch (EnquestaNoExisteix ex) {
                // Already deleted
            }
        }
    }

    @Test
    public void testReloadNonExistentEnquesta() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.reloadEnquesta(999999));
    }

    @Test
    public void testReloadEnquestaNotInMemory() {
        // Create and add enquesta
        Enquesta e = new Enquesta("EnquestaReloadTest");
        enquestaCtrl.AddEnquesta(e);
        int id = e.getId();

        // Save to persistence
        enquestaCtrl.updateEnquesta(id);

        // Create new controller (simulates fresh start, no memory cache)
        EnquestaCtrlLocalPersistence newCtrl = new EnquestaCtrlLocalPersistence();

        // Try to reload without having it in memory first should fail
        assertThrows(EnquestaNoExisteix.class, () -> newCtrl.reloadEnquesta(id));

        // Cleanup
        try {
            enquestaCtrl.DeleteEnquesta(id);
        } catch (EnquestaNoExisteix ex) {
            // Already deleted
        }
    }

    @Test
    public void testReloadEnquestaSuccessfully() {
        Enquesta e = new Enquesta("EnquestaReloadSuccess");
        enquestaCtrl.AddEnquesta(e);
        int id = e.getId();

        // Save to persistence
        enquestaCtrl.updateEnquesta(id);

        // Modify in memory
        e.setNom("ModifiedInMemory");

        // Reload from persistence (should revert to saved state)
        enquestaCtrl.reloadEnquesta(id);

        Enquesta reloaded = enquestaCtrl.GetEnquesta(id);
        assertEquals("EnquestaReloadSuccess", reloaded.getNom());

        // Cleanup
        try {
            enquestaCtrl.DeleteEnquesta(id);
        } catch (EnquestaNoExisteix ex) {
            // Already deleted
        }
    }

    @Test
    public void testAddMultipleEnquestas() {
        Enquesta e1 = new Enquesta("Survey1");
        Enquesta e2 = new Enquesta("Survey2");
        Enquesta e3 = new Enquesta("Survey3");

        enquestaCtrl.AddEnquesta(e1);
        enquestaCtrl.AddEnquesta(e2);
        enquestaCtrl.AddEnquesta(e3);

        // Verify all have different IDs
        assertNotEquals(e1.getId(), e2.getId());
        assertNotEquals(e2.getId(), e3.getId());
        assertNotEquals(e1.getId(), e3.getId());

        // Verify all can be retrieved
        assertEquals("Survey1", enquestaCtrl.GetEnquesta(e1.getId()).getNom());
        assertEquals("Survey2", enquestaCtrl.GetEnquesta(e2.getId()).getNom());
        assertEquals("Survey3", enquestaCtrl.GetEnquesta(e3.getId()).getNom());

        // Cleanup
        try { enquestaCtrl.DeleteEnquesta(e1.getId()); } catch (EnquestaNoExisteix ex) {}
        try { enquestaCtrl.DeleteEnquesta(e2.getId()); } catch (EnquestaNoExisteix ex) {}
        try { enquestaCtrl.DeleteEnquesta(e3.getId()); } catch (EnquestaNoExisteix ex) {}
    }

    @Test
    public void testEnquestaIdsAreIncremental() {
        Enquesta e1 = new Enquesta("Incremental1");
        Enquesta e2 = new Enquesta("Incremental2");

        enquestaCtrl.AddEnquesta(e1);
        enquestaCtrl.AddEnquesta(e2);

        assertTrue(e2.getId() > e1.getId());

        // Cleanup
        try { enquestaCtrl.DeleteEnquesta(e1.getId()); } catch (EnquestaNoExisteix ex) {}
        try { enquestaCtrl.DeleteEnquesta(e2.getId()); } catch (EnquestaNoExisteix ex) {}
    }

    @Test
    public void testEnquestaWithEmptyName() {
        Enquesta e = new Enquesta("");
        enquestaCtrl.AddEnquesta(e);

        Enquesta retrieved = enquestaCtrl.GetEnquesta(e.getId());
        assertEquals("", retrieved.getNom());

        // Cleanup
        try { enquestaCtrl.DeleteEnquesta(e.getId()); } catch (EnquestaNoExisteix ex) {}
    }

    @Test
    public void testEnquestaWithSpecialCharacters() {
        Enquesta e = new Enquesta("Test @#$%^&*() Survey!");
        enquestaCtrl.AddEnquesta(e);

        Enquesta retrieved = enquestaCtrl.GetEnquesta(e.getId());
        assertEquals("Test @#$%^&*() Survey!", retrieved.getNom());

        // Cleanup
        try { enquestaCtrl.DeleteEnquesta(e.getId()); } catch (EnquestaNoExisteix ex) {}
    }

    @Test
    public void testDeleteEnquestaTwice() {
        Enquesta e = new Enquesta("DeleteTwice");
        enquestaCtrl.AddEnquesta(e);
        int id = e.getId();

        // Save to persistence
        enquestaCtrl.updateEnquesta(id);

        // First delete should succeed
        enquestaCtrl.DeleteEnquesta(id);

        // Second delete should throw
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.DeleteEnquesta(id));
    }

    @Test
    public void testGetEnquestaFromPersistence() {
        // Create and save enquesta
        Enquesta e = new Enquesta("PersistenceTest");
        enquestaCtrl.AddEnquesta(e);
        int id = e.getId();
        enquestaCtrl.updateEnquesta(id);

        // Create new controller instance (fresh memory)
        EnquestaCtrlLocalPersistence newCtrl = new EnquestaCtrlLocalPersistence();

        // Get should load from persistence
        Enquesta retrieved = newCtrl.GetEnquesta(id);
        assertNotNull(retrieved);
        assertEquals("PersistenceTest", retrieved.getNom());

        // Cleanup
        try { newCtrl.DeleteEnquesta(id); } catch (EnquestaNoExisteix ex) {}
    }

    @Test
    public void testReloadEnquestaNonPresentInPersistence()
    {
        Enquesta e = new Enquesta("NonPersistedReload");
        enquestaCtrl.AddEnquesta(e);
        int id = e.getId();

        // Attempt to reload without saving first
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.reloadEnquesta(id));

        // Cleanup
        try { enquestaCtrl.DeleteEnquesta(id); } catch (EnquestaNoExisteix ex) {}
    }
}
