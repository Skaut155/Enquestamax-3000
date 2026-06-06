package DomainTests;

import java.util.*;

import Domain.Model.CentroidePreguntaObertaStrategy;
import Domain.Model.RespostaAPregunta;
import Domain.Model.RespostaAPreguntaNumerica;
import Domain.Model.RespostaAPreguntaOberta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestCentroidePreguntaObertaStrategy {
    private RespostaAPregunta<?> expected;
    private ArrayList<RespostaAPregunta<?>> respostes;

    public TestCentroidePreguntaObertaStrategy(RespostaAPregunta<?> expected, ArrayList<RespostaAPregunta<?>> respostes) {
        this.expected = expected;
        this.respostes = respostes;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { new RespostaAPreguntaOberta("pringat", 1),
              new ArrayList<RespostaAPreguntaOberta>(Arrays.asList( 
                new RespostaAPreguntaOberta("Pringat?", 1)))
            },
            { new RespostaAPreguntaOberta("pre", 1),
              new ArrayList<RespostaAPreguntaOberta>(Arrays.asList( 
                new RespostaAPreguntaOberta("pre?", 1), 
                new RespostaAPreguntaOberta("Pre", 1), 
                new RespostaAPreguntaOberta("after", 1)))
            },
            { new RespostaAPreguntaOberta("pasta", 1),
              new ArrayList<RespostaAPreguntaOberta>(Arrays.asList( 
                new RespostaAPreguntaOberta("carn i pasta a la bolognesa !", 1), 
                new RespostaAPreguntaOberta("Pasta amb tomaquet", 1), 
                new RespostaAPreguntaOberta("amanida", 1)))
            },
            { new RespostaAPreguntaOberta("pasta", 1),
              new ArrayList<RespostaAPreguntaOberta>(Arrays.asList( 
                new RespostaAPreguntaOberta("bolognesa !", 1), 
                new RespostaAPreguntaOberta("carn i spaghetti", 1), 
                new RespostaAPreguntaOberta("tot menys carn !", 1), 
                new RespostaAPreguntaOberta("deliciosa pasta !", 1), 
                new RespostaAPreguntaOberta("carbonara!", 1), 
                new RespostaAPreguntaOberta("verdura", 1), 
                new RespostaAPreguntaOberta("arros", 1), 
                new RespostaAPreguntaOberta("qualsevol cosa", 1), 
                new RespostaAPreguntaOberta("menjar bo", 1), 
                new RespostaAPreguntaOberta("fruita", 1), 
                new RespostaAPreguntaOberta("de tot", 1), 
                new RespostaAPreguntaOberta("ni idea", 1), 
                new RespostaAPreguntaOberta("tu que vols", 1), 
                new RespostaAPreguntaOberta("digues", 1), 
                new RespostaAPreguntaOberta("Pasta amb tomaquet", 1), 
                new RespostaAPreguntaOberta("amanida", 1)))
            }
        });
    }

    @Test
    public void isSingleton(){
        assertSame(CentroidePreguntaObertaStrategy.getInstance(), CentroidePreguntaObertaStrategy.getInstance());
    }

    @Test
    public void calcularCentroide(){
        RespostaAPregunta<?> centroide = respostes.get(0).calcularCentroide(respostes);        
        assertEquals(expected.getResposta(), centroide.getResposta());
    }

    @Test
    public void calcularCentroideEmpty(){
        ArrayList<RespostaAPregunta<?>> respostes = new ArrayList<>(0);
        CentroidePreguntaObertaStrategy c = CentroidePreguntaObertaStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }

    @Test
    public void calcularCentroideNoOberta(){
        ArrayList<RespostaAPregunta<?>> respostes = 
            new ArrayList<>(Arrays.asList(new RespostaAPreguntaNumerica(2, 1)));
        CentroidePreguntaObertaStrategy c = CentroidePreguntaObertaStrategy.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularCentroide(respostes);
        });
    }
}
