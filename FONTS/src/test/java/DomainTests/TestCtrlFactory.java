package DomainTests;

import Domain.Factories.CtrlFactory;
import Domain.Factories.NoDBCtrlFactory;
import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Unit tests for the CtrlFactory abstract factory class.
 * Tests singleton pattern behavior and factory instance retrieval.
 */
public class TestCtrlFactory {

    @Before
    public void setUp() throws Exception {
        // Reset the singleton instance before each test using reflection
        resetSingleton();
    }

    @After
    public void tearDown() throws Exception {
        // Clean up singleton instance after each test
        resetSingleton();
    }

    private void resetSingleton() throws Exception {
        Field instance = CtrlFactory.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void testGetInstanceReturnsNonNull() {
        CtrlFactory factory = CtrlFactory.getInstance();
        assertNotNull("Factory instance should not be null", factory);
    }

    @Test
    public void testGetInstanceReturnsSameInstance() {
        CtrlFactory factory1 = CtrlFactory.getInstance();
        CtrlFactory factory2 = CtrlFactory.getInstance();
        assertSame("getInstance should return the same instance", factory1, factory2);
    }


    @Test
    public void testSetInstanceChangesFactory() {
        CtrlFactory customFactory = new NoDBCtrlFactory();
        CtrlFactory.setInstance(customFactory);

        CtrlFactory retrievedFactory = CtrlFactory.getInstance();
        assertSame("Retrieved factory should be the custom factory", customFactory, retrievedFactory);
    }

    @Test
    public void testSetInstanceWithNull() throws Exception {
        // First create an instance
        CtrlFactory.getInstance();

        // Reset using reflection
        resetSingleton();

        // Getting instance again should create a new one
        CtrlFactory newFactory = CtrlFactory.getInstance();
        assertNotNull("Should create new instance after setting to null", newFactory);
    }

    @Test
    public void testGetAdminCtrlReturnsNonNull() {
        CtrlFactory factory = CtrlFactory.getInstance();
        IAdminCtrl adminCtrl = factory.GetAdminCtrl();
        assertNotNull("Admin controller should not be null", adminCtrl);
    }

    @Test
    public void testGetEnquestaCtrlReturnsNonNull() {
        CtrlFactory factory = CtrlFactory.getInstance();
        IEnquestaCtrl enquestaCtrl = factory.GetEnquestaCtrl();
        assertNotNull("Enquesta controller should not be null", enquestaCtrl);
    }

    @Test
    public void testGetAdminCtrlReturnsSameInstance() {
        CtrlFactory factory = CtrlFactory.getInstance();
        IAdminCtrl ctrl1 = factory.GetAdminCtrl();
        IAdminCtrl ctrl2 = factory.GetAdminCtrl();
        assertSame("Should return the same Admin controller instance", ctrl1, ctrl2);
    }

    @Test
    public void testGetEnquestaCtrlReturnsSameInstance() {
        CtrlFactory factory = CtrlFactory.getInstance();
        IEnquestaCtrl ctrl1 = factory.GetEnquestaCtrl();
        IEnquestaCtrl ctrl2 = factory.GetEnquestaCtrl();
        assertSame("Should return the same Enquesta controller instance", ctrl1, ctrl2);
    }

    @Test
    public void testMultipleFactoriesHaveDifferentControllers() {
        CtrlFactory factory1 = new NoDBCtrlFactory();
        CtrlFactory factory2 = new NoDBCtrlFactory();

        IAdminCtrl admin1 = factory1.GetAdminCtrl();
        IAdminCtrl admin2 = factory2.GetAdminCtrl();

        assertNotSame("Different factory instances should have different controllers", admin1, admin2);
    }

    @Test
    public void testSingletonPersistsAcrossMultipleGetInstanceCalls() {
        CtrlFactory factory1 = CtrlFactory.getInstance();
        IAdminCtrl adminCtrl1 = factory1.GetAdminCtrl();

        CtrlFactory factory2 = CtrlFactory.getInstance();
        IAdminCtrl adminCtrl2 = factory2.GetAdminCtrl();

        assertSame("Singleton should maintain same factory", factory1, factory2);
        assertSame("Controllers from singleton should be same", adminCtrl1, adminCtrl2);
    }
}
