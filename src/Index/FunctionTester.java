package Index;

import Index.Pair;
import java.util.ArrayList;


public class FunctionTester {

	public static void main(String[] args) {

		ArrayList<Pair<Long, Double>> searchResults = new ArrayList<Pair<Long, Double>>();
		
		searchResults.add(new Pair<Long, Double>(8L, 20D));
		searchResults.add(new Pair<Long, Double>(1L, 2D));
		searchResults.add(new Pair<Long, Double>(2L, 3D));
		searchResults.add(new Pair<Long, Double>(4L, 5D));
		searchResults.add(new Pair<Long, Double>(6L, 7D));
		searchResults.add(new Pair<Long, Double>(3L, 4D));
		searchResults.add(new Pair<Long, Double>(5L, 6D));
		searchResults.add(new Pair<Long, Double>(7L, 10D));
		
		for (int i = 0; i < searchResults.size(); i++) {
		   for (int j = i; j < searchResults.size(); j++) {
			   if (searchResults.get(j).getR() > searchResults.get(i).getR()) {
				   Pair<Long, Double> swap = new Pair<Long,Double>(searchResults.get(j).getL(), searchResults.get(j).getR());
				   searchResults.set(j, searchResults.get(i));
				   searchResults.set(i, swap);
			   }
		   }
		}
		
		for (Pair<Long, Double> i : searchResults) {
			System.out.println(i.getL());
		}
	}
}
