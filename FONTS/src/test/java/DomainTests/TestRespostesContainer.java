package DomainTests;

import Domain.Model.Enquesta;
import Domain.Model.Pregunta;
import Domain.Model.RespostaAPregunta;
import Domain.Model.RespostaEnquesta;
import Domain.Model.RespostesContainer;
import Domain.Exceptions.EnquestaNoConteResposta;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;

public class TestRespostesContainer {

    private Enquesta mockEnquesta;
    private RespostesContainer container;

    @Before
    public void setUp() {
        mockEnquesta = Mockito.mock(Enquesta.class);
        container = new RespostesContainer(mockEnquesta);
    }

    @Test
    public void testConstructor() {
        assertNotNull(container);
        assertTrue(container.getIdsRespostes().isEmpty());
    }

    @Test
    public void testAddRespostaSingleResponse() {
        RespostaEnquesta resposta = Mockito.mock(RespostaEnquesta.class);

        container.addResposta(resposta);

        Mockito.verify(resposta).setId(0);
        Mockito.verify(resposta).associaContainer(container);
        assertEquals(1, container.getIdsRespostes().size());
    }

    @Test
    public void testAddRespostaMultipleResponses() {
        RespostaEnquesta resposta1 = Mockito.mock(RespostaEnquesta.class);
        RespostaEnquesta resposta2 = Mockito.mock(RespostaEnquesta.class);
        RespostaEnquesta resposta3 = Mockito.mock(RespostaEnquesta.class);

        container.addResposta(resposta1);
        container.addResposta(resposta2);
        container.addResposta(resposta3);

        Mockito.verify(resposta1).setId(0);
        Mockito.verify(resposta2).setId(1);
        Mockito.verify(resposta3).setId(2);

        assertEquals(3, container.getIdsRespostes().size());
    }

    @Test
    public void testAddRespostaAssignsUniqueIds() {
        RespostaEnquesta resposta1 = Mockito.mock(RespostaEnquesta.class);
        RespostaEnquesta resposta2 = Mockito.mock(RespostaEnquesta.class);

        container.addResposta(resposta1);
        container.addResposta(resposta2);

        // Use ArgumentCaptor to capture the IDs
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        Mockito.verify(resposta1).setId(captor.capture());
        int id1 = captor.getValue();
        assertTrue(id1 >= 0);

        Mockito.verify(resposta2).setId(captor.capture());
        int id2 = captor.getValue();
        assertTrue(id2 >= 0);

        // Verify they are different
        assertNotEquals(id1, id2);
    }

    @Test
    public void testGetIdsRespostesEmpty() {
        ArrayList<Integer> ids = container.getIdsRespostes();

        assertNotNull(ids);
        assertEquals(0, ids.size());
    }

    @Test
    public void testGetIdsRespostesSingleResponse() {
        RespostaEnquesta resposta = new RespostaEnquesta();

        container.addResposta(resposta);
        ArrayList<Integer> ids = container.getIdsRespostes();

        assertEquals(1, ids.size());
        assertTrue(ids.contains(resposta.getId()));
    }

    @Test
    public void testGetIdsRespostesMultipleResponses() {
        RespostaEnquesta resposta1 = new RespostaEnquesta();
        RespostaEnquesta resposta2 = new RespostaEnquesta();
        RespostaEnquesta resposta3 = new RespostaEnquesta();

        container.addResposta(resposta1);
        container.addResposta(resposta2);
        container.addResposta(resposta3);

        ArrayList<Integer> ids = container.getIdsRespostes();

        assertEquals(3, ids.size());
        assertTrue(ids.contains(resposta1.getId()));
        assertTrue(ids.contains(resposta2.getId()));
        assertTrue(ids.contains(resposta3.getId()));
    }

    @Test
    public void testGetRespostaValidId() {
        RespostaEnquesta resposta = new RespostaEnquesta();
        container.addResposta(resposta);

        RespostaEnquesta retrieved = container.getResposta(resposta.getId());

        assertSame(resposta, retrieved);
    }

    @Test
    public void testGetRespostaMultipleValidIds() {
        RespostaEnquesta resposta1 = new RespostaEnquesta();
        RespostaEnquesta resposta2 = new RespostaEnquesta();
        RespostaEnquesta resposta3 = new RespostaEnquesta();

        container.addResposta(resposta1);
        container.addResposta(resposta2);
        container.addResposta(resposta3);

        assertSame(resposta1, container.getResposta(resposta1.getId()));
        assertSame(resposta2, container.getResposta(resposta2.getId()));
        assertSame(resposta3, container.getResposta(resposta3.getId()));
    }

    @Test(expected = EnquestaNoConteResposta.class)
    public void testGetRespostaInvalidId() {
        container.getResposta(999);
    }

    @Test(expected = EnquestaNoConteResposta.class)
    public void testGetRespostaEmptyContainer() {
        container.getResposta(0);
    }

    @Test(expected = EnquestaNoConteResposta.class)
    public void testGetRespostaNegativeId() {
        RespostaEnquesta resposta = new RespostaEnquesta();
        container.addResposta(resposta);

        container.getResposta(-1);
    }

