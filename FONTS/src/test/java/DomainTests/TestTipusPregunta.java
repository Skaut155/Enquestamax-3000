package DomainTests;


import Domain.Exceptions.RespostaNoValida;
import Domain.Model.*;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestTipusPregunta {

    private List<TipusPregunta> allImplementations;
    private List<TipusPregunta> typesWithOptions;
    private List<TipusPregunta> typesWithoutOptions;

    @Before
    public void setUp() {
        // All implementations of TipusPregunta
        allImplementations = Arrays.asList(
                PreguntaOberta.getInstance(),
                PreguntaNumerica.getInstance(),
                PreguntaSeleccioUnicaOrdenada.getInstance(),
                PreguntaSeleccioUnicaNoOrdenada.getInstance(),
                PreguntaSeleccioMultiple.getInstance()
        );

        // Types that support options
        typesWithOptions = Arrays.asList(
                PreguntaSeleccioUnicaOrdenada.getInstance(),
                PreguntaSeleccioUnicaNoOrdenada.getInstance(),
                PreguntaSeleccioMultiple.getInstance()
        );

        // Types that don't support options
        typesWithoutOptions = Arrays.asList(
                PreguntaOberta.getInstance(),
                PreguntaNumerica.getInstance()
        );
    }

    @Test
    public void allImplementationsAreSingletons() {
        // Test PreguntaOberta
        assertSame(PreguntaOberta.getInstance(), PreguntaOberta.getInstance());

        // Test PreguntaNumerica
        assertSame(PreguntaNumerica.getInstance(), PreguntaNumerica.getInstance());

        // Test PreguntaSeleccioUnicaOrdenada
        assertSame(PreguntaSeleccioUnicaOrdenada.getInstance(), PreguntaSeleccioUnicaOrdenada.getInstance());

        // Test PreguntaSeleccioUnicaNoOrdenada
        assertSame(PreguntaSeleccioUnicaNoOrdenada.getInstance(), PreguntaSeleccioUnicaNoOrdenada.getInstance());

        // Test PreguntaSeleccioMultiple
        assertSame(PreguntaSeleccioMultiple.getInstance(), PreguntaSeleccioMultiple.getInstance());
    }

    @Test
    public void typesWithOptionsAllowCheckOpcions() {
        for (TipusPregunta tipus : typesWithOptions) {
            try {
                tipus.checkOpcions();
                // Should not throw exception
                assertTrue("Type " + tipus.getClass().getSimpleName() + " should support options", true);
            } catch (UnsupportedOperationException e) {
                fail("Type " + tipus.getClass().getSimpleName() + " should support options but threw exception");
            }
        }
    }

    @Test
    public void typesWithoutOptionsThrowExceptionOnCheckOpcions() {
        for (TipusPregunta tipus : typesWithoutOptions) {
            assertThrows("Type " + tipus.getClass().getSimpleName() + " should not support options",
                    UnsupportedOperationException.class,
                    tipus::checkOpcions);
        }
    }

    @Test
    public void typesWithOptionsCanAddOptions() {
        for (TipusPregunta tipus : typesWithOptions) {
            Pregunta p = new Pregunta(tipus);
            // Should not throw exception
            p.afegirOpcio("Opció 1");
            p.afegirOpcio("Opció 2");
            assertEquals("Type " + tipus.getClass().getSimpleName() + " should have 2 options",
                    2, p.numOpcions());
        }
    }

    @Test
    public void typesWithoutOptionsCannotAddOptions() {
        for (TipusPregunta tipus : typesWithoutOptions) {
            Pregunta p = new Pregunta(tipus);
            assertThrows("Type " + tipus.getClass().getSimpleName() + " should not allow adding options",
                    UnsupportedOperationException.class,
                    () -> p.afegirOpcio("Opció"));
        }
    }

    @Test
    public void allTypesCanTransitionToSeleccioUnicaOrdenada() {
        for (TipusPregunta tipus : allImplementations) {
            Pregunta p = new Pregunta(tipus);
            p.getTipusPregunta().setPreguntaRespostaOberta(p);
            p.getTipusPregunta().setPreguntaRespostaOberta(p);
            p.getTipusPregunta().setPreguntaSeleccioUnicaNoOrdenada(p);
            p.getTipusPregunta().setPreguntaSeleccioUnicaNoOrdenada(p);
            p.getTipusPregunta().setPreguntaRespostaObertaNumerica(p);
            p.getTipusPregunta().setPreguntaRespostaObertaNumerica(p);
            p.getTipusPregunta().setPreguntaSeleccioMultiple(p);
            p.getTipusPregunta().setPreguntaSeleccioMultiple(p);
            p.getTipusPregunta().setPreguntaSeleccioUnicaOrdenada(p);
            p.getTipusPregunta().setPreguntaSeleccioUnicaOrdenada(p);
            // After transition, should be able to add options
            p.afegirOpcio("Opció");
            assertEquals(1, p.numOpcions());
        }
    }

    @Test
    public void allTypesCanTransitionToSeleccioUnicaNoOrdenada() {
        for (TipusPregunta tipus : allImplementations) {
            Pregunta p = new Pregunta(tipus);
            p.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
            // After transition, should be able to add options
            p.afegirOpcio("Opció");
            assertEquals(1, p.numOpcions());
        }
    }

    @Test
    public void allTypesCanTransitionToSeleccioMultiple() {
        for (TipusPregunta tipus : allImplementations) {
            Pregunta p = new Pregunta(tipus);
            p.setTipusPregunta(PreguntaSeleccioMultiple.getInstance());
            // After transition, should be able to add options
            p.afegirOpcio("Opció");
            assertEquals(1, p.numOpcions());
        }
    }

    @Test
    public void allTypesCanTransitionToRespostaOberta() {
        for (TipusPregunta tipus : allImplementations) {
            Pregunta p = new Pregunta(tipus);
            p.setTipusPregunta(PreguntaOberta.getInstance());
            // After transition, should not be able to add options
            assertThrows(UnsupportedOperationException.class, () -> p.afegirOpcio("Opció"));
        }
    }

    @Test
    public void allTypesCanTransitionToRespostaObertaNumerica() {
        for (TipusPregunta tipus : allImplementations) {
            Pregunta p = new Pregunta(tipus);
            p.setTipusPregunta(PreguntaNumerica.getInstance());
            // After transition, should not be able to add options
            assertThrows(UnsupportedOperationException.class, () -> p.afegirOpcio("Opció"));
        }
    }

    @Test
    public void transitionToSameStateIsIdempotent() {
        // Test each type transitioning to itself
        Pregunta p1 = new Pregunta(PreguntaOberta.getInstance());
        p1.setTipusPregunta(PreguntaOberta.getInstance());
        assertThrows(UnsupportedOperationException.class, () -> p1.afegirOpcio("Opció"));

        Pregunta p2 = new Pregunta(PreguntaNumerica.getInstance());
        p2.setTipusPregunta(PreguntaNumerica.getInstance());
        assertThrows(UnsupportedOperationException.class, () -> p2.afegirOpcio("Opció"));

        Pregunta p3 = new Pregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
        p3.setTipusPregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
        p3.afegirOpcio("Opció");
        assertEquals(1, p3.numOpcions());

        Pregunta p4 = new Pregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p4.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p4.afegirOpcio("Opció");
        assertEquals(1, p4.numOpcions());

        Pregunta p5 = new Pregunta(PreguntaSeleccioMultiple.getInstance());
        p5.setTipusPregunta(PreguntaSeleccioMultiple.getInstance());
        p5.afegirOpcio("Opció");
        assertEquals(1, p5.numOpcions());
    }

    @Test
    public void transitionsPreserveQuestionData() {
        for (TipusPregunta sourceType : allImplementations) {
            for (TipusPregunta targetType : typesWithOptions) {
                Pregunta p = new Pregunta(sourceType);
                p.setTitol("Test Question");
                p.setObligatoria(true);
                p.setOrdre(5);

                // Transition to target type
                p.setTipusPregunta(targetType);

                // Check that data is preserved
                assertEquals("Test Question", p.getTitol());
                assertFalse(p.isOptional());
                assertEquals(5, p.getOrdre());
            }
        }
    }

    @Test
    public void checkOpcionsExceptionMessagesAreCorrect() {
        try {
            PreguntaOberta.getInstance().checkOpcions();
            fail("Should throw exception");
        } catch (UnsupportedOperationException e) {
            assertEquals("Les preguntes obertes no tenen opcions.", e.getMessage());
        }

        try {
            PreguntaNumerica.getInstance().checkOpcions();
            fail("Should throw exception");
        } catch (UnsupportedOperationException e) {
            assertEquals("Les preguntes numèriques no tenen opcions.", e.getMessage());
        }
    }

    @Test
    public void allImplementationsAreNotNull() {
        for (TipusPregunta tipus : allImplementations) {
            assertNotNull("Implementation should not be null: " + tipus.getClass().getSimpleName(), tipus);
        }
    }

    @Test
    public void multipleTransitionsWork() {
        Pregunta p = new Pregunta(PreguntaOberta.getInstance());

        // Oberta -> Selecció Única Ordenada
        p.getTipusPregunta().setPreguntaSeleccioUnicaOrdenada(p);
        p.afegirOpcio("Opció 1");
        assertEquals(1, p.numOpcions());

        // Selecció Única Ordenada -> Selecció Múltiple
        p.getTipusPregunta().setPreguntaSeleccioMultiple(p);
        p.afegirOpcio("Opció 2");
        assertEquals(2, p.numOpcions());

        // Selecció Múltiple -> Numèrica
        p.getTipusPregunta().setPreguntaRespostaObertaNumerica(p);
        assertThrows(UnsupportedOperationException.class, () -> p.numOpcions());
        assertThrows(UnsupportedOperationException.class, () -> p.afegirOpcio("Opció"));


        // Numèrica -> Selecció Única No Ordenada
        p.getTipusPregunta().setPreguntaSeleccioUnicaNoOrdenada(p);
        p.afegirOpcio("Opció 3");
        assertEquals(3, p.numOpcions());
    }

    @Test
    public void testGeneraRespostaBuidaPerPreguntaOberta() throws RespostaNoValida {
        Pregunta p = new Pregunta(PreguntaOberta.getInstance());
        p.setObligatoria(false);
        RespostaAPregunta<?> resposta = p.generaResposta("");
        assertTrue(resposta.getResposta() instanceof String);
        assertEquals("", resposta.getResposta());
    }

    @Test
    public void testGeneraRespostaBuidaPerPreguntaNumerica() throws RespostaNoValida {
        Pregunta p = new Pregunta(PreguntaNumerica.getInstance());
        p.setObligatoria(false);
        RespostaAPregunta<?> resposta = p.generaResposta("");
        assertNull(resposta.getResposta());
    }

    @Test
    public void testGeneraRespostaBuidaPerPreguntaSeleccioUnicaOrdenada() throws RespostaNoValida {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
        p.setObligatoria(false);
        p.afegirOpcio("a");
        RespostaAPregunta<?> resposta = p.generaResposta("");
        assertTrue(resposta.getResposta() instanceof Integer);
        assertEquals(-1, resposta.getResposta());
    }

    @Test
    public void testGeneraRespostaBuidaPerPreguntaSeleccioUnicaNoOrdenada() throws RespostaNoValida {
        Pregunta p = new Pregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p.setObligatoria(false);
        p.afegirOpcio("a");
        RespostaAPregunta<?> resposta = p.generaResposta("");
        assertTrue(resposta.getResposta() instanceof Integer);
        assertEquals(-1, resposta.getResposta());
    }

    @Test
    public void testGeneraRespostaBuidaPerPreguntaSeleccioMultiple() throws RespostaNoValida {
        Pregunta p = new Pregunta(PreguntaSeleccioMultiple.getInstance());
        p.setObligatoria(false);
        p.afegirOpcio("a");
        RespostaAPregunta<?> resposta = p.generaResposta("");
        assertTrue(resposta.getResposta() instanceof ArrayList);
        assertTrue(((ArrayList<?>) resposta.getResposta()).isEmpty());
    }

    @Test
    public void testToStringPreguntaOpcional() {
        Pregunta p = new Pregunta(PreguntaOberta.getInstance());
        p.setOrdre(0);
        p.setTitol("Test Title");
        p.setObligatoria(false);
        String expected = "[#1] Test Title  - tipus: OBERTA\n";
        assertEquals(expected, p.toString());
    }

    @Test
    public void testToStringPreguntaObligatoria() {
        Pregunta p = new Pregunta(PreguntaOberta.getInstance());
        p.setOrdre(0);
        p.setTitol("Test Title");
        p.setObligatoria(true);
        String expected = "[#1] Test Title* - tipus: OBERTA\n";
    }
}