package DomainTests;

import Domain.Factories.NoDBCtrlFactory;
import Domain.Controllers.IAdminCtrl;
import Domain.Controllers.IEnquestaCtrl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the NoDBCtrlFactory class.
 * Tests the creation and management of in-memory controller instances.
 */
public class TestNoDBCtrlFactory {

    private NoDBCtrlFactory factory;

    @Before
    public void setUp() {
        factory = new NoDBCtrlFactory();
    }

    @Test
    public void testConstructorInitializesControllers() {
        assertNotNull("Factory should be initialized", factory);
        assertNotNull("Admin controller should be initialized", factory.GetAdminCtrl());
        assertNotNull("Enquesta controller should be initialized", factory.GetEnquestaCtrl());
    }

    @Test
    public void testGetAdminCtrlReturnsNonNull() {
        IAdminCtrl adminCtrl = factory.GetAdminCtrl();
        assertNotNull("GetAdminCtrl should return non-null instance", adminCtrl);
    }

    @Test
    public void testGetEnquestaCtrlReturnsNonNull() {
        IEnquestaCtrl enquestaCtrl = factory.GetEnquestaCtrl();
        assertNotNull("GetEnquestaCtrl should return non-null instance", enquestaCtrl);
    }

    @Test
    public void testGetAdminCtrlReturnsSameInstance() {
        IAdminCtrl ctrl1 = factory.GetAdminCtrl();
        IAdminCtrl ctrl2 = factory.GetAdminCtrl();
        assertSame("GetAdminCtrl should always return the same instance", ctrl1, ctrl2);
    }

    @Test
    public void testGetEnquestaCtrlReturnsSameInstance() {
        IEnquestaCtrl ctrl1 = factory.GetEnquestaCtrl();
        IEnquestaCtrl ctrl2 = factory.GetEnquestaCtrl();
        assertSame("GetEnquestaCtrl should always return the same instance", ctrl1, ctrl2);
    }

    @Test
    public void testDifferentFactoryInstancesHaveDifferentControllers() {
        NoDBCtrlFactory factory1 = new NoDBCtrlFactory();
        NoDBCtrlFactory factory2 = new NoDBCtrlFactory();

        IAdminCtrl admin1 = factory1.GetAdminCtrl();
        IAdminCtrl admin2 = factory2.GetAdminCtrl();

        assertNotSame("Different factories should have different admin controllers", admin1, admin2);

        IEnquestaCtrl enquesta1 = factory1.GetEnquestaCtrl();
        IEnquestaCtrl enquesta2 = factory2.GetEnquestaCtrl();

        assertNotSame("Different factories should have different enquesta controllers", enquesta1, enquesta2);
    }

    @Test
    public void testAdminAndEnquestaControllersAreIndependent() {
        IAdminCtrl adminCtrl = factory.GetAdminCtrl();
        IEnquestaCtrl enquestaCtrl = factory.GetEnquestaCtrl();

        assertNotNull("Admin controller should exist", adminCtrl);
        assertNotNull("Enquesta controller should exist", enquestaCtrl);
        assertNotSame("Admin and Enquesta controllers should be different objects", adminCtrl, enquestaCtrl);
    }

    @Test
    public void testControllersAreImmutable() {
        IAdminCtrl adminCtrl1 = factory.GetAdminCtrl();
        IEnquestaCtrl enquestaCtrl1 = factory.GetEnquestaCtrl();

        // Call multiple times to ensure immutability
        IAdminCtrl adminCtrl2 = factory.GetAdminCtrl();
        IEnquestaCtrl enquestaCtrl2 = factory.GetEnquestaCtrl();

        assertSame("Admin controller reference should be immutable", adminCtrl1, adminCtrl2);
        assertSame("Enquesta controller reference should be immutable", enquestaCtrl1, enquestaCtrl2);
    }

    @Test
    public void testMultipleFactoryInstances() {
        NoDBCtrlFactory factory1 = new NoDBCtrlFactory();
        NoDBCtrlFactory factory2 = new NoDBCtrlFactory();
        NoDBCtrlFactory factory3 = new NoDBCtrlFactory();

        assertNotNull(factory1);
        assertNotNull(factory2);
        assertNotNull(factory3);

        // Each factory should have its own controllers
        assertNotSame(factory1.GetAdminCtrl(), factory2.GetAdminCtrl());
        assertNotSame(factory2.GetAdminCtrl(), factory3.GetAdminCtrl());
        assertNotSame(factory1.GetEnquestaCtrl(), factory2.GetEnquestaCtrl());
    }
}

