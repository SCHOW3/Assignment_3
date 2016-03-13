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


public class TotalTermFrequency {

	public static HashMap<Long, Integer> computeWeightedIndex() {
		HashMap<Long, Integer> wordFrequencyTotal = new HashMap<Long, Integer>();
		
		try {
			ObjectInputStream istream = new ObjectInputStream(new FileInputStream("docid2termlist.index"));
			
			@SuppressWarnings("unchecked")
			HashMap<Integer, ArrayList<Integer>> docid2termlist = (HashMap<Integer, ArrayList<Integer>>) istream.readObject();
			
			for (Map.Entry<Integer, ArrayList<Integer>> entry : docid2termlist.entrySet()) {
				for (Integer termid : entry.getValue()){
					if(!wordFrequencyTotal.containsKey(termid.longValue())){
						wordFrequencyTotal.put(termid.longValue(), 1);
					}
					else{
						wordFrequencyTotal.put(termid.longValue(), wordFrequencyTotal.get(termid.longValue())+1);
					}
				}
			}
			
			istream.close();
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
		return wordFrequencyTotal;
	}
}
