package DomainTests;

import Domain.Model.K_meansRandom;
import Domain.Model.RespostaEnquesta;

import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestK_meansRandom {

    @Test
    public void testInitialize(){
        ArrayList<RespostaEnquesta> respostes = new ArrayList<RespostaEnquesta>(20);
        for (int i = 0; i < 20; i++) {
            respostes.add(new RespostaEnquesta());
        }
        
        K_meansRandom km = new K_meansRandom();
        int k = 10;

        ArrayList<RespostaEnquesta> c = km.initialize(k, respostes);
        
        ArrayList<RespostaEnquesta> respostesList = new ArrayList<>();
        for(RespostaEnquesta r : respostes) respostesList.add(r);

        assertNotNull(c);
        assertTrue("Should have k centroids", c.size() == k);
        for(RespostaEnquesta r : c){
            assertTrue("The centroid should be an existing RespostaEnquesta", respostesList.contains(r));
        }
    }
}
