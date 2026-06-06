package DomainTests;

import Domain.Controllers.IAdminCtrl;
import Domain.Model.Admin;
import Domain.Model.Enquesta;
import Domain.Factories.CtrlFactory;
import Domain.Transactions.TxImportarEnquesta;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestTxImportarEnquesta {
    String pathToFile;
    String nomAdmin = "test_admin1";
    private IAdminCtrl iAdminCtrl;
    @Before
    public void setUp() {
        pathToFile = "test_import_enquesta.json";

        // create user admin1 in the system for the test, then delete it after the test

        Admin a = new Admin(nomAdmin,"password123");
        iAdminCtrl = CtrlFactory.getInstance().GetAdminCtrl();
        iAdminCtrl.AddAdmin(a);
    }

    @After
    public void tearDown() {
        // remove admin1 from the system after the test
        iAdminCtrl.deleteAdmin(nomAdmin);
    }

    @Test
    public void testImportarEnquesta() {
        TxImportarEnquesta txImportar = new TxImportarEnquesta(pathToFile,nomAdmin);
        txImportar.execute();

        Admin a = iAdminCtrl.GetAdmin(nomAdmin);
        assertTrue(a.getEnquestesIds().size() == 1);

        Enquesta e = CtrlFactory.getInstance().GetEnquestaCtrl().GetEnquesta(a.getEnquestesIds().get(0));
        // start testing
        assertEquals("E1", e.getNom());
//        assertEquals(1, e.getId());
        assertEquals("Quina edat tens?", e.getPregunta(0).getTitol());
        assertEquals("Quin és el teu color preferit?", e.getPregunta(1).getTitol());
        // Also check options count for the second question
        assertEquals(3, e.getPregunta(1).getOpcions().size());
        assertTrue(e.getPregunta(1).getOpcions().stream().anyMatch(o -> o.getText().equals("Vermell")));
        assertTrue(e.getPregunta(1).getOpcions().stream().anyMatch(o -> o.getText().equals("Blau")));
        assertTrue(e.getPregunta(1).getOpcions().stream().anyMatch(o -> o.getText().equals("Verd")));
    }
}
