package main.distle;

import static main.distle.EditDistanceUtils.*;
import java.util.Random;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Samuel Richard
 * 
 * AI Distle Player! Contains all logic used to automagically play the game of
 * Distle with frightening accuracy (hopefully).
 */
public class DistlePlayer {
    
    Set<String> words = new HashSet<String>();
    Set<String> filter = new HashSet<String>();
    Set<String> graveyard = new HashSet<String>();
    List<String> moves = new ArrayList<>();
    String lastGuess;
    String firstGuess;
    int max;
    int flag;
    int eDist;
    int wordSize;
    Random rng = new Random();
    
    /**
     * Constructs a new DistlePlayer.
     * Still, you can use this constructor to initialize any fields that need to be,
     * though you may prefer to do this in the {@link #startNewGame(Set<String> dictionary, int maxGuesses)}
     * method.
     */
    public DistlePlayer () {
        
    }
    
    /**
     * Called at the start of every new game of Distle, and parameterized by the
     * dictionary composing all possible words that can be used as guesses / one of
     * which is the correct.
     * 
     * @param dictionary The dictionary from which the correct answer and guesses
     * can be drawn.
     * @param maxGuesses The max number of guesses available to the player.
     */
    public void startNewGame (Set<String> dictionary, int maxGuesses) {
        words.addAll(dictionary);
        max = maxGuesses;
        graveyard.clear();
        lastGuess = null;
        wordSize = 5;
        flag = 0;
        
        if (words.contains("abstrusenesses")) {
            firstGuess = "abstrusenesses";
        } else if (words.contains("mysterious")) {
            firstGuess = "mysterious";
        } else {
            firstGuess = "slater";
        }
        
        
    }
    
    /**
     * Requests a new guess to be made in the current game of Distle. Uses the
     * DistlePlayer's fields to arrive at this decision.
     * 
     * @return The next guess from this DistlePlayer.
     */
    public String makeGuess () {
        String newGuess;
                
        if (flag == 0) {
            newGuess = firstGuess;
            flag++;
        } else {
            newGuess = words.stream().skip(this.rng.nextInt(this.words.size()+1)).findFirst().orElse(null);
        }
        
        lastGuess = newGuess;
        graveyard.add(newGuess);
        return newGuess;
        
    }
    
    /**
     * Called by the DistleGame after the DistlePlayer has made an incorrect guess. The
     * feedback furnished is as follows:
     * <ul>
     *   <li>guess, the player's incorrect guess (repeated here for convenience)</li>
     *   <li>editDistance, the numerical edit distance between the guess and secret word</li>
     *   <li>transforms, a list of top-down transforms needed to turn the guess into the secret word</li>
     * </ul>
     * 
     * @param guess The last, incorrect, guess made by the DistlePlayer
     * @param editDistance Numerical distance between the guess and the secret word
     * @param transforms List of top-down transforms needed to turn the guess into the secret word
     */
    public void getFeedback (String guess, int editDistance, List<String> transforms) {
        Set<String> filter = new HashSet<String>();
        lastGuess = guess;
        eDist = editDistance;
        moves = transforms;

        for (int i = 0; i < moves.size() - 1; i ++) {
            switch(moves.get(i)) {
                case "I":
                    wordSize += 1;
                    break;
                case "D":
                    wordSize -= 1;
                    break;
                default:
                    break;
            }
        }
        
        for (String str: words) {
            if (getTransformationList(guess, str).equals(transforms) && !(graveyard.contains(str))) {
                filter.add(str);
            }
        }

        words.clear();
        words.addAll(filter);

    }
    
}
