package DomainTests;

import Domain.Exceptions.EnquestaNoConteResposta;
import Domain.Exceptions.PreguntaNoExisteix;
import Domain.Model.RespostaEnquesta;
import org.junit.Test;
import Domain.Model.Enquesta;
import Domain.Model.Pregunta;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestEnquesta {

    @Test
    public void constructorAndNameAndDate() {
        Enquesta e = new Enquesta("Survey A");
        assertEquals("Survey A", e.getNom());
        assertNotNull(e.getDate());
        assertTrue(e.getId() >= 0);
    }

    @Test
    public void addAndGetPreguntaAndInvalidIndexes(){
        Enquesta e = new Enquesta("QTest");
        Pregunta p0 = new Pregunta();
        Pregunta p1 = new Pregunta();

        e.afegirPregunta(p0);
        e.afegirPregunta(p1);

        // getPregunta should return the same instances
        assertSame(p0, e.getPregunta(0));
        assertSame(p1, e.getPregunta(1));

        // invalid indexes
        assertThrows(PreguntaNoExisteix.class, () -> e.getPregunta(-1));
        assertThrows(PreguntaNoExisteix.class, () -> e.getPregunta(2));
    }

    @Test
    public void swapPreguntes_updatesOrderAndPositions(){
        Enquesta e = new Enquesta("SwapTest");
        Pregunta a = new Pregunta();
        Pregunta b = new Pregunta();

        e.afegirPregunta(a); // ordre 0
        e.afegirPregunta(b); // ordre 1

        e.swapPreguntes(0, 1);

        // positions swapped
        assertSame(b, e.getPregunta(0));
        assertSame(a, e.getPregunta(1));

        // ordres updated by swapPreguntes
        assertEquals(0, b.getOrdre());
        assertEquals(1, a.getOrdre());
    }

    @Test
    public void eliminarPregunta_shiftsElementsAndThrowsOnBadIndex(){
        Enquesta e = new Enquesta("DelTest");
        Pregunta p0 = new Pregunta();
        Pregunta p1 = new Pregunta();
        Pregunta p2 = new Pregunta();

        e.afegirPregunta(p0);
        e.afegirPregunta(p1);
        e.afegirPregunta(p2);

        // remove middle element
        e.eliminarPregunta(1);

        // after removal, former index 2 should now be at index 1
        assertSame(p2, e.getPregunta(1));

        // index 2 is out of range now
        assertThrows(PreguntaNoExisteix.class, () -> e.getPregunta(2));

        // invalid negative and too-large removals throw
        assertThrows(PreguntaNoExisteix.class, () -> e.eliminarPregunta(-1));
        assertThrows(PreguntaNoExisteix.class, () -> e.eliminarPregunta(5));
    }

    @Test
    public void afegirPregunta_setsOrdre() {
        Enquesta e = new Enquesta("OrdTest");
        Pregunta p0 = new Pregunta();
        Pregunta p1 = new Pregunta();

        e.afegirPregunta(p0);
        e.afegirPregunta(p1);

        assertEquals(0, p0.getOrdre());
        assertEquals(1, p1.getOrdre());

        // sanity: getPregunta still returns same instances
        assertSame(p0, e.getPregunta(0));
        assertSame(p1, e.getPregunta(1));
    }

    @Test
    public void swapPreguntes_invalidIndices_throws() {
        Enquesta e = new Enquesta("BadSwap");
        Pregunta a = new Pregunta();
        e.afegirPregunta(a);

        // swap with out-of-range index should throw (uses list.get internally)
        assertThrows(IndexOutOfBoundsException.class, () -> e.swapPreguntes(0, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> e.swapPreguntes(-1, 0));
    }

    @Test
    public void setNom_updatesName() {
        Enquesta e = new Enquesta("Initial Name");
        assertEquals("Initial Name", e.getNom());

        e.setNom("Updated Name");
        assertEquals("Updated Name", e.getNom());
    }


    @Test
    public void numPreguntes_returnsCorrectCount() {
        Enquesta e = new Enquesta("CountTest");
        assertEquals(0, e.numPreguntes());

        e.afegirPregunta(new Pregunta());
        assertEquals(1, e.numPreguntes());

        e.afegirPregunta(new Pregunta());
        e.afegirPregunta(new Pregunta());
        assertEquals(3, e.numPreguntes());
    }

    @Test
    public void afegirNovaResposta_createsAndReturnsId() {
        Enquesta e = new Enquesta("ResponseTest");

        int id1 = e.afegirNovaResposta();
        int id2 = e.afegirNovaResposta();

        assertTrue(id1 >= 0);
        assertTrue(id2 >= 0);
        assertNotEquals(id1, id2);
    }

    @Test
    public void getIdsRespostes_returnsAllResponseIds() {
        Enquesta e = new Enquesta("ResponseIdsTest");

        ArrayList<Integer> ids = e.getIdsRespostes();
        assertTrue(ids.isEmpty());

        int id1 = e.afegirNovaResposta();
        int id2 = e.afegirNovaResposta();
        int id3 = e.afegirNovaResposta();

        ids = e.getIdsRespostes();
        assertEquals(3, ids.size());
        assertTrue(ids.contains(id1));
        assertTrue(ids.contains(id2));
        assertTrue(ids.contains(id3));
    }

    @Test
    public void getResposta_returnsCorrectResponse() {
        Enquesta e = new Enquesta("GetResponseTest");

        int id1 = e.afegirNovaResposta();
        int id2 = e.afegirNovaResposta();

        RespostaEnquesta r1 = e.getResposta(id1);
        RespostaEnquesta r2 = e.getResposta(id2);

        assertNotNull(r1);
        assertNotNull(r2);
        assertEquals(id1, r1.getId());
        assertEquals(id2, r2.getId());
        assertNotSame(r1, r2);
    }

    @Test
    public void getResposta_throwsRuntimeExceptionForInvalidId() {
        Enquesta e = new Enquesta("InvalidResponseTest");

        assertThrows(EnquestaNoConteResposta.class, () -> e.getResposta(99999));
    }

    @Test
    public void getAllRespostes_returnsAllResponses() {
        Enquesta e = new Enquesta("AllResponsesTest");

        ArrayList<RespostaEnquesta> responses = e.getAllRespostes();
        assertTrue(responses.isEmpty());

        int id1 = e.afegirNovaResposta();
        int id2 = e.afegirNovaResposta();
        int id3 = e.afegirNovaResposta();

        responses = e.getAllRespostes();
        assertEquals(3, responses.size());

        // Verify all responses are present
        boolean found1 = false, found2 = false, found3 = false;
        for (RespostaEnquesta r : responses) {
            if (r.getId() == id1) found1 = true;
            if (r.getId() == id2) found2 = true;
            if (r.getId() == id3) found3 = true;
        }
        assertTrue(found1 && found2 && found3);
    }

    @Test
    public void esborrarRespostes_removesAllResponses() {
        Enquesta e = new Enquesta("DeleteResponsesTest");

        e.afegirNovaResposta();
        e.afegirNovaResposta();
        e.afegirNovaResposta();

        assertEquals(3, e.getIdsRespostes().size());

        e.esborrarRespostes();

        assertEquals(0, e.getIdsRespostes().size());
        assertTrue(e.getAllRespostes().isEmpty());
    }

    @Test
    public void toString_containsCorrectInformation() {
        Enquesta e = new Enquesta("ToString Test");
        Pregunta p1 = new Pregunta();
        Pregunta p2 = new Pregunta();

        e.afegirPregunta(p1);
        e.afegirPregunta(p2);

        String str = e.toString();

        assertNotNull(str);
        assertTrue(str.contains("ToString Test"));
        assertTrue(str.contains("id: " + e.getId()));
        assertTrue(str.contains("Data de creacio"));
    }

    @Test
    public void eliminarPregunta_updatesOrdreCorrectly() throws PreguntaNoExisteix {
        Enquesta e = new Enquesta("OrdreUpdateTest");
        Pregunta p0 = new Pregunta();
        Pregunta p1 = new Pregunta();
        Pregunta p2 = new Pregunta();
        Pregunta p3 = new Pregunta();

        e.afegirPregunta(p0);
        e.afegirPregunta(p1);
        e.afegirPregunta(p2);
        e.afegirPregunta(p3);

        // Remove question at index 1
        e.eliminarPregunta(1);

        // Verify remaining questions have correct ordre
        assertEquals(0, e.getPregunta(0).getOrdre());
        assertEquals(1, e.getPregunta(1).getOrdre()); // was p2, now at index 1
        assertEquals(2, e.getPregunta(2).getOrdre()); // was p3, now at index 2
    }

    @Test
    public void swapPreguntes_withSameIndex_doesNothing() throws PreguntaNoExisteix {
        Enquesta e = new Enquesta("SameSwapTest");
        Pregunta p0 = new Pregunta();
        Pregunta p1 = new Pregunta();

        e.afegirPregunta(p0);
        e.afegirPregunta(p1);

        e.swapPreguntes(0, 0);

        // Should remain unchanged
        assertSame(p0, e.getPregunta(0));
        assertEquals(0, p0.getOrdre());
    }

    @Test
    public void eliminarPregunta_lastQuestion_works() throws PreguntaNoExisteix {
        Enquesta e = new Enquesta("DeleteLastTest");
        Pregunta p0 = new Pregunta();
        Pregunta p1 = new Pregunta();

        e.afegirPregunta(p0);
        e.afegirPregunta(p1);

        e.eliminarPregunta(1);

        assertEquals(1, e.numPreguntes());
        assertSame(p0, e.getPregunta(0));
    }

    @Test
    public void eliminarPregunta_firstQuestion_shiftsAll() throws PreguntaNoExisteix {
        Enquesta e = new Enquesta("DeleteFirstTest");
        Pregunta p0 = new Pregunta();
        Pregunta p1 = new Pregunta();
        Pregunta p2 = new Pregunta();

        e.afegirPregunta(p0);
        e.afegirPregunta(p1);
        e.afegirPregunta(p2);

        e.eliminarPregunta(0);

        assertEquals(2, e.numPreguntes());
        assertSame(p1, e.getPregunta(0));
        assertSame(p2, e.getPregunta(1));
        assertEquals(0, p1.getOrdre());
        assertEquals(1, p2.getOrdre());
    }

    @Test
    public void emptyEnquesta_operations() throws PreguntaNoExisteix {
        Enquesta e = new Enquesta("EmptyTest");

        assertEquals(0, e.numPreguntes());
        assertTrue(e.getIdsRespostes().isEmpty());
        assertTrue(e.getAllRespostes().isEmpty());

        // Operations on empty enquesta should throw
        assertThrows(PreguntaNoExisteix.class, () -> e.getPregunta(0));
        assertThrows(PreguntaNoExisteix.class, () -> e.eliminarPregunta(0));
    }

    @Test
    public void eliminarResposta_removesSpecificResponse() {
        Enquesta e = new Enquesta("DeleteSpecificResponseTest");

        int id1 = e.afegirNovaResposta();
        int id2 = e.afegirNovaResposta();
        int id3 = e.afegirNovaResposta();

        assertEquals(3, e.getIdsRespostes().size());

        e.eliminaResposta(id2);

        ArrayList<Integer> ids = e.getIdsRespostes();
        assertEquals(2, ids.size());
        assertTrue(ids.contains(id1));
        assertTrue(ids.contains(id3));
        assertFalse(ids.contains(id2));

        // Attempting to get the deleted response should throw
        assertThrows(EnquestaNoConteResposta.class, () -> e.getResposta(id2));
    }
}
