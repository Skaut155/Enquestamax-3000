package DomainTests;

import Domain.Model.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class TestAlgoritmeClustering {

    /**
     * Concrete testable implementation of AlgoritmeClustering
     * to test the abstract class methods
     */
    private static class AlgoritmeClusteringTestable extends AlgoritmeClustering {

        @Override
        protected ArrayList<RespostaEnquesta>[] computeClustering(int k, ArrayList<RespostaEnquesta> respostes) {
            // Simple implementation: each response in its own cluster up to k clusters
            @SuppressWarnings("unchecked")
            ArrayList<RespostaEnquesta>[] clusters = new ArrayList[k];

            for (int i = 0; i < k; i++) {
                clusters[i] = new ArrayList<>();
                if (i < respostes.size()) {
                    clusters[i].add(respostes.get(i));
                }
            }

            Random rand = new Random();

            // Add remaining responses to a random cluster
            for (int i = k; i < respostes.size(); i++) {
                int index = rand.nextInt(k);
                clusters[index].add(respostes.get(i));
            }

            return clusters;
        }

        // Make protected method accessible for testing
        public double distRespostesEnquestaPublic(RespostaEnquesta r1, RespostaEnquesta r2) {
            return distRespostesEnquesta(r1, r2);
        }

        @Override
        public ArrayList<RespostaEnquesta> getCentroids() {
            return new ArrayList<>();
        }
    }

    private AlgoritmeClusteringTestable algoritme;
    private ArrayList<RespostaEnquesta> respostes;

    @Before
    public void setUp() {
        algoritme = spy(new AlgoritmeClusteringTestable());
        respostes = createSampleRespostes(20);
    }

    private ArrayList<RespostaEnquesta> createSampleRespostes(int count) {
        ArrayList<RespostaEnquesta> list = new ArrayList<>();

        Enquesta enquesta = new Enquesta("sampleSurvey");
        RespostesContainer container = new RespostesContainer(enquesta);

        for (int i = 0; i < count; i++) {
            RespostaEnquesta re = new RespostaEnquesta();
            re.setId(i);
            re.associaContainer(container);

            Random rand = new Random();

            int v = rand.nextInt(i + 1);

            re.addRespostaAPregunta(new RespostaAPreguntaOberta("Answer " + v, 0));
            re.addRespostaAPregunta(new RespostaAPreguntaNumerica(v + 10, 1));
            re.addRespostaAPregunta(
                    new RespostaAPreguntaMultiple(new ArrayList<>(Arrays.asList(Integer.valueOf(v % 5))), 2));
            re.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(v % 4, 3));
            re.addRespostaAPregunta(new RespostaAPreguntaOrdenada(v % 3, 4) {
                @Override
                public int getNumOpcionsPregunta() {
                    return 3;
                }
            });

            list.add(re);
        }

        return list;
    }

    @Test
    public void testSetAndGetDistanceStrategy() {
        DistanceEuclideanStrategy des = DistanceEuclideanStrategy.getInstance();
        algoritme.setDistanceStrategy(des);

        assertEquals("Distance strategy should be set correctly", des, algoritme.getDistanceStrategy());
    }

    @Test
    public void testSetAndGetCoefficientClusterStrategy() {
        CoefficientDunnStrategy ccss = CoefficientDunnStrategy.getInstance();
        
        algoritme.setCoefficientClusterStrategy(ccss);
        assertEquals("Coefficient cluster strategy should be set correctly", ccss, algoritme.getCoefficientClusterStrategy());
    }

    @Test
    public void testComputeAlgorithmWithValidParameters() {
        int k = 3;

        ArrayList<RespostaEnquesta>[] clusters = algoritme.computeAlgorithm(k, respostes);

        assertNotNull("Clusters should not be null", clusters);
        assertEquals("Should have k clusters", k, clusters.length);

        for (int i = 0; i < k; i++) {
            assertNotNull("Cluster " + i + " should not be null", clusters[i]);
            for (int j = 0; j < clusters[i].size(); j++) {
                assertNotNull("Response " + j + " in cluster " + i + " should not be null", clusters[i].get(j));
                assertTrue("Response " + j + " in cluster " + i + " should be from original respostes",
                        respostes.contains(clusters[i].get(j)));
            }
        }

        // Verify that computeClustering was called
        verify(algoritme, atLeast(1)).computeClustering(eq(k), any());
    }

    @Test
    public void testComputeAlgorithmThrowsExceptionForNullRespostes() {
        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeAlgorithm(3, null);
        });
    }

    @Test
    public void testComputeAlgorithmThrowsExceptionForRespostesOfDifferentSizes() {
        ArrayList<RespostaEnquesta> invalidRespostes = new ArrayList<>(respostes);
        // Add a response with different number of answers
        RespostaEnquesta r = new RespostaEnquesta();
        r.addRespostaAPregunta(new RespostaAPreguntaOberta("Different", 0));
        invalidRespostes.add(r);
        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeAlgorithm(3, invalidRespostes);
        });
    }

    @Test
    public void testComputeAlgorithmThrowsExceptionForEmptyRespostes() {
        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeAlgorithm(3, new ArrayList<>());
        });
    }

    @Test
    public void testComputeAlgorithmThrowsExceptionForInvalidK() {
        // k < 1
        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeAlgorithm(0, respostes);
        });

        // k > respostes.size()
        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeAlgorithm(respostes.size() + 1, respostes);
        });
    }

    @Test
    public void testComputeAlgorithmThrowsExceptionForInvalidRespostaEnquesta() {
        RespostaEnquesta invalidResposta = new RespostaEnquesta();
        ArrayList<RespostaEnquesta> invalidRespostes = new ArrayList<>();
        invalidRespostes.add(invalidResposta);
        invalidRespostes.add(invalidResposta);

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeAlgorithm(1, invalidRespostes);
        });
    }

    @Test
    public void testComputeBestAlgorithmWithValidParameters() {
        double thresholdRatio = 0.9;

        ArrayList<RespostaEnquesta>[] clusters = algoritme.computeBestAlgorithm(respostes, thresholdRatio);

        assertNotNull("Clusters should not be null", clusters);
        assertTrue("Should have at least 1 cluster", clusters.length >= 1);

        for (int i = 0; i < clusters.length; i++) {
            assertNotNull("Cluster " + i + " should not be null", clusters[i]);
            for (int j = 0; j < clusters[i].size(); j++) {
                assertNotNull("Response " + j + " in cluster " + i + " should not be null", clusters[i].get(j));
                assertTrue("Response " + j + " in cluster " + i + " should be from original respostes",
                        respostes.contains(clusters[i].get(j)));
            }
        }

        // Verify that computeClustering was called at least once
        verify(algoritme, atLeastOnce()).computeClustering(anyInt(), any());
    }

    @Test
    public void testComputeBestAlgorithmWithVariousThresholdRatios() {
        for (double thresholdRatio = 0.1; thresholdRatio <= 1.0; thresholdRatio += 0.1) {
            ArrayList<RespostaEnquesta>[] clusters = algoritme.computeBestAlgorithm(respostes, thresholdRatio);

            assertNotNull("Clusters should not be null for thresholdRatio " + thresholdRatio, clusters);
            assertTrue("Should have at least 1 cluster for thresholdRatio " + thresholdRatio, clusters.length >= 1);
        }

        // Verify that computeClustering was called at least once
        verify(algoritme, atLeastOnce()).computeClustering(anyInt(), any());
    }

    @Test
    public void testComputeBestAlgorithmWithOneResposta() {
        double thresholdRatio = 0.9;

        ArrayList<RespostaEnquesta> singleRespostaList = new ArrayList<>();
        singleRespostaList.add(respostes.get(0));

        ArrayList<RespostaEnquesta>[] clusters = algoritme.computeBestAlgorithm(singleRespostaList, thresholdRatio);

        assertNotNull("Clusters should not be null", clusters);
        assertTrue("Should have at least 1 cluster", clusters.length >= 1);

        // Verify that computeClustering was called at least once
        verify(algoritme, atLeastOnce()).computeClustering(anyInt(), any());
    }

    @Test
    public void testComputeBestAlgorithmThrowsExceptionForNullRespostes() {
        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeBestAlgorithm(null, 0.9);
        });
    }

    @Test
    public void testComputeBestAlgorithmThrowsExceptionForEmptyRespostes() {
        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeBestAlgorithm(new ArrayList<>(), 0.9);
        });
    }

    @Test
    public void testComputeBestAlgorithmWithInvalidThresholdRatio() {
        // thresholdRatio <= 0
        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeBestAlgorithm(respostes, 0.0);
        });
        // thresholdRatio >= 1
        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeBestAlgorithm(respostes, 1.0);
        });
    }

    @Test
    public void testComputeAlgorithmWithNulls() {
        Enquesta enquesta = new Enquesta("sampleSurvey");
        RespostesContainer container = new RespostesContainer(enquesta);

        ArrayList<RespostaEnquesta> respostesWithNulls = new ArrayList<>();

        RespostaEnquesta rWithNulls = new RespostaEnquesta();

        rWithNulls.setId(0);
        rWithNulls.associaContainer(container);

        RespostaAPreguntaOberta roNull = new RespostaAPreguntaOberta(0);
        RespostaAPreguntaNumerica rnNull = new RespostaAPreguntaNumerica(1);

        rWithNulls.addRespostaAPregunta(roNull); // null open answer
        rWithNulls.addRespostaAPregunta(rnNull); // null numeric answer
        respostesWithNulls.add(rWithNulls);

        RespostaEnquesta rWithoutNulls = new RespostaEnquesta();

        rWithoutNulls.setId(1);
        rWithoutNulls.associaContainer(container);

        RespostaAPreguntaOberta ro = new RespostaAPreguntaOberta("hola", 0);
        RespostaAPreguntaNumerica rn = new RespostaAPreguntaNumerica(3, 1);

        rWithoutNulls.addRespostaAPregunta(ro); // no-null open answer
        rWithoutNulls.addRespostaAPregunta(rn); // no-null numeric answer

        respostesWithNulls.add(rWithoutNulls);

        int k = 1;

        ArrayList<RespostaEnquesta>[] clusters = algoritme.computeAlgorithm(k, respostesWithNulls);

        assertNotNull("Clusters should not be null", clusters);
        assertEquals("Should have k clusters", k, clusters.length);

        // Verify that computeClustering was called
        verify(algoritme, atLeast(1)).computeClustering(eq(k), any());
    }

    @Test
    public void testComputeAlgorithmWithEmptyAnswers() {
        RespostaEnquesta r1 = new RespostaEnquesta();

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.computeAlgorithm(1, new ArrayList<>(Arrays.asList(r1)));
        });
    }

    @Test
    public void testRestoreNullsWithEmptyCluster() {
        AlgoritmeClusteringTestable alg = spy(new AlgoritmeClusteringTestable());

        Enquesta enquesta = new Enquesta("sampleSurvey");
        RespostesContainer container = new RespostesContainer(enquesta);

        ArrayList<RespostaEnquesta> respostesWithNulls = new ArrayList<>();

        // First response: HAS nulls
        RespostaEnquesta r1 = new RespostaEnquesta();
        r1.setId(0);
        r1.associaContainer(container);
        r1.addRespostaAPregunta(new RespostaAPreguntaOberta(0)); // null
        r1.addRespostaAPregunta(new RespostaAPreguntaNumerica(1)); // null
        respostesWithNulls.add(r1);

        // Second response: NO nulls (needed for centroid calculation)
        RespostaEnquesta r2 = new RespostaEnquesta();
        r2.setId(1);
        r2.associaContainer(container);
        r2.addRespostaAPregunta(new RespostaAPreguntaOberta("Valid answer", 0));
        r2.addRespostaAPregunta(new RespostaAPreguntaNumerica(42, 1));
        respostesWithNulls.add(r2);

        // Third response: NO nulls
        RespostaEnquesta r3 = new RespostaEnquesta();
        r3.setId(2);
        r3.associaContainer(container);
        r3.addRespostaAPregunta(new RespostaAPreguntaOberta("Another answer", 0));
        r3.addRespostaAPregunta(new RespostaAPreguntaNumerica(100, 1));
        respostesWithNulls.add(r3);

        int k = 2;

        ArrayList<RespostaEnquesta>[] cluster = new ArrayList[k];

        when(alg.computeClustering(3, respostesWithNulls)).thenReturn(cluster);

        alg.computeAlgorithm(k, respostesWithNulls);
    }

    @Test
    public void testComputeAlgorithmWithNullsDistributedAcrossClusters() {
        // Create a custom algorithm that distributes responses predictably
        AlgoritmeClusteringTestable customAlg = new AlgoritmeClusteringTestable() {
            @Override
            protected ArrayList<RespostaEnquesta>[] computeClustering(int k, ArrayList<RespostaEnquesta> respostes) {
                // Distribute responses across k clusters in a round-robin fashion
                @SuppressWarnings("unchecked")
                ArrayList<RespostaEnquesta>[] clusters = new ArrayList[k];

                for (int i = 0; i < k; i++) {
                    clusters[i] = new ArrayList<>();
                }

                for (int i = 0; i < respostes.size(); i++) {
                    clusters[i % k].add(respostes.get(i));
                }

                return clusters;
            }
        };

        Enquesta enquesta = new Enquesta("sampleSurvey");
        RespostesContainer container = new RespostesContainer(enquesta);

        ArrayList<RespostaEnquesta> respostesWithNulls = new ArrayList<>();

        // Create 6 responses: 3 with nulls, 3 without
        for (int i = 0; i < 6; i++) {
            RespostaEnquesta r = new RespostaEnquesta();
            r.setId(i);
            r.associaContainer(container);

            if (i < 3) {
                // First 3: add null answers
                r.addRespostaAPregunta(new RespostaAPreguntaOberta(0)); // null
                r.addRespostaAPregunta(new RespostaAPreguntaNumerica(1)); // null
            } else {
                // Last 3: add valid answers
                r.addRespostaAPregunta(new RespostaAPreguntaOberta("Answer " + i, 0));
                r.addRespostaAPregunta(new RespostaAPreguntaNumerica(i + 10, 1));
            }

            respostesWithNulls.add(r);
        }

        int k = 3; // 3 clusters - responses will be distributed across them

        // With round-robin distribution:
        // Cluster 0: responses 0, 3 (one with null, one without)
        // Cluster 1: responses 1, 4 (one with null, one without)
        // Cluster 2: responses 2, 5 (one with null, one without)

        ArrayList<RespostaEnquesta>[] clusters = customAlg.computeAlgorithm(k, respostesWithNulls);

        assertNotNull("Clusters should not be null", clusters);
        assertEquals("Should have k clusters", k, clusters.length);

        // Verify all responses are present
        int totalResponses = 0;
        for (ArrayList<RespostaEnquesta> cluster : clusters) {
            totalResponses += cluster.size();
        }
        assertEquals("All responses should be in clusters", respostesWithNulls.size(), totalResponses);
    }

    @Test
    public void testDistRespostesEnquestaWithSameResponse() {
        RespostaEnquesta r1 = respostes.get(0);

        double dist = algoritme.distRespostesEnquestaPublic(r1, r1);

        assertEquals("Distance to itself should be 0", 0.0, dist, 0.001);
    }

    @Test
    public void testDistRespostesEnquestaWithDifferentResponses() {
        RespostaEnquesta r1 = respostes.get(0);
        RespostaEnquesta r2 = respostes.get(1);

        double dist = algoritme.distRespostesEnquestaPublic(r1, r2);

        assertTrue("Distance should be positive for different responses", dist >= 0);
        assertTrue("Distance should be inferior to 1", dist <= 1.0);
    }

    @Test
    public void testDistRespostesEnquestaThrowsExceptionForDifferentSizes() {
        RespostaEnquesta r1 = respostes.get(0);
        RespostaEnquesta r2 = new RespostaEnquesta();
        r2.addRespostaAPregunta(new RespostaAPreguntaOberta("Test", 0));
        // r2 has only 1 answer, r1 has 2

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.distRespostesEnquestaPublic(r1, r2);
        });
    }

    @Test
    public void testDistRespostesEnquestaThrowsExceptionForEmptyAnswers() {
        RespostaEnquesta r1 = new RespostaEnquesta();
        r1.addRespostaAPregunta(new RespostaAPreguntaOberta("", 0)); // empty answer
        r1.addRespostaAPregunta(new RespostaAPreguntaNumerica(20, 1));

        RespostaEnquesta r2 = respostes.get(0);

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.distRespostesEnquestaPublic(r1, r2);
        });
    }

    @Test
    public void testDistRespostesEnquestaWithNullResponses() {
        RespostaEnquesta r1 = respostes.get(0);

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.distRespostesEnquestaPublic(r1, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.distRespostesEnquestaPublic(null, r1);
        });
    }

    @Test
    public void testDistRespostesEnquestaThrowsExceptionForDifferentsAnswers() {
        RespostaEnquesta r1 = new RespostaEnquesta();
        r1.addRespostaAPregunta(new RespostaAPreguntaOberta("Test", 0));

        RespostaEnquesta r2 = new RespostaEnquesta();
        r2.addRespostaAPregunta(new RespostaAPreguntaNumerica(20, 0));

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.distRespostesEnquestaPublic(r1, r2);
        });
    }

    @Test
    public void testCoefficientWithValidClusters() {

        int k = 3;
        ArrayList<RespostaEnquesta>[] clusters = algoritme.computeAlgorithm(k, respostes);

        double silhouette = algoritme.coefficientCluster(clusters);

        assertTrue("Silhouette coefficient should be between -1 and 1", silhouette >= -1.0 && silhouette <= 1.0);
    }

    @Test
    public void testCoefficientThrowsExceptionForEmptyClusterArray() {
        @SuppressWarnings("unchecked")
        ArrayList<RespostaEnquesta>[] emptyClusters = new ArrayList[0];

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.coefficientCluster(emptyClusters);
        });
    }

    @Test
    public void testCoefficientThrowsExceptionForEmptyCluster() {
        @SuppressWarnings("unchecked")
        ArrayList<RespostaEnquesta>[] clusters = new ArrayList[1];
        clusters[0] = new ArrayList<>(); // Empty cluster

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.coefficientCluster(clusters);
        });
    }

    @Test
    public void testGet2DPlotForEmptyClusters() {
        ArrayList<RespostaEnquesta>[] clusters = null;

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.get2DPlot(null, clusters);
        });
    }

    @Test
    public void testGet2DPlotForEmptyRespostes() {
        ArrayList<RespostaEnquesta> nullRespostes = null;
        ArrayList<RespostaEnquesta>[] clusters = new ArrayList[2];

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.get2DPlot(nullRespostes, clusters);
        });

        ArrayList<RespostaEnquesta> emptyRespostes = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            algoritme.get2DPlot(emptyRespostes, clusters);
        });
    }

    @Test
    public void testget2DPlotGivesSameValueForSameRespostes() {
        K_meansPlusPlus k_meansPlusPlus = new K_meansPlusPlus();

        ArrayList<RespostaEnquesta> r = createSampleRespostesK_means(100);
        ArrayList<RespostaEnquesta>[] clusters = k_meansPlusPlus.computeAlgorithm(3, r);

        double[][] plot1 = algoritme.get2DPlot(r, clusters);
        double[][] plot2 = algoritme.get2DPlot(r, clusters);

        assertArrayEquals("2D plots for same respostes should be identical", plot1, plot2);
    }

    @Test
    public void testget2DPlotWithValidParameters() {
        K_meansPlusPlus k_meansPlusPlus = new K_meansPlusPlus();

        ArrayList<RespostaEnquesta> r = createSampleRespostesK_means(50);
        ArrayList<RespostaEnquesta>[] clusters = k_meansPlusPlus.computeAlgorithm(3, r);

        double[][] plot = algoritme.get2DPlot(r, clusters);

        assertNotNull("2D plot should not be null", plot);
        assertEquals("Plot should have same number of rows as respostes", r.size(), plot.length);
        assertEquals("Each row should have 3 columns", 3, plot[0].length);

        for (int i = 0; i < plot.length; i++) {
            for (int j = 0; j < 2; j++) {
                assertTrue("Plot coordinates should be normalized", plot[i][j] <= 1 && plot[i][j] >= 0);
            }
        }

        for (int i = 0; i < plot.length; i++) {
            for (int j = i + 1; j < plot.length; j++) {
                double dx = plot[i][0] - plot[j][0];
                double dy = plot[i][1] - plot[j][1];
                double dist = Math.sqrt(dx * dx + dy * dy);
                double delta = algoritme.distRespostesEnquestaPublic(r.get(i), r.get(j));

                if (dist == 0) {
                    // Only allow overlap if target distance is zero
                    assertEquals("Points overlap only if target distance is zero", 0.0, delta, 0.001);
                }
            }
        }
    }

    @Test
    public void testClusteringAndPlot() {

        K_meansPlusPlus k_meansPlusPlus = new K_meansPlusPlus();

        ArrayList<RespostaEnquesta> r = createSampleRespostesK_means(100);

        ArrayList<RespostaEnquesta>[] clusters = k_meansPlusPlus.computeAlgorithm(3, r);
        k_meansPlusPlus.get2DPlot(r, clusters);
    }

    @Test
    public void testClusteringAndPlotWithIdenticalValues() {

        K_meansPlusPlus k_meansPlusPlus = new K_meansPlusPlus();

        ArrayList<RespostaEnquesta> r = new ArrayList<>();
        Enquesta enquesta = new Enquesta("sampleSurvey");
        RespostesContainer container = new RespostesContainer(enquesta);

        for(int i = 0; i < 50; ++i){
            RespostaEnquesta re = new RespostaEnquesta();
            re.setId(i);
            re.associaContainer(container);

            re.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(2, 0));

            r.add(re);
        }

        ArrayList<RespostaEnquesta>[] clusters = k_meansPlusPlus.computeAlgorithm(3, r);
        k_meansPlusPlus.get2DPlot(r, clusters);
    }

    @Test
    public void testClusteringAndPlotWithIncorrectCluster() {

        K_meansPlusPlus k_meansPlusPlus = new K_meansPlusPlus();

        ArrayList<RespostaEnquesta> r = createSampleRespostesK_means(100);
        ArrayList<RespostaEnquesta>[] clusters = new ArrayList[1];
        clusters[0] = new ArrayList<>();
        clusters[0].add(r.get(0));

        assertThrows(RuntimeException.class, () -> k_meansPlusPlus.get2DPlot(r, clusters));
    }

    private ArrayList<RespostaEnquesta> createSampleRespostesK_means(int count){
        ArrayList<RespostaEnquesta> list = new ArrayList<>();

        Enquesta enquesta = new Enquesta("sampleSurvey");
        RespostesContainer container = new RespostesContainer(enquesta);

        String[] answers1 = {"roig", "blau", "bleu"};
        String[] answers2 = {"abc", "abd", "xyz"};

        for(int i = 0; i < count; ++i){
            RespostaEnquesta re = spy(new RespostaEnquesta());
            re.setId(i);
            re.associaContainer(container);

            Random rand = new Random();

            int a1 = rand.nextInt(answers1.length);
            int a2 = rand.nextInt(answers2.length);
            int option = rand.nextInt(4);

            int totalOpcions = 4;
            int numOpcions = rand.nextInt(totalOpcions)+1;
            ArrayList<Integer> options = new ArrayList<>();
            for(int j = 0; j < numOpcions; j++) options.add(rand.nextInt(totalOpcions));

            re.addRespostaAPregunta(new RespostaAPreguntaOberta(answers1[a1], 0));
            re.addRespostaAPregunta(new RespostaAPreguntaOberta(answers2[a2], 1));
            re.addRespostaAPregunta(new RespostaAPreguntaNoOrdenada(option, 2));
            re.addRespostaAPregunta(new RespostaAPreguntaMultiple(options, 3));

            list.add(re);
        }
        return list;
    }
}