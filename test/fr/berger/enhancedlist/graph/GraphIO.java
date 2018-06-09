package fr.berger.enhancedlist.graph;

import fr.berger.enhancedlist.graph.builder.VertexBuilder;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.lexicon.LexiconBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GraphIO {
	
	public static Graph<Object, Object> read(@NotNull String path) {
		if (path == null)
			throw new NullPointerException();
		
		Graph<Object, Object> graph = new Graph<>();
		Lexicon<String> content = readContent(path);
		final byte PARSE_NOTHING = 0;
		final byte PARSE_VERTEX = 1;
		final byte PARSE_EDGE = 2;
		byte parse = PARSE_NOTHING;
		
		for (int i = 0; i < content.size(); i++) {
			String line = content.get(i);
			
			if (line == null)
				continue;
			
			if (line.equals("--- Liste des sommets"))
				parse = PARSE_VERTEX;
			else if (line.equals("--- Liste des aretes"))
				parse = PARSE_EDGE;
			else {
				switch (parse) {
					case PARSE_NOTHING:
						if (line.startsWith("Oriente(non/oui):")) {
							String[] infoLine = line.split(":");
							
							if (infoLine.length == 2) {
								if (infoLine[1].contains("oui"))
									graph.setOriented(true);
								else if (infoLine[1].contains("non"))
									graph.setOriented(false);
							}
						}
						break;
					case PARSE_VERTEX:
						String[] infoVertex = line.split(" ");
						
						if (infoVertex.length == 2) {
							// TODO: Search that the label does not exist already
							boolean exist = false;
							for (Vertex<Object> v : graph.getVertices()) {
								if (v.getLabel().equals(infoVertex[0])) {
									exist = true;
									v.setData(infoVertex[1]);
								}
							}
							
							if (!exist) {
								graph.getVertices().add(new VertexBuilder<>()
										.setLabel(infoVertex[0])
										.setData(infoVertex[1])
										.createVertex());
							}
						}
						break;
					case PARSE_EDGE:
						String[] infoEdge = line.split(" ");
						
						if (infoEdge.length == 2) {
							Edge<Object> e = new Edge<>();
							
							// Search the vertices
							Vertex<Object> x = null, y = null;
							for (Vertex<Object> v : graph.getVertices()) {
								if (infoEdge[0].equals(v.getLabel()))
									x = v;
								else if (infoEdge[1].equals(v.getLabel()))
									y = v;
							}
							
							// If does not exist, create the vertices
							if (x == null) {
								x = new VertexBuilder<>()
										.setLabel(infoEdge[0])
										.createVertex();
								graph.getVertices().add(x);
							}
							if (y == null) {
								y = new VertexBuilder<>()
										.setLabel(infoEdge[1])
										.createVertex();
								graph.getVertices().add(y);
							}
							
							e.setX(x);
							e.setY(y);
							graph.getEdges().add(e);
						}
						break;
				}
			}
		}
		
		return graph;
	}
	
	@SuppressWarnings("ConstantConditions")
	public static Lexicon<String> readContent(@NotNull String path) {
		if (path == null)
			throw new NullPointerException();
		
		File f = new File(path);
		FileReader fr = null;
		BufferedReader br = null;
		Lexicon<String> content = new LexiconBuilder<String>()
				.setAcceptNullValues(false)
				.createLexicon();
		
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
				content.add(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return content;
	}
	
	/* TESTS */
	
	@Test
	public void test_read() {
		long start = System.currentTimeMillis();
		Graph<Object, Object> queen5 = read("res/queen5.txt");
		long stop = System.currentTimeMillis();
		System.out.println("GraphIO.test_read> queen5 = " + queen5.toString());
		System.out.println("GraphIO.test_read> nb of edges: " + queen5.getEdges().size());
		System.out.println("GraphIO.test_read> Time: " + (stop - start) + "ms");
	}
}
