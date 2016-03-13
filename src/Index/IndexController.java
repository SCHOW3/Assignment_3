package Index;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

import org.json.simple.parser.ParseException;

import Index.TotalTermFrequency;


public class IndexController {

	public static HashMap<Long, HashMap<Long, Double>> IndexedListWithWeight = new HashMap<Long, HashMap<Long, Double>>();
	
	public static void main(String[] args) throws IOException, ParseException {
		final long startTime = System.currentTimeMillis();

		System.out.println("IndexController");
		Indexer.runIndex(); // Changed. default value is -1, uses all docs
		HashMap<Long, Integer> totalWordFrequency = TotalTermFrequency.computeWeightedIndex();
		HashMap<Long, HashMap<Long, Integer>> wordFrequencyDocument = 
				DocumentTermFrequency.computeWeightedIndex();

		for(Long document : wordFrequencyDocument.keySet()){
			HashMap<Long, Integer> wordPerDocument = wordFrequencyDocument.get(document);
			for(Long word : wordPerDocument.keySet()){
				double tf = (double) 1 + Math.log(wordPerDocument.get(word));
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
		ObjectOutputStream IndexedListWithWeightWriter = new ObjectOutputStream(new FileOutputStream("IndexedListWithWeight.index"));
		IndexedListWithWeightWriter.writeObject(IndexedListWithWeight);
		IndexedListWithWeightWriter.close();
		
		final long endTime = System.currentTimeMillis();
		System.out.println("Total elapsed time: " + Long.toString(endTime - startTime));
	}
}
