package DataTests;

import Data.Controllers.NoDBCtrl.EnquestaCtrlNoDB;
import Domain.Model.Enquesta;
import Domain.Exceptions.EnquestaJaExisteix;
import Domain.Exceptions.EnquestaNoExisteix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the EnquestaCtrlNoDB class.
 * Tests in-memory management of Enquesta instances.
 */
public class TestEnquestaCtrlNoDB {

    private EnquestaCtrlNoDB enquestaCtrl;

    @Before
    public void setUp() {
        enquestaCtrl = new EnquestaCtrlNoDB();
    }

    @Test
    public void testAddEnquestaSuccessfully() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);

        Enquesta retrieved = enquestaCtrl.GetEnquesta(enquesta.getId());
        assertNotNull("Retrieved enquesta should not be null", retrieved);
        assertEquals("Retrieved enquesta should have same name", "Test Survey", retrieved.getNom());
    }


    @Test
    public void testGetEnquestaSuccessfully() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);

        Enquesta retrieved = enquestaCtrl.GetEnquesta(enquesta.getId());
        assertSame("Retrieved enquesta should be the same instance", enquesta, retrieved);
    }

    @Test(expected = EnquestaNoExisteix.class)
    public void testGetEnquestaNonExistent() throws EnquestaNoExisteix {
        enquestaCtrl.GetEnquesta(999);
    }

    @Test
    public void testDeleteEnquestaSuccessfully() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        int id = enquesta.getId();

        enquestaCtrl.AddEnquesta(enquesta);
        enquestaCtrl.DeleteEnquesta(id);

        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.GetEnquesta(id));
    }

    @Test(expected = EnquestaNoExisteix.class)
    public void testDeleteEnquestaNonExistent() throws EnquestaNoExisteix {
        enquestaCtrl.DeleteEnquesta(999);
    }

    @Test
    public void testAddMultipleEnquestas() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta1 = new Enquesta("Survey 1");
        Enquesta enquesta2 = new Enquesta("Survey 2");
        Enquesta enquesta3 = new Enquesta("Survey 3");

        enquestaCtrl.AddEnquesta(enquesta1);
        enquestaCtrl.AddEnquesta(enquesta2);
        enquestaCtrl.AddEnquesta(enquesta3);

        assertEquals("Survey 1", enquestaCtrl.GetEnquesta(enquesta1.getId()).getNom());
        assertEquals("Survey 2", enquestaCtrl.GetEnquesta(enquesta2.getId()).getNom());
        assertEquals("Survey 3", enquestaCtrl.GetEnquesta(enquesta3.getId()).getNom());
    }

    @Test
    public void testAddEnquestaWithEmptyName() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("");
        enquestaCtrl.AddEnquesta(enquesta);

        Enquesta retrieved = enquestaCtrl.GetEnquesta(enquesta.getId());
        assertNotNull("Should be able to add and retrieve enquesta with empty name", retrieved);
        assertEquals("", retrieved.getNom());
    }

    @Test
    public void testGetEnquestaReturnsCorrectInstance() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta1 = new Enquesta("Survey 1");
        Enquesta enquesta2 = new Enquesta("Survey 2");

        enquestaCtrl.AddEnquesta(enquesta1);
        enquestaCtrl.AddEnquesta(enquesta2);

        Enquesta retrieved1 = enquestaCtrl.GetEnquesta(enquesta1.getId());
        Enquesta retrieved2 = enquestaCtrl.GetEnquesta(enquesta2.getId());

        assertSame("Should return exact instance of enquesta1", enquesta1, retrieved1);
        assertSame("Should return exact instance of enquesta2", enquesta2, retrieved2);
        assertNotSame("Different enquestas should be different instances", retrieved1, retrieved2);
    }

    @Test
    public void testDeleteAndReAddEnquesta() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta1 = new Enquesta("Survey 1");
        int id1 = enquesta1.getId();

        enquestaCtrl.AddEnquesta(enquesta1);
        enquestaCtrl.DeleteEnquesta(id1);

        // Create new enquesta (will have different ID)
        Enquesta enquesta2 = new Enquesta("Survey 2");
        enquestaCtrl.AddEnquesta(enquesta2);

        // Should be able to retrieve the new one
        Enquesta retrieved = enquestaCtrl.GetEnquesta(enquesta2.getId());
        assertEquals("Survey 2", retrieved.getNom());
    }

    @Test(expected = EnquestaNoExisteix.class)
    public void testDeleteEnquestaTwice() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        int id = enquesta.getId();

        enquestaCtrl.AddEnquesta(enquesta);
        enquestaCtrl.DeleteEnquesta(id);
        enquestaCtrl.DeleteEnquesta(id); // Should throw
    }

    @Test
    public void testAddEnquestaWithSpecialCharacters() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test @#$ Survey 123!");
        enquestaCtrl.AddEnquesta(enquesta);

        Enquesta retrieved = enquestaCtrl.GetEnquesta(enquesta.getId());
        assertEquals("Test @#$ Survey 123!", retrieved.getNom());
    }

    @Test
    public void testAddEnquestaWithLongName() throws EnquestaJaExisteix, EnquestaNoExisteix {
        String longName = "a".repeat(1000);
        Enquesta enquesta = new Enquesta(longName);
        enquestaCtrl.AddEnquesta(enquesta);

        Enquesta retrieved = enquestaCtrl.GetEnquesta(enquesta.getId());
        assertEquals(longName, retrieved.getNom());
    }

    @Test
    public void testMultipleEnquestaControllers() throws EnquestaJaExisteix, EnquestaNoExisteix {
        EnquestaCtrlNoDB ctrl1 = new EnquestaCtrlNoDB();
        EnquestaCtrlNoDB ctrl2 = new EnquestaCtrlNoDB();

        Enquesta enquesta1 = new Enquesta("Survey 1");
        Enquesta enquesta2 = new Enquesta("Survey 2");

        ctrl1.AddEnquesta(enquesta1);
        ctrl2.AddEnquesta(enquesta2);

        // ctrl1 should only have enquesta1
        assertEquals("Survey 1", ctrl1.GetEnquesta(enquesta1.getId()).getNom());

        // ctrl2 should only have enquesta2
        assertEquals("Survey 2", ctrl2.GetEnquesta(enquesta2.getId()).getNom());
    }

    @Test(expected = EnquestaNoExisteix.class)
    public void testControllerIsolation() throws EnquestaJaExisteix, EnquestaNoExisteix {
        EnquestaCtrlNoDB ctrl1 = new EnquestaCtrlNoDB();
        EnquestaCtrlNoDB ctrl2 = new EnquestaCtrlNoDB();

        Enquesta enquesta = new Enquesta("Test Survey");
        ctrl1.AddEnquesta(enquesta);

        // Should throw - enquesta not in ctrl2
        ctrl2.GetEnquesta(enquesta.getId());
    }

    @Test
    public void testAddManyEnquestas() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta[] enquestas = new Enquesta[100];

        for (int i = 0; i < 100; i++) {
            enquestas[i] = new Enquesta("Survey " + i);
            enquestaCtrl.AddEnquesta(enquestas[i]);
        }

        // Verify all were added
        for (int i = 0; i < 100; i++) {
            Enquesta retrieved = enquestaCtrl.GetEnquesta(enquestas[i].getId());
            assertNotNull("Enquesta " + i + " should exist", retrieved);
            assertEquals("Survey " + i, retrieved.getNom());
        }
    }

    @Test
    public void testDeleteMultipleEnquestas() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta1 = new Enquesta("Survey 1");
        Enquesta enquesta2 = new Enquesta("Survey 2");
        Enquesta enquesta3 = new Enquesta("Survey 3");

        enquestaCtrl.AddEnquesta(enquesta1);
        enquestaCtrl.AddEnquesta(enquesta2);
        enquestaCtrl.AddEnquesta(enquesta3);

        enquestaCtrl.DeleteEnquesta(enquesta2.getId());

        // enquesta1 and enquesta3 should still exist
        assertNotNull(enquestaCtrl.GetEnquesta(enquesta1.getId()));
        assertNotNull(enquestaCtrl.GetEnquesta(enquesta3.getId()));

        // enquesta2 should not exist
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.GetEnquesta(enquesta2.getId()));
    }


    @Test
    public void testExceptionMessageForNonExistentEnquesta() {
        EnquestaNoExisteix exception = assertThrows(EnquestaNoExisteix.class,
            () -> enquestaCtrl.GetEnquesta(999));
        assertTrue("Exception message should contain enquesta id",
                  exception.getMessage().contains("999"));
    }

    @Test
    public void testExceptionMessageForDeleteNonExistent() {
        EnquestaNoExisteix exception = assertThrows(EnquestaNoExisteix.class,
            () -> enquestaCtrl.DeleteEnquesta(999));
        assertTrue("Exception message should contain enquesta id",
                  exception.getMessage().contains("999"));
    }

    @Test
    public void testGetEnquestaWithNegativeId() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.GetEnquesta(-1));
    }

    @Test
    public void testDeleteEnquestaWithNegativeId() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.DeleteEnquesta(-1));
    }

    // ==================== Tests for updateEnquesta ====================

    @Test
    public void testUpdateEnquestaSuccessfully() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);

        // updateEnquesta should not throw when enquesta exists
        enquestaCtrl.updateEnquesta(enquesta.getId());
    }

    @Test
    public void testUpdateEnquestaNonExistent() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.updateEnquesta(999));
    }

    @Test
    public void testUpdateEnquestaExceptionMessage() {
        EnquestaNoExisteix exception = assertThrows(EnquestaNoExisteix.class,
            () -> enquestaCtrl.updateEnquesta(999));
        assertTrue("Exception message should contain enquesta id",
                  exception.getMessage().contains("999"));
    }

    @Test
    public void testUpdateEnquestaMultipleTimes() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);

        // Multiple updates should work
        enquestaCtrl.updateEnquesta(enquesta.getId());
        enquestaCtrl.updateEnquesta(enquesta.getId());
        enquestaCtrl.updateEnquesta(enquesta.getId());
    }

    @Test
    public void testUpdateDifferentEnquestas() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta1 = new Enquesta("Survey 1");
        Enquesta enquesta2 = new Enquesta("Survey 2");

        enquestaCtrl.AddEnquesta(enquesta1);
        enquestaCtrl.AddEnquesta(enquesta2);

        enquestaCtrl.updateEnquesta(enquesta1.getId());
        enquestaCtrl.updateEnquesta(enquesta2.getId());
    }

    @Test
    public void testUpdateEnquestaWithNegativeId() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.updateEnquesta(-1));
    }

    @Test
    public void testUpdateEnquestaAfterDelete() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);
        int id = enquesta.getId();

        enquestaCtrl.DeleteEnquesta(id);

        // Update should throw after delete
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.updateEnquesta(id));
    }

    @Test
    public void testUpdateEnquestaWithZeroId() {
        // First enquesta gets id 0, so this should work if we add one first
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);

        // Should not throw since id 0 exists
        enquestaCtrl.updateEnquesta(0);
    }

    // ==================== Tests for reloadEnquesta ====================

    @Test
    public void testReloadEnquestaSuccessfully() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);

        // reloadEnquesta should not throw when enquesta exists
        enquestaCtrl.reloadEnquesta(enquesta.getId());
    }

    @Test
    public void testReloadEnquestaNonExistent() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.reloadEnquesta(999));
    }

    @Test
    public void testReloadEnquestaExceptionMessage() {
        EnquestaNoExisteix exception = assertThrows(EnquestaNoExisteix.class,
            () -> enquestaCtrl.reloadEnquesta(999));
        assertTrue("Exception message should contain enquesta id",
                  exception.getMessage().contains("999"));
    }

    @Test
    public void testReloadEnquestaMultipleTimes() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);

        // Multiple reloads should work
        enquestaCtrl.reloadEnquesta(enquesta.getId());
        enquestaCtrl.reloadEnquesta(enquesta.getId());
        enquestaCtrl.reloadEnquesta(enquesta.getId());
    }

    @Test
    public void testReloadDifferentEnquestas() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta1 = new Enquesta("Survey 1");
        Enquesta enquesta2 = new Enquesta("Survey 2");

        enquestaCtrl.AddEnquesta(enquesta1);
        enquestaCtrl.AddEnquesta(enquesta2);

        enquestaCtrl.reloadEnquesta(enquesta1.getId());
        enquestaCtrl.reloadEnquesta(enquesta2.getId());
    }

    @Test
    public void testReloadEnquestaWithNegativeId() {
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.reloadEnquesta(-1));
    }

    @Test
    public void testReloadEnquestaAfterDelete() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);
        int id = enquesta.getId();

        enquestaCtrl.DeleteEnquesta(id);

        // Reload should throw after delete
        assertThrows(EnquestaNoExisteix.class, () -> enquestaCtrl.reloadEnquesta(id));
    }

    @Test
    public void testReloadEnquestaWithZeroId() {
        // First enquesta gets id 0, so this should work if we add one first
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);

        // Should not throw since id 0 exists
        enquestaCtrl.reloadEnquesta(0);
    }

    @Test
    public void testUpdateAndReloadSequence() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta enquesta = new Enquesta("Test Survey");
        enquestaCtrl.AddEnquesta(enquesta);

        enquestaCtrl.updateEnquesta(enquesta.getId());
        enquestaCtrl.reloadEnquesta(enquesta.getId());
        enquestaCtrl.updateEnquesta(enquesta.getId());

        // Enquesta should still be retrievable
        Enquesta retrieved = enquestaCtrl.GetEnquesta(enquesta.getId());
        assertNotNull(retrieved);
    }

    @Test
    public void testUpdateAndReloadAllEnquestas() throws EnquestaJaExisteix, EnquestaNoExisteix {
        Enquesta[] enquestas = new Enquesta[10];

        for (int i = 0; i < 10; i++) {
            enquestas[i] = new Enquesta("Survey " + i);
            enquestaCtrl.AddEnquesta(enquestas[i]);
        }

        // Update and reload all
        for (int i = 0; i < 10; i++) {
            enquestaCtrl.updateEnquesta(enquestas[i].getId());
            enquestaCtrl.reloadEnquesta(enquestas[i].getId());
        }

        // Verify all still exist
        for (int i = 0; i < 10; i++) {
            assertNotNull(enquestaCtrl.GetEnquesta(enquestas[i].getId()));
        }
    }
}
