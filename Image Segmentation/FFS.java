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

class Node {
	public Integer value;
	public Integer nodename;

	public Node(Integer node) {
		this.nodename = node;
	}

	Map<Integer, Integer> hp = new HashMap<Integer, Integer>();
	public String color;
	public Integer distance;
	public Integer parent;
}

public class FFS {
	static ArrayList<Integer> objList = new ArrayList<Integer>();
	static int pathflow = Integer.MAX_VALUE;
	static int flow = 0;
	static int start_node = 0;
	static int val = 0;

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		FileInputStream f = null;
		ArrayList<Node> nodearray = new ArrayList<Node>();
		// Reading the input file line by line and differentiating as keys and
		// values in every line
		try {
			String inputfile = args[0];
			f = new FileInputStream(inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(f));
			List<List<String>> list = new ArrayList<List<String>>();
			String[] s;
			List<String> l;
			String line;
			while ((line = br.readLine()) != null) {
				String str = line;
				l = new ArrayList<String>();
				s = str.split(" ");
				for (String s1 : s) {
					l.add(s1);
				}
				list.add(l);
			}
			int k = 0;
			for (List<String> s3 : list) {
				Node n3 = new Node(k);
				k++;
				String[] s6 = new String[s3.size()];
				String[] s8 = s3.toArray(s6);
				for (int i = 0; i < s8.length; i++) {
					String key1 = s8[i];
					i++;
					String value1 = s8[i];
					n3.hp.put(Integer.parseInt(key1), Integer.parseInt(value1));
				}
				nodearray.add(n3);
			}
			Object[] graph = nodearray.toArray(); // graph as an array
			BFS(graph);
			long endTime = System.currentTimeMillis();
			System.out.println("\nThe time of execution in MilliSeconds: "
					+ (endTime - startTime));
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void BFS(Object[] graph) {
		// Gives the shortest path
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

	private static void maxflow(int maxflow) {
		System.out.println("Max Flow: " + flow);
	}

	public static void getpath(Object[] graph, Integer start_node,
			Integer final_node) {
		// Prints the shortest path and finds the min capacity on the edges of a
		// shortest path found
		Node sink = (Node) graph[final_node];
		sink.color = "Red";
		Integer parent = sink.parent;
		if (parent != null) {
			Node lastnode = (Node) graph[parent];
			Integer value = lastnode.hp.get(final_node);
			pathflow = Math.min(pathflow, value);
			getpath(graph, start_node, parent);
		} else {
			flow = flow + pathflow;
			updategraph(graph, graph.length - 1);
		}
	}

	private static void updategraph(Object[] graph, Integer lastvertex) {
		// Updates the graph by substracting the min capacity on the forward
		// direction
		// and by adding the new edge with the min capacity in the reverse
		// direction if
		// reverse edge doesn't exist else updates the reverse edge if already
		// exists
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

	private static void removeedge(Object[] graph, Integer lastvertex) {
		// Updates the graph by removing the forward and reverse edges in a
		// graph
		// with the capacity zero.
		Node sink = (Node) graph[lastvertex];
		Integer parent = sink.parent;
		if (parent != null) {
			Node parent1 = (Node) graph[parent];
			if (sink.color == "Red" && parent1.color == "Red") {
				Integer value = parent1.hp.get(lastvertex);
				if (value == 0) {
					parent1.hp.remove(lastvertex);
				}
				Integer reversevalue = sink.hp.get(parent);
				if (reversevalue == 0) {
					sink.hp.remove(parent);
				}
				removeedge(graph, parent);
			}
		} else {
			BFS(graph);
		}
	}
}