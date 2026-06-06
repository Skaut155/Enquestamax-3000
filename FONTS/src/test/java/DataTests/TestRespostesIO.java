package DataTests;

import Data.Persistance.RespostesIO;
import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.*;
import Domain.Transactions.TxImportarResposta;
import Domain.Transactions.TxNovaRespostaEnquesta;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestRespostesIO {

    private int enquestaId;
    private static final String TEST_FILE_PATH = "./.data/test_export_resposta.json";
    private static final String TEST_FILE_PATH_ALL_TYPES = "./.data/test_all_types.json";
    private static final String TEST_FILE_PATH_EMPTY = "./.data/test_empty_responses.json";
    private static final String TEST_FILE_PATH_NULL = "./.data/test_null_cases.json";

    @Before
    public void setUp() {
        // Crear enquesta amb preguntes
        Enquesta e = new Enquesta("E1");

        Pregunta p = new Pregunta();
        p.setTitol("Quina edat tens?");
        p.setObligatoria(true);
        p.setTipusPregunta(PreguntaNumerica.getInstance());
        e.afegirPregunta(p);

        Pregunta p2 = new Pregunta();
        p2.setTitol("Quin és el teu color preferit?");
        p2.setObligatoria(false);
        p2.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p2.afegirOpcio("Vermell");
        p2.afegirOpcio("Blau");
        p2.afegirOpcio("Verd");
        e.afegirPregunta(p2);

        CtrlFactory f = CtrlFactory.getInstance();
        f.GetEnquestaCtrl().AddEnquesta(e);
        enquestaId = e.getId();

        // Crear una resposta
        TxNovaRespostaEnquesta tx = new TxNovaRespostaEnquesta(e.getId(), new String[]{"25", "1"});
        tx.execute();
    }

    @After
    public void tearDown() {
        // Netejar fitxers de test
        deleteTestFile(TEST_FILE_PATH);
        deleteTestFile(TEST_FILE_PATH_ALL_TYPES);
        deleteTestFile(TEST_FILE_PATH_EMPTY);
        deleteTestFile(TEST_FILE_PATH_NULL);
    }

    private void deleteTestFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testExportarResposta() {
        // Obtenir la resposta original
        IEnquestaCtrl enqCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = enqCtrl.GetEnquesta(enquestaId);

        assertNotNull("L'enquesta hauria d'existir", e);
        assertFalse("L'enquesta hauria de tenir respostes", e.getAllRespostes().isEmpty());

        RespostaEnquesta re = e.getAllRespostes().get(0);
        assertNotNull("La resposta a l'enquesta no hauria de ser null", re);

        // Verificar que la resposta té les dades correctes
        ArrayList<RespostaAPregunta> respostes = re.getRespostes();
        assertEquals("Hauria d'haver-hi 2 respostes", 2, respostes.size());

        // Exportar la resposta
        RespostesIO rio = new RespostesIO();
        rio.exportResposta(re, TEST_FILE_PATH);

        // Verificar que el fitxer s'ha creat
        File exportedFile = new File(TEST_FILE_PATH);
        assertTrue("El fitxer exportat hauria d'existir", exportedFile.exists());
        assertTrue("El fitxer exportat hauria de tenir contingut", exportedFile.length() > 0);
    }

    @Test
    public void testImportarResposta() {
        // Primer exportar una resposta
        IEnquestaCtrl enqCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = enqCtrl.GetEnquesta(enquestaId);
        RespostaEnquesta reOriginal = e.getAllRespostes().get(0);

        RespostesIO rio = new RespostesIO();
        rio.exportResposta(reOriginal, TEST_FILE_PATH);

        // Obtenir el nombre de respostes abans d'importar
        int numRespostesAntes = e.getAllRespostes().size();

        // Importar la resposta
        TxImportarResposta txImportar = new TxImportarResposta(enquestaId, TEST_FILE_PATH);
        txImportar.execute();

        // Verificar que s'ha afegit una nova resposta
        e = enqCtrl.GetEnquesta(enquestaId);
        assertEquals("Hauria d'haver-hi una resposta més després d'importar",
                     numRespostesAntes + 1, e.getAllRespostes().size());

        // Obtenir la resposta importada (l'última)
        RespostaEnquesta reImportada = e.getAllRespostes().get(e.getAllRespostes().size() - 1);
        assertNotNull("La resposta importada no hauria de ser null", reImportada);

        // Verificar que les respostes són consistents
        assertEquals("El nombre de respostes hauria de coincidir",
                     reOriginal.getRespostes().size(), reImportada.getRespostes().size());
    }

    @Test
    public void testRoundTripExportImport() {
        // Test complet: exportar i importar, verificant que les dades es mantenen
        IEnquestaCtrl enqCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = enqCtrl.GetEnquesta(enquestaId);
        RespostaEnquesta reOriginal = e.getAllRespostes().get(0);

        // Guardar les dades originals
        ArrayList<RespostaAPregunta> respostesOriginals = reOriginal.getRespostes();

        // Exportar
        RespostesIO rio = new RespostesIO();
        rio.exportResposta(reOriginal, TEST_FILE_PATH);

        // Importar directament des del fitxer
        RespostaEnquesta reImportada = rio.importResposta(TEST_FILE_PATH);

        assertNotNull("La resposta importada no hauria de ser null", reImportada);
        assertEquals("El nombre de respostes hauria de coincidir",
                     respostesOriginals.size(), reImportada.getRespostes().size());

        // Verificar cada resposta individual
        for (int i = 0; i < respostesOriginals.size(); i++) {
            RespostaAPregunta original = respostesOriginals.get(i);
            RespostaAPregunta importada = reImportada.getRespostes().get(i);

            assertEquals("L'ordre de la pregunta hauria de coincidir",
                        original.getOrdrePregunta(), importada.getOrdrePregunta());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testExportarRespostaPathInvalid() {
        // Provar exportar a un path que no existeix
        IEnquestaCtrl enqCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = enqCtrl.GetEnquesta(enquestaId);
        RespostaEnquesta re = e.getAllRespostes().get(0);

        RespostesIO rio = new RespostesIO();
        rio.exportResposta(re, "/path/inexistent/fitxer.json");
    }

    @Test
    public void testExportImportAllQuestionTypes() {
        // Crear enquesta amb tots els tipus de preguntes
        Enquesta e = new Enquesta("E_AllTypes");

        // Pregunta numèrica
        Pregunta pNumerica = new Pregunta();
        pNumerica.setTitol("Pregunta numèrica");
        pNumerica.setObligatoria(true);
        pNumerica.setTipusPregunta(PreguntaNumerica.getInstance());
        e.afegirPregunta(pNumerica);

        // Pregunta oberta
        Pregunta pOberta = new Pregunta();
        pOberta.setTitol("Pregunta oberta");
        pOberta.setObligatoria(false);
        pOberta.setTipusPregunta(PreguntaOberta.getInstance());
        e.afegirPregunta(pOberta);

        // Pregunta múltiple
        Pregunta pMultiple = new Pregunta();
        pMultiple.setTitol("Pregunta múltiple");
        pMultiple.setObligatoria(false);
        pMultiple.setTipusPregunta(PreguntaSeleccioMultiple.getInstance());
        pMultiple.afegirOpcio("Opció 1");
        pMultiple.afegirOpcio("Opció 2");
        pMultiple.afegirOpcio("Opció 3");
        e.afegirPregunta(pMultiple);

        // Pregunta ordenada
        Pregunta pOrdenada = new Pregunta();
        pOrdenada.setTitol("Pregunta ordenada");
        pOrdenada.setObligatoria(false);
        pOrdenada.setTipusPregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
        pOrdenada.afegirOpcio("Primera");
        pOrdenada.afegirOpcio("Segona");
        e.afegirPregunta(pOrdenada);

        // Pregunta no ordenada
        Pregunta pNoOrdenada = new Pregunta();
        pNoOrdenada.setTitol("Pregunta no ordenada");
        pNoOrdenada.setObligatoria(false);
        pNoOrdenada.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        pNoOrdenada.afegirOpcio("A");
        pNoOrdenada.afegirOpcio("B");
        e.afegirPregunta(pNoOrdenada);

        CtrlFactory.getInstance().GetEnquestaCtrl().AddEnquesta(e);

        // Crear resposta amb tots els tipus
        TxNovaRespostaEnquesta tx = new TxNovaRespostaEnquesta(
            e.getId(),
            new String[]{"42.5", "Resposta oberta de test", "1 2", "1", "1"}
        );
        tx.execute();

        // Obtenir i exportar la resposta
        RespostaEnquesta resposta = e.getAllRespostes().getFirst();
        RespostesIO rio = new RespostesIO();
        rio.exportResposta(resposta, TEST_FILE_PATH_ALL_TYPES);

        // Importar i verificar
        RespostaEnquesta respostaImportada = rio.importResposta(TEST_FILE_PATH_ALL_TYPES);
        assertNotNull("La resposta importada no hauria de ser null", respostaImportada);
        assertEquals("Hauria de tenir 5 respostes", 5, respostaImportada.getRespostes().size());

        // Verificar cada tipus
        ArrayList<RespostaAPregunta> respostes = respostaImportada.getRespostes();
        assertTrue("Resposta 0 hauria de ser numèrica", respostes.get(0) instanceof RespostaAPreguntaNumerica);
        assertTrue("Resposta 1 hauria de ser oberta", respostes.get(1) instanceof RespostaAPreguntaOberta);
        assertTrue("Resposta 2 hauria de ser múltiple", respostes.get(2) instanceof RespostaAPreguntaMultiple);
        assertTrue("Resposta 3 hauria de ser ordenada", respostes.get(3) instanceof RespostaAPreguntaOrdenada);
        assertTrue("Resposta 4 hauria de ser no ordenada", respostes.get(4) instanceof RespostaAPreguntaNoOrdenada);
    }

    @Test
    public void testExportImportEmptyResponses() {
        // Crear enquesta amb respostes buides
        Enquesta e = new Enquesta("E_Empty");

        Pregunta pNumerica = new Pregunta();
        pNumerica.setTitol("Pregunta numèrica");
        pNumerica.setObligatoria(false);
        pNumerica.setTipusPregunta(PreguntaNumerica.getInstance());
        e.afegirPregunta(pNumerica);

        Pregunta pOberta = new Pregunta();
        pOberta.setTitol("Pregunta oberta");
        pOberta.setObligatoria(false);
        pOberta.setTipusPregunta(PreguntaOberta.getInstance());
        e.afegirPregunta(pOberta);

        Pregunta pMultiple = new Pregunta();
        pMultiple.setTitol("Pregunta múltiple");
        pMultiple.setObligatoria(false);
        pMultiple.setTipusPregunta(PreguntaSeleccioMultiple.getInstance());
        pMultiple.afegirOpcio("Op1");
        pMultiple.afegirOpcio("Op2");
        e.afegirPregunta(pMultiple);

        CtrlFactory.getInstance().GetEnquestaCtrl().AddEnquesta(e);

        // Crear resposta amb valors buits
        TxNovaRespostaEnquesta tx = new TxNovaRespostaEnquesta(e.getId(), new String[]{"", "", ""});
        tx.execute();

        // Exportar i importar
        RespostaEnquesta resposta = e.getAllRespostes().getFirst();
        RespostesIO rio = new RespostesIO();
        rio.exportResposta(resposta, TEST_FILE_PATH_EMPTY);

        RespostaEnquesta respostaImportada = rio.importResposta(TEST_FILE_PATH_EMPTY);
        assertNotNull("La resposta importada no hauria de ser null", respostaImportada);
        assertEquals("Hauria de tenir 3 respostes", 3, respostaImportada.getRespostes().size());
    }

    @Test
    public void testImportRespostaWithMinusOneValues() {
        // Crear un fitxer JSON amb valors -1 per testejar aquests casos
        String jsonContent = "{\n" +
            "  \"respostes\": [\n" +
            "    {\"code\": \"UNICA_ORDENADA\", \"ordrePregunta\": 0, \"resposta\": -1},\n" +
            "    {\"code\": \"UNICA_NO_ORDENADA\", \"ordrePregunta\": 1, \"resposta\": -1}\n" +
            "  ]\n" +
            "}";

        try (FileWriter writer = new FileWriter(TEST_FILE_PATH_NULL)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            fail("No s'ha pogut crear el fitxer de test");
        }

        RespostesIO rio = new RespostesIO();
        RespostaEnquesta resposta = rio.importResposta(TEST_FILE_PATH_NULL);

        assertNotNull("La resposta no hauria de ser null", resposta);
        assertEquals("Hauria de tenir 2 respostes", 2, resposta.getRespostes().size());
    }

    @Test
    public void testImportRespostaWithNullCode() {
        // Crear un fitxer JSON amb code null
        String jsonContent = "{\n" +
            "  \"respostes\": [\n" +
            "    {\"code\": null, \"ordrePregunta\": 0, \"resposta\": 5}\n" +
            "  ]\n" +
            "}";

        try (FileWriter writer = new FileWriter(TEST_FILE_PATH_NULL)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            fail("No s'ha pogut crear el fitxer de test");
        }

        RespostesIO rio = new RespostesIO();
        RespostaEnquesta resposta = rio.importResposta(TEST_FILE_PATH_NULL);

        assertNotNull("La resposta no hauria de ser null", resposta);
        // El deserialitzador retorna null per respostes amb code null
    }

    @Test
    public void testImportRespostaWithNullOrdrePregunta() {
        // Crear un fitxer JSON amb ordrePregunta null
        String jsonContent = "{\n" +
            "  \"respostes\": [\n" +
            "    {\"code\": \"NUMERICA\", \"ordrePregunta\": null, \"resposta\": 5}\n" +
            "  ]\n" +
            "}";

        try (FileWriter writer = new FileWriter(TEST_FILE_PATH_NULL)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            fail("No s'ha pogut crear el fitxer de test");
        }

        RespostesIO rio = new RespostesIO();
        RespostaEnquesta resposta = rio.importResposta(TEST_FILE_PATH_NULL);

        assertNotNull("La resposta no hauria de ser null", resposta);
    }

    @Test
    public void testImportRespostaWithNullResposta() {
        // Crear un fitxer JSON amb diferents tipus i resposta null
        String jsonContent = "{\n" +
            "  \"respostes\": [\n" +
            "    {\"code\": \"NUMERICA\", \"ordrePregunta\": 0, \"resposta\": null},\n" +
            "    {\"code\": \"OBERTA\", \"ordrePregunta\": 1, \"resposta\": null},\n" +
            "    {\"code\": \"MULTIPLE\", \"ordrePregunta\": 2, \"resposta\": null},\n" +
            "    {\"code\": \"UNICA_ORDENADA\", \"ordrePregunta\": 3, \"resposta\": null},\n" +
            "    {\"code\": \"UNICA_NO_ORDENADA\", \"ordrePregunta\": 4, \"resposta\": null}\n" +
            "  ]\n" +
            "}";

        try (FileWriter writer = new FileWriter(TEST_FILE_PATH_NULL)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            fail("No s'ha pogut crear el fitxer de test");
        }

        RespostesIO rio = new RespostesIO();
        RespostaEnquesta resposta = rio.importResposta(TEST_FILE_PATH_NULL);

        assertNotNull("La resposta no hauria de ser null", resposta);
        assertEquals("Hauria de tenir 5 respostes", 5, resposta.getRespostes().size());
    }

    @Test
    public void testImportRespostaWithUnknownCode() {
        // Crear un fitxer JSON amb un code desconegut (default case)
        String jsonContent = "{\n" +
            "  \"respostes\": [\n" +
            "    {\"code\": \"TIPUS_DESCONEGUT\", \"ordrePregunta\": 0, \"resposta\": \"valor\"}\n" +
            "  ]\n" +
            "}";

        try (FileWriter writer = new FileWriter(TEST_FILE_PATH_NULL)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            fail("No s'ha pogut crear el fitxer de test");
        }

        RespostesIO rio = new RespostesIO();
        RespostaEnquesta resposta = rio.importResposta(TEST_FILE_PATH_NULL);

        assertNotNull("La resposta no hauria de ser null", resposta);
        // El deserialitzador retorna null per tipus desconeguts
    }

    @Test(expected = RuntimeException.class)
    public void testImportRespostaFileNotFound() {
        // Intentar importar un fitxer que no existeix
        RespostesIO rio = new RespostesIO();
        rio.importResposta("./.data/fitxer_que_no_existeix.json");
    }

    @Test
    public void testImportRespostaWithEmptyArray() {
        // Crear un fitxer JSON amb array buit per MULTIPLE
        String jsonContent = "{\n" +
            "  \"respostes\": [\n" +
            "    {\"code\": \"MULTIPLE\", \"ordrePregunta\": 0, \"resposta\": []}\n" +
            "  ]\n" +
            "}";

        try (FileWriter writer = new FileWriter(TEST_FILE_PATH_NULL)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            fail("No s'ha pogut crear el fitxer de test");
        }

        RespostesIO rio = new RespostesIO();
        RespostaEnquesta resposta = rio.importResposta(TEST_FILE_PATH_NULL);

        assertNotNull("La resposta no hauria de ser null", resposta);
        assertEquals("Hauria de tenir 1 resposta", 1, resposta.getRespostes().size());
    }

    @Test
    public void testImportRespostaWithMultipleArray() {
        // Crear un fitxer JSON amb array amb múltiples valors
        String jsonContent = "{\n" +
            "  \"respostes\": [\n" +
            "    {\"code\": \"MULTIPLE\", \"ordrePregunta\": 0, \"resposta\": [0, 1, 2]}\n" +
            "  ]\n" +
            "}";

        try (FileWriter writer = new FileWriter(TEST_FILE_PATH_NULL)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            fail("No s'ha pogut crear el fitxer de test");
        }

        RespostesIO rio = new RespostesIO();
        RespostaEnquesta resposta = rio.importResposta(TEST_FILE_PATH_NULL);

        assertNotNull("La resposta no hauria de ser null", resposta);
        assertEquals("Hauria de tenir 1 resposta", 1, resposta.getRespostes().size());

        RespostaAPregunta respostaPregunta = resposta.getRespostes().get(0);
        assertTrue("Hauria de ser RespostaAPreguntaMultiple", respostaPregunta instanceof RespostaAPreguntaMultiple);
    }

    @Test
    public void testExportRespostaCreatesValidJson() {
        // Verificar que el JSON exportat és vàlid i pot ser llegit
        IEnquestaCtrl enqCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = enqCtrl.GetEnquesta(enquestaId);
        RespostaEnquesta re = e.getAllRespostes().get(0);

        RespostesIO rio = new RespostesIO();
        rio.exportResposta(re, TEST_FILE_PATH);

        // Verificar que el fitxer conté JSON vàlid llegint-lo
        RespostaEnquesta importada = rio.importResposta(TEST_FILE_PATH);
        assertNotNull("Hauria de poder importar el que s'ha exportat", importada);
    }

    @Test(expected = RuntimeException.class)
    public void testExportRespostaIOException() {
        // Crear un directori amb el nom del fitxer per forçar un IOException
        File dirAsFile = new File(TEST_FILE_PATH_NULL);
        dirAsFile.mkdirs(); // Crear-lo com a directori

        try {
            IEnquestaCtrl enqCtrl = CtrlFactory.getInstance().GetEnquestaCtrl();
            Enquesta e = enqCtrl.GetEnquesta(enquestaId);
            RespostaEnquesta re = e.getAllRespostes().get(0);

            RespostesIO rio = new RespostesIO();
            // Intentar escriure a un directori (no un fitxer) hauria de llançar IOException
            rio.exportResposta(re, TEST_FILE_PATH_NULL);
        } finally {
            // Netejar el directori creat
            if (dirAsFile.exists() && dirAsFile.isDirectory()) {
                dirAsFile.delete();
            }
        }
    }
}
