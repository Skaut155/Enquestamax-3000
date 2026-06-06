package DomainTests;

import Domain.Exceptions.RespostaNoValida;
import Domain.Model.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestGeneraRespostaAPregunta {

    @Test
    public void generaRespostaOberta() {
        Pregunta p = new Pregunta(PreguntaOberta.getInstance());
        p.setOrdre(3);

        RespostaAPregunta<?> rap = p.generaResposta("Hola món");
        assertTrue(rap instanceof RespostaAPreguntaOberta);
        assertEquals(Integer.valueOf(3), rap.getOrdrePregunta());
        assertEquals("Hola món", ((RespostaAPreguntaOberta) rap).getResposta());
        assertFalse(rap.isEmpty());
    }

    @Test
    public void generaRespostaNumerica() {
        Pregunta p = new Pregunta(PreguntaNumerica.getInstance());
        p.setOrdre(2);

        RespostaAPregunta<?> rap = p.generaResposta("12.5");
        assertTrue(rap instanceof RespostaAPreguntaNumerica);
        assertEquals(Integer.valueOf(2), rap.getOrdrePregunta());
        assertEquals(12.5, ((RespostaAPreguntaNumerica) rap).getResposta(), 1e-9);
        assertFalse(rap.isEmpty());
    }

    @Test
    public void generaRespostaNumericaInvalidInputThrows() {
        Pregunta p = new Pregunta(PreguntaNumerica.getInstance());
        p.setOrdre(0);

        assertThrows(RespostaNoValida.class, () -> p.generaResposta("abc"));
    }

    @Test
    public void generaRespostaSeleccioUnicaOrdenada() {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
        p.setOrdre(1);
        // force size to be > sol
        p.afegirOpcio("Opció 1");
        p.afegirOpcio("Opció 2");
        p.afegirOpcio("Opció 3");

        RespostaAPregunta<?> rap = p.generaResposta("2");
        assertTrue(rap instanceof RespostaAPreguntaOrdenada);
        assertEquals(Integer.valueOf(1), rap.getOrdrePregunta());
        assertEquals(Integer.valueOf(1), ((RespostaAPreguntaOrdenada) rap).getResposta());
        assertFalse(rap.isEmpty());
    }

    @Test
    public void generaRespostaSeleccioUnicaNoOrdenada() {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p.setOrdre(4);
        // force size to be > sol
        p.afegirOpcio("Opció 1");
        p.afegirOpcio("Opció 2");
        p.afegirOpcio("Opció 3");

        RespostaAPregunta<?> rap = p.generaResposta("2");
        assertTrue(rap instanceof RespostaAPreguntaNoOrdenada);
        assertEquals(Integer.valueOf(4), rap.getOrdrePregunta());
        assertFalse(rap.isEmpty());

    }

    @Test
    public void generaRespostaSeleccioMultiple() {
        TipusPregunta tipus = spy(PreguntaSeleccioMultiple.getInstance());
        Pregunta p = new Pregunta(tipus);
        p.setOrdre(5);
        p.afegirOpcio("Opció 1");
        p.afegirOpcio( "Opció 2");
        p.afegirOpcio( "Opció 3");
        p.afegirOpcio( "Opció 4");
        RespostaAPregunta<?> rap = p.generaResposta("1 3 4");
        assertTrue(rap instanceof RespostaAPreguntaMultiple);
        assertEquals(Integer.valueOf(5), rap.getOrdrePregunta());
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(0); expected.add(2); expected.add(3);
        assertEquals(expected, ((RespostaAPreguntaMultiple) rap).getResposta());
        assertFalse(rap.isEmpty());
    }

    @Test
    public void generaRespostaSeleccioMultipleInvalidInputThrows() {
        Pregunta p = new Pregunta(PreguntaSeleccioMultiple.getInstance());
        p.setOrdre(6);

        assertThrows(RespostaNoValida.class, () -> p.generaResposta("1 x 3"));
    }

    // New tests to cover catch blocks and assert exact error messages
    @Test
    public void generaRespostaSeleccioUnicaOrdenadaInvalidInputThrowsWithMessage() {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
        p.setOrdre(7);

        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("x"));
        assertEquals("La resposta proporcionada no és vàlida per a una pregunta de selecció única ordenada.", ex.getMessage());
    }

    @Test
    public void generaRespostaSeleccioUnicaNoOrdenadaInvalidInputThrowsWithMessage() {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p.setOrdre(8);

        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("no-num"));
        assertEquals("La resposta proporcionada no és vàlida per a una pregunta de selecció única no ordenada.", ex.getMessage());
    }
    @Test
    public void generaRespostaBuidaEnPreguntaObligatoriaLlancaExcepcio() {
        Pregunta p = new Pregunta(PreguntaOberta.getInstance());
        p.setObligatoria(true); // Make it mandatory

        // Test with null
        RespostaNoValida exNull = assertThrows(RespostaNoValida.class, () -> p.generaResposta(null));
        assertEquals("La resposta és obligatòria, l'has de respondre.", exNull.getMessage());

        // Test with blank string
        RespostaNoValida exBlank = assertThrows(RespostaNoValida.class, () -> p.generaResposta("   "));
        assertEquals("La resposta és obligatòria, l'has de respondre.", exBlank.getMessage());
    }

    @Test
    public void respostaJaMarcada(){
        Pregunta p = new Pregunta(PreguntaSeleccioMultiple.getInstance());
        p.setOrdre(9);
        p.afegirOpcio("Opció 1");
        p.afegirOpcio("Opció 2");

        // Attempt to select the same option again
        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("1 1"));
        assertEquals("La resposta proporcionada ja està marcada", ex.getMessage());
    }

    @Test
    public void wrongFormat(){
        Pregunta p = new Pregunta(PreguntaSeleccioMultiple.getInstance());
        p.setOrdre(10);
        p.afegirOpcio("Opció 1");
        p.afegirOpcio("Opció 2");

        // Attempt to provide a non-integer input
        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("one two"));
        assertEquals("La resposta proporcionada no és vàlida per a una pregunta de selecció múltiple.", ex.getMessage());
    }

    @Test
    public void testIdExtreme(){
        Pregunta p = new Pregunta(PreguntaNumerica.getInstance());
        p.setOrdre(Integer.MAX_VALUE);

        RespostaAPregunta<?> rap = p.generaResposta("999999999");
        assertTrue(rap instanceof RespostaAPreguntaNumerica);
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), rap.getOrdrePregunta());
        assertEquals(999999999.0, ((RespostaAPreguntaNumerica) rap).getResposta(), 1e-9);
        assertFalse(rap.isEmpty());
    }

    @Test
    public void generaRespostaSeleccioMultipleOpcioNegativa() {
        Pregunta p = new Pregunta(PreguntaSeleccioMultiple.getInstance());
        p.afegirOpcio("Opció 1");
        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("-1"));
        assertEquals("La resposta proporcionada no és vàlida per a una pregunta de selecció múltiple.", ex.getMessage());
    }

    @Test
    public void generaRespostaSeleccioMultipleOpcioForaDeLimits() {
        Pregunta p = new Pregunta(PreguntaSeleccioMultiple.getInstance());
        p.afegirOpcio("Opció 1");
        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("2"));
        assertEquals("La resposta proporcionada no és vàlida per a una pregunta de selecció múltiple.", ex.getMessage());
    }

    @Test
    public void generaRespostaSeleccioUnicaOrdenadaOpcioNegativa() {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
        p.afegirOpcio("Opció 1");
        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("0"));
        assertEquals("La resposta proporcionada no és vàlida per a una pregunta de selecció única ordenada.", ex.getMessage());
    }

    @Test
    public void generaRespostaSeleccioUnicaOrdenadaOpcioForaDeLimits() {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
        p.afegirOpcio("Opció 1");
        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("2"));
        assertEquals("La resposta proporcionada no és vàlida per a una pregunta de selecció única ordenada.", ex.getMessage());
    }

    @Test
    public void generaRespostaSeleccioUnicaNoOrdenadaOpcioNegativa() {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p.afegirOpcio("Opció 1");
        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("0"));
        assertEquals("La resposta proporcionada no és vàlida per a una pregunta de selecció única no ordenada.", ex.getMessage());
    }

    @Test
    public void generaRespostaSeleccioUnicaNOOrdenadaOpcioForaDeLimits() {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p.afegirOpcio("Opció 1");
        RespostaNoValida ex = assertThrows(RespostaNoValida.class, () -> p.generaResposta("2"));
        assertEquals("La resposta proporcionada no és vàlida per a una pregunta de selecció única no ordenada.", ex.getMessage());
    }
}
