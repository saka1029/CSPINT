package test.jp.saka1029.cspint.sequential.puzzle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

/**
 * The following version of the puzzle appeared in Life International in 1962:
 * 
 *  1. There are five houses.
 *  2. The Englishman lives in the red house.
 *  3. The Spaniard owns the dog.
 *  4. Coffee is drunk in the green house.
 *  5. The Ukrainian drinks tea.
 *  6. The green house is immediately to the right of the ivory house.
 *  7. The Old Gold smoker owns snails.
 *  8. Kools are smoked in the yellow house.
 *  9. Milk is drunk in the middle house.
 * 10. The Norwegian lives in the first house.
 * 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
 * 12. Kools are smoked in the house next to the house where the horse is kept.
 * 13. The Lucky Strike smoker drinks orange juice.
 * 14. The Japanese smokes Parliaments.
 * 15. The Norwegian lives next to the blue house.
 * 
 * Now, who drinks water? Who owns the zebra?
 * 
 * Color        : Red         Yellow        Blue        Ivory         Green
 * Nationality  : Englishman  Spaniard      Ukrainian   Norwegian     Japanese
 * Drink        : Coffee      Water         Tea         Milk          Orange juice  
 * Smoke        : Kools       Chesterfield  Old Gold    Lucky Strike  Parliament
 * Pet          : Dog         Fox           Horse       Snails        Zebra
 * 
 */
public class TestEinsteinPuzzle {

    static final Logger logger = Logger.getLogger(TestEinsteinPuzzle.class.toString());

    static final int NUM_HOUSES = 5;
    static final int NUM_ATTRIBUTES = Attribute.values().length;
    static final int NUM_VALUES = 5;

    enum Attribute { Color, Nationality, Drink, Smoke, Pet }
    enum Color { Red, Yellow, Blue, Ivory, Green }
    enum Nationality { Englishman, Spaniard, Ukrainian, Norwegian, Japanese }
    enum Drink { Coffee, Water, Tea, Milk, OrangeJuice }
    enum Smoke { Kools, Chesterfield, OldGold, LuckyStrike, Parliament }
    enum Pet { Dog, Fox, Horse, Snails, Zebra }

    static String name(int i, int j) { return i + "@" + Attribute.values()[j]; }
    
    static Variable[][] selectColumn(Variable[][] matrix, int... selections) {
        int rows = matrix.length;
        int cols = selections.length;
        Variable[][] result = new Variable[rows][cols];
        for (int r = 0; r < rows; ++r)
            for (int c = 0; c < cols; ++c)
                result[r][c] = matrix[r][selections[c]];
        return result;
    }

    @Disabled
    @Test
    void test() {
        Problem p = new Problem();
        Domain d = Domain.range(0, NUM_VALUES);
        Variable[][] v = new Variable[NUM_HOUSES][NUM_ATTRIBUTES];
        for (int i = 0; i < NUM_HOUSES; ++i)
            for (int j = 0; j < NUM_ATTRIBUTES; ++j)
                v[i][j] = p.variable(name(i, j), d);
        p.allDifferentEachColumns(v);
        //  2. The Englishman lives in the red house.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[Attribute.Nationality.ordinal()] == Nationality.Englishman.ordinal()
                && r[Attribute.Color.ordinal()] == Color.Red.ordinal()), v);
        //  3. The Spaniard owns the dog.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[Attribute.Nationality.ordinal()] == Nationality.Spaniard.ordinal()
        		&& r[Attribute.Pet.ordinal()] == Pet.Dog.ordinal()), v);
        //  4. Coffee is drunk in the green house.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[Attribute.Drink.ordinal()] == Drink.Coffee.ordinal()
        		&& r[Attribute.Color.ordinal()] == Color.Green.ordinal()), v);
        //  5. The Ukrainian drinks tea.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[Attribute.Nationality.ordinal()] == Nationality.Ukrainian.ordinal()
        		&& r[Attribute.Drink.ordinal()] == Drink.Tea.ordinal()), v);
        //  6. The green house is immediately to the right of the ivory house.
        p.constraint(a -> IntStream.range(1, NUM_HOUSES)
            .anyMatch(i -> a[i][Attribute.Color.ordinal()] == Color.Green.ordinal()
                && a[i - 1][Attribute.Color.ordinal()] == Color.Ivory.ordinal()), v);
        //  7. The Old Gold smoker owns snails.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[Attribute.Smoke.ordinal()] == Smoke.OldGold.ordinal()
        		&& r[Attribute.Pet.ordinal()] == Pet.Snails.ordinal()), v);
        //  8. Kools are smoked in the yellow house.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[Attribute.Smoke.ordinal()] == Smoke.Kools.ordinal()
        		&& r[Attribute.Color.ordinal()] == Color.Yellow.ordinal()), v);
        //  9. Milk is drunk in the middle house.
        p.constraint(x -> x == Drink.Milk.ordinal(), v[2][Attribute.Drink.ordinal()]);
        // 10. The Norwegian lives in the first house.
        p.constraint(x -> x == Nationality.Norwegian.ordinal(), v[0][Attribute.Nationality.ordinal()]);
        // 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
        p.constraint(a -> IntStream.range(1, NUM_HOUSES)
            .anyMatch(i -> a[i][Attribute.Smoke.ordinal()] == Smoke.Chesterfield.ordinal()
                && a[i - 1][Attribute.Pet.ordinal()] == Pet.Fox.ordinal()
                || a[i][Attribute.Pet.ordinal()] == Pet.Fox.ordinal()
                && a[i - 1][Attribute.Smoke.ordinal()] == Smoke.Chesterfield.ordinal()) , v);
        // 12. Kools are smoked in the house next to the house where the horse is kept.
        p.constraint(a -> IntStream.range(1, NUM_HOUSES)
            .anyMatch(i -> a[i][Attribute.Smoke.ordinal()] == Smoke.Kools.ordinal()
                && a[i - 1][Attribute.Pet.ordinal()] == Pet.Horse.ordinal()
                || a[i][Attribute.Pet.ordinal()] == Pet.Horse.ordinal()
                && a[i - 1][Attribute.Smoke.ordinal()] == Smoke.Kools.ordinal()) , v);
        // 13. The Lucky Strike smoker drinks orange juice.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[Attribute.Smoke.ordinal()] == Smoke.LuckyStrike.ordinal()
        		&& r[Attribute.Drink.ordinal()] == Drink.OrangeJuice.ordinal()), v);
        // 14. The Japanese smokes Parliaments.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[Attribute.Nationality.ordinal()] == Nationality.Japanese.ordinal()
        		&& r[Attribute.Smoke.ordinal()] == Smoke.Parliament.ordinal()), v);
        // 15. The Norwegian lives next to the blue house.
        p.constraint(a -> IntStream.range(1, NUM_HOUSES)
            .anyMatch(i -> a[i][Attribute.Nationality.ordinal()] == Nationality.Norwegian.ordinal()
                && a[i - 1][Attribute.Color.ordinal()] == Color.Blue.ordinal()
                || a[i][Attribute.Color.ordinal()] == Color.Blue.ordinal()
                && a[i - 1][Attribute.Nationality.ordinal()] == Nationality.Norwegian.ordinal()) , v);
        Solver.printConstraintOrder(p);
        Solver s = new Solver();
        s.solve(p, m -> logger.info("answer: " + m));
    }

}
