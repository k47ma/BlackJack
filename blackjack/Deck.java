

import java.util.Random;

public class Deck {
	private Random r = new Random();
	private int[] played = new int[13];

	Deck() {
		clearDeck();
	}

	public int nextCard() {
		if (emptyDeck()) {
			clearDeck();
		}
		
		boolean redraw = true;
		int card = -1;
		while (redraw) {
			card = r.nextInt(13) + 1;
			redraw = (played[card - 1] / 4 + r.nextDouble()) > 1;
		}
		++played[card - 1];
		return card;

	}

	public boolean emptyDeck() {
		for (int i = 0; i < played.length; ++i) {
			if (played[i] != 4) {
				return false;
			}
		}
		return true;
	}
	
	public void clearDeck() {
		for (int i = 0; i < played.length; ++i) {
			played[i] = 0;
		}
	}
}
