package DomainTests;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.Enquesta;
import Domain.Transactions.DomainController;
import Domain.Transactions.TxCancelarEdicioEnquesta;
import Domain.Transactions.TxGuardarEnquesta;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for TxCancelarEdicioEnquesta.
 * Tests that canceling an edition properly reverts unsaved changes.
 */
public class TestTxCancelarEdicioEnquesta {

    private DomainController dc;
    private IEnquestaCtrl enquestaCtrl;

    @Before
    public void setUp() {
        dc = new DomainController();
        enquestaCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();

        try{
            //ensure the admin does not exist
            dc.eliminarAdmin("testAdmin");
        } catch (Exception e){
            //do nothing
        }

        //create admin
        dc.registrarAdmin("testAdmin", "password1");
    }

    @Test
    public void testCancelarEdicioEnquesta() {

        // Step 1: Create a new enquesta
        int idEnquesta = dc.crearEnquesta("Test Enquesta");

        // Step 2: Add one question
        dc.afegirPregunta(idEnquesta);
        dc.modificarPregunta(idEnquesta, 0, "Quina és la teva edat?");
        dc.canviarTipusPregunta(idEnquesta, 0, "NUMERICA");

        // Verify the enquesta has 1 question
        Enquesta enquesta = enquestaCtrl.GetEnquesta(idEnquesta);
        assertEquals("Enquesta should have 1 question before saving", 1, enquesta.numPreguntes());
        assertEquals("First question should have correct text", "Quina és la teva edat?", enquesta.getPregunta(0).getTitol());

        // Step 3: Save the enquesta using TxGuardarEnquesta
        TxGuardarEnquesta txGuardar = new TxGuardarEnquesta(idEnquesta, "testAdmin");
        txGuardar.execute();

        // Verify it still has 1 question after saving
        enquesta = enquestaCtrl.GetEnquesta(idEnquesta);
        assertEquals("Enquesta should have 1 question after saving", 1, enquesta.numPreguntes());

        // Step 4: Add more questions (without saving)
        dc.afegirPregunta(idEnquesta);
        dc.modificarPregunta(idEnquesta, 1, "Quin és el teu nom?");
        dc.canviarTipusPregunta(idEnquesta, 1, "OBERTA");

        dc.afegirPregunta(idEnquesta);
        dc.modificarPregunta(idEnquesta, 2, "Quin és el teu cognom?");
        dc.canviarTipusPregunta(idEnquesta, 2, "OBERTA");

        // Verify the enquesta now has 3 questions
        enquesta = enquestaCtrl.GetEnquesta(idEnquesta);
        assertEquals("Enquesta should have 3 questions before canceling", 3, enquesta.numPreguntes());

        // Step 5: Cancel the edition using TxCancelarEdicioEnquesta
        TxCancelarEdicioEnquesta txCancelar = new TxCancelarEdicioEnquesta(idEnquesta);
        txCancelar.execute();

        // Step 6: Verify that only the original question remains
        enquesta = enquestaCtrl.GetEnquesta(idEnquesta);
        assertEquals("Enquesta should have 1 question after canceling", 1, enquesta.numPreguntes());
        assertEquals("First question should still have correct text", "Quina és la teva edat?", enquesta.getPregunta(0).getTitol());

        // Verify the newly added questions don't appear
        try {
            enquesta.getPregunta(1);
            fail("Should throw exception when accessing question at index 1");
        } catch (Exception e) {
            // Expected behavior - question 1 should not exist
            assertTrue("Exception should be about non-existent question", e.getMessage().contains("No existeix"));
        }

        try {
            enquesta.getPregunta(2);
            fail("Should throw exception when accessing question at index 2");
        } catch (Exception e) {
            // Expected behavior - question 2 should not exist
            assertTrue("Exception should be about non-existent question", e.getMessage().contains("No existeix"));
        }
    }

    @After
    public void tearDown() {
        try {
            //eliminate the admin (and all its enquestas)
            dc.eliminarAdmin("testAdmin");
        } catch (Exception e) {
            //do nothing
        }
    }
}
