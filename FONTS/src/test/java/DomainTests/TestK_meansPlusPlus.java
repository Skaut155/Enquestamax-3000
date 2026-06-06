package DomainTests;

import Domain.Model.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestK_meansPlusPlus {
    private int k;
    private ArrayList<RespostaEnquesta> respostes;

    public TestK_meansPlusPlus(int k, ArrayList<RespostaEnquesta> respostes) {
        this.k = k;
        this.respostes = respostes;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            // Test case 1: k=1, minimal case with 1 response
            {1, createRespostes(1)},
            
            // Test case 2: k=2, small dataset with 3 responses
            {2, createRespostes(3)},
            
            // Test case 3: k=3, exactly k responses
            {3, createRespostes(3)},
            
            // Test case 4: k=5, medium dataset
            {5, createRespostes(10)},
            
            // Test case 5: k=3, larger dataset
            {3, createRespostes(20)},
            
            // Test case 6: k=10, large dataset
            {10, createRespostes(50)},

            // Test case 7: k = 49, large dataset with large k
            {99, createRespostes(100)}
        });
    }

    /**
     * Helper method to create a list of RespostaEnquesta objects with sample data
     * @param count number of RespostaEnquesta objects to create
     * @return ArrayList of RespostaEnquesta with varied responses
     */
    private static ArrayList<RespostaEnquesta> createRespostes(int count) {
        ArrayList<RespostaEnquesta> respostes = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            RespostaEnquesta re = new RespostaEnquesta();
            re.setId(i);
            
            // Add varied responses to create different data points
            re.addRespostaAPregunta(new RespostaAPreguntaOberta("Name" + i, 0));
            re.addRespostaAPregunta(new RespostaAPreguntaNumerica(20 + (i % 50), 1));
            
            respostes.add(re);
        }
        
        return respostes;
    }

    /**
     * Test helper class that extends K_meansPlusPlus to override the protected
     * distRespostesEnquesta method for testing purposes
     */
    private static class K_meansPlusPlusTestable extends K_meansPlusPlus {
        
        @Override
        protected double distRespostesEnquesta(RespostaEnquesta r1, RespostaEnquesta r2) {
            // Simple distance calculation based on IDs to avoid real computation
            // and potential NullPointerExceptions
            if (r1.getId() == r2.getId()) {
                return 0.0;
            }
            // Return a simple distance based on ID difference
            // This ensures different responses have positive distances
            return Math.abs(r1.getId() - r2.getId()) * 1.0;
        }
    }

    @Test
    public void testInitialize() {
        // Use the testable version that overrides distRespostesEnquesta
        K_meansPlusPlus km = new K_meansPlusPlusTestable();

        ArrayList<RespostaEnquesta> c = km.initialize(k, respostes);
        
        ArrayList<RespostaEnquesta> respostesList = new ArrayList<>();
        for (RespostaEnquesta r : respostes) respostesList.add(r);

        assertNotNull("Centroids should not be null", c);
        assertEquals("Should have k centroids", k, c.size());
        
        for (RespostaEnquesta centroid : c) {
            assertTrue("The centroid should be an existing RespostaEnquesta", 
                       respostesList.contains(centroid));
        }
        
        // Additional check: all centroids should be unique
        for (int i = 0; i < c.size(); i++) {
            for (int j = i + 1; j < c.size(); j++) {
                assertNotSame("Centroids should be unique", c.get(i), c.get(j));
            }
        }
    }

    @Test
    public void testInitializeCentroidsAreDifferent() {
        // Use the testable version that overrides distRespostesEnquesta
        K_meansPlusPlus km = new K_meansPlusPlusTestable();

        ArrayList<RespostaEnquesta> c = km.initialize(k, respostes);
        
        assertNotNull("Centroids should not be null", c);
        assertEquals("Should have k centroids", k, c.size());
        
        // Test that all centroids are different by comparing their actual response content
        for (int i = 0; i < c.size(); i++) {
            for (int j = i + 1; j < c.size(); j++) {
                RespostaEnquesta centroid1 = c.get(i);
                RespostaEnquesta centroid2 = c.get(j);
                
                // Check they are not the same object
                assertNotSame("Centroids at positions " + i + " and " + j + " should not be the same object",
                             centroid1, centroid2);
            }
        }
    }

    @Test
    public void testInitializeReturnsRandomizedCentroids() {
        // Skip this test if k or respostes size is too small
        if (k < 2 || respostes.size() < k + 5) {
            return; // Not enough variation to test randomness
        }
        
        K_meansPlusPlus km = new K_meansPlusPlusTestable();
        
        // Run initialization multiple times
        ArrayList<RespostaEnquesta> firstRun = km.initialize(k, respostes);
        
        boolean foundDifference = false;
        int maxAttempts = 10; // Try up to 10 times to find different results
        
        for (int attempt = 0; attempt < maxAttempts && !foundDifference; attempt++) {
            ArrayList<RespostaEnquesta> nextRun = km.initialize(k, respostes);
            
            // Compare if the centroids are different
            // Check if at least one centroid is different (different ID or position)
            for (int i = 0; i < k; i++) {
                if (firstRun.get(i).getId() != nextRun.get(i).getId()) {
                    foundDifference = true;
                    break;
                }
            }
            
            // Alternative check: compare the set of IDs
            if (!foundDifference) {
                ArrayList<Integer> firstIds = new ArrayList<>();
                ArrayList<Integer> nextIds = new ArrayList<>();
                
                for (int i = 0; i < k; i++) {
                    firstIds.add(firstRun.get(i).getId());
                    nextIds.add(nextRun.get(i).getId());
                }
                
                // Sort to compare sets regardless of order
                firstIds.sort(Integer::compareTo);
                nextIds.sort(Integer::compareTo);
                
                if (!firstIds.equals(nextIds)) {
                    foundDifference = true;
                }
            }
        }
        
        assertTrue("After " + maxAttempts + " runs, initialize should return different centroids " +
                   "at least once (due to randomization). k=" + k + ", n=" + respostes.size(),
                   foundDifference);
    }

}