    @Test
    public void testCalcMaxSingleResponse() {
        RespostaEnquesta resposta = Mockito.mock(RespostaEnquesta.class);
        RespostaAPregunta<Double> rap = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap.getResposta()).thenReturn(5.0);
        Mockito.when(resposta.getRespostaAPregunta(0)).thenReturn(rap);

        container.addResposta(resposta);

        double max = container.calcMax(0);
        assertEquals(5.0, max, 1e-9);
    }

    @Test
    public void testCalcMaxMultipleResponses() {
        RespostaEnquesta resposta1 = Mockito.mock(RespostaEnquesta.class);
        RespostaEnquesta resposta2 = Mockito.mock(RespostaEnquesta.class);
        RespostaEnquesta resposta3 = Mockito.mock(RespostaEnquesta.class);

        RespostaAPregunta<Double> rap1 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta<Double> rap2 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta<Double> rap3 = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap1.getResposta()).thenReturn(3.5);
        Mockito.when(rap2.getResposta()).thenReturn(8.2);
        Mockito.when(rap3.getResposta()).thenReturn(2.1);

        Mockito.when(resposta1.getRespostaAPregunta(0)).thenReturn(rap1);
        Mockito.when(resposta2.getRespostaAPregunta(0)).thenReturn(rap2);
        Mockito.when(resposta3.getRespostaAPregunta(0)).thenReturn(rap3);

        container.addResposta(resposta1);
        container.addResposta(resposta2);
        container.addResposta(resposta3);

        double max = container.calcMax(0);
        assertEquals(8.2, max, 1e-9);
    }

    @Test
    public void testCalcMaxNegativeValues() {
        RespostaEnquesta resposta1 = Mockito.mock(RespostaEnquesta.class);
        RespostaEnquesta resposta2 = Mockito.mock(RespostaEnquesta.class);

        RespostaAPregunta<Double> rap1 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta<Double> rap2 = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap1.getResposta()).thenReturn(-5.0);
        Mockito.when(rap2.getResposta()).thenReturn(-2.0);

        Mockito.when(resposta1.getRespostaAPregunta(0)).thenReturn(rap1);
        Mockito.when(resposta2.getRespostaAPregunta(0)).thenReturn(rap2);

        container.addResposta(resposta1);
        container.addResposta(resposta2);

        double max = container.calcMax(0);
        assertEquals(-2.0, max, 1e-9);
    }

    @Test
    public void testCalcMaxEmptyContainer() {
        double max = container.calcMax(0);
        assertEquals(-Double.MAX_VALUE, max, 1e-9);
    }

    @Test
    public void testCalcMaxWithEmptyResponses() {
        RespostaEnquesta resposta1 = Mockito.mock(RespostaEnquesta.class);
        RespostaAPregunta<Double> rap1 = Mockito.mock(RespostaAPregunta.class);
        Mockito.when(rap1.isEmpty()).thenReturn(true);
        Mockito.when(resposta1.getRespostaAPregunta(0)).thenReturn(rap1);

        container.addResposta(resposta1);

        double max = container.calcMax(0);
        assertEquals(-Double.MAX_VALUE, max, 1e-9);
    }

    @Test
    public void testCalcMinSingleResponse() {
        RespostaEnquesta resposta = Mockito.mock(RespostaEnquesta.class);
        RespostaAPregunta<Double> rap = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap.getResposta()).thenReturn(5.0);
        Mockito.when(resposta.getRespostaAPregunta(0)).thenReturn(rap);

        container.addResposta(resposta);

        double min = container.calcMin(0);
        assertEquals(5.0, min, 1e-9);
    }

    @Test
    public void testCalcMinMultipleResponses() {
        RespostaEnquesta resposta1 = Mockito.mock(RespostaEnquesta.class);
        RespostaEnquesta resposta2 = Mockito.mock(RespostaEnquesta.class);
        RespostaEnquesta resposta3 = Mockito.mock(RespostaEnquesta.class);

        RespostaAPregunta<Double> rap1 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta<Double> rap2 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta<Double> rap3 = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap1.getResposta()).thenReturn(3.5);
        Mockito.when(rap2.getResposta()).thenReturn(8.2);
        Mockito.when(rap3.getResposta()).thenReturn(2.1);

        Mockito.when(resposta1.getRespostaAPregunta(0)).thenReturn(rap1);
        Mockito.when(resposta2.getRespostaAPregunta(0)).thenReturn(rap2);
        Mockito.when(resposta3.getRespostaAPregunta(0)).thenReturn(rap3);

        container.addResposta(resposta1);
        container.addResposta(resposta2);
        container.addResposta(resposta3);

        double min = container.calcMin(0);
        assertEquals(2.1, min, 1e-9);
    }

    @Test
    public void testCalcMinNegativeValues() {
        RespostaEnquesta resposta1 = Mockito.mock(RespostaEnquesta.class);
        RespostaEnquesta resposta2 = Mockito.mock(RespostaEnquesta.class);

        RespostaAPregunta<Double> rap1 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta<Double> rap2 = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap1.getResposta()).thenReturn(-5.0);
        Mockito.when(rap2.getResposta()).thenReturn(-2.0);

        Mockito.when(resposta1.getRespostaAPregunta(0)).thenReturn(rap1);
        Mockito.when(resposta2.getRespostaAPregunta(0)).thenReturn(rap2);

        container.addResposta(resposta1);
        container.addResposta(resposta2);

        double min = container.calcMin(0);
        assertEquals(-5.0, min, 1e-9);
    }

    @Test
    public void testCalcMinEmptyContainer() {
        double min = container.calcMin(0);
        assertEquals(Double.MAX_VALUE, min, 1e-9);
    }

    @Test
    public void testCalcMinWithEmptyResponses() {
        RespostaEnquesta resposta1 = Mockito.mock(RespostaEnquesta.class);
        RespostaAPregunta<Double> rap1 = Mockito.mock(RespostaAPregunta.class);
        Mockito.when(rap1.isEmpty()).thenReturn(true);
        Mockito.when(resposta1.getRespostaAPregunta(0)).thenReturn(rap1);

        container.addResposta(resposta1);

        double min = container.calcMin(0);
        assertEquals(Double.MAX_VALUE, min, 1e-9);
    }

    @Test
    public void testGetPregunta() {
        Pregunta mockPregunta = Mockito.mock(Pregunta.class);
        Mockito.when(mockEnquesta.getPregunta(0)).thenReturn(mockPregunta);

        Pregunta pregunta = container.getPregunta(0);

        assertSame(mockPregunta, pregunta);
        Mockito.verify(mockEnquesta).getPregunta(0);
    }

    @Test
    public void testGetPreguntaMultipleQuestions() {
        Pregunta mockPregunta1 = Mockito.mock(Pregunta.class);
        Pregunta mockPregunta2 = Mockito.mock(Pregunta.class);

        Mockito.when(mockEnquesta.getPregunta(0)).thenReturn(mockPregunta1);
        Mockito.when(mockEnquesta.getPregunta(1)).thenReturn(mockPregunta2);

        assertSame(mockPregunta1, container.getPregunta(0));
        assertSame(mockPregunta2, container.getPregunta(1));
    }

    @Test
    public void testEsborrarRespostesEmptyContainer() {
        container.EsborrarRespostes();

        assertEquals(0, container.getIdsRespostes().size());
    }

    @Test
    public void testEsborrarRespostesWithResponses() {
        RespostaEnquesta resposta1 = new RespostaEnquesta();
        RespostaEnquesta resposta2 = new RespostaEnquesta();
        RespostaEnquesta resposta3 = new RespostaEnquesta();

        container.addResposta(resposta1);
        container.addResposta(resposta2);
        container.addResposta(resposta3);

        assertEquals(3, container.getIdsRespostes().size());

        container.EsborrarRespostes();

        assertEquals(0, container.getIdsRespostes().size());
    }

    @Test
    public void testEsborrarRespostesResetsIds() {
        RespostaEnquesta resposta1 = new RespostaEnquesta();
        container.addResposta(resposta1);

        int firstId = resposta1.getId();

        container.EsborrarRespostes();

        RespostaEnquesta resposta2 = new RespostaEnquesta();
        container.addResposta(resposta2);

        // After clearing, ids should reset to 0
        assertEquals(0, resposta2.getId());
    }

    @Test(expected = EnquestaNoConteResposta.class)
    public void testGetRespostaAfterEsborrar() {
        RespostaEnquesta resposta = new RespostaEnquesta();
        container.addResposta(resposta);
        int id = resposta.getId();

        container.EsborrarRespostes();

        // Should throw exception since responses were cleared
        container.getResposta(id);
    }

    @Test
    public void testCompleteWorkflow() {
        // Add responses
        RespostaEnquesta resposta1 = new RespostaEnquesta();
        RespostaEnquesta resposta2 = new RespostaEnquesta();

        container.addResposta(resposta1);
        container.addResposta(resposta2);

        // Verify they were added
        assertEquals(2, container.getIdsRespostes().size());
        assertSame(resposta1, container.getResposta(resposta1.getId()));
        assertSame(resposta2, container.getResposta(resposta2.getId()));

        // Clear
        container.EsborrarRespostes();
        assertEquals(0, container.getIdsRespostes().size());

        // Add new ones
        RespostaEnquesta resposta3 = new RespostaEnquesta();
        container.addResposta(resposta3);

        assertEquals(1, container.getIdsRespostes().size());
        assertEquals(0, resposta3.getId());
    }

    @Test
    public void removeRespostaValidId() {
        RespostaEnquesta resposta = new RespostaEnquesta();
        container.addResposta(resposta);
        int id = resposta.getId();

        container.removeResposta(id);

        assertEquals(0, container.getIdsRespostes().size());
    }

    @Test(expected = EnquestaNoConteResposta.class)
    public void removeRespostaInvalidId() {
        container.removeResposta(999);
    }


}
