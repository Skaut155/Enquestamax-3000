package DomainTests;

import java.util.*;

import Domain.Model.RespostaAPreguntaMultiple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestRespostaAPreguntaMultiple {
    private double expected;
    private ArrayList<Integer> resposta1;
    private ArrayList<Integer> resposta2;

    public TestRespostaAPreguntaMultiple(double expected, ArrayList<Integer> resposta1, ArrayList<Integer> resposta2) {
        this.expected = expected;
        this.resposta1 = resposta1;
        this.resposta2 = resposta2;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { 0.0, new ArrayList<>(Arrays.asList(1, 2, 3)), new ArrayList<>(Arrays.asList(1, 2, 3))},
            { 1.0, new ArrayList<>(Arrays.asList(4)), new ArrayList<>(Arrays.asList(1, 2, 3))},
            { 0.5, new ArrayList<>(Arrays.asList(1, 2)), new ArrayList<>(Arrays.asList(1))},
            { 0.67, new ArrayList<>(Arrays.asList(1, 2, 3)), new ArrayList<>(Arrays.asList(1))}
        });
    }

    @Test
    public void isEmpty(){
        RespostaAPreguntaMultiple r1= new RespostaAPreguntaMultiple(resposta1, 1);
        RespostaAPreguntaMultiple r2 = new RespostaAPreguntaMultiple(1);
        RespostaAPreguntaMultiple r3 = new RespostaAPreguntaMultiple(null, 1);
        
        assertFalse(r1.isEmpty());
        assertTrue(r2.isEmpty());
        assertTrue(r3.isEmpty());
    }

    @Test
    public void distRespostes() {
        RespostaAPreguntaMultiple r1 = new RespostaAPreguntaMultiple(resposta1, 1);
        RespostaAPreguntaMultiple r2 = new RespostaAPreguntaMultiple(resposta2, 1);

        assertEquals(expected, r1.distRespostesPregunta(r2), 0.01);
    }

    @Test
    public void getStringRespostaEmpty(){
        RespostaAPreguntaMultiple r1 = new RespostaAPreguntaMultiple(0);

        assertEquals("No hi ha resposta", r1.getStringResposta());
    }

    @Test
    public void getStringResposta(){
        RespostaAPreguntaMultiple r1 = new RespostaAPreguntaMultiple(new ArrayList<>(Arrays.asList(1, 2, 3)), 0);

        assertEquals("2 3 4 ", r1.getStringResposta());
    }
}
