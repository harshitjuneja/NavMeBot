package com.example.kriti.aninterface;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.kriti.aninterface.Utilities.Node;

import java.util.ArrayList;

/**
 * Created by harshit on 17/4/17.
 */

public class Route {

    final int INT_MAX;
    int V;
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
    MyMap myMap;
    Context context;
    Canvas canvas;



    public Route(MyMap map) {
        V = 20;
        INT_MAX = Integer.MAX_VALUE;

        this.myMap = map;
        this.node0 = new Node(110, 138);
        this.node1 = new Node(355, 138);
        this.node2 = new Node(610, 138);
        this.node3 = new Node(986, 138);
        this.node4 = new Node(110, 454);
        this.node5 = new Node(355, 454);
        this.node6 = new Node(610, 454);
        this.node7 = new Node(986, 454);
        this.node8 = new Node(110, 757);
        this.node9 = new Node(355, 757);
        this.node10 = new Node(610, 757);
        this.node11 = new Node(986, 757);
        this.node12 = new Node(110, 1070);
        this.node13 = new Node(355, 1070);
        this.node14 = new Node(610, 1070);
        this.node15 = new Node(986, 1070);
        this.node16 = new Node(110, 1331);
        this.node17 = new Node(355, 1331);
        this.node18 = new Node(610, 1331);
        this.node19 = new Node(986, 1331);
        this.arrayList = new ArrayList<String>();
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

    Node getRealNodeObject(String number) {

        switch (number) {
            case "0":
                return node0;
            case "1":
                return node1;
            case "2":
                return node2;
            case "3":
                return node3;
            case "4":
                return node4;
            case "5":
                return node5;
            case "6":
                return node6;
            case "7":
                return node7;
            case "8":
                return node8;
            case "9":
                return node9;
            case "10":
                return node10;
            case "11":
                return node11;
            case "12":
                return node12;
            case "13":
                return node13;
            case "14":
                return node14;
            case "15":
                return node15;
            case "16":
                return node16;
            case "17":
                return node17;
            case "18":
                return node18;
            case "19":
                return node19;
            default:
                return null;
        }
    }

    void drawRouteOnScreen(int source) {
        arrayList.add(0, Integer.toString(source));
        ArrayList<Node> mapData = new ArrayList<>();
        for(int i = 0 ; i < arrayList.size(); ++i){
            Node n = getRealNodeObject(arrayList.get(i).toString());
            mapData.add(n);
        }

        myMap.setMapData(mapData);
        myMap.invalidate();
        for (int i=0;i<arrayList.size();i++){
            Log.w("AL elements",arrayList.get(i).toString());
        }
//        for (int i = 0, j = i+1; i < (arrayList.size() - 1) && j < arrayList.size(); i++, j++) {
//            Log.w("nodei",arrayList.get(i).toString());
//            Log.w("nodej",arrayList.get(j).toString());
//            Node n1 = getRealNodeObject(arrayList.get(i).toString());
//            Node n2 = getRealNodeObject(arrayList.get(j).toString());
//            myMap.setN1(n1);
//            myMap.setN2(n2);
//            myMap.invalidate();
//        }
        arrayList.clear();
    }
}