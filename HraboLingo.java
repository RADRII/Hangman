
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
public class HraboLingo
{
	public static Scanner sc= new Scanner(System.in);
	static Random  rand = new Random ( );       
	public static void main (String[] args)
	{
		String [] fives = new String[8883];
		readFivesFromFile(fives);
		// Read from an already existing text file
		String [] wordList = new String[200];
		readWordsFromFile(wordList);
		int [] quit = new int[10];


		//prints the array words if wanted
		//for (int i=0; i<wordList.length; i++)
		//System.out.println(wordList[i]);
		int round = -1;
		String user = welcome();
		String picked = "";
		int score = 0; //declaring all valubles i need outside of the loop
		while(!picked.equalsIgnoreCase("Q"))
		{
			picked = printOptions();

			if(picked.equalsIgnoreCase("I"))
			{
				printInstructions();
			}
			else if(picked.equalsIgnoreCase("P"))
			{
				round = 0;
				int wordAmount = 0;
				
				round = playGame(user, quit, fives, wordList,score); //plays game once through
				score = score + round;
				wordAmount++;

				while(tellIfICanPlayMore(score,quit, wordAmount))//checks if the conditions are met
				{

					if(round == 0) //this means they entered quit so game done
					{

						if(quit[1] == 3) // checks if they are playing with the timer mode
						{
							System.out.println("You were able to make it through " + wordAmount + " words in your set time~!");
						}
						picked = "o"; //makes sure it doesnt go round again, jsut in case
						break;
					}
					else if (tellIfICanPlayMore(score,quit, wordAmount)==false) //this checks if they r playing in word amount mode and if they are over
					{
						System.out.println("You have finished your amount of words.");
						picked = "o";
						break; //just to get out of the loop
					}
					System.out.println("Onto the next word!");
					round = playGame(user, quit, fives, wordList,score); //plays again


					score = score + round;
					wordAmount++;
					tellIfICanPlayMore(score, quit, wordAmount);
				}
				goodBye(user,score);
				System.out.println("Would you like to start over?(Y or N)");
				String playAgain = sc.next();
				if(playAgain.equalsIgnoreCase("Y"))
				{
					score = 0;
					picked = "P"; //justto make sure it cntinues this while loop, just in case
				}
				else
				{
					System.out.println("Bye.");
					break;
				}
			}
			else if(!picked.equalsIgnoreCase("Q"))
				System.out.println("Please pick a valid option");
			if(round == 0&& picked.equalsIgnoreCase("o")) //this means they said no to playing again
				break;
		}



	}
	public static int playGame(String name, int [] quit, String [] fives, String [] wordList, int realS)
	{

		int score = 0;
		int howMany = 0;

		if(realS == 0) //this checks if the score is 0 and if it is gives the user instructions
		{
			int we = 0;
			String type = options(name); //gets wether they want to play infinite, by word amount or timer, also explains instructions for each version
			if(type.equalsIgnoreCase("W"))
			{
				System.out.println("How many words do you want to go through?");
				howMany = sc.nextInt();

			}
			if(type.equalsIgnoreCase("W"))
				we =2;
			if(type.equalsIgnoreCase("T"))
				we =3;

			quit[1] = we; //this array was made in main so it can be accessed in both, first thing is to store what type of game they r playing
			quit[2] = howMany; //second thing holds the amount of words they want to play, nothing if there playing timer or infinitely

		}

		String word = getWord(wordList);
		String guess = "";
		String display = "-----";
		int counter = 0;

		while(counter<8 && !guess.equalsIgnoreCase(word))
		{
			if(counter == 0)
				System.out.println("The first letter is " + word.charAt(0)); //only pritns if this is their first guess
			System.out.println("");
			System.out.print("Guess #" + (counter+1) + " ");
			guess = sc.next();
			guess = guess.toUpperCase(); //so its more user friendly


			ArrayList<String> wrongL = new ArrayList<String>( );  
			String [] guessA = turnIntoArray(guess);
			String [] displayA = turnIntoArray(display);
			String [] wordA = turnIntoArray(word);
			//turns everything into arrays to make things easier

			if(guess.equalsIgnoreCase("quit"))
				break; //brings it back to the main method and doesnt do anything else
			if(checkIfTrueWord(guess,fives))
			{

				checkRightThings(wordA,guessA, displayA, wrongL);
				printOutTheThing(displayA, wrongL);

				emptyOutWrong(wrongL);

			}
			else
			{System.out.println("ERROR: Not a valid word, you lose a guess.");
			}

			counter++;


		}
		if(guess.equals(word))
		{
			System.out.println("Gr8! You have guessed the word!");
			score = score + 10;
			score = score + bonusFinder(counter);

		}
		else if(counter == 0)
			return 0; 
		else if(counter==8)
		{
			System.out.println("You have run out of guesses ;-; take a pity point." );
			System.out.println("The word was " + word);
			score = 1;
		}
		
		System.out.println("Your score for this round is: " + score);


		return score;
	}

