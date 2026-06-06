package DomainTests;

import Domain.Model.Admin;
import Domain.Exceptions.AdminJaTeEnquesta;
import Domain.Exceptions.AdminNoTeEnquesta;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestAdmin {

    @Test
    public void testConstructorAndGetters() {
        Admin admin = new Admin("testUser", "password");
        assertEquals("testUser", admin.getNom());
    }

    @Test
    public void testIniciarSessio() {
        Admin admin = new Admin("testUser", "password");
        assertTrue(admin.iniciarSessio("password"));
        assertFalse(admin.iniciarSessio("wrongpassword"));
    }

    @Test
    public void testAddEnquesta() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        assertTrue(admin.hasEnquesta(1));
    }

    @Test(expected = AdminJaTeEnquesta.class)
    public void testAddExistingEnquesta() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.addEnquesta(1);
    }

    @Test
    public void testRemoveEnquesta() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.removeEnquesta(1);
        assertFalse(admin.hasEnquesta(1));
    }

    @Test(expected = AdminNoTeEnquesta.class)
    public void testRemoveNonExistingEnquesta() throws AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.removeEnquesta(1);
    }

    @Test
    public void testHasEnquesta() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        assertFalse(admin.hasEnquesta(1));
        admin.addEnquesta(1);
        assertTrue(admin.hasEnquesta(1));
    }

    @Test
    public void testGetEnquestesIds() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.addEnquesta(2);
        ArrayList<Integer> ids = admin.getEnquestesIds();
        assertEquals(2, ids.size());
        assertTrue(ids.contains(1));
        assertTrue(ids.contains(2));
    }

    @Test
    public void testConstructorInitializesEmptyEnquestesList() {
        Admin admin = new Admin("testUser", "password");
        assertNotNull(admin.getEnquestesIds());
        assertEquals(0, admin.getEnquestesIds().size());
    }

    @Test
    public void testConstructorWithDifferentCredentials() {
        Admin admin1 = new Admin("user1", "pass1");
        Admin admin2 = new Admin("user2", "pass2");

        assertEquals("user1", admin1.getNom());
        assertEquals("user2", admin2.getNom());
        assertTrue(admin1.iniciarSessio("pass1"));
        assertTrue(admin2.iniciarSessio("pass2"));
        assertFalse(admin1.iniciarSessio("pass2"));
        assertFalse(admin2.iniciarSessio("pass1"));
    }

    @Test
    public void testIniciarSessioWithEmptyPassword() {
        Admin admin = new Admin("testUser", "");
        assertTrue(admin.iniciarSessio(""));
        assertTrue(admin.iniciarSessio(null));
        assertFalse(admin.iniciarSessio("password"));
    }

    @Test
    public void testIniciarSessioWithNullPassword() {
        Admin admin = new Admin("testUser", null);
        assertTrue(admin.iniciarSessio(null));
        assertFalse(admin.iniciarSessio("password"));
    }

    @Test
    public void testIniciarSessioWithNullGivenPassword() {
        Admin admin = new Admin("testUser", "password");
        assertFalse(admin.iniciarSessio(null));
    }

    @Test
    public void testIniciarSessioCaseSensitive() {
        Admin admin = new Admin("testUser", "Password123");
        assertTrue(admin.iniciarSessio("Password123"));
        assertFalse(admin.iniciarSessio("password123"));
        assertFalse(admin.iniciarSessio("PASSWORD123"));
    }

    @Test
    public void testAddMultipleEnquestesInOrder() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.addEnquesta(2);
        admin.addEnquesta(3);

        ArrayList<Integer> ids = admin.getEnquestesIds();
        assertEquals(3, ids.size());
        assertEquals(Integer.valueOf(1), ids.get(0));
        assertEquals(Integer.valueOf(2), ids.get(1));
        assertEquals(Integer.valueOf(3), ids.get(2));
    }

    @Test
    public void testAddEnquestaWithZeroId() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(0);
        assertTrue(admin.hasEnquesta(0));
    }

    @Test
    public void testAddEnquestaWithNegativeId() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(-1);
        assertTrue(admin.hasEnquesta(-1));
    }

    @Test(expected = AdminJaTeEnquesta.class)
    public void testAddExistingEnquestaMultipleTimes() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.addEnquesta(2);
        admin.addEnquesta(1); // Should throw
    }

    @Test
    public void testRemoveEnquestaFromMiddle() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.addEnquesta(2);
        admin.addEnquesta(3);

        admin.removeEnquesta(2);

        assertEquals(2, admin.getEnquestesIds().size());
        assertTrue(admin.hasEnquesta(1));
        assertFalse(admin.hasEnquesta(2));
        assertTrue(admin.hasEnquesta(3));
    }

    @Test
    public void testRemoveFirstEnquesta() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.addEnquesta(2);
        admin.addEnquesta(3);

        admin.removeEnquesta(1);

        assertEquals(2, admin.getEnquestesIds().size());
        assertFalse(admin.hasEnquesta(1));
        assertTrue(admin.hasEnquesta(2));
        assertTrue(admin.hasEnquesta(3));
    }

    @Test
    public void testRemoveLastEnquesta() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.addEnquesta(2);
        admin.addEnquesta(3);

        admin.removeEnquesta(3);

        assertEquals(2, admin.getEnquestesIds().size());
        assertTrue(admin.hasEnquesta(1));
        assertTrue(admin.hasEnquesta(2));
        assertFalse(admin.hasEnquesta(3));
    }

    @Test
    public void testRemoveOnlyEnquesta() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);

        admin.removeEnquesta(1);

        assertEquals(0, admin.getEnquestesIds().size());
        assertFalse(admin.hasEnquesta(1));
    }

    @Test(expected = AdminNoTeEnquesta.class)
    public void testRemoveNonExistingEnquestaFromNonEmpty() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.addEnquesta(2);
        admin.removeEnquesta(3); // Should throw
    }

    @Test(expected = AdminNoTeEnquesta.class)
    public void testRemoveEnquestaTwice() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.removeEnquesta(1);
        admin.removeEnquesta(1); // Should throw
    }

    @Test
    public void testHasEnquestaOnEmptyList() {
        Admin admin = new Admin("testUser", "password");
        assertFalse(admin.hasEnquesta(1));
        assertFalse(admin.hasEnquesta(0));
        assertFalse(admin.hasEnquesta(-1));
    }

    @Test
    public void testHasEnquestaWithMultipleEnquestas() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);
        admin.addEnquesta(5);
        admin.addEnquesta(10);

        assertTrue(admin.hasEnquesta(1));
        assertFalse(admin.hasEnquesta(2));
        assertTrue(admin.hasEnquesta(5));
        assertFalse(admin.hasEnquesta(7));
        assertTrue(admin.hasEnquesta(10));
    }

    @Test
    public void testGetEnquestesIdsReturnsInternalList() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(1);

        ArrayList<Integer> ids1 = admin.getEnquestesIds();
        ArrayList<Integer> ids2 = admin.getEnquestesIds();

        // Should return same reference
        assertSame(ids1, ids2);

        // Modifications to returned list affect internal state
        ids1.add(2);
        assertTrue(admin.hasEnquesta(2));
    }

    @Test
    public void testGetEnquestesIdsEmptyList() {
        Admin admin = new Admin("testUser", "password");
        ArrayList<Integer> ids = admin.getEnquestesIds();

        assertNotNull(ids);
        assertEquals(0, ids.size());
    }

    @Test
    public void testAddAndRemoveSequence() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");

        // Add several
        admin.addEnquesta(1);
        admin.addEnquesta(2);
        admin.addEnquesta(3);
        assertEquals(3, admin.getEnquestesIds().size());

        // Remove some
        admin.removeEnquesta(2);
        assertEquals(2, admin.getEnquestesIds().size());

        // Add more
        admin.addEnquesta(4);
        admin.addEnquesta(5);
        assertEquals(4, admin.getEnquestesIds().size());

        // Remove all
        admin.removeEnquesta(1);
        admin.removeEnquesta(3);
        admin.removeEnquesta(4);
        admin.removeEnquesta(5);
        assertEquals(0, admin.getEnquestesIds().size());
    }

    @Test
    public void testAdminWithLargeNumberOfEnquestas() throws AdminJaTeEnquesta {
        Admin admin = new Admin("testUser", "password");

        // Add 100 enquestas
        for (int i = 0; i < 100; i++) {
            admin.addEnquesta(i);
        }

        assertEquals(100, admin.getEnquestesIds().size());

        // Verify all are present
        for (int i = 0; i < 100; i++) {
            assertTrue(admin.hasEnquesta(i));
        }
    }

    @Test
    public void testRemoveEnquestaWithZeroId() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(0);
        admin.removeEnquesta(0);
        assertFalse(admin.hasEnquesta(0));
    }

    @Test
    public void testRemoveEnquestaWithNegativeId() throws AdminJaTeEnquesta, AdminNoTeEnquesta {
        Admin admin = new Admin("testUser", "password");
        admin.addEnquesta(-5);
        admin.removeEnquesta(-5);
        assertFalse(admin.hasEnquesta(-5));
    }
}
