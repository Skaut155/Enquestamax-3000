package DomainTests;

import Domain.Model.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class TestDistanceEuclideanStrategy {

    DistanceEuclideanStrategy distanceEuclideanStrategy;
    ArrayList<RespostaAPregunta> respostaEnquesta1;
    ArrayList<RespostaAPregunta> respostaEnquesta2;

    @Before
    public void setUp() {
        distanceEuclideanStrategy = DistanceEuclideanStrategy.getInstance();
        
        respostaEnquesta1 = new ArrayList<>();
        respostaEnquesta1.add(new RespostaAPreguntaNoOrdenada(0, 0));

        respostaEnquesta2 = new ArrayList<>();        
        respostaEnquesta2.add(new RespostaAPreguntaNoOrdenada(1, 0));
    }
    
    @Test
    public void testSingleton() {
        DistanceEuclideanStrategy instance1 = DistanceEuclideanStrategy.getInstance();
        DistanceEuclideanStrategy instance2 = DistanceEuclideanStrategy.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testDistRespostesEnquestaWithSameResponse() {
        double dist = distanceEuclideanStrategy.calcularDistCorrectRespostesEnquesta(respostaEnquesta1, respostaEnquesta1);

        assertEquals("Distance to itself should be 0", 0.0, dist, 0.001);
    }

    @Test
    public void testDistRespostesEnquestaWithDifferentResponses() {
        double dist = distanceEuclideanStrategy.calcularDistCorrectRespostesEnquesta(respostaEnquesta1, respostaEnquesta2);

        assertTrue("Distance should be positive for different responses", dist >= 0);
        assertTrue("Distance should be inferior to 1", dist <= 1);
    }

    @Test
    public void testDistRespostesEnquestaThrowsExceptionForDifferentsAnswers() {
        ArrayList<RespostaAPregunta> r1 = new ArrayList<>();
        r1.add(new RespostaAPreguntaNumerica(20, 0));

        assertThrows(IllegalArgumentException.class, () -> {
            distanceEuclideanStrategy.calcularDistCorrectRespostesEnquesta(r1, respostaEnquesta2);
        });
    }
}
