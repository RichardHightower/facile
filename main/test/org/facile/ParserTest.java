package org.facile;

import static org.facile.Facile.*;
import static org.facile.Parser.*;
import static org.junit.Assert.*;

import java.util.List;

import org.facile.Parser.Match;
import org.facile.Parser.Rule;
import org.junit.Before;
import org.junit.Test;


public class ParserTest {

	Rule firstName = null, personalPart = null, initial = null,
			lastName = null, suffix = null, zipCode = null, houseNum, apt, streetName, street;
	
	@Before
	public void setup() {

		// zip-code = 5DIGIT ["-" 4DIGIT]
		zipCode = once("zip-code", digits(5),
				optional(allowOnce('-'), digits(4)));

		// first-name = *ALPHA
		firstName = alpha("first-name", zeroOrMore);

		// initial = ALPHA
		initial = alpha("initial", once);
		// last-name = *ALPHA
		lastName = alpha("last-name", zeroOrMore);
		// suffix = ("Jr." / "Sr." / 1*("I" / "V" / "X"))
		suffix = oneOf("suffix", exact("Jr."), exact("Sr."),
				allow('I', 'V', 'X').oneOrMore());

		//personal-part    = first-name / (initial ".")
		personalPart = oneOf("personal-part", firstName, once(initial, exact(".")));
		
		
		//apt              = 1*4DIGIT
		apt = DIGIT.times(1,4);
		
		//house-num        = 1*8(DIGIT / ALPHA)
		houseNum = ALPHA_NUMERIC.times(1,8);
		
		//street-name      = 1*VCHAR
		streetName = VCHAR.oneOrMore();
		
		//street           = [apt SP] house-num SP street-name CRLF
		street = once( optional(apt, SP.once()), houseNum, SP.once(), streetName, CRLF.once() );
		


	}

	@Test
	public void optionalTest() {
		Rule myrule = ALPHA_NUMERIC.times(1,4).optional();		
		
		
		List<String> tests = ls("Rick", "Ricky", "Ri", "Rick ", "", " Rick");
		List<Boolean> results = ls(true, true, true, true, true, true);
		
		for (int index = 0; index < tests.size(); index++) {
			print(idx(tests, index));
			Match match = matchex(idx(tests, index), myrule);
			boolean match2 = match(idx(tests, index), myrule);
			assertEquals(idx(tests,index), match.isMatch(), match2);
			assertEquals(idx(results, index), match.isMatch());
						
		}
			
	}

	@Test
	public void requiredTest() {
		Rule myrule = ALPHA_NUMERIC.times(1,4).required();		

		List<String> tests =    ls("Yes", "Ye", "Left ", "",   " Right", "12345");
		List<Boolean> results = ls(true,  true, false,  false, false,     false);
		
		for (int index = 0; index < tests.size(); index++) {
			print(idx(tests, index));
			Match match = matchex(idx(tests, index), myrule);
			boolean match2 = match(idx(tests, index), myrule);
			
			print (match2, match); 
			
			assertEquals(idx(tests,index), match.isMatch(), match2);
			assertEquals(idx(results, index), match.isMatch());
						
		}
		

	}
	
	@Test
	public void oneOfInSequenceDetailed() {
			Rule myrule = oneOf(exact("RICK"), exact("SAM"), exact("joe")).required().times(1);	
			Match match;
			
			match = matchex("RICK",myrule);
			assertEquals(0,match.getErrorCount());
			assertEquals(4, match.index());
			assertEquals(4, match.getLastMatch());
			assertEquals(true, match.isMatch());
			
			match = matchex("SAM",myrule);
			assertEquals(0,match.getErrorCount());
			assertEquals(3, match.index());
			assertEquals(3, match.getLastMatch());
			assertEquals(true, match.isMatch());

			match = matchex("joe",myrule);
			assertEquals(0,match.getErrorCount());
			assertEquals(3, match.index());
			assertEquals(3, match.getLastMatch());
			assertEquals(true, match.isMatch());
			
			match = matchex("joe ",myrule);
			print (match);
			assertFalse(match.isMatch());


	}

