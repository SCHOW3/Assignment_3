package Index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;

import java.io.ObjectInputStream;
import java.io.FileInputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;

public class Searcher {
	
	public static int numberOfDocsToGet = 5;
	
	public static void main(String[] args) {
		try {
			search("machine learning");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> search(String searchTerms) 
			throws FileNotFoundException, ClassNotFoundException, IOException, ParseException {
		final long startTime = System.currentTimeMillis();
		
		ObjectInputStream term2termidStream = new ObjectInputStream(new FileInputStream("term2termid.index"));
		ObjectInputStream tfidfStream = new ObjectInputStream(new FileInputStream("IndexedListWithWeight.index"));
		
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> term2termid = (HashMap<String, Integer>) term2termidStream.readObject();
		term2termidStream.close();
		@SuppressWarnings("unchecked")
		HashMap<Long, HashMap<Long, Double>> mainScore = (HashMap<Long, HashMap<Long, Double>>) tfidfStream.readObject();
		tfidfStream.close();
		
		ArrayList<Long> searchTermIDs = new ArrayList<Long>();
		for (String term : searchTerms.split("\\s+")) {
			searchTermIDs.add(term2termid.get(term).longValue());
		}
		
		HashMap<Long, Double> docScore = new HashMap<Long, Double>();
		for (Long termid : searchTermIDs) {
			// a map of all documents and their tfidf score for the given term
			if (mainScore.containsKey(termid)) {
				HashMap<Long, Double> tfIDF = mainScore.get(termid);
				for(Long doc : tfIDF.keySet()){
					double value = tfIDF.get(doc);
					if(docScore.containsKey(doc)){
						docScore.put(doc, docScore.get(doc) + value);
					}
					else {
						docScore.put(doc, value);
					}
				}
			}
		}
		// NO SEARCH RESULTS FOUND
		if (docScore.isEmpty()) {
			System.out.println("No results found :(");
			
			final long endTime = System.currentTimeMillis();
			System.out.println("Total elapsed time: " + Long.toString(endTime - startTime) + " ms");
			
			return null;
		}
		
		// stores the ids of the top numberOfDocsToGet search results
		ArrayList<Pair<Long, Double>> searchResults = new ArrayList<Pair<Long, Double>>();
		for (int i = 0; i < numberOfDocsToGet; i++) {
			searchResults.add(new Pair<Long, Double>(0L, Double.MIN_VALUE));
		}
				
		for (Map.Entry<Long, Double> docToScore : docScore.entrySet()) {
			if (docToScore.getValue() > searchResults.get(numberOfDocsToGet - 1).getR()) {
				searchResults.set(numberOfDocsToGet - 1, new Pair<Long, Double>(docToScore.getKey(), docToScore.getValue()));
				
				// sort via selection sort
				for (int i = 0; i < searchResults.size(); i++) {
				   for (int j = i; j < searchResults.size(); j++) {
					   if (searchResults.get(j).getR() > searchResults.get(i).getR()) {
						   Pair<Long, Double> swap = new Pair<Long,Double>(searchResults.get(j).getL(), searchResults.get(j).getR());
						   searchResults.set(j, searchResults.get(i));
						   searchResults.set(i, swap);
					   }
				   }
				}
			}
		}
		
		// We use this as a docid to doc translator
		JSONParser parser = new JSONParser();
		Object raw_data = parser.parse(new FileReader("html_files.json"));
		JSONObject tableOfContents = (JSONObject) raw_data;
		
		ArrayList<String> urls = new ArrayList<String>();
		for (Pair<Long, Double> docid2score : searchResults) {
			urls.add( (String) ((JSONObject) tableOfContents.get(Long.toString(docid2score.getL()))).get("url"));
			System.out.println(docid2score.getR().toString() + " " + ((JSONObject) tableOfContents.get(Long.toString(docid2score.getL()))).get("url"));
		}
		
		final long endTime = System.currentTimeMillis();
		System.out.println("Total elapsed time: " + Long.toString(endTime - startTime) + " ms");
		
		return urls;
	}
}

/*
public class Searcher {
	public static void main(String[] args) {
		try {
			// keep track of how long it takes our search to run
			final long startTime = System.currentTimeMillis();
			
			ObjectInputStream term2termidStream = new ObjectInputStream(new FileInputStream("term2termid.index"));
			ObjectInputStream tfidfStream = new ObjectInputStream(new FileInputStream("IndexedListWithWeight.index"));
			
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> term2termid = (HashMap<String, Integer>) term2termidStream.readObject();
			term2termidStream.close();
			
System.out.println("term2termid: " + term2termid.toString());
			
			// Using Long here rather than Integer because Long is used later on with our tfidf matrix
			ArrayList<Long> searchTermIDs = new ArrayList<Long>();
			for (String term : args) {
				// only use the search term if we have it indexed in the frist place
				if (term2termid.containsKey(term)) {
					searchTermIDs.add(term2termid.get(term).longValue());
					System.out.println(term);
				}
			}
			System.out.println("");
			
//			searchTermIDs.add((long)83);
//			searchTermIDs.add((long)897);
			
//			// free up memory
//			term2termid = null;
			
			// right now we are using our tfidf scores as the final score for our document selection
			// mainScore: doc : (term : value)
			@SuppressWarnings("unchecked")
			HashMap<Long, HashMap<Long, Double>> mainScore = (HashMap<Long, HashMap<Long, Double>>) tfidfStream.readObject();
			tfidfStream.close();
						
System.out.println("mainScore: " + mainScore.toString());

			
			// sort documents based on the combined scores of all the search terms given
			ArrayList<Pair<Long, Double>> searchResults = new ArrayList<Pair<Long, Double>>();
			
			// dtm: document term matrix
			for (Map.Entry<Long, HashMap<Long, Double>> dtm : mainScore.entrySet()) {
				Double docScore = (double) 0;
				
				ArrayList<Long> termIntersection = new ArrayList<Long>(searchTermIDs);

//				System.out.println(termIntersection.toString());
//				System.out.println(dtm.getValue().keySet().toString());
			
//				System.out.println(Boolean.toString(dtm.getValue().keySet().contains((long) 83)));
//				System.out.println(Boolean.toString(dtm.getValue().keySet().contains((long) 897)));
				
//				System.out.println(dtm.getValue().toString());

				termIntersection.retainAll(dtm.getValue().keySet());

//				System.out.println(termIntersection.toString());
//				System.out.println("-----");
//				
//				System.out.println(dtm.getValue().toString());
				
				for (Long termid : termIntersection) {
					docScore += dtm.getValue().get(termid);
//					System.out.println(termid.toString() + ": " + dtm.getValue().get(termid).toString());
				}
//				System.out.println("-------------");
				
				searchResults.add(new Pair<Long, Double>(dtm.getKey(), docScore));
			}
			
			// free up memory
			mainScore = null;
			searchTermIDs = null;
			
			// sort via selection sort
			for (int i = 0; i < searchResults.size(); i++) {
			   for (int j = i; j < searchResults.size(); j++) {
			      if (searchResults.get(j).getR() > searchResults.get(i).getR()) {
			    	  Pair<Long, Double> swap = new Pair<Long,Double>(searchResults.get(j).getL(), searchResults.get(j).getR());
			    	  searchResults.set(j, searchResults.get(i));
			    	  searchResults.set(i, swap);
			      }
			   }
			}
			
			// We use this as a docid to doc translator
			JSONParser parser = new JSONParser();
			Object raw_data = parser.parse(new FileReader("html_files.json"));
			JSONObject tableOfContents = (JSONObject) raw_data;
			
			for (Pair<Long, Double> docid2score : searchResults.subList(0, 10)) {
				System.out.println(docid2score.getR().toString() + " " + ((JSONObject) tableOfContents.get(Long.toString(docid2score.getL()))).get("url"));
			}
			
			final long endTime = System.currentTimeMillis();
			System.out.println("Total elapsed time: " + Long.toString(endTime - startTime));
		}
		catch (FileNotFoundException e) {
			System.out.println("Index was not found; try running IndexController.");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
//		catch (Exception e) {
//			System.out.println("Unknown error encountered: " + e.getMessage());
//		}
	}
}
*/