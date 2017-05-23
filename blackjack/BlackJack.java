import java.util.Scanner;
import java.util.NoSuchElementException;

public class BlackJack {
	private static final int USER_WINS = 1;
	private static final int DEALER_WINS = -1;
	private static final int DRAW = 0;

	public static void main(String[] args) {
		mainMenu();
		System.out.println("Thanks for playing!");
	}
	
	private static void mainMenu() {
		Scanner keyboard = new Scanner(System.in);
		String cmd;

		System.out.println("Welcome to blackjack!");
		System.out.println("");
		System.out.println("Please select an operation:");
		System.out.println("    start - start a new game");
		System.out.println("    quit  - quit game");
		System.out.println("");

		cmd = keyboard.next().toLowerCase();

		while (!cmd.equals("start") && !cmd.equals("quit")) {
			System.out.println("Invalid command: \"" + cmd + "\". Please check your input.");
			System.out.println("");
			System.out.println("Please select an operation:");
			System.out.println("    start - start a new game.");
			System.out.println("    quit  - quit game.");
			System.out.println("");
			cmd = keyboard.next().toLowerCase();
		}

		if (cmd.equals("start")) {
			startGame();
		}
	}
	
	private static void startGame() {
		Scanner keyboard = new Scanner(System.in);
		Deck deck = new Deck();
		String cmd;
		String name;

		System.out.print("Please choose a name: ");
		name = keyboard.next();
		User user = new User(name);
		System.out.println("Hello, " + name + "!");
		System.out.println();
		User.printUserInfo(user);

		while (true) {
			startRound(deck, user);
			User.printUserInfo(user);

			if (user.getBalance() <= 0) {
				System.out.println();
				System.out.println("You have no money! Game over.");
				System.out.println();
				System.out.println();
				mainMenu();
				return;
			}

			System.out.println();
			System.out.println("Would you like to countinue?");
			cmd = keyboard.next();
			while (!cmd.equals("yes") && !cmd.equals("no")) {
				System.out.println("Invalid command: \"" + cmd + "\". Please check your input.");
				System.out.println();
				System.out.println("Would you like to countinue?");
				cmd = keyboard.next();
			}
			if (cmd.equals("no")) {
				break;
			}
		}
	}

