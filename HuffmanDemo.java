import java.io.IOException;
public class HuffmanDemo { //Huffman demo class
	public static void main(String []args) throws IOException {
		Huffman.encode(); //calling encode method
		System.out.println("Printing encoded text to Encoded.txt");
		Huffman.decode(); //calling decode method
		System.out.println("Printing decoded text to Decoded.txt");
	}
}
