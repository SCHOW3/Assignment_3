package Index;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;


public class TotalTermFrequency {

	public static HashMap<Long, Integer> computeWeightedIndex(){
		HashMap<Long, Integer> wordFrequencyTotal
		= new HashMap<Long, Integer>();
	try {
		//FileReader docID2TermList = new FileReader("Tester.txt");
		FileReader docID2TermList = new FileReader("docid2termlist");
		 BufferedReader bufferedReader = 
	                new BufferedReader(docID2TermList); 
		String line;
		while((line = bufferedReader.readLine()) != null) {
	        String[] tempArray = line.split("=", 2);
	        //System.out.println(line);
	        Long document = Long.parseLong(tempArray[0].replace("{", "").replaceAll("\\s+", ""));
	        String terms = tempArray[1].replace("[", "");
	        //System.out.println("terms: " + terms);
	        //System.out.println(document);
	        String[] termArray = terms.split(",");
	        //System.out.println(Arrays.toString(termArray));
			for(String word : termArray){
				String wordIndex = word.replaceAll("\\s+", "").replaceAll("]", "").replaceAll("}", "");
				if(wordIndex.length() == 0)
					continue;
				if(!wordFrequencyTotal.containsKey(Long.parseLong(wordIndex))){
					wordFrequencyTotal.put(Long.parseLong(wordIndex), 1);
				}
				else{
					wordFrequencyTotal.put(Long.parseLong(wordIndex),
							wordFrequencyTotal.get(Long.parseLong(wordIndex))+1);
				}
			}
        } 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	return wordFrequencyTotal;
}
	
/*	
	public static void main(String[] args) {
		HashMap<Integer, Entry<Integer, Integer>> wordFrequencyPerDoc 
			= new HashMap<Integer, Entry<Integer,Integer>>();
		try {
			FileReader docID2TermList = new FileReader("docid2termlist");
			 BufferedReader bufferedReader = 
		                new BufferedReader(docID2TermList); 
			String line;
			while((line = bufferedReader.readLine()) != null) {
		        String[] tempArray = line.split("=", 2);
		        System.out.println(line);
		        String document = tempArray[0].replace("{", "");
		        String terms = tempArray[1].replace("[", "");
		        System.out.println(document);
		        String[] termArray = terms.split(",");
		        System.out.println(Arrays.toString(termArray));
				for(String wordIndex : termArray){
					
				}
	           break;
            } 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
