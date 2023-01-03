package utils;

import java.io.*;
import java.lang.*;
import java.util.*;

public class FloydWarshall {
    public static void doFloydWarshall(int dist[][], int V, int INF) {
        int i, j, k;

        /*
         * Add all vertices one by one
         * to the set of intermediate
         * vertices.
         * ---> Before start of an iteration,
         * we have shortest
         * distances between all pairs
         * of vertices such that
         * the shortest distances consider
         * only the vertices in
         * set {0, 1, 2, .. k-1} as
         * intermediate vertices.
         * ----> After the end of an iteration,
         * vertex no. k is added
         * to the set of intermediate
         * vertices and the set
         * becomes {0, 1, 2, .. k}
         */
        for (k = 0; k < V; k++) {
            // Pick all vertices as source one by one
            for (i = 0; i < V; i++) {
                // Pick all vertices as destination for the
                // above picked source
                for (j = 0; j < V; j++) {
                    // If vertex k is on the shortest path
                    // from i to j, then update the value of
                    // dist[i][j]
                    if (dist[i][k] + dist[k][j] < dist[i][j])
                        dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }

        // Print the shortest distance matrix
        printSolution(dist, V, INF);
    }

    public static void printSolution(int dist[][], int V, int INF) {
        System.out.println(
                "The following matrix shows the shortest "
                        + "distances between every pair of vertices");
        for (int i = 0; i < V; ++i) {
            for (int j = 0; j < V; ++j) {
                if (dist[i][j] == INF)
                    System.out.print("INF ");
                else
                    System.out.print(dist[i][j] + "   ");
            }
            System.out.println();
        }
    }
}