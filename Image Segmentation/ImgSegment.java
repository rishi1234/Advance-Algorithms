/**
  Homework Assignment #1
 * @ Sukesh Reddy
 * @ Shashank Reddy
 * @ Vandana Rao
 * @ Rishi Reddy

   @version 1.0 2014-02-09
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//GraphNodes Data Structure
class Graphnode {
	public int tag, colorValue;

	public Graphnode(int x, int y) {
		this.tag = x;
		this.colorValue = y;
	}
}

// Node Data Structures
class Node {
	public Integer nodename;

	public Node(Integer node) {
		this.nodename = node;
	}

	// Hash Map of Integer, Integer
	Map<Integer, Integer> hp = new HashMap<Integer, Integer>();
	public String color;
	public Integer distance;
	public Integer parent;
}

class ford {
	// Declaring variables that are required
	public Graphnode root_Node, sink_Node;// to represent the root node and sink
	public ArrayList nodes = new ArrayList();// Array that stores all the nodes
												// of graph
	public int[][] adjacency_Matrix;// Edges will be represented as adjacency
									// Matrix
	public int size;// Number of nodes for the graph
	public Graphnode[] nodeList;
	public static int row_Pixels, column_Pixels, scale;
	public static int row_Pixels_1, column_Pixels_1;
	ArrayList<Node> nodearray = new ArrayList<Node>();
	static Graphnode[] nodesList_1;

	static int pathflow = Integer.MAX_VALUE;// Assigning the path flow to a
											// maximum value
	static int flow = 0;
	static int start_node = 0;

	// Assigning nodesList to nodesList_1
	public void graph(Graphnode[] nodesList) {
		nodesList_1 = nodesList;
	}

	// First node of the array list is set as the root node.
	public void setRoot(Graphnode n) {
		this.root_Node = n;
	}

	// Last node of the array list is set as the sink node
	public void setSink(Graphnode n) {
		this.sink_Node = n;
	}

	// To add the node to the array list
	public void addNode(Graphnode n) {
		nodes.add(n);
	}

	// It sets the number of rows, columns and color scale.
	public void setFileData(int numberOfPixelsRowCopy,
			int numberOfPixelsColCopy, int colorScaleCopy) {
		row_Pixels = numberOfPixelsRowCopy;
		column_Pixels = numberOfPixelsColCopy;
		scale = colorScaleCopy;
		row_Pixels_1 = row_Pixels * column_Pixels;
		column_Pixels_1 = row_Pixels * column_Pixels;
	}

	// To connect the nodes in the graph.
	public void nodeConnect(Graphnode start, Graphnode end, int weight) {
		if (adjacency_Matrix == null) {
			size = nodes.size();
			adjacency_Matrix = new int[size][size];
		}

		int startIndex = nodes.indexOf(start);
		int endIndex = nodes.indexOf(end);
		adjacency_Matrix[startIndex][endIndex] = weight;
	}

	public void print() {
		for (int k = 0; k < row_Pixels * column_Pixels + 2; k++) {
			for (int l = 0; l < column_Pixels * row_Pixels + 2; l++) {
				System.out.print(adjacency_Matrix[k][l] + "\t");
			}
			System.out.println();
		}

	}

	// Creating the array of nodes and storing the edge weights of adjacency
	// matrix in the hash map and creating the graph which is represented in
	// array.
	public void Adjacency_list() {
		int k1 = 0;
		for (int k = 0; k < row_Pixels * column_Pixels + 2; k++) {
			Node n3 = new Node(k1);
			for (int l = 0; l < row_Pixels * column_Pixels + 2; l++) {
				if (adjacency_Matrix[k][l] > 0) {
					String key_1 = Integer.toString(l);
					String value1 = Integer.toString(adjacency_Matrix[k][l]);
					n3.hp.put(Integer.parseInt(key_1), Integer.parseInt(value1));

				}
			}
			nodearray.add(n3);
			k1++;
		}
		Object[] nodes = nodearray.toArray();
		Ford_BFS(nodes);
	}

	// To calculate the shortest path in the graph
	public static void Ford_BFS(Object[] graph) {
		Node length = (Node) graph[graph.length - 1];
		for (int i = 0; i < graph.length; i++) {
			Node node = (Node) graph[i];
			if (i == 0) {
				node.color = "gray";
				node.distance = 0;
				node.parent = null;
			} else {
				node.color = "white";
				node.distance = 0;
				node.parent = null;
			}
		}
		List<Node> queue = new ArrayList<Node>();
		queue.add((Node) graph[0]);
		while (!queue.isEmpty()) {
			Node node = queue.remove(0);
			for (Map.Entry<Integer, Integer> entry : node.hp.entrySet()) {
				Node neighbor = (Node) graph[entry.getKey()];
				if (neighbor.color.equalsIgnoreCase("white")) {
					neighbor.color = "gray";
					neighbor.distance += 1;
					neighbor.parent = node.nodename;
					queue.add(neighbor);
				}
			}
			node.color = "black";
		}
		if (length.color.equalsIgnoreCase("black")) {
			pathflow = Integer.MAX_VALUE;
			getpath(graph, start_node, graph.length - 1);
		} else {
			maxflow(flow);
		}
	}

	// Displaying the maximum flow
	private static void maxflow(int maxflow) {
		System.out.println("Max Flow: " + flow);
	}

	// Prints the shortest path found and finds the minimum flow capacity on the
	// edges in that shortest path.
	public static void getpath(Object[] graph, Integer start_node,
			Integer lastvertex) {
		Node sink = (Node) graph[lastvertex];
		sink.color = "Red";
		Integer parent = sink.parent;
		if (parent != null) {
			Node lastnode = (Node) graph[parent];
			Integer value = lastnode.hp.get(lastvertex);
			pathflow = Math.min(pathflow, value);
			getpath(graph, start_node, parent);
		} else {
			flow = flow + pathflow;
			updategraph(graph, graph.length - 1);
		}
	}

	// Updates the path flow on the edges on the shortest path found and adds
	// the reverse edges for a path if reverse edges doesn’t exist else updates
	// the flow on the reverse edges if already exists.
	private static void updategraph(Object[] graph, Integer lastvertex) {
		Node sink = (Node) graph[lastvertex];
		Integer parent = sink.parent;
		if (parent != null) {
			Node parent1 = (Node) graph[parent];
			if (sink.color == "Red" && parent1.color == "Red") {
				Integer value = parent1.hp.get(lastvertex);
				if (value != null) {
					int value1 = value - pathflow;
					parent1.hp.put(lastvertex, value1);
				}
				Integer returnflow = sink.hp.get(parent);
				if (returnflow != null) {
					int reverse = -pathflow;
					int value2 = returnflow - (reverse);
					sink.hp.put(parent, value2);
				} else {
					sink.hp.put(parent, pathflow);
				}
				updategraph(graph, parent);
			}
		} else {
			removeedge(graph, graph.length - 1);
		}
	}

	// Removes the edges with the flow capacity zero for a shortest path found
	// and sends the updated residual graph to Ford_BFS function
	private static void removeedge(Object[] graph, Integer lastvertex) {
		Node sink = (Node) graph[lastvertex];
		Integer parent = sink.parent;
		if (parent != null) {
			Node parent1 = (Node) graph[parent];
			if (sink.color == "Red" && parent1.color == "Red") {
				Integer value = parent1.hp.get(lastvertex);
				if (value == 0) {
					// Minimum cut to change the value of the nodes to 0
					Node parent_1 = (Node) graph[0];
					if (parent1.nodename == parent_1.nodename) {
						nodesList_1[lastvertex].colorValue = 0;
					}
					parent1.hp.remove(lastvertex);
				}
				Integer reversevalue = sink.hp.get(parent);
				if (reversevalue == 0) {
					sink.hp.remove(parent);
				}
				removeedge(graph, parent);
			}
		} else {
			Ford_BFS(graph);
		}
	}

	// Create the output file
	public static void createFile(String outputFileName) {
		File filePointer;
		filePointer = new File(outputFileName);
		try {
			filePointer.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(
					outputFileName, true));
			out.write("P2\n");
			out.write("# Created by IrfanView\n");
			out.write(row_Pixels + " " + column_Pixels + "\n");
			out.write(scale + " " + "\n");
			for (int i = 1; i <= (row_Pixels * column_Pixels); i++) {
				out.write(nodesList_1[i].colorValue + " ");
				if (i % row_Pixels == 0)
					out.write("\n");
			}
			out.close();
			System.out.println("File Created");
		} catch (IOException a) {
			System.out.println(a);
		}
	}
}

public class ImgSegment {

	public static void main(String[] args) {
		int row_Pixels, column_Pixels, numberOfNodes;
		try {
			String inputFileName = args[0];// Read the Input file name
			String outputFileName = args[1];// Read the Output file name
			File filePointer = new File(inputFileName);
			Scanner scanner = new Scanner(filePointer);

			for (int i = 0; i < 5; i++) {
				System.out.println(scanner.next());
			}
			double imageScale = 255;
			row_Pixels = Integer.parseInt(scanner.next());// Read the row pixels
			numberOfNodes = row_Pixels + 2;// Read the no. of nodes
			column_Pixels = Integer.parseInt(scanner.next());// Read the Column
																// pixels

			double colorScale = Double.parseDouble(scanner.next());

			ford g = new ford();
			Graphnode[] nodesList;
			nodesList = new Graphnode[(row_Pixels * column_Pixels) + 2];
			int i;
			// Set the row,column values
			g.setFileData(row_Pixels, column_Pixels, (int) colorScale);

			// Label the root node as 1
			nodesList[0] = new Graphnode(1, 0);
			// Label each node in the graph with a value
			for (i = 1; i <= row_Pixels * column_Pixels; i++) {
				nodesList[i] = new Graphnode(i + 1, Integer.parseInt(scanner
						.next()));
			}

			// Label the sink node as the last Graphnode
			nodesList[i] = new Graphnode(i + 1, (int) colorScale + 1);
			System.out.println("row_Pixels :" + row_Pixels + " numberOfNodes: "
					+ numberOfNodes + " column_Pixels:" + column_Pixels
					+ " colorScale:" + colorScale + " imageScale: "
					+ imageScale);
			// Add source, all pixels and sink as nodes to nodes ArrayList(Which
			// stores the node class objects)
			for (i = 0; i < (row_Pixels * column_Pixels) + 2; i++)
				g.addNode(nodesList[i]);

			// Setting the root node and sink node that is the first and last
			// node of the array nodeList
			g.setRoot(nodesList[0]);
			g.setSink(nodesList[(row_Pixels * column_Pixels) + 1]);

			// Connecting the root node with all the nodes except sink
			// and itself
			for (i = 1; i <= (row_Pixels * column_Pixels); i++) {

				int capacity = (int) ((nodesList[i].colorValue));
				g.nodeConnect(nodesList[0], nodesList[i], capacity);

			}

			// Connecting the sink node with all the nodes except root
			// and itself
			for (i = 1; i <= (row_Pixels * column_Pixels); i++) {
				int capacity = (int) ((colorScale - (nodesList[i].colorValue))
						);
				g.nodeConnect(nodesList[i],
						nodesList[(row_Pixels * column_Pixels) + 1], capacity);
			}

			// Connecting the adjacent nodes row wise
			for (int j = 1; j < (row_Pixels * column_Pixels); j++) {
				if (j % row_Pixels == 0) {
					g.nodeConnect(nodesList[j], nodesList[j + 1], 0);
					g.nodeConnect(nodesList[j + 1], nodesList[j], 0);
				} else {
					int capacity = (int) ((colorScale - Math
							.abs(nodesList[j + 1].colorValue - nodesList[j].colorValue))
									);
					g.nodeConnect(nodesList[j], nodesList[j + 1], capacity);
					g.nodeConnect(nodesList[j + 1], nodesList[j], capacity);
				}
			}

			// Connecting the adjacent nodes by down
			for (i = 1; i < (row_Pixels * column_Pixels); i++) {
				if (i < (row_Pixels * column_Pixels) - row_Pixels + 1) {
					int capacity = (int) ((colorScale - Math
									.abs(nodesList[i + row_Pixels].colorValue - nodesList[i].colorValue))
									);

					g.nodeConnect(nodesList[i], nodesList[i + row_Pixels],
							capacity);
					g.nodeConnect(nodesList[i + row_Pixels], nodesList[i],
							capacity);
				}

			}

			g.graph(nodesList);// Assigning the nodes list
//			g.print();

			long startTime = System.currentTimeMillis();
			System.out.println("\nThe start time: " + startTime);
			g.Adjacency_list();// Creating the array of nodes and storing the
								// edge weights of adjacency matrix in the hash
								// map and creating the graph which is
								// represented in array.

			long endTime = System.currentTimeMillis();
			System.out.println("\nThe time of execution is "
					+ (endTime - startTime)/1000);
			g.createFile(outputFileName);// Creates the output image
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File Not Found");
		}
	}

}
