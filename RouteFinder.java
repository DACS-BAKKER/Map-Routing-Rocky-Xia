import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

import java.io.IOException;

//Author: Rocky Xia
public class RouteFinder {

    public static void main(String[] args) throws IOException {
        promptUser();
    }

    //Console UI for input
    public static void promptUser() throws IOException {
        System.out.println("Map Routing");
        System.out.println("Choose starting index: ");
        int start = StdIn.readInt();
        System.out.println("Choose end index: ");
        int end = StdIn.readInt();
        findPath(start, end);
    }

    //Method for finding the shortest path
    public static void findPath(int startIndex, int endIndex) throws IOException {

        //Construct graph and priority queue
        USAGraph graph = new USAGraph("usa.txt");
        PriorityQueue finder = new PriorityQueue();

        //Set up tracker that checks visited vertices
        int[] tracker = new int[graph.vertices.length];
        for(int i = 0; i < tracker.length; i++){
            tracker[i] = Integer.MAX_VALUE;
        }

        //Enqueue the first vertex with a search node
        finder.enqueue(new SearchNode(startIndex, 0, null));

        //Dequeue the first search node for searching
        SearchNode temp = finder.dequeue();

        //Mark the first dequeue on the distance tracker
        tracker[temp.index] = 0;

        //Continues to loop through the algorithm until reaching destination
        while(temp.index != endIndex){
            //Enqueues all connections
            for(int i = 0; i < graph.adjacencyMatrix[0].length; i++){
                if(graph.adjacencyMatrix[temp.index][i] == true){
                    finder.enqueue(new SearchNode(i, temp.distance + findDistance(graph.vertices[temp.index], graph.vertices[i]), temp));
                }
            }

            //Dequeue lowest priority
            temp = finder.dequeue();

            //If the current search node's recorded distance is lower than the distance on the tracker array,
            //replace the distance with the new one, or else continue to dequeue
            while(temp.distance >= tracker[temp.index]){
                temp = finder.dequeue();
            }

            tracker[temp.index] = temp.distance;
        }

        //Graphically output the result using the last search node
        drawPath(graph, temp);
    }

    //Draw the path from the SearchNode returned by findPath
    public static void drawPath(USAGraph graph, SearchNode searchNode){
        //Set up canvas
        StdDraw.setCanvasSize(1200, 750);
        StdDraw.picture(0.5, 0.5, "map-usa.jpg", 1, 1);

        //Loop through the search nodes to draw a path on the
        SearchNode temp = searchNode;
        while(searchNode.previous != null){
            Vertex last = graph.vertices[temp.index];
            temp = temp.previous;
            Vertex current = graph.vertices[temp.index];

            //All coordinates are scaled down by 10000 to be drawn inside the unit square
            StdDraw.line(last.xCoord/10000, last.yCoord/10000, current.xCoord/10000, current.yCoord/10000);

            if(temp.previous != null){
                temp = temp.previous;
            }
        }
    }

    //Distance between two vertices
    public static int findDistance(Vertex start, Vertex end){
        return (int) Math.sqrt(Math.pow((end.xCoord - start.xCoord), 2) + Math.pow((end.yCoord - start.yCoord), 2));
    }


    //Priority queue for Dijkstra's algorithm
    private static class PriorityQueue{
        SearchNode[] pqArray;
        int size;

        private PriorityQueue(){
            this.pqArray = new SearchNode[10];
            this.size = 0;
        }

        private void enqueue(SearchNode searchNode){
            pqArray[size] = searchNode;
            size++;
            pqFloat();
            resize();
        }

        private SearchNode dequeue(){
            SearchNode temp = pqArray[0];
            pqArray[0] = pqArray[size-1];
            pqArray[size-1] = null;
            size--;
            pqSink();
            resize();
            return temp;
        }

        private void pqFloat(){
            int currentIndex = size - 1;
            while(currentIndex > 0 && pqArray[currentIndex].distance < pqArray[pqParent(currentIndex)].distance){
                swap(pqParent(currentIndex), currentIndex);
                currentIndex = pqParent(currentIndex);
            }
        }

        private void pqSink(){
            int currentIndex = 0;
            while(currentIndex * 2 < size - 1 && ((pqArray[currentIndex * 2 + 1] != null && pqArray[currentIndex].distance > pqArray[currentIndex * 2 + 1].distance) && (pqArray[currentIndex * 2 + 2] != null && pqArray[currentIndex].distance > pqArray[currentIndex * 2 + 2].distance))){
                if(pqArray[currentIndex * 2 + 1].distance < pqArray[currentIndex * 2 + 2].distance){
                    swap(currentIndex, currentIndex * 2 + 1);
                    currentIndex = currentIndex * 2 + 1;
                }
                else {
                    swap(currentIndex, currentIndex * 2 + 2);
                    currentIndex = currentIndex * 2 + 2;
                }
            }
        }

        private int pqParent(int index){
            if(index % 2 == 0){
                return index/2 - 1;
            }
            else {
                return index/2;
            }
        }

        private void swap(int indexFrom, int indexTo){
            SearchNode temp = pqArray[indexTo];
            pqArray[indexTo] = pqArray[indexFrom];
            pqArray[indexFrom] = temp;
        }

        private void resize(){
            if(size == pqArray.length){
                SearchNode[] bigger = new SearchNode[pqArray.length * 2];
                for(int i = 0; i < pqArray.length; i++){
                    bigger[i] = pqArray[i];
                }
                pqArray = bigger;
            }
            else if(size == pqArray.length/4 && pqArray.length > 10){
                SearchNode[] smaller = new SearchNode[pqArray.length/2];
                for(int i = 0; i < pqArray.length; i++){
                    smaller[i] = pqArray[i];
                }
                pqArray = smaller;
            }
        }

    }

    //Search nodes for priority queue
    private static class SearchNode{
        int index;
        int distance;
        SearchNode previous;

        private SearchNode(int index, int distance, SearchNode previous){
            this.index = index;
            this.distance = distance;
            this.previous = previous;
        }
    }
}
