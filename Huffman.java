import java.util.*;
import java.io.*;
public class Huffman {
	public static void encode() throws IOException{
		Scanner kb = new Scanner(System.in);
		System.out.println("Enter the filename to read from/encode: ");
		String textFile = "";
		File file = new File(kb.next());
		//kb.close();
		Scanner inputFile = new Scanner(file);
		while(inputFile.hasNext()) {
			textFile += inputFile.next();
		}
		inputFile.close();
		int[] freq = new int[256];
		char[] chars = textFile.toCharArray();
		for(char c: chars) { //goes through chars array
			freq[c] ++; //increments the correct index in freq array  
		}
		ArrayList<Pair> list = new ArrayList<Pair>(); //list to hold the pairs
		ArrayList<BinaryTree<Pair>> S = new ArrayList<BinaryTree<Pair>>(); //queues
		ArrayList<BinaryTree<Pair>> T = new ArrayList<BinaryTree<Pair>>(); // ^
		for(int i = 0; i< freq.length;i++) {
			Pair pair = new Pair((char) i,Math.round(freq[i]*10000d/chars.length)/10000d);
			if(pair.getProb() >0) { //only add characters that occur in the text
				list.add(pair);
			}
		}
		Collections.sort(list); //sorts list based on overrided compareto  method
		for(Pair p : list) { //goes through the list creates new binarytrees with pairs
			BinaryTree<Pair> tree = new BinaryTree<Pair>();
			tree.makeRoot(p);
			S.add(tree);
		}
		BinaryTree<Pair> A = new BinaryTree<Pair>();
		BinaryTree<Pair> B = new BinaryTree<Pair>();
		while(!S.isEmpty()) { //while the first queue isnt empty
			if (T.isEmpty()){ //if second queue is empty
				A = S.remove(0); //take both trees from first queue
				B = S.remove(0);
			}
			else { //second queue isnt empty
				if(S.get(0).getData().getProb()< T.get(0).getData().getProb()) { //if smallest probability of first queue is smaller than the smallest in the second
					A = S.remove(0); //dequeue
				}
				else {
					A = T.remove(0); //dequeue second queue
				}
				if(T.isEmpty()) {//if second queue is empty
					B= S.remove(0); 
				}
				else if (S.isEmpty()) { //if first queue is empty
					B = T.remove(0);
				}
				else{
					if(S.get(0).getData().getProb()< T.get(0).getData().getProb()) {//if smallest probability of first queue is smaller than the smallest in the second
						B = S.remove(0); //dequeue first queue
					}
					else {
						B = T.remove(0); //dequeue second queue
					}
				}
			}
			BinaryTree<Pair> P = new BinaryTree<Pair>(); //new tree
			Pair temp = new Pair('⁂'  ,A.getData().getProb() + B.getData().getProb()); //new pair with combined probability
			P.makeRoot(temp); //makes new tree roiot from new pair
			P.attachLeft(A); P.attachRight(B);B.setParent(P); A.setParent(P); //sets relationships
			T.add(P); //add new tree to second queue
		}
		while(T.size()>1) { //when first queue is 
			A = T.remove(0); //dequeue 2 from second queue
			B = T.remove(0);
			BinaryTree<Pair> P = new BinaryTree<Pair>(); //new tree
			Pair temp = new Pair('⁂'  ,A.getData().getProb() + B.getData().getProb()); //combines their 2 probabilities
			P.makeRoot(temp); //root
			P.attachLeft(A); P.attachRight(B);B.setParent(P); A.setParent(P); //relationships
			T.add(P);//adds to second queue
		}
		BinaryTree<Pair> finalTree = T.remove(0); //root tree
		String[] huffman = findEncoding(finalTree); //creates array using findencoding method
		PrintWriter output = new PrintWriter("Huffman.txt"); //new printwriter
		output.println("Symbol Prob.	Huffman		Code");
		while(!list.isEmpty()) { //goes through list and displays character, probability, and huffman code
			output.format("%-16s%-16f%-16s\n",list.get(0).getValue(),list.get(0).getProb(),huffman[list.remove(0).getValue()]);
		}
		output.close();
		PrintWriter encoded = new PrintWriter("Encoded.txt");	 //new printwriter	
		for(char c: chars) { //goes through chars array
			encoded.print(huffman[c]); //printwrites each characters code
		}
		encoded.close();
	}
	private static String[] findEncoding(BinaryTree<Pair> bt) {
		String[] result = new String[256];
		 findEncoding(bt, result, "");
		 return result;
	}
	private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix){
		 if (bt.getLeft()==null && bt.getRight()==null){ //base case
			 a[bt.getData().getValue()] = prefix;
		 }
		 else{ //glue
			 findEncoding(bt.getLeft(), a, prefix+"0");
			 findEncoding(bt.getRight(), a, prefix+"1");
		 }
	}
	public static void decode()throws IOException{
		Scanner kb = new Scanner(System.in);
		System.out.println("Enter the filename to read from/decode:");
		File file = new File(kb.nextLine());
		Scanner encoded = new Scanner(file);
		String text = ""; //empty text to hold encoded message
		while(encoded.hasNext()) {
			text+= encoded.next(); //string with no spaces from input file
		}
		text.replaceAll("\\s",  ""); //gets rid of all spaces
		encoded.close();
		System.out.println("Enter the filename of document containing Huffman codes:");
		File file2 = new File(kb.nextLine());		
		Scanner ls = new Scanner(file2);
		ls.nextLine();
		// consume/discard header row and blank line
		ArrayList<Character> symbol = new ArrayList<Character>(); //arraylist to hold symbols
		ArrayList<String> codes = new ArrayList<String>(); //arraylist to hold codes for symbols. the index of each arraylist is the same(ie index 4 in the symbol arraylist is the symbol for the code at index 4 of the other list
		while(ls.hasNext()){
			char c = ls.next().charAt(0); //gets character 
			ls.next(); // consume/discard probability
			String s = ls.next(); //gets code
			symbol.add(c); //adds to list
			codes.add(s); //""
		}
		ls.close();
		PrintWriter decoded = new PrintWriter("Decoded.txt"); //new printwriter
		String temp = ""; //temp string to hold codes
		for(int i=0; i< text.length();i++) { //iterates through encoded text
			temp += text.charAt(i); //adds next character to test string
			if(codes.contains(temp)){// if the code is contained in the codes arraylist			
				decoded.print(symbol.get(codes.indexOf(temp))); //prints the correct symbol from the symbols arraylist
				temp = ""; //once the code is used, start again
			}
		}
		decoded.close();
	}
}
