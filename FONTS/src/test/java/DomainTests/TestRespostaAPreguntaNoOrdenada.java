package DomainTests;

import Domain.Model.RespostaAPreguntaMultiple;
import Domain.Model.RespostaAPreguntaNoOrdenada;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestRespostaAPreguntaNoOrdenada {
    private double expected;
    private Integer resposta1;
    private Integer resposta2;

    public TestRespostaAPreguntaNoOrdenada(double expected, Integer resposta1, Integer resposta2) {
        this.expected = expected;
        this.resposta1 = resposta1;
        this.resposta2 = resposta2;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { 0.0, 1, 1 },
            { 1.0, 1, 2 },
        });
    }

    @Test
    public void isEmpty(){
        RespostaAPreguntaNoOrdenada r1= new RespostaAPreguntaNoOrdenada(resposta1, 1);
        RespostaAPreguntaNoOrdenada r2 = new RespostaAPreguntaNoOrdenada(1);
        RespostaAPreguntaNoOrdenada r3 = new RespostaAPreguntaNoOrdenada(null, 1);
        
        assertFalse(r1.isEmpty());
        assertTrue(r2.isEmpty());
        assertTrue(r3.isEmpty());
    }

    @Test
    public void distRespostes() {
        RespostaAPreguntaNoOrdenada r1 = new RespostaAPreguntaNoOrdenada(resposta1, 1);
        RespostaAPreguntaNoOrdenada r2 = new RespostaAPreguntaNoOrdenada(resposta2, 1);

        assertEquals(expected, r1.distRespostesPregunta(r2), 0);
    }

    @Test
    public void getStringRespostaEmpty(){
        RespostaAPreguntaNoOrdenada r1 = new RespostaAPreguntaNoOrdenada(0);

        assertEquals("No hi ha resposta", r1.getStringResposta());
    }

    @Test
    public void getStringResposta(){
        RespostaAPreguntaNoOrdenada r1 = new RespostaAPreguntaNoOrdenada(1, 0);

        assertEquals("2", r1.getStringResposta());
    }
}
