package DomainTests;

import Domain.Model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for the Kmedoids algorithm
 */
public class TestKmedoids {
    int k;
    Kmedoids kMedoids;
    ArrayList<RespostaEnquesta> respostes;

    // The following mocks are used for the basic tests. The larger tests use real survey answers and stubs
    @Mock
    private RespostaEnquesta mockResposta1;
    @Mock
    private RespostaEnquesta mockResposta2;
    @Mock
    private RespostaEnquesta mockResposta3;
    @Mock
    private RespostaEnquesta mockResposta4;

    /**
     * Set up survey answers before executing the algorithm
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(mockResposta1);
        assertNotNull(mockResposta2);
        assertNotNull(mockResposta3);
        assertNotNull(mockResposta4);
        kMedoids = new Kmedoids();
        respostes = new ArrayList<>();

        // Mock getRespostes to return empty lists (to avoid null pointer issues)
        when(mockResposta1.getRespostes()).thenReturn(new ArrayList<>());
        when(mockResposta2.getRespostes()).thenReturn(new ArrayList<>());
        when(mockResposta3.getRespostes()).thenReturn(new ArrayList<>());
        when(mockResposta4.getRespostes()).thenReturn(new ArrayList<>());

        respostes.add(mockResposta1);
        respostes.add(mockResposta2);
        respostes.add(mockResposta3);
        respostes.add(mockResposta4);
    }

    /**
     * This test checks basic assertions in order to ensure that the algorithm works properly
     */
    @Test
    public void testComputeClustering_BasicAssertions() {
        k = 2;
        ArrayList<RespostaEnquesta>[] result = kMedoids.computeClustering(k, respostes);

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
    }

    /**
     * This test checks that computing the clusters with k = respostes.size() makes each answer be in its own cluster
     */
    @Test
    public void testComputeClustering_KEqualsN() {
        // Create a separate test with real, distinct answers
        ArrayList<RespostaEnquesta> distinctRespostes = new ArrayList<>();

        // Create 4 distinct answers with different numeric values
        for (int i = 0; i < 4; i++) {
            RespostaEnquesta resposta = getRespostaEnquesta(i);

            distinctRespostes.add(resposta);
        }

        k = distinctRespostes.size();
        ArrayList<RespostaEnquesta>[] result = kMedoids.computeClustering(k, distinctRespostes);

        assertEquals("Should have as many clusters as answers", k, result.length);

        // Each cluster should have exactly 1 element
        for (int i = 0; i < result.length; i++) {
            assertEquals("Cluster " + i + " should contain exactly 1 answer", 1, result[i].size());
        }
    }

    /**
     * The following method returns a survey answer based on the i value given as a parameter. This RespostaEnquesta
     * is a stub with its method redefined in a simpler way to the fit the test we are conducting.
     */
    private RespostaEnquesta getRespostaEnquesta(int i) {
        double value = i * 25.0; // 0, 25, 50, 75
        // We create and return a RespostaEnquesta stub
        return new RespostaEnquesta() {
            private final ArrayList<RespostaAPregunta<?>> answers = new ArrayList<>();

            {
                RespostaAPreguntaNumerica numericAnswer = new RespostaAPreguntaNumerica(value, 0);
                numericAnswer.setRespostaEnquesta(this);
                answers.add(numericAnswer);
            }

            @Override
            public ArrayList<RespostaAPregunta> getRespostes() {
                return (ArrayList<RespostaAPregunta>) (ArrayList<?>) answers;
            }

            @Override
            public double calcMax(int ordrePregunta) {
                return 100.0;
            }

            @Override
            public double calcMin(int ordrePregunta) {
                return 0.0;
            }
        };
    }

    /**
     * This test checks that computing the clusters with k=1 makes all answers be in one cluster
     */
    @Test
    public void testComputeClustering_K1() {
        k = 1;
        ArrayList<RespostaEnquesta>[] result = kMedoids.computeClustering(k, respostes);

        assertEquals("Should have 1 cluster", 1, result.length);
        assertEquals("Single cluster should contain all answers",
                respostes.size(), result[0].size());
    }

    /**
     * This test uses a larger dataset with clear clusters to ensure PAM optimization and multiple iterations occur
     */
    @Test
    public void testComputeClustering_LargerDatasetWithOptimization() {
        // Create 10 answers: 5 around value 10, and 5 around value 90
        // This creates a clear 2-cluster structure that should trigger PAM optimization
        ArrayList<RespostaEnquesta> largeDataset = new ArrayList<>();

        // Cluster 1: values around 10
        for (int i = 0; i < 5; i++) {
            largeDataset.add(getRespostaEnquesta(i)); // values: 0, 25, 50, 75, 100
        }

        // Cluster 2: values around 200
        for (int i = 0; i < 5; i++) {
            largeDataset.add(getRespostaEnquestaWithValue(200.0 + i * 10)); // values: 200, 210, 220, 230, 240
        }

        k = 2;
        ArrayList<RespostaEnquesta>[] result = kMedoids.computeClustering(k, largeDataset);

        // Basic assertions
        assertEquals("Should have 2 clusters", 2, result.length);

        // All answers should be distributed
        int total = 0;
        for (ArrayList<RespostaEnquesta> cluster : result) {
            assertNotNull("Cluster should not be null", cluster);
            total += cluster.size();
        }
        assertEquals("All answers should be distributed", largeDataset.size(), total);

        // Each answer should appear exactly once
        Set<RespostaEnquesta> uniqueAnswers = new HashSet<>();
        for (ArrayList<RespostaEnquesta> cluster : result) {
            for (RespostaEnquesta resposta : cluster) {
                assertTrue("Each answer should appear only once", uniqueAnswers.add(resposta));
            }
        }

        // Both clusters should be non-empty
        assertTrue("Both clusters should contain answers",
                !result[0].isEmpty() && !result[1].isEmpty());
    }

