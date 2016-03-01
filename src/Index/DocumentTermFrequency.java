package Index;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class DocumentTermFrequency {
	public static HashMap<Long, HashMap<Long, Integer>> computeWeightedIndex(){
		HashMap<Long,HashMap<Long,Integer>> wordDocumentFrequency
		= new HashMap<Long,HashMap<Long,Integer>>();
		
	try {
		//FileReader docID2TermList = new FileReader("Tester.txt");
		FileReader docID2TermList = new FileReader("docid2termlist.index");
		 BufferedReader bufferedReader = 
	                new BufferedReader(docID2TermList); 
		String line;
		while((line = bufferedReader.readLine()) != null) {
			HashMap<Long, Integer> frequencyPerDocument = new HashMap<Long, Integer>();
	        String[] tempArray = line.split("=", 2);
	        //System.out.println(line);
	        String document = tempArray[0].replace("{", "").replaceAll("\\s+", "");
	        Long longDocument = Long.parseLong(document);
	        String terms = tempArray[1].replace("[", "").replace("}", "").replace("]", "").replaceAll("\\s+", "");
	        String termsCleaned = terms.replace("\\s+", "");
	        //System.out.println(document);
	        String[] termArray = termsCleaned.split(",");
			for(String wordIndex : termArray){
				//System.out.println(wordIndex);
				if(wordIndex.length()==0)
					continue;
				if(!frequencyPerDocument.containsKey(wordIndex)){
					frequencyPerDocument.put(Long.parseLong(wordIndex), 1);
				}
				else{
					frequencyPerDocument.put(Long.parseLong(wordIndex),
							frequencyPerDocument.get(wordIndex)+1);
				}
			}
			wordDocumentFrequency.put(longDocument, frequencyPerDocument);
        } 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	return wordDocumentFrequency;
}

}
