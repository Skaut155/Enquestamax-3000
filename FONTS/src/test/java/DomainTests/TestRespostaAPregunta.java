package DomainTests;

import java.lang.reflect.Array;
import java.util.*;

import Domain.Model.CentroidePreguntaObertaStrategy;
import Domain.Model.CentroideStrategy;
import Domain.Model.RespostaAPregunta;
import Domain.Model.RespostaAPreguntaOrdenada;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestRespostaAPregunta<T> {

    T resposta;
    int ordrePregunta;
    CentroideStrategy centroideStrategy;
    
    public TestRespostaAPregunta(T resposta, int ordrePregunta, CentroideStrategy centroideStrategy) {
        this.resposta = resposta;
        this.ordrePregunta = ordrePregunta;
        this.centroideStrategy = centroideStrategy;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { 1, 1, CentroidePreguntaObertaStrategy.getInstance() },
            { 2, 1, CentroidePreguntaObertaStrategy.getInstance() }
        });
    }

    @Test
    public void isEmpty(){
        RespostaAPregunta<?> r1 = new RespostaAPreguntaOrdenada((Integer) resposta, 1);
        RespostaAPregunta<?> r2 = new RespostaAPreguntaOrdenada(null, 1);
        
        assertFalse(r1.isEmpty());
        assertTrue(r2.isEmpty());
    }

    @Test
    public void getAndSetResposta(){
        RespostaAPregunta<Integer> r = new RespostaAPreguntaOrdenada((Integer) resposta, ordrePregunta);
        
        assertEquals(resposta, r.getResposta());
        
        r.setResposta((Integer) 3);
        assertEquals((Integer) 3, r.getResposta());
    }
}
