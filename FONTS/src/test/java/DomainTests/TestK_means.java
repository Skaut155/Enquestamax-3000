package DomainTests;

import Domain.Model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test for the K_means algorithm
 */
@RunWith(Parameterized.class)
public class TestK_means {
    int test;
    int k;
    K_meansTestable1 K_meansRandomDist;
    K_meansTestable2 K_means;
    ArrayList<RespostaEnquesta> respostes;

    public TestK_means(int test, int k, ArrayList<RespostaEnquesta> respostes) {
        this.test = test;
        this.k = k;
        this.respostes = respostes;
        K_meansRandomDist = new K_meansTestable1();
        K_means = new K_meansTestable2();
    }

    /**
     * Concrete testable implementation of K_means
     * to test the abstract class methods
     */
    private static class K_meansTestable1 extends K_means {
        
        @Override
        public ArrayList<RespostaEnquesta> initialize(int k, ArrayList<RespostaEnquesta> respostes) {
            // Simple initialization: take first k responses as centroids
            ArrayList<RespostaEnquesta> centroids = new ArrayList<>();
            for (int i = 0; i < k && i < respostes.size(); i++) {
                centroids.add(respostes.get(i));
            }
            return centroids;
        }

        @Override
        protected double distRespostesEnquesta(RespostaEnquesta r1, RespostaEnquesta r2){
            Random rand = new Random();
            return rand.nextDouble(0.0, 1.0);
        }
        
        // Make protected method accessible for testing
        public ArrayList<RespostaEnquesta>[] computeClusteringPublic(int k, ArrayList<RespostaEnquesta> respostes) {
            return computeClustering(k, respostes);
        }
    }

    /**
     * Concrete testable implementation of K_means
     * to test the abstract class methods
     */
    private static class K_meansTestable2 extends K_means {
        
        @Override
        public ArrayList<RespostaEnquesta> initialize(int k, ArrayList<RespostaEnquesta> respostes) {
            // Simple initialization: take first k responses as centroids
            ArrayList<RespostaEnquesta> centroids = new ArrayList<>();
            for (int i = 0; i < k && i < respostes.size(); i++) {
                centroids.add(respostes.get(i));
            }
            return centroids;
        }
        
        // Make protected method accessible for testing
        public ArrayList<RespostaEnquesta>[] computeClusteringPublic(int k, ArrayList<RespostaEnquesta> respostes) {
            return computeClustering(k, respostes);
        }
    }

