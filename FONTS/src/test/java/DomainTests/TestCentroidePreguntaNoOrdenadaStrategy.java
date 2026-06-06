package DomainTests;

import java.util.*;

import Domain.Model.CentroidePreguntaNoOrdenadaStrategy;
import Domain.Model.RespostaAPregunta;
import Domain.Model.RespostaAPreguntaNoOrdenada;
import Domain.Model.RespostaAPreguntaOberta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestCentroidePreguntaNoOrdenadaStrategy {
    
    private RespostaAPregunta<?> expected;
    private ArrayList<RespostaAPregunta<?>> respostes;

    public TestCentroidePreguntaNoOrdenadaStrategy(RespostaAPregunta<?> expected, ArrayList<RespostaAPregunta<?>> respostes) {
        this.expected = expected;
        this.respostes = respostes;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { new RespostaAPreguntaNoOrdenada(1, 1),
              new ArrayList<RespostaAPreguntaNoOrdenada>(Arrays.asList( 
                new RespostaAPreguntaNoOrdenada(1, 1), 
                new RespostaAPreguntaNoOrdenada(1, 1)))
            },
            { new RespostaAPreguntaNoOrdenada(2, 1),
              new ArrayList<RespostaAPreguntaNoOrdenada>(Arrays.asList( 
                new RespostaAPreguntaNoOrdenada(1, 1), 
                new RespostaAPreguntaNoOrdenada(2, 1), 
                new RespostaAPreguntaNoOrdenada(2, 1)))
            }
        });
    }

    @Test
    public void isSingleton(){
        assertSame(CentroidePreguntaNoOrdenadaStrategy.getInstance(), CentroidePreguntaNoOrdenadaStrategy.getInstance());
    }

    @Test
    public void calcularCentroide(){
        RespostaAPregunta<?> centroide = respostes.get(0).calcularCentroide(respostes);        
        assertEquals(expected.getResposta(), centroide.getResposta());
    }

    @Test
    public void calcularCentroideEmpty(){
        ArrayList<RespostaAPregunta<?>> respostes = new ArrayList<>(0);
        CentroidePreguntaNoOrdenadaStrategy c = CentroidePreguntaNoOrdenadaStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }

    @Test
    public void calcularCentroideNoNoOrdenada(){
        ArrayList<RespostaAPregunta<?>> respostes = 
            new ArrayList<>(Arrays.asList(new RespostaAPreguntaOberta("h", 1)));
        CentroidePreguntaNoOrdenadaStrategy c = CentroidePreguntaNoOrdenadaStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }
}
