package com.example.kriti.aninterface;

import android.util.Log;

import com.example.kriti.aninterface.Utilities.Node;

import java.util.ArrayList;

/**
 * Created by harshit on 17/4/17.
 */

public class Route {

    final int INT_MAX;
    int V;
    int path[];
    Node node0;
    Node node1;
    Node node2;
    Node node3;
    Node node4;
    Node node5;
    Node node6;
    Node node7;
    Node node8;
    Node node9;
    Node node10;
    Node node11;
    Node node12;
    Node node13;
    Node node14;
    Node node15;
    Node node16;
    Node node17;
    Node node18;
    Node node19;
    ArrayList<String> arrayList;


    public Route() {
        V = 20;
        INT_MAX = Integer.MAX_VALUE;

        node0 = new Node(100, 600);
        node1 = new Node(350, 600);
        node2 = new Node(550, 600);
        node3 = new Node(850, 600);
        node4 = new Node(100, 900);
        node5 = new Node(350, 900);
        node6 = new Node(550, 900);
        node7 = new Node(850, 900);
        node8 = new Node(100, 1200);
        node9 = new Node(350, 1200);
        node10 = new Node(550, 1200);
        node11 = new Node(850, 1200);
        node12 = new Node(100, 1500);
        node13 = new Node(350, 1500);
        node14 = new Node(550, 1500);
        node15 = new Node(850, 1500);
        node16 = new Node(100, 1800);
        node17 = new Node(350, 1800);
        node18 = new Node(550, 1800);
        node19 = new Node(850, 1800);
        arrayList = new ArrayList<String>();
    }

    int min_Distance(int dist[], boolean sptSet[]) {

        int min = INT_MAX, min_index = 0;

        for (int v = 0; v < V; v++) {
            if (!sptSet[v] && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }

        }
        return min_index;
    }

    void printPath(int parent[], int j) {
        if (parent[j] == -1) return;
        printPath(parent, parent[j]);
        Log.w("path", Integer.toString(j));
        arrayList.add(Integer.toString(j));
        for (int i = 0; i < arrayList.size(); i++) Log.w("ArrayList test", arrayList.get(i));
    }


    void printSolution(int dist[], int n, int parent[], int src, int destination) {
        Log.w("Vertex", "distance");
        printPath(parent, destination);
//        for (int i =0 ; i < V; i++) {
//            Log.w(Integer.toString(i), Integer.toString(dist[i]));
//            printPath(parent, i);
//        }
    }

    void routing(int graph[][], int src, int destination) {

        int dist[] = new int[V];
        boolean sptSet[] = new boolean[V];
        int parent[] = new int[V];
        for (int i = 0; i < V; i++) {
            parent[src] = -1;
            dist[i] = INT_MAX;
            sptSet[i] = false;
        }
        dist[src] = 0;

        for (int count = 0; count < V - 1; count++) {
            int u = min_Distance(dist, sptSet);

            sptSet[u] = true;

            for (int v = 0; v < V; v++)

                if (!sptSet[v] && graph[u][v] != 0 && (dist[u] + graph[u][v] < dist[v])) {
                    parent[v] = u;
                    dist[v] = dist[u] + graph[u][v];
                }
        }
        printSolution(dist, V, parent, src, destination);
    }

    void drawRouteOnScreen(int source) {


        arrayList.clear();
    }
}