

package wordladder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.*;

public class Main {
	static ArrayList<String> wordladder = new ArrayList<String>();
	// static variables and constants only here.

	public static void main(String[] args) throws Exception {

		Scanner kb; // input Scanner for commands
		PrintStream ps; // output file
		// If arguments are specified, read/write from/to files instead of Std
		// IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps); // redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out; // default to Stdout
		}

		wordladder = parse(kb);
		if (wordladder.size()==0) {
			return;
		}

		initialize();
		printLadder(getWordLadderDFS(wordladder.get(0), wordladder.get(1)));
		System.out.println("");
		printLadder(getWordLadderBFS(wordladder.get(0), wordladder.get(1)));

		// TODO methods to read in words, output ladder
	}

	public static void initialize() {

		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests. So call it
		// only once at the start of main.
	}

	/**
	 * This functions checks if one of the arguments is /quit and will return an empty arrayList and if 
	 * it does not contain /quit it will return an arrayList with both arguments
	 *
	 * @param keyboard
	 *            Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. If
	 *         command is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		// TO DO
		ArrayList<String> kb = new ArrayList<String>();
		kb.add(keyboard.next().toLowerCase());

		if (kb.contains("/quit")) {
			ArrayList<String> emptylist = new ArrayList<String>();
			return emptylist;
		} else {
			kb.add(keyboard.next().toLowerCase());
			if (kb.contains("/quit")) {
				ArrayList<String> emptylist = new ArrayList<String>();
				return emptylist;
			}

		}
		return kb;
	}
/**
 * This function returns the DFS Ladder and if the DFS ladder is null then they add the start and the end word
 * @param start is the starting String for the word ladder
 * @param end is the ending String for the word ladder
 * @return ArrayList getWordLadderDFS which contains the start and the end word if there is no word ladder and
 * the full ladder if a word ladder exists between start and end.
 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		Set<String> Dictionary = makeDictionary();


		ArrayList<String> beentolist = new ArrayList<String>();
		beentolist.add(start);// add the start word to the been to arraylist and passes it as a paramater with the start word, end word and dictionary.
		beentolist = DFS(start, end, beentolist, Dictionary);
		if(beentolist == null){ // there is no word ladder so we add the start word and end word and return the arraylist
			beentolist = new ArrayList<String>();
			beentolist.add(start.toLowerCase());
			beentolist.add(end.toLowerCase());}
		return beentolist;
		
	}
	
/**
 * This function returns the BFS Ladder and if the BFS ladder is null then they add the start and the end word
 * @param start is the starting String for the word ladder
 * @param end is the ending String for the word ladder
 * @return ArrayList getWordLadderBFS which contains the start and the end word if there is no word ladder and
 * the full ladder if a word ladder exists between start and end.
 */
	public static ArrayList<String> getWordLadderBFS(String start, String end) {

		Set<String> Dictionary = makeDictionary();
		Queue<Node> words = new LinkedList<Node>(); // this is the queue
		Node first = new Node();
		first.storeword = start;
		words.add(first);
		int wordlength = start.length();
		boolean queuechecker = words.isEmpty();
		int x = 0;
		int y = 0;

		while (!queuechecker) { // this continues the loop as long as the queue is not empty
			Node addtoqueue = new Node();
			addtoqueue = words.remove();
			String freshword = addtoqueue.storeword;

			addtoqueue.storeladder.add(freshword);

			if (freshword.equals(end)) { // have a check if the word removed from the queue is the word we are looking for

				

				// return the ladder if the word removed from the queue is the end word

				return addtoqueue.storeladder;

			}
			

			StringBuilder wordusing = new StringBuilder(freshword); // make the word a StringBuilder in order to change the characters of the String

			Dictionary.remove(wordusing.toString().toUpperCase());
			while (x < wordlength) {
				while (y < 26) {
					wordusing.setCharAt(x, (char) ('a' + y));
					if (Dictionary.contains(wordusing.toString().toUpperCase())) {//check if the changed StringBuilder is in the dictionary
						Node addword = new Node(); // create a new node for the word if it is in the dictionary
						addword.storeword = wordusing.toString();
						addword.storeladder = new ArrayList<String>(addtoqueue.storeladder);
						words.add(addword); // add the word to the queue because it is in the dictionary
						Dictionary.remove(wordusing.toString()); // remove the word from the dictionary so we do not repeat words

					}

					wordusing = new StringBuilder(freshword); // this will have the word reset if they start to change the letters of the next index
					y++;

				}

				x++;
				y = 0;

			}

			x = 0;
			queuechecker = words.isEmpty(); // we check again if the queue is empty if it is we terminate the loop if not it continues

		}
		ArrayList<String> emptylist = new ArrayList<String>(); // once the BFS is done and if there is no wordladder we add the start and the end word
		emptylist.add(start.toLowerCase());
		emptylist.add(end.toLowerCase());
		return emptylist; // return emptylist which contains the start and the end word

	}

	public static Set<String> makeDictionary() {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
/**
 * This function prints either the DFS or BFS word ladder
 * @param ladder is the ArrayList<String>  which contains the word ladder between two words
 */
	public static void printLadder(ArrayList<String> ladder) {
		if (ladder.size() <=2 ) { //prints there is no ladder if there is no ladder or if the ladder is just the start word and end word
			System.out.print("no word ladder can be found between " + ladder.get(0) + " and " + ladder.get(1));

		} else { // if a word ladder exists we print each word in the ladder ArrayList
			System.out.println("a " + (ladder.size()) + "-rung word ladder exists between " + ladder.get(0) + " and "
					+ ladder.get(ladder.size()-1));
			for (String s : ladder) {
				System.out.println(s);
			}
		}
	}
	
/**
 * This function changes the start word and checks if its in the dictionary. If it does, it passes that 
 * word as a paramter and is recursively call till it finds the end word.if the new word does not have a path 
 * to the end word it will return null.
 * @param start is the starting String for the word ladder
 * @param end is the ending String for the word ladder
 * @param visited is an ArrayList<String> which keeps track of all the words visited and if it does not have a 
 * path to a new word or the end word, it will remove it from the visited array list
 * @param Dictionary is a Set<String> which is being passed recursively so we do not repeat the words being passed 
 * as a paramter recursively
 * @return is an ArrayList<String> which returns either null if there is no word ladder and the word ladder if there
 * is a word ladder from the start word to the end word.
 */
	public static ArrayList<String> DFS(String start, String end, ArrayList<String> visited, Set<String> Dictionary) {

		Dictionary.remove(start.toUpperCase()); // removes the start word from the dictionary so we do not find it again
		if (start.equals(end)) {// checks if the start word is the end word
			return visited;

		}

		for (int x = 0; x < start.length(); x++) {
			StringBuilder startword = new StringBuilder(start);
			for (int y = 0; y < 26; y++) {

				startword.setCharAt(x, (char) ('a' + y));// changes the string such that it looks for a one letter difference of the word
				if (Dictionary.contains(startword.toString().toUpperCase())// checks the dictionary if that one letter difference of the startword is in the dictionary or has been visited
						&& !visited.contains(startword.toString())) {

					visited.add(startword.toString());// add the newly changed string to the visited list
					Dictionary.remove(startword.toString().toUpperCase());//removes the  newly changes string form the dictionary

					ArrayList<String> visitladder = new ArrayList<String>();
					visitladder = DFS(startword.toString(), end, visited, Dictionary); // calls itself on the newly changed function (recursion)
					if (visitladder == null) {// if the newly changed word does not ever reach the end word recursively it will return null
						visited.remove(startword.toString()); // returning null will remove the newly changed word from the visited list

					} else {
						
						return visitladder; // if the word is eventually found it will return the arraylist which will contain the full ladder
					}

				}

			}

		}
		return null; // this will return null if the start word does not ever reach a one letter difference word in the dictionary

	}

}
