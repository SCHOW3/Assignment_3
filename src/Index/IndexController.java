package Index;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.parser.ParseException;

import Index.TotalTermFrequency;


public class IndexController {

	public static HashMap<Long, HashMap<Long, Double>> IndexedListWithWeight
		= new HashMap<Long, HashMap<Long, Double>>();
	
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		System.out.println("IndexController");
		Indexer.runIndex();
		HashMap<Long, Integer> totalWordFrequency = TotalTermFrequency.computeWeightedIndex();
		HashMap<Long, HashMap<Long, Integer>> wordFrequencyDocument = 
				DocumentTermFrequency.computeWeightedIndex();

		for(Long document : wordFrequencyDocument.keySet()){
			HashMap<Long, Integer> wordPerDocument = wordFrequencyDocument.get(document);
			for(Long word : wordPerDocument.keySet()){
				double tf = (double)wordPerDocument.get(word)/totalWordFrequency.get(word);
				double idf = (double)Math.log((double)(Indexer.docid2termlist.size()/Indexer.term2doclist.get(word).size()));
				double tf_idf = tf*idf;

				if (!IndexedListWithWeight.containsKey(document)){
					HashMap<Long, Double> toAdd = new HashMap<Long, Double>();
					toAdd.put(word, tf_idf);
					IndexedListWithWeight.put(document, toAdd);
				}
				else{
					HashMap<Long, Double> hashMapTFIDF = IndexedListWithWeight.get(document);
					hashMapTFIDF.put(word, tf_idf);
					IndexedListWithWeight.put(document, hashMapTFIDF);
				}
			
			}
		}
		FileWriter IndexedListWithWeightWriter =
				new FileWriter("C:\\Users\\Steven\\workspace\\Assignment_3\\IndexedListWithWeight");
		IndexedListWithWeightWriter.write(IndexedListWithWeight.toString().replace(",", "\n"));
		IndexedListWithWeightWriter.close();
	}
}
