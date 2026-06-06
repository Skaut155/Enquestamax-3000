package DomainTests;

import java.util.*;

import Domain.Model.CentroidePreguntaNumericaStrategy;
import Domain.Model.RespostaAPregunta;
import Domain.Model.RespostaAPreguntaNumerica;
import Domain.Model.RespostaAPreguntaOberta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestCentroidePreguntaNumericaStrategy {
    private RespostaAPreguntaNumerica expected;
    private ArrayList<RespostaAPregunta<?>> respostes;

    public TestCentroidePreguntaNumericaStrategy(RespostaAPreguntaNumerica expected, ArrayList<RespostaAPregunta<?>> respostes) {
        this.expected = expected;
        this.respostes = respostes;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { new RespostaAPreguntaNumerica(0.0, 1),
              new ArrayList<RespostaAPreguntaNumerica>(Arrays.asList( 
                new RespostaAPreguntaNumerica(0.0, 1), 
                new RespostaAPreguntaNumerica(0.0, 1)))
            },
            { new RespostaAPreguntaNumerica(0.2, 1),
              new ArrayList<RespostaAPreguntaNumerica>(Arrays.asList( 
                new RespostaAPreguntaNumerica(0.3, 1), 
                new RespostaAPreguntaNumerica(0.2, 1), 
                new RespostaAPreguntaNumerica(0.1, 1)))
            }
        });
    }

    @Test
    public void isSingleton(){
        assertSame(CentroidePreguntaNumericaStrategy.getInstance(), CentroidePreguntaNumericaStrategy.getInstance());
    }

    @Test
    public void calcularCentroide(){
        RespostaAPreguntaNumerica centroide = (RespostaAPreguntaNumerica) respostes.get(0).calcularCentroide(respostes);

        assertEquals(expected.getResposta(), centroide.getResposta(), 0.01);
    }

    @Test
    public void calcularCentroideEmpty(){
        ArrayList<RespostaAPregunta<?>> respostes = new ArrayList<>(0);
        CentroidePreguntaNumericaStrategy c = CentroidePreguntaNumericaStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }

    @Test
    public void calcularCentroideNoNumerica(){
        ArrayList<RespostaAPregunta<?>> respostes = 
            new ArrayList<>(Arrays.asList(new RespostaAPreguntaOberta("h", 1)));
        CentroidePreguntaNumericaStrategy c = CentroidePreguntaNumericaStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }
}