    @Parameters
     public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][] {
            { 0, 3, getRespostesEnquesta(20, 3, new String[]{"text", "text", "text"})},
            { 1, 3, getRespostesEnquesta(20, 3, new String[]{"unOrdered", "text", "unOrdered"})},
            { 2, 3, getRespostesEnquesta(20, 3, new String[]{"unOrdered", "text", "multiple"})},
            { 3, 3, getRespostesEnquesta(20, 1, new String[]{"text"})},
            { 4, 5, getRespostesEnquesta(30, 5, new String[]{"numerica", "numerica", "numerica", "numerica", "numerica"})},
            { 5, 4, getRespostesEnquesta(20, 4, new String[]{"multiple", "multiple", "inOrder", "unOrdered"})},
            { 6, 3, getRespostesEnquesta(20, 3, new String[]{"text", "unOrdered", "inOrder"})},
            { 7, 3, getRespostesEnquesta(20, 3, new String[]{"multiple", "unOrdered", "inOrder"})}            
        });
    }

    private static ArrayList<RespostaEnquesta> getRespostesEnquesta(int numRespostes, int numPreguntes, String[] tipus){
        Enquesta enquesta = new Enquesta("sampleSurvey");
        RespostesContainer container = new RespostesContainer(enquesta);

        ArrayList<RespostaEnquesta> respostes = new ArrayList<>();
        for(int i = 0; i < numRespostes; ++i){
            RespostaEnquesta r = new RespostaEnquesta();
            r.setId(i);
            r.associaContainer(container);
            respostes.add(r);
        }

        for(int j = 0; j < numPreguntes; ++j){
            ArrayList<RespostaAPregunta<?>> respostesAPreguntes = getRespostesAPregunta(numRespostes, tipus[j], j);
            for(int i = 0; i < numRespostes; ++i){
                RespostaEnquesta r = respostes.get(i);
                r.addRespostaAPregunta(respostesAPreguntes.get(i));
                respostes.set(i, r);
            }
        }
        return respostes;
        
    }

    private static ArrayList<RespostaAPregunta<?>> getRespostesAPregunta(int numRespostes, String tipus, int ordrePregunta){
        Random rand = new Random();

        ArrayList<RespostaAPregunta<?>> respostes = new ArrayList<>();
        for(int i = 0; i < numRespostes; ++i){
            switch (tipus) {
                case "text" -> {
                    String text = "Sample text " + rand.nextInt(1000);
                    RespostaAPreguntaOberta r = new RespostaAPreguntaOberta(text, ordrePregunta);
                    respostes.add(r);
                }
                case "numerica" -> {
                    int value = rand.nextInt(100);
                    RespostaAPreguntaNumerica r = new RespostaAPreguntaNumerica(value, ordrePregunta);
                    respostes.add(r);
                }
                case "multiple" -> {
                    int numOptions = 5;
                    Set<Integer> selectedOptions = new HashSet<>();
                    int numSelected = rand.nextInt(numOptions) + 1; // At least one option

                    while (selectedOptions.size() < numSelected) {
                        selectedOptions.add(rand.nextInt(numOptions));
                    }

                    RespostaAPreguntaMultiple r = new RespostaAPreguntaMultiple(new ArrayList<>(selectedOptions), ordrePregunta);
                    respostes.add(r);
                }
                case "inOrder" -> {
                    int selectedOption = rand.nextInt(5);
                    RespostaAPreguntaOrdenada r = new RespostaAPreguntaOrdenada(selectedOption, ordrePregunta);
                    respostes.add(r);
                }
                case "unOrdered" -> {
                    int selectedOption = rand.nextInt(5);
                    RespostaAPreguntaNoOrdenada r = new RespostaAPreguntaNoOrdenada(selectedOption, ordrePregunta);
                    respostes.add(r);
                }
            }
        }

        return respostes;
    }

    /**
     * This test checks basic assertions in order to ensure that the algorithm works properly
     */
    @Test
    public void testComputeClustering_BasicAssertions() {
        K_meansRandomDist = new K_meansTestable1();

        ArrayList<RespostaEnquesta>[] result = K_meansRandomDist.computeClusteringPublic(k, respostes);

        // 1. Assert the result is not null
        assertNotNull("Result should not be null", result);

        // 2. Assert correct number of clusters
        assertEquals("Should have " + k + " clusters", k, result.length);

        // 3. Assert no cluster is null
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Cluster " + i + " should not be null", result[i]);
        }

        // 4. Assert all answers are distributed (no answer is lost)
        int totalAnswersInClusters = 0;
        for (ArrayList<RespostaEnquesta> cluster : result) {
            totalAnswersInClusters += cluster.size();
        }
        assertEquals("All answers should be distributed across clusters",
                respostes.size(), totalAnswersInClusters);

        // 5. Assert each answer appears exactly once (no duplicates)
        Set<RespostaEnquesta> uniqueAnswers = new HashSet<>();
        for (ArrayList<RespostaEnquesta> cluster : result) {
            for (RespostaEnquesta resposta : cluster) {
                assertTrue("Answer should not appear in multiple clusters",
                        uniqueAnswers.add(resposta));
            }
        }
        assertEquals("All original answers should be present",
                respostes.size(), uniqueAnswers.size());

        // 6. Assert at least one answer per cluster
        int clustersWithResposta = 0;
        for(ArrayList<RespostaEnquesta> r : result) if(!r.isEmpty()) ++clustersWithResposta;
        assertTrue(clustersWithResposta == k);
    }

    @Test
    public void testConvergenceComputeAlgorithm(){
        if(test > 4) return;

        K_means = new K_meansTestable2();

        ArrayList<RespostaEnquesta>[] result = K_means.computeClusteringPublic(k, respostes);

        // 1. Assert the result is not null
        assertNotNull("Result should not be null", result);

        // 2. Assert correct number of clusters
        assertEquals("Should have " + k + " clusters", k, result.length);

        // 3. Assert no cluster is null
        for (int i = 0; i < result.length; i++) {
            assertNotNull("Cluster " + i + " should not be null", result[i]);
        }

        // 4. Assert all answers are distributed (no answer is lost)
        int totalAnswersInClusters = 0;
        for (ArrayList<RespostaEnquesta> cluster : result) {
            totalAnswersInClusters += cluster.size();
        }
        assertEquals("All answers should be distributed across clusters",
                respostes.size(), totalAnswersInClusters);

        // 5. Assert each answer appears exactly once (no duplicates)
        Set<RespostaEnquesta> uniqueAnswers = new HashSet<>();
        for (ArrayList<RespostaEnquesta> cluster : result) {
            for (RespostaEnquesta resposta : cluster) {
                assertTrue("Answer should not appear in multiple clusters",
                        uniqueAnswers.add(resposta));
            }
        }
        assertEquals("All original answers should be present",
                respostes.size(), uniqueAnswers.size());

        // 6. Assert at least one answer per cluster
        int clustersWithResposta = 0;
        for(ArrayList<RespostaEnquesta> r : result) if(!r.isEmpty()) ++clustersWithResposta;
        assertTrue(clustersWithResposta == k);
    }

    @Test
    public void testGetCentroids(){
        if(test > 4) return;

        K_means k_meansReal = new K_meansRandom();

        k_meansReal.computeAlgorithm(k, respostes);
        ArrayList<RespostaEnquesta> centroids = k_meansReal.getCentroids();

        // 1. Assert centroids is not null
        assertNotNull("Centroids should not be null", centroids);

        // 2. Assert correct number of centroids
        assertEquals("Should have " + k + " centroids", k, centroids.size());
    }

}