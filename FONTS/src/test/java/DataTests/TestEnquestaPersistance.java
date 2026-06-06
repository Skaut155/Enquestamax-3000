package DataTests;

import Data.Persistance.EnquestaPersistance;
import Domain.Model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.Assert.*;

public class TestEnquestaPersistance {

    private EnquestaPersistance enquestaPersistance;

    private static final String TEST_DATA_DIR = "./.data/test_enquestes";

    @Before
    public void setUp() throws IOException {
        enquestaPersistance = new EnquestaPersistance();
        cleanUp();
    }

    @After
    public void tearDown() throws IOException {
        cleanUp();
    }

    private void cleanUp() throws IOException {
        File dir = new File(TEST_DATA_DIR);
        if (dir.exists()) {
            Files.walk(dir.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    public void testSaveAndLoadEnquesta() {
        Enquesta enquesta1 = new Enquesta("sample");
        enquesta1.setId(1);
        Pregunta p = new Pregunta();
        p.setTitol("Sample Question");
        p.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p.afegirOpcio("x");
        enquesta1.afegirPregunta(p);

        int respostaId = enquesta1.afegirNovaResposta();
        RespostaEnquesta re = enquesta1.getResposta(respostaId);
        re.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(0, 0));

        int originalId = enquesta1.getId();

        enquestaPersistance.saveEnquesta(enquesta1);
        assertTrue("Enquesta file should exist after save", enquestaPersistance.existsEnquesta(originalId));

        Enquesta enquesta2 = enquestaPersistance.loadEnquesta(originalId);

        assertNotNull(enquesta2);
        assertEquals(enquesta1.getNom(), enquesta2.getNom());
        assertEquals(1, enquesta2.getRespostesContainer().getIdsRespostes().size());
        int lre = enquesta2.getRespostesContainer().getIdsRespostes().iterator().next();
        RespostaEnquesta loadedRe = enquesta2.getResposta(lre);
        assertEquals(0, ((RespostaAPreguntaNoOrdenada)loadedRe.getRespostaAPregunta(0)).getResposta().intValue());

        assertTrue(enquestaPersistance.deleteEnquesta(originalId));
        assertFalse("Enquesta file should not exist after delete", enquestaPersistance.existsEnquesta(originalId));
    }


    @Test
    public void testGetLastIdWithNoFiles() {
        assertTrue( enquestaPersistance.getLastId() > 0);
    }

    @Test
    public void testDeleteNonExistentEnquesta() {
        try {
            enquestaPersistance.deleteEnquesta(999);
        } catch (Exception e) {
            fail("Deleting a non-existent enquesta should not throw an exception.");
        }
    }

    @Test
    public void testSaveAndLoadAllQuestionAndAnswerTypes() {
        int enquestaId = 1;
        Enquesta enquesta = new Enquesta("Comprehensive Test Survey");
        enquesta.setId(enquestaId);

        // Add different types of questions
        Pregunta oberta = new Pregunta();
        oberta.setTitol("Oberta Question");
        oberta.setTipusPregunta(PreguntaOberta.getInstance());
        enquesta.afegirPregunta(oberta);

        Pregunta numerica = new Pregunta();
        numerica.setTitol("Numerica Question");
        numerica.setTipusPregunta(PreguntaNumerica.getInstance());
        enquesta.afegirPregunta(numerica);

        Pregunta multiple = new Pregunta();
        multiple.setTitol("Multiple Choice Question");
        multiple.setTipusPregunta(PreguntaSeleccioMultiple.getInstance());
        multiple.afegirOpcio("Option 1");
        multiple.afegirOpcio("Option 2");
        enquesta.afegirPregunta(multiple);

        Pregunta unicaOrdenada = new Pregunta();
        unicaOrdenada.setTitol("Ordered Single Choice Question");
        unicaOrdenada.setTipusPregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
        unicaOrdenada.afegirOpcio("Bad");
        unicaOrdenada.afegirOpcio("Good");
        enquesta.afegirPregunta(unicaOrdenada);

        Pregunta unicaNoOrdenada = new Pregunta();
        unicaNoOrdenada.setTitol("Unordered Single Choice Question");
        unicaNoOrdenada.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        unicaNoOrdenada.afegirOpcio("Yes");
        unicaNoOrdenada.afegirOpcio("No");
        enquesta.afegirPregunta(unicaNoOrdenada);

        // Create a response with all answer types
        int respostaId = enquesta.afegirNovaResposta();
        RespostaEnquesta respostaEnquesta = enquesta.getResposta(respostaId);
        respostaEnquesta.addRespostaAPregunta(new RespostaAPreguntaOberta("Free text answer", 0));
        respostaEnquesta.addRespostaAPregunta(new RespostaAPreguntaNumerica(42.0, 1));
        ArrayList<Integer> multipleChoice = new ArrayList<>();
        multipleChoice.add(0);
        multipleChoice.add(1);
        respostaEnquesta.addRespostaAPregunta(new RespostaAPreguntaMultiple(multipleChoice, 2));
        respostaEnquesta.addRespostaAPregunta(new RespostaAPreguntaOrdenada(1, 3));
        respostaEnquesta.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(0, 4));

        enquestaPersistance.saveEnquesta(enquesta);
        Enquesta loadedEnquesta = enquestaPersistance.loadEnquesta(enquestaId);

        assertNotNull(loadedEnquesta);
        assertEquals(enquesta.getNom(), loadedEnquesta.getNom());
        assertNotNull(loadedEnquesta.getDate());
        assertEquals(enquesta.numPreguntes(), loadedEnquesta.numPreguntes());
        assertEquals(1, loadedEnquesta.getRespostesContainer().getIdsRespostes().size());

        assertTrue(loadedEnquesta.getPregunta(0).getTipusPregunta() instanceof PreguntaOberta);
        assertTrue(loadedEnquesta.getPregunta(1).getTipusPregunta() instanceof PreguntaNumerica);
        assertTrue(loadedEnquesta.getPregunta(2).getTipusPregunta() instanceof PreguntaSeleccioMultiple);
        assertTrue(loadedEnquesta.getPregunta(3).getTipusPregunta() instanceof PreguntaSeleccioUnicaOrdenada);
        assertTrue(loadedEnquesta.getPregunta(4).getTipusPregunta() instanceof PreguntaSeleccioUnicaNoOrdenada);

        int re = loadedEnquesta.getRespostesContainer().getIdsRespostes().iterator().next();
        RespostaEnquesta loadedResposta = loadedEnquesta.getResposta(re);
        assertTrue(loadedResposta.getRespostaAPregunta(0) instanceof RespostaAPreguntaOberta);
        assertEquals("Free text answer", ((RespostaAPreguntaOberta) loadedResposta.getRespostaAPregunta(0)).getResposta());

        assertTrue(loadedResposta.getRespostaAPregunta(1) instanceof RespostaAPreguntaNumerica);
        assertEquals(42.0, ((RespostaAPreguntaNumerica) loadedResposta.getRespostaAPregunta(1)).getResposta(), 0.0);

        assertTrue(loadedResposta.getRespostaAPregunta(2) instanceof RespostaAPreguntaMultiple);
        assertEquals(2, ((RespostaAPreguntaMultiple) loadedResposta.getRespostaAPregunta(2)).getResposta().size());

        assertTrue(loadedResposta.getRespostaAPregunta(3) instanceof RespostaAPreguntaOrdenada);
        assertEquals(1, (int) ((RespostaAPreguntaOrdenada) loadedResposta.getRespostaAPregunta(3)).getResposta());

        assertTrue(loadedResposta.getRespostaAPregunta(4) instanceof RespostaAPreguntaNoOrdenada);
        assertEquals(0, (int) ((RespostaAPreguntaNoOrdenada) loadedResposta.getRespostaAPregunta(4)).getResposta());
    }

    @Test
    public void testLoadEnquestaByPathWithInvalidPath() {
        assertNull(enquestaPersistance.loadEnquestaByPath("nonexistent/path.json"));
    }

    @Test(expected = RuntimeException.class)
    public void testLoadWithMalformedJson() throws IOException {
        String malformedJson = "{\"id\": 1, \"titol\": \"Malformed JSON\",,}";
        String filePath = enquestaPersistance.getPathOfEnquesta(1);
        Files.write(Paths.get(filePath), malformedJson.getBytes());
        enquestaPersistance.loadEnquesta(1);
    }
}

