package Index;

public class FunctionTester {

	public static void main(String[] args) {
		String tester = "ab34#$#";
		String alphaAndDigits = tester.replaceAll("[^a-zA-Z0-9]+","");
		System.out.println("TESTER:" + tester);
		System.out.println(alphaAndDigits);
	}

}
