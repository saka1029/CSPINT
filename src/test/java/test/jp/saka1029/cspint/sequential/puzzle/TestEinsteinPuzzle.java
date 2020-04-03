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
    
    static Variable[] selectColumn(Variable[][] matrix, int selection) {
        int rows = matrix.length;
        Variable[] result = new Variable[rows];
        for (int r = 0; r < rows; ++r)
            result[r] = matrix[r][selection];
        return result;
    }
    static Variable[] selectColumn(Variable[][] matrix, Attribute selection) {
        return selectColumn(matrix, selection.ordinal());
    }

    static Variable[][] selectColumns(Variable[][] matrix, int... selections) {
        int rows = matrix.length;
        int cols = selections.length;
        Variable[][] result = new Variable[rows][cols];
        for (int r = 0; r < rows; ++r)
            for (int c = 0; c < cols; ++c)
                result[r][c] = matrix[r][selections[c]];
        return result;
    }

    static Variable[][] selectColumns(Variable[][] matrix, Attribute... selections) {
        return selectColumns(matrix, Arrays.stream(selections)
            .mapToInt(a -> a.ordinal())
            .toArray());
    }

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
        	.anyMatch(r -> r[0] == Nationality.Englishman.ordinal() && r[1] == Color.Red.ordinal()),
        	selectColumns(v, Attribute.Nationality, Attribute.Color));
        //  3. The Spaniard owns the dog.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[0] == Nationality.Spaniard.ordinal() && r[1] == Pet.Dog.ordinal()),
        	selectColumns(v, Attribute.Nationality, Attribute.Pet));
        //  4. Coffee is drunk in the green house.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[0] == Drink.Coffee.ordinal() && r[1] == Color.Green.ordinal()),
        	selectColumns(v, Attribute.Drink, Attribute.Color));
        //  5. The Ukrainian drinks tea.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[0] == Nationality.Ukrainian.ordinal() && r[1] == Drink.Tea.ordinal()),
        	selectColumns(v, Attribute.Nationality, Attribute.Drink));
        //  6. The green house is immediately to the right of the ivory house.
        p.constraintVarargs(a -> IntStream.range(1, NUM_HOUSES)
            .anyMatch(i -> a[i] == Color.Green.ordinal() && a[i - 1] == Color.Ivory.ordinal()),
            selectColumn(v, Attribute.Color));
        //  7. The Old Gold smoker owns snails.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[0] == Smoke.OldGold.ordinal() && r[1] == Pet.Snails.ordinal()),
        	selectColumns(v, Attribute.Smoke, Attribute.Pet));
        //  8. Kools are smoked in the yellow house.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[0] == Smoke.Kools.ordinal() && r[1] == Color.Yellow.ordinal()),
        	selectColumns(v, Attribute.Smoke, Attribute.Color));
        //  9. Milk is drunk in the middle house.
        p.constraint(x -> x == Drink.Milk.ordinal(), v[2][Attribute.Drink.ordinal()]);
        // 10. The Norwegian lives in the first house.
        p.constraint(x -> x == Nationality.Norwegian.ordinal(), v[0][Attribute.Nationality.ordinal()]);
        // 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
        p.constraint(a -> IntStream.range(1, NUM_HOUSES)
            .anyMatch(i -> a[i][0] == Smoke.Chesterfield.ordinal() && a[i - 1][1] == Pet.Fox.ordinal()
                || a[i][1] == Pet.Fox.ordinal() && a[i - 1][0] == Smoke.Chesterfield.ordinal()),
            selectColumns(v, Attribute.Smoke, Attribute.Pet));
        // 12. Kools are smoked in the house next to the house where the horse is kept.
        p.constraint(a -> IntStream.range(1, NUM_HOUSES)
            .anyMatch(i -> a[i][0] == Smoke.Kools.ordinal() && a[i - 1][1] == Pet.Horse.ordinal()
                || a[i][1] == Pet.Horse.ordinal() && a[i - 1][0] == Smoke.Kools.ordinal()),
            selectColumns(v, Attribute.Smoke, Attribute.Pet));
        // 13. The Lucky Strike smoker drinks orange juice.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[0] == Smoke.LuckyStrike.ordinal() && r[1] == Drink.OrangeJuice.ordinal()),
        	selectColumns(v, Attribute.Smoke, Attribute.Drink));
        // 14. The Japanese smokes Parliaments.
        p.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[0] == Nationality.Japanese.ordinal() && r[1] == Smoke.Parliament.ordinal()),
        	selectColumns(v, Attribute.Nationality, Attribute.Smoke));
        // 15. The Norwegian lives next to the blue house.
        p.constraint(a -> IntStream.range(1, NUM_HOUSES)
            .anyMatch(i -> a[i][0] == Nationality.Norwegian.ordinal() && a[i - 1][1] == Color.Blue.ordinal()
                || a[i][1] == Color.Blue.ordinal() && a[i - 1][0] == Nationality.Norwegian.ordinal()),
            selectColumns(v, Attribute.Nationality, Attribute.Color));
        Solver.printConstraintOrder(p);
        Solver s = new Solver();
        s.solve(p, m -> logger.info("answer: " + m));
        logger.info(Arrays.toString(s.bindCount));
    }

}
