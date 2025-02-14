package cardGame;
/*OOD group/Array Group 5
 * Juan S Hernandez-Corona
 * Eric Almanza
 * 
 * 
 *CPSC-39-12109 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

	private static ArrayList<Card> deckOfCards = new ArrayList<Card>();//deck of cards
	private static ArrayList<Card> playerCards = new ArrayList<Card>();//"inventory" cards for player
	private static ArrayList<Card> dealerCards = new ArrayList<Card>();//"inventory" cards for dealer

	public static void main(String[] args) {
		
		Scanner input = null;//initializing scanner for later use in reading cards.txt
		Scanner scanner = new Scanner(System.in); //scanner for input
		
        boolean retry = true;
        int rounds = 0, losses = 0, wins = 0, checker = 0, tie = 0;
		
        
        while(retry){//while loop to keep playing the game
        	
        	rounds++;
        	dealerCards.clear();//clears 
        	playerCards.clear();//clears
        	deckOfCards.clear();//clears just incase repeating makes the arraylist insanely massive
        	
			try {//prevents io error
				input = new Scanner(new File("here"));//use your path where "here" is
			} 
			catch (FileNotFoundException e) {
				// error
				System.out.println("error");
				e.printStackTrace();
			}
	
			while(input.hasNext()) {//reads the whole card text document to add every card within the doc
				String[] fields  = input.nextLine().split(",");//looks to be a way to filter through the text file
				Card newCard = new Card(fields[0], fields[1].trim(), Integer.parseInt(fields[2].trim()), fields[3]);
				deckOfCards.add(newCard);//now every card is part of the arraylist (object array)	
			}
			input.close();//closing the scanner, not needed anymore
			
			
			shuffle();//calling shuffle method to randomize the deckOfCards arraylist
	

			//deal the player 2 cards, the dealer two
			while (playerCards.size() == 0) {//using methods by refrencing the arraylist to clean up and add conditions. 
			initialCards(playerCards);
			}
			while ( dealerCards.size() == 0) {//while loop prevents the game from starting is it somehow clears the and passes through (my code bug workaround)
			initialCards(dealerCards);
			}
			
			System.out.println("Players cards");//this section displays the cards
			
			for(int i = 0; i < playerCards.size(); i++) {//pulls out from the deckOfCards arraylist to give to playerCards arraylist
				Card newCard = playerCards.get(i);
				newCard.printCard();
			}
			
			System.out.println("============================");//visual separator in the console
			System.out.println("Dealers Cards");
			
			for(int i = 0; i < dealerCards.size(); i++) {//pulls out from the deckOfCards arraylist to give to dealerCards arraylist
				Card newCard = dealerCards.get(i);
				newCard.printCard();
			}
			
			int playerTotal, dealerTotal ;//new variables introduced here to help keep track easier
			
			
			playerTotal = getDeckAmount(playerCards);// called method to count current deck and return a value. it it initial with no backpacing so we can just have it set the value rather than add on
			
			playerTotal = playerTurn(scanner, playerTotal);
			dealerTotal = getDeckAmount(dealerCards);
			if (playerTotal <= 21) {
			
			dealerTotal = dealerTurn(dealerTotal);
			}
			 
	        if (playerTotal > 21 && dealerTotal > 21) {// Determines the if you lost by exceeding 21 by checking the condition if playerTotal is greater than 21. (21, infinity)
	            System.out.println("You busted with total  " + playerTotal + "! Dealer wins with total " + dealerTotal + "!");// print "You busted! Dealer wins." followed by a new line
	            losses++;
	            
	            
	            //return;// returns nothing, changed to break, it seems to avoid situation of you win but also lost scenarios. does it return/loop to main?
	        }
	        else if(playerTotal < 21 && dealerTotal > 21) {

	            System.out.println("dealer busted with total  " + dealerTotal + "! You win.");// print "You busted! Dealer wins." followed by a new line
	            wins++;
	        }
	        else {//added
	        	//dealerTotal = getDeckAmount(dealerCards);// gives the game commands to hit or stand by calling this method
	        	checker = determineWinner(playerTotal, dealerTotal);
	        	switch (checker){
		        	case 1://win
		        		wins++;
		        		break;
		        	case 2://tie
		        		tie++;
		        		break;
		        	case 3://losses
		        		losses++;
		        		break;
	        	}
	        }
			//add code here to finish the game ex: System.out.println("Would you like to draw another card?");
	        System.out.println("Session wins: " + wins);
	        System.out.println("Session ties: " + tie);
	        System.out.println("Session losses: " + losses);
	        
	        
	        
	        
	        
	        while (true) {//loop to check for correct input -------------------------NEW CODE -- replay with output to win/loss/tie
	        System.out.println("play again? Y/N");
	        String action = scanner.nextLine().toLowerCase();
	        if (action.equals("y")) {//.equals is for strings, acts like ==
	            break;
	        } else if (action.equals("n")) {
	            retry = false;
	            System.out.print("SESSION ENDED");
	            break;
	        } else {
	            System.out.println("Invalid action. Please type 'hit' or 'stand'.");
	        }//----------------------------------------------------------------------END OF NEW CODE
	
	        scanner.close();//closes scanner for no resource leak, good practice
	        }
	
			
        }	
	}//end main
	
	private static int dealerTurn(int dealersTotal) {//similar to player turn without needing to print as much or have commands
	    	int i = 0;
	    	Card currCard = new Card();
	    	while (dealersTotal < 17) {
	    		i++;
				dealerCards.add(deckOfCards.remove(i));//removes while adding a new card to playerCards 
	           	dealersTotal = getDeckAmount(dealerCards);
	           	currCard = dealerCards.get(dealerCards.size() - i);
	           	System.out.print("Dealer drew a " );
	           	currCard.printCard();
	        }
	        //System.out.println("Dealer's total is " + dealersTotal);
	        return dealersTotal;
	    }
    private static int determineWinner(int playerTotal, int dealerTotal) {//completes the game by checking values//modified to return 3 values
            if ( playerTotal > dealerTotal && playerTotal < 22 && dealerTotal != playerTotal ) {//if dealer total is greater than 21 or player total is greater than dealer, prints "You win!""
                System.out.println("You win!" + dealerTotal);
                return 1;
            } else if (dealerTotal == playerTotal) {//if both player and dealer have the same amount then its a tie
                System.out.println("It's a tie!");
                return 2;
            } else {//else if none is correct you lost to dealer
                System.out.println("Dealer wins with " + dealerTotal + "!");
                return 3;
            }
        }
        
        
    private static int playerTurn(Scanner scanner, int playerTotal) {//method for players turn
        int i = 1 ;
        while (true) {//while loop to repeat, always true as the conditions will break the loop themselves
        	//variables
        	Card currCard = new Card();//think of it as an empty variable but its an object instead of that primitive stuff
        	
            System.out.println("Your total is " + playerTotal + ". Do you want to hit or stand?");
            String action = scanner.nextLine().toLowerCase();// Makes the input lower case In CaSe ThE UsEr TyPes LiKe tHiS
            if (action.equals("hit")) {//.equals is for strings, acts like ==
            	 
				playerCards.add(deckOfCards.remove(i));//removes while adding a new card to playerCards 
            	playerTotal = getDeckAmount(playerCards);
            	currCard = playerCards.get(playerCards.size() - 1);
            	
            	System.out.print("You Drew a " );
            	currCard.printCard();
            	
                if (playerTotal > 21) {
                    break;//breaks the loop
                }
            } 
            else if (action.equals("stand")) {
                break;
            } 
            else {
                System.out.println("Invalid action. Please type 'hit' or 'stand'.");
            }
            i++;
        }
        return playerTotal;//returns this value 
    }
	
        
        
    private static int getDeckAmount(ArrayList<Card> Cards2) {//method for deck size
        int count = 0;
        
    	for (int i = 0; i < Cards2.size(); i++) { //for loop for counting cards amount by creating card obj to get primitive value
    		Card countCard;
    		countCard = Cards2.get(i);
    		count = countCard.getCardNumber() + count;
    		//System.out.println("TEST"+count);
    		//countCard.printCard();
    	}
    	return count;
    }
	
	
	public static void shuffle() {//shuffle method to randomized the deckOfCards arraylist.
		System.out.println("Shuffling");
		//shuffling the cards by deleting and reinserting
		for (int i = 0; i < deckOfCards.size(); i++) {
			int index = (int) (Math.random()*deckOfCards.size());
			Card c = deckOfCards.remove(index);
			//System.out.println("c is " + c + ", index is " + index);
			deckOfCards.add(c);
			Card newCard = deckOfCards.get(i);
			//newCard.printCard();
		}
		//System.out.println("Shuffling has ended");
	}
	
	public static void initialCards(ArrayList<Card> playerCards2) {
		int count = 0;
		int i = 0;
		playerCards2.add(deckOfCards.remove(0));
		playerCards2.add(deckOfCards.remove(1));
		
		while (i < 2) {
			
		for (int p = 0; p < playerCards2.size(); p++) {
			Card currCard = new Card();
			currCard = playerCards2.get(i);
			count = currCard.getCardNumber() + count;
			//System.out.print("current count: " + count);
		}
		if (count > 21 && count != 0) {
			i = 0;
			playerCards2.clear();
			count = 0;
			}
		else {
			//System.out.println("intitial card method break activated: ");
			break;
		}
		}
		
	}
	
	
	/*check for 2 of a kind in the players hand. we dont need this method for the game, as the game is for a black replica
	public static boolean checkFor2Kind() {

		int count = 0;
		for(int i = 0; i < playerCards.size() - 1; i++) {
			Card current = playerCards.get(i);
			Card next = playerCards.get(i+1);
			
			for(int j = i+1; j < playerCards.size(); j++) {
				next = playerCards.get(j);
				//System.out.println(" comparing " + current);
				//System.out.println(" to " + next);
				if(current.equals(next))
					count++;
			}//end of inner for
			if(count == 1)
				return true;

		}//end outer for
		return false;
	}*/
}//end class
