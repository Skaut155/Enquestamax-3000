package DomainTests;

import Domain.Model.Opcio;
import Domain.Model.Pregunta;
import Domain.Model.PreguntaSeleccioMultiple;
import Domain.Model.PreguntaSeleccioUnicaNoOrdenada;
import Domain.Model.PreguntaSeleccioUnicaOrdenada;
import Domain.Model.TipusPregunta;
import Domain.Exceptions.OpcioNoExisteix;
import Domain.Exceptions.OrdreIncorrecte;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestPregunta {

    Pregunta p;
    Pregunta pselectmulti;
    TipusPregunta spyStateDefault;
    TipusPregunta spyStateMulti;

    @Before
    public void setUp() {
        p = new Pregunta();
        pselectmulti = new Pregunta(PreguntaSeleccioMultiple.getInstance());
        // Spy real states to keep valid behavior but verify interactions
        spyStateDefault = spy(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        spyStateMulti = spy(PreguntaSeleccioMultiple.getInstance());
        p.setTipusPregunta(spyStateDefault);
        pselectmulti.setTipusPregunta(spyStateMulti);
    }

    @Test
    public void doesNotAllowNullTitleInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            Pregunta p = new Pregunta();
            p.setTitol(null);
        });
    }

    @Test
    public void doesNotAllowBlankTitleInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            Pregunta p = new Pregunta();
            p.setTitol("   ");
        });
    }

    @Test
    public void constructorSetsFields() {
        Pregunta p = new Pregunta();
        p.setTitol("Quin nom tens?");
        p.setObligatoria(true);
        assert(p.getTitol().equals("Quin nom tens?"));
        assert(!p.isOptional());
    }

    @Test
    public void optionalQuestion(){
        Pregunta p = new Pregunta();
        p.setTitol("Quin nom tens?");
        p.setObligatoria(false);
        assert(p.isOptional());
    }

    @Test
    public void mandatoryQuestion(){
        Pregunta p = new Pregunta();
        p.setTitol("Quin nom tens?");
        p.setObligatoria(true);
        assert(!p.isOptional());
    }

    @Test
    public void getOpcionsWorks_andCallsCheckOpcions() {
        // Add real options via API
        pselectmulti.afegirOpcio("Blau");
        pselectmulti.afegirOpcio("Vermell");

        ArrayList<Opcio> ops = pselectmulti.getOpcions();
        assertEquals("Blau", ops.get(0).getText());
        assertEquals(0, ops.get(0).getOrdre());
        assertEquals("Vermell", ops.get(1).getText());
        assertEquals(1, ops.get(1).getOrdre());
        // verify that state.checkOpcions was invoked both on add and on get
        verify(spyStateMulti, atLeastOnce()).checkOpcions();
    }

    @Test
    public void getNumOpcionsWorks_andCallsCheckOpcions() {
        p.afegirOpcio("Blau");
        p.afegirOpcio("Vermell");

        assertEquals(2, p.numOpcions());
        verify(spyStateDefault, atLeastOnce()).checkOpcions();
    }

    @Test
    public void eliminarOpcioWorks_withRealOpcions() {
        p.afegirOpcio("Opció 1");
        p.afegirOpcio("Opció 2");

        assertEquals(2, p.numOpcions());
        p.eliminarOpcio(0);
        assertEquals(1, p.numOpcions());
        ArrayList<Opcio> ops = p.getOpcions();
        // After deletion, remaining option should be "Opció 2" with ordre renumbered to 0
        assertEquals("Opció 2", ops.get(0).getText());
        assertEquals(0, ops.get(0).getOrdre());
        verify(spyStateDefault, atLeastOnce()).checkOpcions();
    }

    @Test
    public void eliminarOpcioOutOfBoundsThrows() {
        p.afegirOpcio("Opció 1");
        p.afegirOpcio("Opció 2");

        assertEquals(2, p.numOpcions());
        assertThrows(OpcioNoExisteix.class, () -> p.eliminarOpcio(4));
        assertThrows(OpcioNoExisteix.class, () -> p.eliminarOpcio(-1));
    }

    @Test
    public void setOrdreWorks() {
        p.setOrdre(5);
        assertEquals(5, p.getOrdre());

        p.setOrdre(0);
        assertEquals(0, p.getOrdre());

        assertThrows(OrdreIncorrecte.class, () -> p.setOrdre(-1));
    }

    @Test
    public void afegirOpcioWithStringWorks() {
        assertEquals(0, p.numOpcions());

        p.afegirOpcio("Opció 1");
        assertEquals(1, p.numOpcions());

        p.afegirOpcio("Opció 2");
        assertEquals(2, p.numOpcions());

        ArrayList<Opcio> opcions = p.getOpcions();
        assertEquals("Opció 1", opcions.get(0).getText());
        assertEquals("Opció 2", opcions.get(1).getText());
    }

    @Test
    public void modificarOpcioWorks_updatesText() {
        // Arrange: add real options
        p.afegirOpcio("Opció 1"); // ordre 0
        p.afegirOpcio("Opció 2"); // ordre 1

        // Act
        assertTrue(p.modificarOpcio(1, "Opció Modificada"));

        // Assert: the option's text was updated
        ArrayList<Opcio> opcions = p.getOpcions();
        assertEquals("Opció Modificada", opcions.get(1).getText());
        assertEquals(2, p.numOpcions());
        // checkOpcions should be invoked during modificar
        verify(spyStateDefault, atLeastOnce()).checkOpcions();
    }

    @Test
    public void modificarOpcioWithEmptyStringDeletesOption() {
        p.afegirOpcio("Opció 1"); // ordre 0
        p.afegirOpcio("Opció 2"); // ordre 1
        assertEquals(2, p.numOpcions());

        assertTrue(p.modificarOpcio(1, "   "));
        assertEquals(1, p.numOpcions());
        ArrayList<Opcio> remaining = p.getOpcions();
        assertEquals(0, remaining.get(0).getOrdre());
        assertEquals("Opció 1", remaining.get(0).getText());
    }

    @Test
    public void modificarOpcioWithBlankStringDeletesOption() {
        p.afegirOpcio("Opció 1");
        assertEquals(1, p.numOpcions());

        assertTrue(p.modificarOpcio(0, ""));
        assertEquals(0, p.numOpcions());
    }

    @Test
    public void modificarOpcioNonExistentThrows() {
        p.afegirOpcio("Opció 1");
        assertThrows(OpcioNoExisteix.class, () -> p.modificarOpcio(999, "Nova Opció"));
        assertThrows(OpcioNoExisteix.class, () -> p.modificarOpcio(-1, "Nova Opció"));
    }

    @Test
    public void setTipusPreguntaWorks() {
        Pregunta pregunta = new Pregunta();

        // Test changing to different types
        assertTrue(pregunta.setTipusPregunta(PreguntaSeleccioMultiple.getInstance()));
        assertTrue(pregunta.setTipusPregunta(PreguntaSeleccioUnicaOrdenada.getInstance()));
    }

    @Test
    public void testToString_FormatsCorrectlyWithRealOpcions() {
        // Ensure known state code
        p.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p.setTitol("Quin és el teu color preferit?");
        // Obligatorietat false by default prints a trailing space
        // Add real options
        p.afegirOpcio("Vermell");
        p.afegirOpcio("Blau");

        String expectedHeader = "[#0] Quin és el teu color preferit?  - tipus: UNICA_NO_ORDENADA\n";
        String expected = expectedHeader +
                "\t[1] Vermell\n" +
                "\t[2] Blau\n";
        assertEquals(expected, p.toString());
    }

    @Test
    public void testToString_MandatoryHeaderFormatsWithAsterisk() throws OrdreIncorrecte {
        // Cover obligatoria == true branch in header creation
        Pregunta pregunta = new Pregunta();
        pregunta.setTitol("Pregunta obligatòria");
        pregunta.setObligatoria(true);
        pregunta.setOrdre(2);
        pregunta.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        // No options
        String expected = "[#3] Pregunta obligatòria* - tipus: UNICA_NO_ORDENADA\n";
        assertEquals(expected, pregunta.toString());
    }

    @Test
    public void testToString_OptionalHeaderSpaceWithOrdreAndNoOptions() throws OrdreIncorrecte {
        Pregunta pregunta = new Pregunta();
        pregunta.setTitol("Pregunta opcional sense opcions");
        pregunta.setObligatoria(false);
        pregunta.setOrdre(9);
        pregunta.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        String expected = "[#10] Pregunta opcional sense opcions  - tipus: UNICA_NO_ORDENADA\n"; // two spaces: one after title due to (obligatoria ? "*" : " ") and one before dash
        assertEquals(expected, pregunta.toString());
    }
}
