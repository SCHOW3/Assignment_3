package Index;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.io.ObjectInputStream;
import java.io.FileInputStream;


public class DocumentTermFrequency {
	public static HashMap<Long, HashMap<Long, Integer>> computeWeightedIndex(){
		HashMap<Long,HashMap<Long,Integer>> wordDocumentFrequency = new HashMap<Long,HashMap<Long,Integer>>();
			
		try {
			ObjectInputStream istream = new ObjectInputStream(new FileInputStream("docid2termlist.index"));
			
			@SuppressWarnings("unchecked")
			HashMap<Integer, ArrayList<Integer>> docid2termlist = (HashMap<Integer, ArrayList<Integer>>) istream.readObject();
			
			for (Map.Entry<Integer, ArrayList<Integer>> entry : docid2termlist.entrySet()) {
				HashMap<Long, Integer> frequencyPerDocument = new HashMap<Long, Integer>();
				
				for (Integer termid : entry.getValue()) {
					if (!frequencyPerDocument.containsKey(termid)) {
						frequencyPerDocument.put(termid.longValue(), 1);
					}
					else {
						frequencyPerDocument.put(termid.longValue(), frequencyPerDocument.get(termid.longValue()) + 1);
					}
				}
				wordDocumentFrequency.put(entry.getKey().longValue(), frequencyPerDocument);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return wordDocumentFrequency;
	}
}