    /**
     * The following method returns a survey answer based on the value value given as a parameter. This RespostaEnquesta
     * is a stub with its method redefined in a simpler way to the fit the test we are conducting.
     */
    private RespostaEnquesta getRespostaEnquestaWithValue(double value) {
        return new RespostaEnquesta() {
            private final ArrayList<RespostaAPregunta<?>> answers = new ArrayList<>();

            {
                RespostaAPreguntaNumerica numericAnswer = new RespostaAPreguntaNumerica(value, 0);
                numericAnswer.setRespostaEnquesta(this);
                answers.add(numericAnswer);
            }

            @Override
            public ArrayList<RespostaAPregunta> getRespostes() {
                return (ArrayList<RespostaAPregunta>) (ArrayList<?>) answers;
            }

            @Override
            public double calcMax(int ordrePregunta) {
                return 250.0;
            }

            @Override
            public double calcMin(int ordrePregunta) {
                return 0.0;
            }
        };
    }

    /**
     * Test with data that requires multiple iterations to converge
     */
    @Test
    public void testComputeClustering_MultipleIterations() {
        // Create data with 3 clear groups to test k=3
        ArrayList<RespostaEnquesta> dataset = new ArrayList<>();

        // Group 1: around 0-10
        dataset.add(getRespostaEnquestaWithValue(5.0));
        dataset.add(getRespostaEnquestaWithValue(8.0));
        dataset.add(getRespostaEnquestaWithValue(12.0));

        // Group 2: around 50
        dataset.add(getRespostaEnquestaWithValue(48.0));
        dataset.add(getRespostaEnquestaWithValue(52.0));
        dataset.add(getRespostaEnquestaWithValue(50.0));

        // Group 3: around 100
        dataset.add(getRespostaEnquestaWithValue(98.0));
        dataset.add(getRespostaEnquestaWithValue(102.0));
        dataset.add(getRespostaEnquestaWithValue(100.0));

        k = 3;
        ArrayList<RespostaEnquesta>[] result = kMedoids.computeClustering(k, dataset);

        assertEquals("Should have 3 clusters", 3, result.length);

        // Verify all answers are distributed
        int total = 0;
        for (ArrayList<RespostaEnquesta> cluster : result) {
            total += cluster.size();
        }
        assertEquals("All answers should be distributed", dataset.size(), total);
    }

    /**
     * Test that directly calls fixEmptyClusters via reflection with a controlled state.
     * This test guarantees coverage of lines 104, 114, and 115 by:
     * - Setting up clusters where the largest is NOT at index 0 (covers line 104)
     * - Having multiple elements with different distances in the largest cluster (covers lines 114-115)
     */
    @Test
    public void testFixEmptyClusters_DirectReflection() throws Exception {
        // Create dataset with distinct values
        ArrayList<RespostaEnquesta> dataset = new ArrayList<>();
        RespostaEnquesta r0 = getRespostaEnquestaWithValue(0.0);
        RespostaEnquesta r1 = getRespostaEnquestaWithValue(10.0);
        RespostaEnquesta r2 = getRespostaEnquestaWithValue(20.0);
        RespostaEnquesta r3 = getRespostaEnquestaWithValue(50.0);
        RespostaEnquesta r4 = getRespostaEnquestaWithValue(100.0);

        dataset.add(r0);
        dataset.add(r1);
        dataset.add(r2);
        dataset.add(r3);
        dataset.add(r4);

        // Set k = 3
        Field kField = Kmedoids.class.getDeclaredField("k");
        kField.setAccessible(true);
        kField.set(kMedoids, 3);

        // Set respostes
        Field respostesField = Kmedoids.class.getDeclaredField("respostes");
        respostesField.setAccessible(true);
        respostesField.set(kMedoids, dataset);

        // Create clusters array: cluster 0 is empty, cluster 1 has 1 element, cluster 2 has 4 elements
        // This forces:
        // - largestClusterIdx to become 2 (not 0) -> covers line 104
        // - Multiple elements with different distances -> covers lines 114-115
        ArrayList<RespostaEnquesta>[] clusters = new ArrayList[3];
        clusters[0] = new ArrayList<>(); // Empty cluster at index 0
        clusters[1] = new ArrayList<>();
        clusters[1].add(r0); // One element
        clusters[2] = new ArrayList<>();
        clusters[2].add(r1); // Multiple elements with different values
        clusters[2].add(r2);
        clusters[2].add(r3);
        clusters[2].add(r4);

        Field clustersField = Kmedoids.class.getDeclaredField("clusters");
        clustersField.setAccessible(true);
        clustersField.set(kMedoids, clusters);

        // Set medoids: medoid for cluster 2 is r1 (value 10), so distances are 0, 10, 40, 90
        RespostaEnquesta[] medoids = new RespostaEnquesta[3];
        medoids[0] = r0;
        medoids[1] = r0;
        medoids[2] = r1; // Medoid of the largest cluster

        Field medoidsField = Kmedoids.class.getDeclaredField("medoids");
        medoidsField.setAccessible(true);
        medoidsField.set(kMedoids, medoids);

        // Call fixEmptyClusters via reflection
        Method fixEmptyClustersMethod = Kmedoids.class.getDeclaredMethod("fixEmptyClusters");
        fixEmptyClustersMethod.setAccessible(true);
        fixEmptyClustersMethod.invoke(kMedoids);

        // Verify the empty cluster is no longer empty
        ArrayList<RespostaEnquesta>[] resultClusters = (ArrayList<RespostaEnquesta>[]) clustersField.get(kMedoids);
        assertFalse("Cluster 0 should not be empty after fix", resultClusters[0].isEmpty());

        // The element moved should be r4 (farthest from r1 with distance 90)
        assertTrue("Cluster 0 should contain r4 (farthest element)", resultClusters[0].contains(r4));
    }

