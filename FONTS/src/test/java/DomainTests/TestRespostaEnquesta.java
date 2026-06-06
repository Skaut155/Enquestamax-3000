package DomainTests;
import Domain.Model.Pregunta;
import Domain.Model.RespostaAPregunta;
import Domain.Model.RespostaEnquesta;
import Domain.Model.RespostesContainer;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class TestRespostaEnquesta {
    @Test
    public void testSetAndGetId() {
        RespostaEnquesta re = new RespostaEnquesta();
        re.setId(42);
        assertEquals(42, re.getId());
    }

    @Test
    public void testCalcMaxDelegatesToContainer() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostesContainer container = Mockito.mock(RespostesContainer.class);
        Mockito.when(container.calcMax(1)).thenReturn(9.5);
        re.associaContainer(container);

        assertEquals(9.5, re.calcMax(1), 1e-9);
        Mockito.verify(container).calcMax(1);
    }

    @Test
    public void testCalcMinDelegatesToContainer() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostesContainer container = Mockito.mock(RespostesContainer.class);
        Mockito.when(container.calcMin(2)).thenReturn(-3.25);
        re.associaContainer(container);

        assertEquals(-3.25, re.calcMin(2), 1e-9);
        Mockito.verify(container).calcMin(2);
    }

    @Test
    public void testGetRespostaAPreguntaByOrdre() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostaAPregunta rap = Mockito.mock(RespostaAPregunta.class);
        Mockito.when(rap.getOrdrePregunta()).thenReturn(2);

        re.addRespostaAPregunta(rap);
        RespostaAPregunta found = re.getRespostaAPregunta(2);

        assertSame(rap, found);
    }

    @Test
    public void testGetRespostesReturnsClone() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostaAPregunta rap = Mockito.mock(RespostaAPregunta.class);
        Mockito.when(rap.getOrdrePregunta()).thenReturn(1);

        re.addRespostaAPregunta(rap);

        java.util.ArrayList<RespostaAPregunta> returned = re.getRespostes();
        assertEquals(1, returned.size());

        // Mutate the returned list and ensure original internal list is unaffected
        returned.clear();
        assertEquals(1, re.getRespostes().size());
    }

    @Test
    public void testGetPreguntaDelegatesToContainer() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostesContainer container = Mockito.mock(RespostesContainer.class);
        Pregunta p = Mockito.mock(Pregunta.class);
        Mockito.when(container.getPregunta(3)).thenReturn(p);

        re.associaContainer(container);
        assertSame(p, re.getPregunta(3));
        Mockito.verify(container).getPregunta(3);
    }

    @Test
    public void testGetRespostaAPreguntaNotFound() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostaAPregunta rap = Mockito.mock(RespostaAPregunta.class);
        Mockito.when(rap.getOrdrePregunta()).thenReturn(1);

        re.addRespostaAPregunta(rap);

        // Request a question that doesn't exist
        RespostaAPregunta notFound = re.getRespostaAPregunta(5);
        assertNull(notFound);
    }

    @Test
    public void testGetRespostaAPreguntaFromEmptyList() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostaAPregunta notFound = re.getRespostaAPregunta(0);
        assertNull(notFound);
    }

    @Test
    public void testGetRespostaAPreguntaMultipleResponses() {
        RespostaEnquesta re = new RespostaEnquesta();

        RespostaAPregunta rap1 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta rap2 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta rap3 = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap1.getOrdrePregunta()).thenReturn(0);
        Mockito.when(rap2.getOrdrePregunta()).thenReturn(1);
        Mockito.when(rap3.getOrdrePregunta()).thenReturn(2);

        re.addRespostaAPregunta(rap1);
        re.addRespostaAPregunta(rap2);
        re.addRespostaAPregunta(rap3);

        assertSame(rap1, re.getRespostaAPregunta(0));
        assertSame(rap2, re.getRespostaAPregunta(1));
        assertSame(rap3, re.getRespostaAPregunta(2));
    }

    @Test
    public void testGetRespostesEmptyList() {
        RespostaEnquesta re = new RespostaEnquesta();
        java.util.ArrayList<RespostaAPregunta> respostes = re.getRespostes();

        assertNotNull(respostes);
        assertEquals(0, respostes.size());
    }

    @Test
    public void testAddRespostaAPreguntaSetsRespostaEnquesta() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostaAPregunta rap = Mockito.mock(RespostaAPregunta.class);
        Mockito.when(rap.getOrdrePregunta()).thenReturn(0);

        re.addRespostaAPregunta(rap);

        // Verify that setRespostaEnquesta was called on the RespostaAPregunta
        Mockito.verify(rap).setRespostaEnquesta(re);
    }

    @Test
    public void testAddMultipleRespostesAPregunta() {
        RespostaEnquesta re = new RespostaEnquesta();

        RespostaAPregunta rap1 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta rap2 = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap1.getOrdrePregunta()).thenReturn(0);
        Mockito.when(rap2.getOrdrePregunta()).thenReturn(1);

        re.addRespostaAPregunta(rap1);
        re.addRespostaAPregunta(rap2);

        assertEquals(2, re.getRespostes().size());
    }

    @Test
    public void testGetRespostesStringEmpty() {
        RespostaEnquesta re = new RespostaEnquesta();
        String[] respostesStr = re.getRespostesString();

        assertNotNull(respostesStr);
        assertEquals(0, respostesStr.length);
    }

    @Test
    public void testGetRespostesStringSingleResponse() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostaAPregunta rap = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap.getOrdrePregunta()).thenReturn(0);
        Mockito.when(rap.getStringResposta()).thenReturn("Answer 1");

        re.addRespostaAPregunta(rap);
        String[] respostesStr = re.getRespostesString();

        assertEquals(1, respostesStr.length);
        assertEquals("Answer 1", respostesStr[0]);
    }

    @Test
    public void testGetRespostesStringMultipleResponses() {
        RespostaEnquesta re = new RespostaEnquesta();

        RespostaAPregunta rap1 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta rap2 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta rap3 = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap1.getOrdrePregunta()).thenReturn(0);
        Mockito.when(rap2.getOrdrePregunta()).thenReturn(1);
        Mockito.when(rap3.getOrdrePregunta()).thenReturn(2);

        Mockito.when(rap1.getStringResposta()).thenReturn("42");
        Mockito.when(rap2.getStringResposta()).thenReturn("Text answer");
        Mockito.when(rap3.getStringResposta()).thenReturn("true");

        re.addRespostaAPregunta(rap1);
        re.addRespostaAPregunta(rap2);
        re.addRespostaAPregunta(rap3);

        String[] respostesStr = re.getRespostesString();

        assertEquals(3, respostesStr.length);
        assertEquals("42", respostesStr[0]);
        assertEquals("Text answer", respostesStr[1]);
        assertEquals("true", respostesStr[2]);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPreguntaWithoutContainer() {
        RespostaEnquesta re = new RespostaEnquesta();
        // Should throw NullPointerException since no container is associated
        re.getPregunta(0);
    }

    @Test(expected = NullPointerException.class)
    public void testCalcMaxWithoutContainer() {
        RespostaEnquesta re = new RespostaEnquesta();
        // Should throw NullPointerException since no container is associated
        re.calcMax(0);
    }

    @Test(expected = NullPointerException.class)
    public void testCalcMinWithoutContainer() {
        RespostaEnquesta re = new RespostaEnquesta();
        // Should throw NullPointerException since no container is associated
        re.calcMin(0);
    }

    @Test
    public void testGetContainerReturnsAssociatedContainer() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostesContainer container = Mockito.mock(RespostesContainer.class);

        re.associaContainer(container);

        assertSame(container, re.getContainer());
    }

    @Test(expected = NullPointerException.class)
    public void testGetContainerThrowsExceptionWhenNoContainerAssociated() {
        RespostaEnquesta re = new RespostaEnquesta();
        // Should throw NullPointerException since no container is associated
        re.getContainer();
    }

    @Test
    public void testGetContainerAfterReassociation() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostesContainer container1 = Mockito.mock(RespostesContainer.class);
        RespostesContainer container2 = Mockito.mock(RespostesContainer.class);

        re.associaContainer(container1);
        assertSame(container1, re.getContainer());

        // Reassociate with a different container
        re.associaContainer(container2);
        assertSame(container2, re.getContainer());
    }

    @Test
    public void testAssociaContainer() {
        RespostaEnquesta re = new RespostaEnquesta();
        RespostesContainer container = Mockito.mock(RespostesContainer.class);
        Pregunta p = Mockito.mock(Pregunta.class);

        Mockito.when(container.getPregunta(0)).thenReturn(p);

        re.associaContainer(container);

        // Verify container is properly associated
        assertSame(p, re.getPregunta(0));
    }

    @Test
    public void testCompleteWorkflow() {
        // Test a complete workflow: create, associate container, add responses, retrieve data
        RespostaEnquesta re = new RespostaEnquesta();
        re.setId(100);

        RespostesContainer container = Mockito.mock(RespostesContainer.class);
        Pregunta p1 = Mockito.mock(Pregunta.class);
        Pregunta p2 = Mockito.mock(Pregunta.class);

        Mockito.when(container.getPregunta(0)).thenReturn(p1);
        Mockito.when(container.getPregunta(1)).thenReturn(p2);
        Mockito.when(container.calcMax(0)).thenReturn(10.0);
        Mockito.when(container.calcMin(0)).thenReturn(1.0);

        re.associaContainer(container);

        RespostaAPregunta rap1 = Mockito.mock(RespostaAPregunta.class);
        RespostaAPregunta rap2 = Mockito.mock(RespostaAPregunta.class);

        Mockito.when(rap1.getOrdrePregunta()).thenReturn(0);
        Mockito.when(rap2.getOrdrePregunta()).thenReturn(1);
        Mockito.when(rap1.getStringResposta()).thenReturn("5");
        Mockito.when(rap2.getStringResposta()).thenReturn("Answer");

        re.addRespostaAPregunta(rap1);
        re.addRespostaAPregunta(rap2);

        // Verify everything works together
        assertEquals(100, re.getId());
        assertEquals(2, re.getRespostes().size());
        assertSame(rap1, re.getRespostaAPregunta(0));
        assertSame(rap2, re.getRespostaAPregunta(1));
        assertEquals(10.0, re.calcMax(0), 1e-9);
        assertEquals(1.0, re.calcMin(0), 1e-9);
        assertSame(p1, re.getPregunta(0));

        String[] respostesStr = re.getRespostesString();
        assertEquals("5", respostesStr[0]);
        assertEquals("Answer", respostesStr[1]);
    }
}
