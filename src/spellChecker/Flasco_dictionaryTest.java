package spellChecker;

//Name: Michael Flasco
//Date: 03/24/2019	
//Program Name: Unit Test
//Purpose: Unit test for Spell Checker

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Flasco_dictionaryTest {

	@Test
	void test() {
		Flasco_SpellChecker test = new Flasco_SpellChecker();
		int output = test.dictionary();
		assertEquals(50, output);
		System.out.println("Test Successful...");
	}
}
