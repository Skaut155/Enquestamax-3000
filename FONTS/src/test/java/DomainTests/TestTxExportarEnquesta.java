package DomainTests;

import Domain.Model.Enquesta;
import Domain.Model.Pregunta;
import Domain.Model.PreguntaNumerica;
import Domain.Model.PreguntaSeleccioUnicaNoOrdenada;
import Domain.Factories.CtrlFactory;
import Domain.Transactions.TxExportarEnquesta;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class TestTxExportarEnquesta {

    private int enquestaId;

    @Before
    public void setUp() {
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
        f.GetEnquestaCtrl().updateEnquesta(enquestaId);
    }

    @Test
    public void testExportarEnquesta() throws IOException {
        String path = "./.data/test_export_enquesta.json";

        TxExportarEnquesta txExportar = new TxExportarEnquesta(enquestaId, path);
        txExportar.execute();

        // Check that the file has been created
        java.nio.file.Path exported = java.nio.file.Paths.get(path);
        org.junit.Assert.assertTrue("Exported file should exist", java.nio.file.Files.exists(exported));

        String exportedContent = java.nio.file.Files.readString(exported);

        // Parse JSON and compare structure, ignoring dynamic fields
        JsonObject exportedJson = JsonParser.parseString(exportedContent).getAsJsonObject();

        // Verify structure exists
        org.junit.Assert.assertTrue("Exported JSON should have 'preguntes' field", exportedJson.has("preguntes"));
        org.junit.Assert.assertTrue("Exported JSON should have 'nom' field", exportedJson.has("nom"));
        org.junit.Assert.assertTrue("Exported JSON should have 'id' field", exportedJson.has("id"));
        org.junit.Assert.assertTrue("Exported JSON should have 'dataCreacio' field", exportedJson.has("dataCreacio"));

        // Verify content
        org.junit.Assert.assertEquals("Survey name should be E1", "E1", exportedJson.get("nom").getAsString());

        // Verify questions structure
        var preguntes = exportedJson.getAsJsonArray("preguntes");
        org.junit.Assert.assertEquals("Should have 2 questions", 2, preguntes.size());

        // Verify first question (numeric)
        JsonObject pregunta1 = preguntes.get(0).getAsJsonObject();
        org.junit.Assert.assertEquals("First question title", "Quina edat tens?", pregunta1.get("titol").getAsString());
        org.junit.Assert.assertTrue("First question should be obligatory", pregunta1.get("obligatoria").getAsBoolean());
        org.junit.Assert.assertEquals("First question order", 0, pregunta1.get("ordre").getAsInt());
        org.junit.Assert.assertEquals("First question type", "NUMERICA", pregunta1.getAsJsonObject("state").get("code").getAsString());
        org.junit.Assert.assertEquals("First question should have no options", 0, pregunta1.getAsJsonArray("opcions").size());

        // Verify second question (single choice)
        JsonObject pregunta2 = preguntes.get(1).getAsJsonObject();
        org.junit.Assert.assertEquals("Second question title", "Quin és el teu color preferit?", pregunta2.get("titol").getAsString());
        org.junit.Assert.assertFalse("Second question should not be obligatory", pregunta2.get("obligatoria").getAsBoolean());
        org.junit.Assert.assertEquals("Second question order", 1, pregunta2.get("ordre").getAsInt());
        org.junit.Assert.assertEquals("Second question type", "UNICA_NO_ORDENADA", pregunta2.getAsJsonObject("state").get("code").getAsString());

        // Verify options
        var opcions = pregunta2.getAsJsonArray("opcions");
        org.junit.Assert.assertEquals("Second question should have 3 options", 3, opcions.size());

        JsonObject opcio1 = opcions.get(0).getAsJsonObject();
        org.junit.Assert.assertEquals("Option 1 text", "Vermell", opcio1.get("text").getAsString());
        org.junit.Assert.assertEquals("Option 1 order", 0, opcio1.get("ordre").getAsInt());

        JsonObject opcio2 = opcions.get(1).getAsJsonObject();
        org.junit.Assert.assertEquals("Option 2 text", "Blau", opcio2.get("text").getAsString());
        org.junit.Assert.assertEquals("Option 2 order", 1, opcio2.get("ordre").getAsInt());

        JsonObject opcio3 = opcions.get(2).getAsJsonObject();
        org.junit.Assert.assertEquals("Option 3 text", "Verd", opcio3.get("text").getAsString());
        org.junit.Assert.assertEquals("Option 3 order", 2, opcio3.get("ordre").getAsInt());
    }

}

