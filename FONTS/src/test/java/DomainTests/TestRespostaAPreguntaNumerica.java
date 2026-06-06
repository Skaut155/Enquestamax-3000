package DomainTests;

import java.util.*;

import Domain.Model.RespostaAPreguntaNumerica;

import Domain.Model.RespostaAPreguntaOrdenada;
import Domain.Model.RespostaEnquesta;
import org.mockito.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class TestRespostaAPreguntaNumerica{
    private final double expected;
    private final double v1;
    private final double v2;
    
    @Mock
    RespostaEnquesta reMock;
    
    public TestRespostaAPreguntaNumerica(double e, double v1, double v2){
       this.expected = e; this.v1 = v1; this.v2 = v2;
    }
    
    @Parameters
    public static Collection<Double[]> getTestParameters(){
       return Arrays.asList( new Double[][] {
         { 0.0, 150.0, 150.0 }, // expected, v1, v2
         { 0.0, -150.0, -150.0 },
         { 1.0, 150.0, -150.0},
         { 1.0, -150.0, 150.0},
         { 0.5, -150.0, 0.0},
         { 64.0 / 300.0, 137.0, 73.0},
         { 1.0 / 300.0, 0.0, 1.0} });
    }
    
    @Before
    public void initMocks(){
       MockitoAnnotations.initMocks(this);
    }

    @Test
    public void distRespostes(){
       assertNotNull(reMock);
       when(reMock.calcMax(1)).thenReturn(150.0);
       when(reMock.calcMin(1)).thenReturn(-150.0);
    
       RespostaAPreguntaNumerica r1 = new RespostaAPreguntaNumerica(v1, 1);
       RespostaAPreguntaNumerica r2 = new RespostaAPreguntaNumerica(v2, 1);
       r1.setRespostaEnquesta(reMock);
       r2.setRespostaEnquesta(reMock);
       assertEquals(expected, r1.distRespostesPregunta(r2), 0);
    }

    @Test
    public void testMinMaxEqual(){
       assertNotNull(reMock);
       when(reMock.calcMax(1)).thenReturn(100.0);
       when(reMock.calcMin(1)).thenReturn(100.0);

       RespostaAPreguntaNumerica r1 = new RespostaAPreguntaNumerica(50.0, 1);
       RespostaAPreguntaNumerica r2 = new RespostaAPreguntaNumerica(75.0, 1);
       r1.setRespostaEnquesta(reMock);
       r2.setRespostaEnquesta(reMock);
       assertEquals(0.0, r1.distRespostesPregunta(r2), 0);
    }

    @Test
    public void testBlankConstructor(){
        RespostaAPreguntaNumerica r = new RespostaAPreguntaNumerica(1);
        Double value = r.getResposta();
        assertNull(value);
    }

    @Test
    public void TestGetString(){
        RespostaAPreguntaNumerica r1 = new RespostaAPreguntaNumerica(1);
        RespostaAPreguntaNumerica r2 = new RespostaAPreguntaNumerica(1,2);
        assertEquals("No hi ha resposta", r1.getStringResposta());
        assertEquals("1.0", r2.getStringResposta());
    }

}
