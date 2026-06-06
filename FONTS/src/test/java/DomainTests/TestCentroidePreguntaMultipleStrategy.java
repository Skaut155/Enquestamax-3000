package DomainTests;

import java.util.*;

import Domain.Model.CentroidePreguntaMultipleStrategy;
import Domain.Model.RespostaAPregunta;
import Domain.Model.RespostaAPreguntaMultiple;
import Domain.Model.RespostaAPreguntaOberta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestCentroidePreguntaMultipleStrategy {

    private RespostaAPregunta<?> expected;
    private ArrayList<RespostaAPregunta<?>> respostes;

    public TestCentroidePreguntaMultipleStrategy(RespostaAPregunta<?> expected, ArrayList<RespostaAPregunta<?>> respostes) {
        this.expected = expected;
        this.respostes = respostes;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { new RespostaAPreguntaMultiple(new ArrayList<>(Arrays.asList(1, 2, 3)), 1),
              new ArrayList<RespostaAPreguntaMultiple>(Arrays.asList( 
                new RespostaAPreguntaMultiple(new ArrayList<>(Arrays.asList(1, 2, 3)), 1), 
                new RespostaAPreguntaMultiple(new ArrayList<>(Arrays.asList(1, 2, 3)), 1)))
            },
            { new RespostaAPreguntaMultiple(new ArrayList<>(Arrays.asList(1, 2, 3)), 1),
              new ArrayList<RespostaAPreguntaMultiple>(Arrays.asList( 
                new RespostaAPreguntaMultiple(new ArrayList<>(Arrays.asList(1, 2, 3)), 1), 
                new RespostaAPreguntaMultiple(new ArrayList<>(Arrays.asList(1, 2, 3)), 1), 
                new RespostaAPreguntaMultiple(new ArrayList<>(Arrays.asList(1, 2, 3)), 1)))
            }
        });
    }

    @Test
    public void isSingleton(){
        assertSame(CentroidePreguntaMultipleStrategy.getInstance(), CentroidePreguntaMultipleStrategy.getInstance());
    }

    @Test
    public void calcularCentroide(){
        RespostaAPregunta<?> centroide = respostes.get(0).calcularCentroide(respostes);        
        assertEquals(expected.getResposta(), centroide.getResposta());
    }

    @Test
    public void calcularCentroideEmpty(){
         ArrayList<RespostaAPregunta<?>> respostes = new ArrayList<>(0);
        CentroidePreguntaMultipleStrategy c = CentroidePreguntaMultipleStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }

    @Test
    public void calcularCentroideNoMultiple(){
        ArrayList<RespostaAPregunta<?>> respostes = 
            new ArrayList<>(Arrays.asList(new RespostaAPreguntaOberta("h", 1)));
        CentroidePreguntaMultipleStrategy c = CentroidePreguntaMultipleStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }
}