	public static int bonusFinder(int counter)
	//determines the amunt of bonus points by seeing how many guesses they took
	{
		int score = 9 - counter;
		return score;

	}
	public static void emptyOutWrong(ArrayList <String> wrong)
	//empties the wrong letter list between rounds
	{
		
		int size = wrong.size();
		int i = 0;

		while(i<size)
		{
			wrong.remove(i);
			i++;
		}

	}
	public static void printOutTheThing(String [] display, ArrayList <String> wrong)
	//prints out correct spot and correct letters
	{

		System.out.print("Correct spot:  " );
		printArray(display);
		System.out.println(" " );
		System.out.println("Correct letters: " + wrong);

	}
	public static boolean isInWord(String letter, String [] word)
	//checks if the letter received is in the word received
	{

		int i = 0;
		while(i<5)
		{
			if(word[i].equals(letter))
			{
				return true;
			}
			i++;
		}
		return false; 
	}
	public static void checkRightThings(String [] word, String [] guess, String [] display, ArrayList <String> wrong)
	//checks both what are the right letters wrong spot and right letter right spot and puts them into arrays
	{
		int i = 0;
		int size = word.length;
		while(i<size)
		{


			if(word[i].equals(guess[i]))
			{
				if(wrong.indexOf(guess[i]) != -1) //checks if its already in the wrong catagory and if it is it takes it out so that its not doublesd
				{
					int place = wrong.indexOf(guess[i]);
					wrong.remove(place);
				}
					
					
				display[i] = guess[i];


			}
			else if(isInWord(guess[i], word))
			{
				
				if(wrong.indexOf(guess[i]) == -1 && isInWord(guess[i], display) == false)//finds out wether it was already placed in wrong letter or if its already in display
					wrong.add(guess[i]);

			}
			i++;
		}

	}

	static public String [] turnIntoArray(String word)
	//turns strings received into array lists with a 1 letter in each
	{
		int size = word.length();
		String [] wordYe = new String[size];
		int i = 0;
		while(i<size)
		{
			wordYe[i] = word.substring(i,i+1);
			i++;
		}
		return wordYe;
	}
	static public void printArray(String numbers[])
	//check the name
	{
		int size=numbers.length;
		for (int i=0;i<size;i++)
			System.out.print(numbers[i] + " "); 

	}

	public static boolean checkIfTrueWord(String guess, String [] fives)
	//checks if the guess user made is a good 5 letter real word
	{
		int i = 0;
		while(i<8883)
		{
			if(guess.equalsIgnoreCase(fives[i]))
				return true;
			i++;
		}
		return false;

	}
	public static String getWord(String [] wordList)
	//gets a word to be the secret word
	{
		int random = rand.nextInt(200);
		String word = wordList[random];
		//System.out.println(word);
		return word;

	}
	public static String options(String user)
	//prints out their options for what type of game they want to play and the user chooses one
	{
		int i = 0;
		String choice = "";
		while(i == 0)
		{
			System.out.println(user + " which version would you like to play?");
			System.out.println("Infinitely: Play forever until you decide to enter 'quit' as your guess(enter I");
			System.out.println("By word amount: You set an amount of words you want to run through and then the game ends(enter W)");
			System.out.println("Timed: Get out your phone and start the timer at the begining, when it hits 2 minutes, enter 'quit' as your guess and the game will end(enter T)");
			System.out.println("Enter your choice:");
			choice = sc.next();
			if(choice.equalsIgnoreCase("I") || choice.equalsIgnoreCase("W") || choice.equalsIgnoreCase("T"))
				return choice;
			else
				System.out.println("Please pick a valid option.");
		}
		return choice;

	}
	public static void goodBye(String user, int score)
	//in the name
	{
		System.out.println("You have finished the game! Your total score was " + score + ", good job " + user + "!");

	}
	public static boolean tellIfICanPlayMore(int score, int [] quit, int wordAmount)
	//checks if all ocnditions are met and if they can play more
	{

		if(quit[1] == 2 && quit[2] <= wordAmount)
		{

			return false;
		}
		return true;


	}
	public static void printInstructions()
	//in the name
	{

		System.out.println("Lingo is a fun guessing game. A random five letter word is picked, and you are only given the first letter in that word. ");
		System.out.println("You will receive 8 guesses to try and get the word, each guess must be a valid five letter word. ");
		System.out.println("After each guess you will receive feedback on wether some letters in your guess were in the right spot, or if you have letters that are in the word but not in the right spot.");
		System.out.println("Every word you get right is ten points, and there are extra points depending on how fast you guess right.");
		System.out.println("Have fun!");



	}
	public static String printOptions()
	//wether they want to pplay, quit, or get isntructions , returns choice
	{
		System.out.println("Would you like to get the instructions (enter I), play the game(enter P), or quit altogether(enter Q).");
		String picked = sc.next();

		return picked;
	}
	public static String welcome()
	{
		System.out.println("Welcome to lingo! Hope you have fun ;)");
		System.out.println("What is your name?");
		String user = sc.next();
		return user;

	}
	public static void readWordsFromFile(String[] wordList)
	{
		// opens a Scanner object to read from the text file of fives
		@SuppressWarnings("resource")
		Scanner sc =new Scanner("");
		File inputFile = new File("./src/myFives.TXT");

		try {
			sc = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//reads every String from the file, and stores in the String array
		int counter=0;
		while (sc.hasNext())
		{
			String s = sc.next();
			wordList[counter]=s;
			counter++;
		}	
	}
	public static void readFivesFromFile(String[] fives)
	{
		// opens a Scanner object to read from the text file of fives
		@SuppressWarnings("resource")
		Scanner sc =new Scanner("");
		File inputFile = new File("./src/Fives.TXT");

		try {
			sc = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//reads every String from the file, and stores in the String array
		int counter=0;
		while (sc.hasNext())
		{
			String s = sc.next();
			fives[counter]=s;
			counter++;
		}	
	}


}