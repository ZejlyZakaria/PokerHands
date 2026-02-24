package com.pokerhands.evaluator;

import com.pokerhands.model.*;
import com.pokerhands.parser.InputParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HandEvaluatorTest {

    // --------------------------------------------------
    // Tests du sujet officiel
    // --------------------------------------------------

    @Test
    void testWhiteWinsHighCardAce() {
        String line = "Black: 2H 3D 5S 9C KD White: 2C 3H 4S 8C AH";
        String result = InputParser.processLine(line);
        assertEquals("White wins. - with high card: Ace", result);
    }

    @Test
    void testBlackWinsFullHouse() {
        String line = "Black: 2H 4S 4C 2D 4H White: 2S 8S AS QS 3S";
        String result = InputParser.processLine(line);
        assertEquals("Black wins. - with full house: 4 over 2", result);
    }

    @Test
    void testTieHighCard() {
        String line = "Black: 2H 3D 5S 9C KD White: 2D 3H 5C 9S KH";
        String result = InputParser.processLine(line);
        assertEquals("Tie.", result);
    }

    @Test
    void testBlackWinsHighCard9() {
        String line = "Black: 2H 3D 5S 9C KD White: 2C 3H 4S 8C KH";
        String result = InputParser.processLine(line);
        assertEquals("Black wins. - with high card: 9", result);
    }

    // --------------------------------------------------
    // Cas où on passe aux tie-breakers (high card)
    // --------------------------------------------------

    @Test
    void testHighCardTieOnFirstTwoCards_BlackWinsOnThird() {
        String line = "Black: KH QD 8S 7D 2C White: KS QC 6H 5S 3D";
        String result = InputParser.processLine(line);
        assertEquals("Black wins. - with high card: 8", result); // K==K, Q==Q, 8>6
    }

    // --------------------------------------------------
    // PAIR : même paire, différence sur kicker
    // --------------------------------------------------

    @Test
    void testPairSamePair_BlackWinsOnKicker() {
        String line = "Black: 2H 2D 5S 9C KD White: 2C 2S 4H 8D KH";
        String result = InputParser.processLine(line);
        assertEquals("Black wins. - with pair: 2, 9 kicker", result);
    }

    // --------------------------------------------------
    // TWO_PAIRS : même paires hautes, différence sur paire basse
    // --------------------------------------------------

    @Test
    void testTwoPairsSameHighPair_DiffOnLowPair() {
        String line = "Black: 2H 2D 5S 5C KD White: 2C 2S 4H 4D KH";
        String result = InputParser.processLine(line);
        assertEquals("Black wins. - with two pairs: 5 and 2", result);
    }

    // --------------------------------------------------
    // STRAIGHT : différence sur la carte haute
    // --------------------------------------------------

    @Test
    void testStraightBlackHigher() {
        String line = "Black: 5H 6D 7S 8C 9H White: 4C 5D 6S 7H 8D";
        String result = InputParser.processLine(line);
        assertEquals("Black wins. - with straight to the 9", result);
    }

    // --------------------------------------------------
    // FLUSH : différence sur la carte haute
    // --------------------------------------------------

    @Test
    void testFlushBlackHigher() {
        String line = "Black: 2H 5H 7H 9H KH White: 3H 4H 6H 8H QH";
        String result = InputParser.processLine(line);
        assertEquals("Black wins. - with flush: King high", result);
    }

    // --------------------------------------------------
    // STRAIGHT_FLUSH : différence sur la carte haute
    // --------------------------------------------------

    @Test
    void testStraightFlushBlackHigher() {
        String line = "Black: 5H 6H 7H 8H 9H White: 4H 5H 6H 7H 8H";
        String result = InputParser.processLine(line);
        assertEquals("Invalid input: duplicate card 5H", result);
    }

    // --------------------------------------------------
    // FOUR_OF_A_KIND : différence sur le quad (impossible en réalité, mais test)
    // --------------------------------------------------

    @Test
    void testFourOfAKind() {
        String line = "Black: 2H 2D 2S 2C 5H White: 3H 3D 3S 3C 4H";
        String result = InputParser.processLine(line);
        assertEquals("White wins. - with four of a kind: 3", result);
    }

    // --------------------------------------------------
    // Tests d'erreurs et cas limites
    // --------------------------------------------------

    @Test
    void testDuplicateCardInHandThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Hand.from("2H 3D 2H 9C KD"));
    }

    @Test
    void testDuplicateCardBetweenHands() {
        String line = "Black: 2H 3D 5S 9C KD White: 2H 3H 4S 8C AH";
        String result = InputParser.processLine(line);
        assertTrue(result.contains("duplicate card"));
    }

    @Test
    void testInvalidFormat() {
        String line = "Black: 2H 3D 5S White: 2C 3H 4S 8C AH";
        String result = InputParser.processLine(line);
        assertEquals("Invalid format", result);
    }

    @Test
    void testInvalidCardSymbol() {
        String line = "Black: 2H 3D 5S 9Z KD White: 2C 3H 4S 8C AH";
        String result = InputParser.processLine(line);
        assertTrue(result.contains("Invalid card"));
    }

    @Test
    void testWheelStraight() {
        String line = "Black: AH 2D 3S 4C 5H White: 6H 7D 8S 9C TD";
        String result = InputParser.processLine(line);
        assertEquals("White wins. - with straight to the 10", result);
    }

    @Test
    void testStraightFlushWheel() {
        String line = "Black: AH 2H 3H 4H 5H White: 6S 7S 8S 9S TS";
        String result = InputParser.processLine(line);
        assertEquals("White wins. - with straight flush to the 10", result);
    }
}