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
	private final static String root = "C:\\Users\\Steven\\workspace\\Assignment_3\\Html\\";
	
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
						|| word.contains(")") || word.contains("(") || word.contains(":")){
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
			JSONObject jsonObjectData = (JSONObject) raw_data;
			for (int readingCounter = 0; readingCounter < limiter; readingCounter++){
				JSONObject realData = (JSONObject) jsonObjectData.get(Integer.toString(readingCounter));
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
	
	public static void runIndex() throws IOException, ParseException {
		/*
		int counter = 0;
		while (true){
			try {
				if(counter%100 == 0)
				{
					System.out.println("Counter: " + counter);
				}
					String file = obtain_url(root, counter);
					if (file == "Not_valid"){
						System.out.println("The file is not valid");
						continue;
					}
					else{
						ret_file(new File(root + file), counter);
					}
					counter++;
				}
			catch (NullPointerException e) {
				System.out.println("Counter: "+ counter);
				System.out.println("Unique words: " + term2termid.size());
	
				FileWriter term2TermIdWriter = new FileWriter("C:\\Users\\Chow Chow\\workspace\\Assignment_3\\term2termid");
				term2TermIdWriter.write(term2termid.toString().replace("],", "]\n"));
				term2TermIdWriter.close();
				FileWriter termId2TermWriter = new FileWriter("C:\\Users\\Chow Chow\\workspace\\Assignment_3\\termid2term");
				termId2TermWriter.write(term2termid.toString().replace("],", "]\n"));
				termId2TermWriter.close();
				FileWriter docId2TermListWriter = new FileWriter("C:\\Users\\Chow Chow\\workspace\\Assignment_3\\docid2termlist");
				docId2TermListWriter.write(docid2termlist.toString().replace("],", "]\n"));
				docId2TermListWriter.close();
				FileWriter term2DocListWriter = new FileWriter("C:\\Users\\Chow Chow\\workspace\\Assignment_3\\term2doclist");
				term2DocListWriter.write(term2doclist.toString().replace("]", "]\n"));
				term2DocListWriter.close();
				term2termid.clear();
				termid2term.clear();
				docid2termlist.clear();
				term2doclist.clear();
				break;
			}
		}*/
			
				//Code for running it 100 times only.
				ArrayList<String> fileList = obtain_url(100);
				int counter = 0;
				for(String file : fileList){
					ret_file(new File(root + file), counter);
					counter++;
				}
			
			FileWriter term2TermIdWriter = new FileWriter("C:\\Users\\Steven\\workspace\\Assignment_3\\term2termid");
			term2TermIdWriter.write(term2termid.toString().replace(",", "\n"));
			term2TermIdWriter.close();
			FileWriter termId2TermWriter = new FileWriter("C:\\Users\\Steven\\workspace\\Assignment_3\\termid2term");
			termId2TermWriter.write(termid2term.toString().replace(",", "\n"));
			termId2TermWriter.close();
			FileWriter docId2TermListWriter = new FileWriter("C:\\Users\\Steven\\workspace\\Assignment_3\\docid2termlist");
			docId2TermListWriter.write(docid2termlist.toString().replace("],", "]\n"));
			docId2TermListWriter.close();
			FileWriter term2DocListWriter = new FileWriter("C:\\Users\\Steven\\workspace\\Assignment_3\\term2doclist");
			term2DocListWriter.write(term2doclist.toString().replace("]", "]\n"));
			term2DocListWriter.close();
			/*
			term2termid.clear();
			termid2term.clear();
			docid2termlist.clear();
			term2doclist.clear();
			*/
			
			/*
			JSONParser parser = new JSONParser();
			Object raw_data = parser.parse(new FileReader("html_files.json"));
			JSONObject jsonObjectData = (JSONObject) raw_data;
			JSONObject realData = (JSONObject) jsonObjectData.get("0");
			String file = (String) realData.get("file");
			System.out.println(file);
			ret_file(new File(root + file));

			JSONArray array_json =  new JSONArray();
			for(Object o : array_json){
				JSONObject document = (JSONObject) o;
				String docFile = (String) document.get("file");
				System.out.println("File: " + docFile);
			}
			*/
			//ret_file(new File("C:\\Users\\Chow Chow\\workspace\\Assignment_3\\Html\\access.ics.uci.educontact.html"));
			//ret_file(new File("C:\\Users\\Chow Chow\\workspace\\Assignment_3\\Html\\access.ics.uci.eduindex.html"));
	}
}

