package DomainTests;

import java.util.*;

import Domain.Model.CentroidePreguntaOrdenadaStrategy;
import Domain.Model.RespostaAPregunta;
import Domain.Model.RespostaAPreguntaOrdenada;
import Domain.Model.RespostaAPreguntaOberta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestCentroidePreguntaOrdenadaStrategy {
        
    private RespostaAPregunta<?> expected;
    private ArrayList<RespostaAPregunta<?>> respostes;

    public TestCentroidePreguntaOrdenadaStrategy(RespostaAPregunta<?> expected, ArrayList<RespostaAPregunta<?>> respostes) {
        this.expected = expected;
        this.respostes = respostes;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { new RespostaAPreguntaOrdenada(1, 1),
              new ArrayList<RespostaAPreguntaOrdenada>(Arrays.asList(
                new RespostaAPreguntaOrdenada(1, 1), 
                new RespostaAPreguntaOrdenada(1, 1)))
            },
            { new RespostaAPreguntaOrdenada(2, 1),
              new ArrayList<RespostaAPreguntaOrdenada>(Arrays.asList( 
                new RespostaAPreguntaOrdenada(1, 1), 
                new RespostaAPreguntaOrdenada(2, 1), 
                new RespostaAPreguntaOrdenada(2, 1)))
            }
        });
    }

    @Test
    public void isSingleton(){
        assertSame(CentroidePreguntaOrdenadaStrategy.getInstance(), CentroidePreguntaOrdenadaStrategy.getInstance());
    }

    @Test
    public void calcularCentroide(){
        RespostaAPregunta<?> centroide = respostes.get(0).calcularCentroide(respostes);        
        assertEquals(expected.getResposta(), centroide.getResposta());
    }

    @Test
    public void calcularCentroideEmpty(){
        ArrayList<RespostaAPregunta<?>> respostes = new ArrayList<>(0);
        CentroidePreguntaOrdenadaStrategy c = CentroidePreguntaOrdenadaStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }

    @Test
    public void calcularCentroideNoOrdenada(){
        ArrayList<RespostaAPregunta<?>> respostes = 
            new ArrayList<>(Arrays.asList(new RespostaAPreguntaOberta("h", 1)));
        CentroidePreguntaOrdenadaStrategy c = CentroidePreguntaOrdenadaStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }
}
