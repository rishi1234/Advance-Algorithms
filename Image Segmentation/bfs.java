/**
  Homework Assignment #1
   * @ Sukesh Reddy
   * @ Shashank Reddy
   * @ Vandana Rao
   * @ Rishi Reddy
   @version 1.0 2014-02-09
*/

import java.io.*;
import java.util.*;

class Node {    //Node data structure
	public Integer value;
	public Integer nodename;

	public Node(Integer node) {
		this.nodename = node;
	}

	Map<Integer, Integer> hp = new HashMap<Integer, Integer>();  //hash map to store the edges between the nodes
	public String color;
	public Integer distance;
	public Integer parent;
}

public class bfs {  //To calculate the shortest path in the graph

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		FileInputStream f = null;
		ArrayList<Node> nodearray = new ArrayList<Node>(); // to store the keys
															// and values which
															// are connected to
															// a particular node
		int start_node = Integer.parseInt(args[1]); // starting node number to
													// give as input
		int final_node = Integer.parseInt(args[2]); // last node number to give
													// as input

		try {
			String inputfile = args[0]; 
			f = new FileInputStream(inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(f));

			List<ArrayList<String>> list = new ArrayList<ArrayList<String>>(); // Creating the ArrayList
																				// of type Arraylist
																				
			String[] s; // to store keys and values after reading a line
			ArrayList<String> l;
			String line;
			while ((line = br.readLine()) != null) { // read all the lines until EOF														
				String str = line;
				l = new ArrayList<String>();
				s = str.split(" ");
				for (String s1 : s) {
					l.add(s1); // every line we read from input file, we store
								// its values in ArrayList
				}
				list.add(l); // storing all the lines data in ArrayList
			}
			int k = 0;
			for (List<String> s3 : list) { // Every line we read from input, we
											// are creating a node
				Node n3 = new Node(k);
				k++;
				String[] s6 = new String[s3.size()];
				String[] s8 = s3.toArray(s6); // Every line we read, we are
												// reading them as keys and
												// values
				for (int i = 0; i < s8.length; i++) {
					String key1 = s8[i]; // every first element of a line and
											// its alternate elements are
											// treated as keys
					i++;
					String value1 = s8[i]; // every second element of a line and
											// its alternate elements are
											// treated as values
					n3.hp.put(Integer.parseInt(key1), Integer.parseInt(value1)); // we are adding the keys
																					// and values of the nodes
																					// connected to particular node
				}
				nodearray.add(n3); // we are adding all the nodes to the ArrayList
			}

			Object[] nodes = nodearray.toArray(); // converting ArrayList to array (graph)
													
			BFS(nodes, start_node, final_node);
			System.out.println("Shortest path");
			System.out.print("(Final Node) " + final_node);
			getpath(nodes, start_node, final_node);
			long endTime = System.currentTimeMillis();
			System.out.println("\nThe time of execution in MilliSeconds: "
					+ (endTime - startTime));
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void BFS(Object[] graph, int start_node, int final_node) { 
		// finds the shortest path
		for (int i = start_node; i < graph.length; i++) {
			Node node = (Node) graph[i];

			if (i == start_node) {
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
		queue.add((Node) graph[start_node]);
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
	}
	
	public static void getpath(Object[] graph, Integer start_node,Integer final_node)
	// prints the shortest path
	{
		Node sink = (Node) graph[final_node];
		Integer parent = sink.parent;
		if (parent != null) {
			if (parent != start_node) {
				System.out.print(" -> " + parent);
				getpath(graph, start_node, parent);
			} else {
				System.out.println(" -> " + start_node + " (Source Node)");
			}
		} else {
			System.out.println(" -> (no source found)" + " ----> " + " No Path Found for the input given");
		}
	}
}