	private static void startRound(Deck deck, User user) {
		Scanner keyboard = new Scanner(System.in);

		int stake = 0;
		boolean valid = false;
		while (!valid) {
			try {
				System.out.println();
				System.out.print("Please set your stake: ");
				stake = keyboard.nextInt();
				if (stake > user.balance || stake <= 0) {
					System.out.println("Invalid amount! Please choose a smaller stake.");
				} else {
					valid = true;
					System.out.println();
				}
			} catch (NoSuchElementException e) {
				System.out.println("Invalid input! Please check your input.");
			}
		}

		User.printStake(user, stake);

		int[] mycards = new int[15];
		clearCards(mycards);

		int newcard1 = deck.nextCard();
		int newcard2 = deck.nextCard();
		if (newcard1 > 10) {
			newcard1 = 10;
		} else if (newcard1 == 1) {
			newcard1 = 11;
		}
		if (newcard2 > 10) {
			newcard2 = 10;
		} else if (newcard2 == 1 && newcard1 != 11) {
			newcard2 = 11;
		}
		mycards[0] = newcard1;
		mycards[1] = newcard2;

		int mytotal = 2;
		int mypoints = newcard1 + newcard2;
		System.out.println("Your cards:");
		printCards(mycards, mytotal, false);
		System.out.println("Your total: " + mypoints);
		System.out.println("");

		int[] dcards = new int[15];
		clearCards(dcards);

		newcard1 = deck.nextCard();
		newcard2 = deck.nextCard();
		if (newcard1 > 10) {
			newcard1 = 10;
		} else if (newcard1 == 1) {
			newcard1 = 11;
		}
		if (newcard2 > 10) {
			newcard2 = 10;
		} else if (newcard2 == 1 && newcard1 != 11) {
			newcard2 = 11;
		}
		dcards[0] = newcard1;
		dcards[1] = newcard2;

		int dtotal = 2;
		int dpoints = newcard1 + newcard2;
		System.out.println("Dealer's cards:");
		printCards(dcards, dtotal, true);
		System.out.println("Dealer's total is hidden.");
		System.out.println("");

		boolean hit = askHit();
		while (hit) {
			int newcard = deck.nextCard();
			if (newcard == 1) {
				if (mypoints + 11 <= 21) {
					newcard = 11;
				}
			}
			if (newcard > 10) {
				newcard = 10;
			}

			mycards[mytotal] = newcard;
			++mytotal;
			mypoints += newcard;
			System.out.println("");
			System.out.printf("You drew a %02d##\n", newcard);
			System.out.println("           ####");
			System.out.println("           ####");
			printCards(mycards, mytotal, false);
			System.out.println("Your total: " + mypoints);
			System.out.println("");

			if (mypoints > 21) {
				System.out.println("You bust.");
				System.out.println("");
				System.out.println("YOU LOSE!");
				System.out.println("");
				user.balance -= stake;
				return;
			}
			hit = askHit();
		}

		System.out.println("Dealer's turn");
		System.out.printf("Dealer's hidden card was a %02d##\n", dcards[0]);
		System.out.println("                           ####");
		System.out.println("                           ####");
		System.out.println("Dealer's cards:");
		printCards(dcards, dtotal, false);
		System.out.println("Dealer's total: " + dpoints);
		System.out.println("");

		while (dpoints <= 16) {
			System.out.println("Dealer chooses to hit.");
			int newcard = deck.nextCard();
			if (newcard == 1) {
				if (mypoints + 11 <= 21) {
					newcard = 11;
				}
			}
			if (newcard > 10) {
				newcard = 10;
			}

			dcards[dtotal] = newcard;
			++dtotal;
			dpoints += newcard;

			System.out.printf("Dealer draws a %02d##\n", newcard);
			System.out.println("               ####");
			System.out.println("               ####");
			System.out.println("Dealer's cards:");
			printCards(dcards, dtotal, false);
			System.out.println("Dealer's total: " + dpoints);
			System.out.println("");

			if (dpoints > 21) {
				System.out.println("Dealer busts.");
				System.out.println("");
				System.out.println("YOU WIN!");
				System.out.println("");
				user.balance += stake;
				return;
			}
		}
		System.out.println("Dealer stays.");
		System.out.println("");
		int result = comparePoints(mypoints, dpoints);
		switch (result) {
		case USER_WINS:
			user.balance += stake;
			break;
		case DEALER_WINS:
			user.balance -= stake;
			break;
		case DRAW:
			break;
		}
	}

	private static void clearCards(int[] cards) {
		for (int i = 0; i < cards.length; ++i) {
			cards[i] = 0;
		}
	}

	private static int sumCards(int[] cards) {
		int sum = 0;
		for (int i : cards) {
			sum += i;
		}
		return sum;
	}

	private static void printCards(int[] cards, int length, boolean hidden) {
		for (int i = 0; i < length; ++i) {
			if (hidden && i == 0) {
				System.out.print("  ####");
			} else {
				System.out.printf("  %02d##", cards[i]);
			}
		}
		System.out.println("");
		for (int i = 0; i < length; ++i) {
			System.out.print("  " + "####");
		}
		System.out.println("");
		for (int i = 0; i < length; ++i) {
			System.out.print("  " + "####");
		}
		System.out.println("");
	}

	private static boolean askHit() {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Would you like to hit or stay?");
		String cmd = keyboard.next();

		while (!cmd.equals("hit") && !cmd.equals("stay")) {
			System.out.println("Invalid command: \"" + cmd + "\". Please check your input.");
			System.out.println("");
			System.out.println("Would you like to hit or stay?");
			cmd = keyboard.next();
		}

		if (cmd.equals("hit")) {
			return true;
		} else {
			return false;
		}
	}

	private static int comparePoints(int mypoints, int dpoints) {
		System.out.println("Your total is " + mypoints + ".");
		System.out.println("Dealer's total is " + dpoints + ".");
		System.out.println("");
		if (mypoints > dpoints) {
			System.out.println("YOU WIN!");
			return USER_WINS;
		} else if (mypoints < dpoints) {
			System.out.println("YOU LOSE!");
			return DEALER_WINS;
		} else {
			System.out.println("DRAW!");
			return DRAW;
		}
	}
}
