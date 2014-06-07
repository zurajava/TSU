package ge.tsu.algorithm;
import java.util.Scanner;

public class FloydWarshall {
	private int distancematrix[][];
	private int numberofvertices;
	public static final int INFINITY = 999;

	public FloydWarshall(int numberofvertices) {
		distancematrix = new int[numberofvertices + 1][numberofvertices + 1];
		this.numberofvertices = numberofvertices;
	}

	public void floydwarshall(int adjacencymatrix[][]) {
		for (int source = 1; source <= numberofvertices; source++) {
			for (int destination = 1; destination <= numberofvertices; destination++) {
				distancematrix[source][destination] = adjacencymatrix[source][destination];
			}
		}

		for (int intermediate = 1; intermediate <= numberofvertices; intermediate++) {
			for (int source = 1; source <= numberofvertices; source++) {
				for (int destination = 1; destination <= numberofvertices; destination++) {
					if (distancematrix[source][intermediate]
							+ distancematrix[intermediate][destination] < distancematrix[source][destination])
						distancematrix[source][destination] = distancematrix[source][intermediate]
								+ distancematrix[intermediate][destination];
				}
			}
		}

		for (int source = 1; source <= numberofvertices; source++)
			System.out.print("\t" + source);

		System.out.println();
		for (int source = 1; source <= numberofvertices; source++) {
			System.out.print(source + "\t");
			for (int destination = 1; destination <= numberofvertices; destination++) {
				System.out.print(distancematrix[source][destination] + "\t");
			}
			System.out.println();
		}
	}
	
	public String floydwarshall1(int adjacencymatrix[][]) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=\"1\"  BORDERCOLOR=\"#85A366\" style=\"width:300px\">");
		
		for (int source = 0; source <= numberofvertices-1; source++) {
			for (int destination = 0; destination <= numberofvertices-1; destination++) {
				distancematrix[source][destination] = adjacencymatrix[source][destination];
			}
		}

		for (int intermediate = 0; intermediate <= numberofvertices-1; intermediate++) {
			for (int source = 0; source <= numberofvertices-1; source++) {
				for (int destination = 0; destination <= numberofvertices-1; destination++) {
					if (distancematrix[source][intermediate]
							+ distancematrix[intermediate][destination] < distancematrix[source][destination])
						distancematrix[source][destination] = distancematrix[source][intermediate]
								+ distancematrix[intermediate][destination];
				}
			}
		}

		for (int source = 0; source <= numberofvertices-1; source++)
			System.out.print("\t" + source);

		System.out.println();
		for (int source = 0; source <= numberofvertices-1; source++) {
			System.out.print(source + "\t");
			sb.append("<tr>");
			for (int destination = 0; destination <= numberofvertices-1; destination++) {
				sb.append("<td>");
				System.out.print(distancematrix[source][destination] + "\t");
				sb.append(distancematrix[source][destination]);
				sb.append("</td>");
			}
			System.out.println();
			sb.append("</tr>");
		}
		
		return sb.toString();
	}

	public static void main(String... arg) {
		int adjacency_matrix[][];
		int numberofvertices;

		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the number of vertices");
		numberofvertices = scan.nextInt();

		adjacency_matrix = new int[numberofvertices + 1][numberofvertices + 1];
		System.out.println("Enter the Weighted Matrix for the graph");
		for (int source = 1; source <= numberofvertices; source++) {
			for (int destination = 1; destination <= numberofvertices; destination++) {
				adjacency_matrix[source][destination] = scan.nextInt();
				if (source == destination) {
					adjacency_matrix[source][destination] = 0;
					continue;
				}
				if (adjacency_matrix[source][destination] == 0) {
					adjacency_matrix[source][destination] = INFINITY;
				}
			}
		}

		System.out.println("The Transitive Closure of the Graph");
		FloydWarshall floydwarshall = new FloydWarshall(numberofvertices);
		floydwarshall.floydwarshall(adjacency_matrix);
		System.out.println(floydwarshall.floydwarshall1(adjacency_matrix));
		scan.close();
	}
}