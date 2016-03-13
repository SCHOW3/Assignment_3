package Index;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Indexer {
	public static HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	public static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();
	public static HashMap<Integer, String> docid2docname = new HashMap<Integer, String>();
	public static HashMap<Integer, ArrayList<Integer>> docid2termlist 
		= new HashMap<Integer, ArrayList<Integer>>();
	public static HashMap<Long, ArrayList<Integer>> term2doclist
		= new HashMap<Long, ArrayList<Integer>>();
	private final static String root = "Html\\";
	
	public static void ret_file(File to_read, Integer counter) throws FileNotFoundException {
		ArrayList<Integer> termList = new ArrayList<Integer>(); //ArrayList of the terms that appear in a document
		Scanner file_scanner = new Scanner(to_read);
		String html_doc = "";
		while(file_scanner.hasNext()) {
			html_doc += file_scanner.nextLine();
		}
		html_doc = html_doc.toLowerCase();
		Document parsed_doc = Jsoup.parse(html_doc);
		try{
			String[] split_doc = parsed_doc.body().text().split("\\s+");
			for(String wordComp : split_doc){
				String word = wordComp.replaceAll("[^a-zA-Z0-9]+","");
				if(word.contains("}") || word.contains("{") || word.contains("%") || word.contains("-")
						|| word.contains("_")|| word.contains("@")|| word.contains("#")
						|| word.contains("$")|| word.contains("^")|| word.contains("&")
						|| word.contains("{")|| word.contains("}")|| word.contains("/")
						|| word.contains(")") || word.contains("(") || word.contains(":")
						|| (word == "")){
					continue;
				}
					if (!term2termid.containsKey(word)){
						term2termid.put(word, term2termid.size()+1);
						termid2term.put(termid2term.size()+1, word);
					}
					termList.add(term2termid.get(word));
					
					if(!term2doclist.containsKey((long)term2termid.get(word))){
						ArrayList<Integer> docList = new ArrayList<Integer>();
						docList.add(counter);
						term2doclist.put((long)term2termid.get(word),docList);
					}
					else{
						ArrayList<Integer> updatedDocList = term2doclist.get((long)term2termid.get(word));
						updatedDocList.add(counter);
						term2doclist.put((long)term2termid.get(word), updatedDocList);
					}
				
			}
			docid2termlist.put(counter, termList);
		}
		catch (NullPointerException e){
		}
	}
	
	public static ArrayList<String> obtain_url(int limiter) throws FileNotFoundException, IOException, ParseException{
		ArrayList<String> htmlString = new ArrayList<String>();
		try{
			JSONParser parser = new JSONParser();
			Object raw_data = parser.parse(new FileReader("html_files.json"));
			JSONObject tableOfContents = (JSONObject) raw_data;
			
			if (limiter == -1) {
				limiter = tableOfContents.size();
			}
			System.out.println(limiter);
			
			for (int readingCounter = 0; readingCounter < limiter; readingCounter++){
				JSONObject realData = (JSONObject) tableOfContents.get(Integer.toString(readingCounter));
				htmlString.add(((String) realData.get("file")));
				docid2docname.put(readingCounter, (String) realData.get("file"));
				readingCounter++;
			}
			return htmlString;
		}
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return htmlString;
	}
	
	// Making the default value of the first arguement to -1. -1: all docs are used
	public static void runIndex() throws IOException, ParseException {
		runIndex(-1);
	}
	
	public static void runIndex(int numberOfDocsToIndex) throws IOException, ParseException {			
		
		// If numberOfDocsToIndex is -1, the all docs are returned
		ArrayList<String> fileList = obtain_url(numberOfDocsToIndex);
		int counter = 0;
		for(String file : fileList){
			ret_file(new File(root + file), counter);
			counter++;
		}
			
		ObjectOutputStream term2TermIdWriter = new ObjectOutputStream(new FileOutputStream("term2termid.index"));
		term2TermIdWriter.writeObject(term2termid);
		term2TermIdWriter.close();
		ObjectOutputStream termId2TermWriter = new ObjectOutputStream(new FileOutputStream("termid2term.index"));
		termId2TermWriter.writeObject(termid2term);
		termId2TermWriter.close();
		ObjectOutputStream docId2TermListWriter = new ObjectOutputStream(new FileOutputStream("docid2termlist.index"));
		docId2TermListWriter.writeObject(docid2termlist);
		docId2TermListWriter.close();		
		ObjectOutputStream term2DocListWriter = new ObjectOutputStream(new FileOutputStream("term2doclist.index"));
		term2DocListWriter.writeObject(term2doclist);
		term2DocListWriter.close();
	}
}