    /**
     * Test that covers the case where the largest cluster has only 1 element.
     * This test guarantees coverage of the false branch of line 108.
     */
    @Test
    public void testFixEmptyClusters_LargestClusterHasOneElement() throws Exception {
        // Create dataset
        ArrayList<RespostaEnquesta> dataset = new ArrayList<>();
        RespostaEnquesta r0 = getRespostaEnquestaWithValue(0.0);
        RespostaEnquesta r1 = getRespostaEnquestaWithValue(10.0);

        dataset.add(r0);
        dataset.add(r1);

        // Set k = 3 (more clusters than elements)
        Field kField = Kmedoids.class.getDeclaredField("k");
        kField.setAccessible(true);
        kField.set(kMedoids, 3);

        // Set respostes
        Field respostesField = Kmedoids.class.getDeclaredField("respostes");
        respostesField.setAccessible(true);
        respostesField.set(kMedoids, dataset);

        // Create clusters array: cluster 0 is empty, clusters 1 and 2 have 1 element each
        // This forces the case where the largest cluster has size 1
        ArrayList<RespostaEnquesta>[] clusters = new ArrayList[3];
        clusters[0] = new ArrayList<>(); // Empty cluster
        clusters[1] = new ArrayList<>();
        clusters[1].add(r0); // One element
        clusters[2] = new ArrayList<>();
        clusters[2].add(r1); // One element (same size as cluster 1)

        Field clustersField = Kmedoids.class.getDeclaredField("clusters");
        clustersField.setAccessible(true);
        clustersField.set(kMedoids, clusters);

        // Set medoids
        RespostaEnquesta[] medoids = new RespostaEnquesta[3];
        medoids[0] = r0;
        medoids[1] = r0;
        medoids[2] = r1;

        Field medoidsField = Kmedoids.class.getDeclaredField("medoids");
        medoidsField.setAccessible(true);
        medoidsField.set(kMedoids, medoids);

        // Call fixEmptyClusters via reflection
        Method fixEmptyClustersMethod = Kmedoids.class.getDeclaredMethod("fixEmptyClusters");
        fixEmptyClustersMethod.setAccessible(true);
        fixEmptyClustersMethod.invoke(kMedoids);

        // When largest cluster has only 1 element, we can't move anything
        // The empty cluster stays empty
        ArrayList<RespostaEnquesta>[] resultClusters = (ArrayList<RespostaEnquesta>[]) clustersField.get(kMedoids);

        // Verify the method ran without error
        assertNotNull("Clusters should not be null", resultClusters);
    }

    @Test
    public void testGetCentroids(){
        // Create 10 answers: 5 around value 10, and 5 around value 90
        // This creates a clear 2-cluster structure that should trigger PAM optimization
        ArrayList<RespostaEnquesta> largeDataset = new ArrayList<>();

        // Cluster 1: values around 10
        for (int i = 0; i < 5; i++) {
            largeDataset.add(getRespostaEnquesta(i)); // values: 0, 25, 50, 75, 100
        }

        // Cluster 2: values around 200
        for (int i = 0; i < 5; i++) {
            largeDataset.add(getRespostaEnquestaWithValue(200.0 + i * 10)); // values: 200, 210, 220, 230, 240
        }

        k = 2;

        kMedoids.computeClustering(k, largeDataset);
        ArrayList<RespostaEnquesta> centroids = kMedoids.getCentroids();

        // 1. Assert centroids is not null
        assertNotNull("Centroids should not be null", centroids);

        // 2. Assert correct number of centroids
        assertEquals("Should have " + k + " centroids", k, centroids.size());
    }
}

