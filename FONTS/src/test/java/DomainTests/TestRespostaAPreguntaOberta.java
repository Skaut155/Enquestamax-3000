package DomainTests;

import java.util.*;

import Domain.Model.RespostaAPreguntaMultiple;
import Domain.Model.RespostaAPreguntaOberta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestRespostaAPreguntaOberta {

    private double expected;
    private String resposta1;
    private String resposta2;

    public TestRespostaAPreguntaOberta(double expected, String resposta1, String resposta2) {
        this.expected = expected;
        this.resposta1 = resposta1;
        this.resposta2 = resposta2;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { 0.0, "hola", "hola" },
        });
    }

    @Test
    public void isEmpty(){
        RespostaAPreguntaOberta r1= new RespostaAPreguntaOberta(resposta1, 1);
        RespostaAPreguntaOberta r2 = new RespostaAPreguntaOberta(1);
        RespostaAPreguntaOberta r3 = new RespostaAPreguntaOberta(null, 1);
        
        assertFalse(r1.isEmpty());
        assertTrue(r2.isEmpty());
        assertTrue(r3.isEmpty());
    }

    @Test
    public void distRespostes() {
        RespostaAPreguntaOberta r1 = new RespostaAPreguntaOberta(resposta1, 1);
        RespostaAPreguntaOberta r2 = new RespostaAPreguntaOberta(resposta2, 1);

        assertEquals(expected, r1.distRespostesPregunta(r2), 0);
    }

    @Test
    public void getStringRespostaEmpty(){
        RespostaAPreguntaOberta r1 = new RespostaAPreguntaOberta(0);

        assertEquals("No hi ha resposta", r1.getStringResposta());
    }

    @Test
    public void getStringResposta(){
        RespostaAPreguntaOberta r1 = new RespostaAPreguntaOberta("hola", 0);

        assertEquals("hola", r1.getStringResposta());
    }
}
