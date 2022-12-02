package main.distle;

import java.util.*;

public class EditDistanceUtils {
    
    /**
     * Samuel Richard
     * 
     * Returns the completed Edit Distance memoization structure, a 2D array
     * of ints representing the number of string manipulations required to minimally
     * turn each subproblem's string into the other.
     * 
     * @param s0 String to transform into other
     * @param s1 Target of transformation
     * @return Completed Memoization structure for editDistance(s0, s1)
     */
    public static int[][] getEditDistTable (String s0, String s1) {

        int[][] table = new int[s0.length()+1][s1.length()+1];
        int num = 1000;
        
        table[0][0] = 0;

        for (int i = 1; i < s0.length()+1; i++) {
            table[i][0] = i;
        }
        for (int j = 1; j < s1.length()+1; j++) {
            table[0][j] = j;
        }
        
        
        for (int i = 1; i < s0.length()+1; i++) {
            for (int j = 1; j < s1.length()+1; j++) {
               
                if (s0.charAt(i-1) == s1.charAt(j-1)) {
                    num = table[i-1][j-1] - 1; //Equal Letters
                } else {
                    num = 1000;
                    if (table[i-1][j-1] < num) {
                        num = table[i-1][j-1]; //Replacement
                    }
                    if (i > 1 && j > 1 && s0.charAt(i-1) == s1.charAt(j-2) && s0.charAt(i-2) == s1.charAt(j-1)) {
                        if (table[i-2][j-2] < num) {
                            num = table[i-2][j-2]; //Transposition
                        }
                    } 
                    if (table[i][j-1] < num){
                        num = table[i][j-1];//Insertion
                    }
                    if (table[i-1][j] < num) {
                        num = table[i-1][j];//Deletion
                    }
                    
                }
                table[i][j] = num + 1;
            }
        }
                
        return table;
    }
    
    /**
     * Returns one possible sequence of transformations that turns String s0
     * into s1. The list is in top-down order (i.e., starting from the largest
     * subproblem in the memoization structure) and consists of Strings representing
     * the String manipulations of:
     * <ol>
     *   <li>"R" = Replacement</li>
     *   <li>"T" = Transposition</li>
     *   <li>"I" = Insertion</li>
     *   <li>"D" = Deletion</li>
     * </ol>
     * In case of multiple minimal edit distance sequences, returns a list with
     * ties in manipulations broken by the order listed above (i.e., replacements
     * preferred over transpositions, which in turn are preferred over insertions, etc.)
     * @param s0 String transforming into other
     * @param s1 Target of transformation
     * @param table Precomputed memoization structure for edit distance between s0, s1
     * @return List that represents a top-down sequence of manipulations required to
     * turn s0 into s1, e.g., ["R", "R", "T", "I"] would be two replacements followed
     * by a transposition, then insertion.
     */
    public static List<String> getTransformationList (String s0, String s1, int[][] table) {

        List<String> moves = new ArrayList<>();
        String move;
        int num = 1000;
        int distance = editDistance(s0, s1);
        int r = s0.length();
        int c = s1.length();
        
        while (distance > 0) {
            move = "";
            if (r != 0 && c != 0 && s0.charAt(r-1) == s1.charAt(c-1)) {
                r -= 1;
                c -= 1;
            } else {
                num = 1000;
                if (r > 0 && c > 0) {
                    if (table[r-1][c-1] < num) {
                        num = table[r-1][c-1];
                        move = "R";
                    }
                    
                }
                if (r > 1 && c > 1 && s0.charAt(r-1) == s1.charAt(c-2) && s0.charAt(r-2) == s1.charAt(c-1)) {
                    if (table[r-2][c-2] < num) {
                        num = table[r-2][c-2];
                        move = "T";
                    }
                } 
                if (c > 0) {
                    if (table[r][c-1] < num) {
                        num = table[r][c-1];
                        move = "I";
                    }
                }
                if (r > 0) {
                    if (table[r-1][c] < num) {
                        num = table[r-1][c];
                        move = "D";
                    }
                }
                
                switch(move) {
                    case "R":
                        r -= 1;
                        c -= 1;
                        break;
                    case "T":
                        r -= 2;
                        c -= 2;
                        break;
                    case "I":
                        c -= 1;
                        break;
                    case "D":
                        r -= 1;
                        break;
                    default:
                        break;
                }
                
                distance -= 1;
                moves.add(move);
            }
            
        }
        
        return moves;
    }
    
    /**
     * Returns the edit distance between the two given strings: an int
     * representing the number of String manipulations (Insertions, Deletions,
     * Replacements, and Transpositions) minimally required to turn one into
     * the other.
     * 
     * @param s0 String to transform into other
     * @param s1 Target of transformation
     * @return The minimal number of manipulations required to turn s0 into s1
     */
    public static int editDistance (String s0, String s1) {
        if (s0.equals(s1)) { return 0; }
        return getEditDistTable(s0, s1)[s0.length()][s1.length()];
    }
    
    /**
     * See {@link #getTransformationList(String s0, String s1, int[][] table)}.
     */
    public static List<String> getTransformationList (String s0, String s1) {
        return getTransformationList(s0, s1, getEditDistTable(s0, s1));
    }

}
