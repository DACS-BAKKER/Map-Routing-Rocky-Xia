import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Author: Rocky Xia
public class USAGraph {

    //Variables and objects
    public Vertex[] vertices;
    public boolean[][] adjacencyMatrix;
    private int vertexNumber;
    private int connectionNumber;

    //Constructor
    //File is loaded into array on construction
    public USAGraph(String filename) throws IOException {
        //Set up BufferedReader
        //Each line is split with a regular expression that detects one or more spaces or tabs
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String[] current = reader.readLine().split("[ |\\t]+");

        //Get the number of vertices and number of connections
        vertexNumber = Integer.valueOf(current[0]);
        connectionNumber = Integer.valueOf(current[1]);

        //Construct the array
        vertices = new Vertex[vertexNumber];
        adjacencyMatrix = new boolean[vertexNumber][vertexNumber];

        //Setup adjacency matrix
        for(int row = 0; row < adjacencyMatrix.length; row++){
            for(int col = 0; col < adjacencyMatrix[0].length; col++){
                adjacencyMatrix[row][col] = false;
            }
        }

        //Load vertices into array
        for(int i = 0; i < vertexNumber; i++){
            current = reader.readLine().split("[ |\\t]+");
            vertices[Integer.valueOf(current[1])] = new Vertex(Integer.valueOf(current[2]), Integer.valueOf(current[3]));
        }

        //Load connections into vertices
        for(int i = 0; i < connectionNumber; i++){
            current = reader.readLine().split("[ |\\t]+");
            adjacencyMatrix[Integer.valueOf(current[1])][Integer.valueOf(current[2])] = true;
        }

        //Close file after reading
        reader.close();
    }
}
