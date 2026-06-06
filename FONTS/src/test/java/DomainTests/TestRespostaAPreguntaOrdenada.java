package DomainTests;

import java.util.*;

import Domain.Model.RespostaAPreguntaOrdenada;

import Domain.Model.RespostaEnquesta;
import Domain.Model.Pregunta;
import org.mockito.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class TestRespostaAPreguntaOrdenada{
    private final double expected;
    private final double v1;
    private final double v2;
    
    @Mock
    RespostaEnquesta re;
    @Mock
    Pregunta p;
    
    public TestRespostaAPreguntaOrdenada(double e, double v1, double v2){
       this.expected = e; this.v1 = v1; this.v2 = v2;
    }
    
    @Parameters
    public static Collection<Double[]> getTestParameters(){
       return Arrays.asList( new Double[][] {
         { 1.0, 0.0, 4.0 }, // expected, v1, v2
         { 0.0, 2.0, 2.0 }, 
         { 0.5, 1.0, 3.0} });
    }
    
    @Before
    public void initMocks(){
       MockitoAnnotations.initMocks(this);
    }

    @Test
    public void distRespostes(){
       assertNotNull(p);
       assertNotNull(re);
       when(re.getPregunta(1)).thenReturn(p);
       when(p.numOpcions()).thenReturn(5);
       
       RespostaAPreguntaOrdenada r1 = new RespostaAPreguntaOrdenada((int) v1, 1);
       RespostaAPreguntaOrdenada r2 = new RespostaAPreguntaOrdenada((int) v2, 1);
       r1.setRespostaEnquesta(re);
       r2.setRespostaEnquesta(re);
       assertEquals(expected, r1.distRespostesPregunta(r2), 0);
    }

    @Test
    public void TestBlankConstructor(){
        RespostaAPreguntaOrdenada r = new RespostaAPreguntaOrdenada(1);
        int value = r.getResposta();
        assertEquals(-1, value);
    }

    @Test
    public void TestGetString(){
        RespostaAPreguntaOrdenada r1 = new RespostaAPreguntaOrdenada(1);
        RespostaAPreguntaOrdenada r2 = new RespostaAPreguntaOrdenada(1,2);
        assertEquals("No hi ha resposta", r1.getStringResposta());
        assertEquals("2", r2.getStringResposta());
    }

}