	@Test
	public void oneOfInSequenceDetailed2() {
			Rule myrule = oneOf("ONEOF|RICK|SAM|joe", exact("RICK"), exact("SAM"), exact("joe")).required().times(3);	
			Match match;
			
			match = matchex("RICKjoeSAM",myrule);
			print (join(match.getReasonsForErrors(), "\n"));
			assertEquals(0,match.getErrorCount());
			assertEquals(10, match.index());
			assertEquals(10, match.getLastMatch());
			assertEquals(true, match.isMatch());
			
			
			match = matchex("RICKjoeSAM ",myrule);
			print (join(match.getReasonsForErrors(), "\n"));
			assertEquals(false, match.isMatch());
	}

	@Test
	public void personalPart() {

		assertEquals(true, match("Rick", personalPart));
		assertEquals(true, match("R.", personalPart));

		assertEquals(false, match(" Rick", personalPart));
		assertEquals(false, match(" R.", personalPart));

		assertEquals(false, match("Rick ", personalPart));
		assertEquals(false, match("R. ", personalPart));

		assertEquals(true, matchex("Rick", personalPart).isMatch());
		//assertEquals(true, matchex("R.", personalPart).isMatch());

		assertEquals(false, matchex(" Rick", personalPart).isMatch());
		assertEquals(false, matchex(" R.", personalPart).isMatch());

		assertEquals(false, matchex("Rick ", personalPart).isMatch());
		assertEquals(false, matchex("R. ", personalPart).isMatch());

	}
	@Test
	public void zipCodeWithOptional() {

		assertEquals(true, match("85710-1111", zipCode));
		assertEquals(false, match("85710-111", zipCode));
		//assertEquals(true, match("85710", zipCode)); //TODO Fix
		assertEquals(false, match("857ab-111", zipCode));
		

		assertEquals(true, matchex("85710-1111", zipCode).isMatch());
		assertEquals(false, matchex("85710-111", zipCode).isMatch());
		//assertEquals(true, matchex("85710", zipCode).isMatch()); //TODO Fix
		assertEquals(false, matchex("857ab-111", zipCode).isMatch());

	}
	
	@Test
	public void exactTest2() {
		Rule rule = exact("ab");
		Match match = matchex(new char []  {' ', 'a', 'b'}, rule);
		assertEquals(false, match.isMatch());
		assertEquals(1, match.getErrorCount());
		assertEquals(rule, match.getRulesForErrors().get(0));
		print (match.index());
		
		match = matchex(new char []  {' ', 'a', 'b'}, rule, 1);
		assertEquals(true, match.isMatch());
		assertEquals(0, match.getErrorCount());
		print (3, match.index());

		match = matchex(new char []  {' ', 'a', 'b', ' ', ' ', 'a', 'z'}, rule, 1, 3);
		assertEquals(true, match.isMatch());
		assertEquals(0, match.getErrorCount());
		print (3, match.index());

		
	}
	
	@Test
	public void exactTest() {
		
		assertEquals(true, match("Jr.", exact("Jr.")));
		assertEquals(false, match("J.r", exact("Jr.")));
		assertEquals(false, match("Jr", exact("Jr.")));
		assertEquals(false, match("Jr. ", exact("Jr.")));
		assertEquals(false, match(" Jr.", exact("Jr.")));
		

	}

	@Test
	public void oneOfTest() {
		
		assertEquals(true, match("Jr.", oneOf( exact("Jr."), exact("Sr.")  )));
		assertEquals(true, match("Sr.", oneOf( exact("Jr."), exact("Sr.")  )));
		assertEquals(false, match("Jr. ", oneOf( exact("Jr."), exact("Sr.")  )));
		assertEquals(false, match(" Sr.", oneOf( exact("Jr."), exact("Sr.")  )));
		assertEquals(false, match("S.r", oneOf( exact("Jr."), exact("Sr.")  )));
		assertEquals(false, match(" Sr. ", oneOf( exact("Jr."), exact("Sr.")  )));


	}
	
	@Test
	public void suffixTestingCombo() {
		
		assertEquals(true, match("V", suffix));
		assertEquals(true, match("III", suffix));
		assertEquals(true, match("Jr.", suffix));
		assertEquals(true, match("Sr.", suffix));
		
		assertEquals(false, match(" Sr .", suffix));
	}


}
