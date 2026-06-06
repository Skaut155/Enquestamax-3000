package DomainTests;

import Domain.Model.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class TestCoefficientDunnStrategy {
    
    CoefficientDunnStrategy coefficientDunnStrategy;
    ArrayList<RespostaEnquesta>[] clusters;

    @Before
    public void setUp() {
        coefficientDunnStrategy = CoefficientDunnStrategy.getInstance();
        clusters = new ArrayList[3];
        clusters[0] = new ArrayList<>();
        clusters[1] = new ArrayList<>();
        clusters[2] = new ArrayList<>();

        RespostaEnquesta re1 = new RespostaEnquesta();
        re1.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(0, 0));
        clusters[0].add(re1);
        
        RespostaEnquesta re2 = new RespostaEnquesta();
        re2.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(1, 0));
        clusters[1].add(re2);

        RespostaEnquesta re3 = new RespostaEnquesta();
        re3.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(2, 1));
        clusters[2].add(re3);
    }


    @Test
    public void testSingleton() {
        CoefficientDunnStrategy instance1 = CoefficientDunnStrategy.getInstance();
        CoefficientDunnStrategy instance2 = CoefficientDunnStrategy.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testCalcularCoefficientWithOneCluster() {
        ArrayList<RespostaEnquesta>[] emptyClusters = new ArrayList[1];
        assertThrows(IllegalArgumentException.class, () -> coefficientDunnStrategy.calcularCoefficient(emptyClusters));
    }

    @Test
    public void testCalcularCoefficientWithEmptyClusters() {
        ArrayList<RespostaEnquesta>[] emptyClusters = new ArrayList[2];
        emptyClusters[0] = new ArrayList<>();
        emptyClusters[1] = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> coefficientDunnStrategy.calcularCoefficient(emptyClusters));
    }

    @Test
    public void testCalcularCoefficient() {
        double dunnCoefficient = coefficientDunnStrategy.calcularCoefficient(clusters);
        assertTrue("Dunn Coefficient should be non-negative", dunnCoefficient >= 0);

        RespostaEnquesta re4 = new RespostaEnquesta();
        re4.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(3, 0));
        clusters[2].add(re4);

        RespostaEnquesta re5 = new RespostaEnquesta();
        re5.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(4, 0));
        clusters[2].add(re5);

        double newDunnCoefficient = coefficientDunnStrategy.calcularCoefficient(clusters);
        assertTrue("Dunn Coefficient should change after modifying clusters", dunnCoefficient != newDunnCoefficient);
    }

    @Test
    public void testAcceptableThreshold() {
        double threshold = coefficientDunnStrategy.acceptableThreshold();
        assertEquals("Acceptable threshold should be 10.0", 1.0, threshold, 0.0001);
    }
}
