package framework;

import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

/**
 * Is used for generation valid credit cards for testing
 */
public class Generator {
	// -------------------- Generating --------------------------
	/**
	 * Generating random boolean value
	 * 
	 * @return - random boolean value
	 */
	public static boolean randomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}

	/**
	 * Generates needed quantity of symbols from numbers and chars.
	 * 
	 * @param numOfCars
	 *            - how many symbols
	 * @return String
	 */
	public static String charsAndNumbers(int numOfCars) {
		return generateOnAlfabet(numOfCars,
				"abcdefghijklmnopqrstuvwxyz0123456789");
	}

	/**
	 * Generates needed quantity of symbols from chars.
	 * 
	 * @param numOfCars
	 * @return String
	 */
	public static String chars(int numOfCars) {
		return generateOnAlfabet(numOfCars, "abcdefghijklmnopqrstuvwxyz");
	}

	/**
	 * Generates needed quantity of symbols from numbers.
	 * 
	 * @param numOfCars
	 * @return String
	 */
	public static String numbers(int numOfCars) {
		return generateOnAlfabet(numOfCars, "0123456789");
	}

	/**
	 * Generates needed quantity of symbols with spaces
	 * 
	 * @param numOfCars
	 * @return String
	 */
	public static String words(int numOfCars) {
		return generateOnAlfabet(numOfCars, "abcdefghijklmnopqrstuvwxyz ");
	}

	/**
	 * Generates valid email.
	 * 
	 * @return String
	 */
	public static String email() {
		int numOfCars = 4;
		return chars(numOfCars) + "@" + chars(numOfCars) + ".com";
	}

	/**
	 * Generating number in rhe range
	 * @param from
	 * @param till
	 * @return
	 */
	public static String number(int min, int max) {
		return String.valueOf(min + (int)(Math.random() * ((max - min) + 1)));
	}
	
	/**
	 * Generating symbols on prepared alphabet
	 * 
	 * @param numOfCars
	 * @param alfabet
	 * @return
	 */
	private static String generateOnAlfabet(int numOfCars, String alfabet) {
		String text = "";
		int length = alfabet.length();
		if (numOfCars <= 0)
			return "";
		for (int i = 0; i < numOfCars; i++) {
			text += alfabet.charAt((int) Math.floor(Math.random() * length));
		}

		return text;
	}

	// ----------------- Credit cards
	/**
	 * Generates valid master card number.
	 * 
	 * @return String
	 */
	public static String ccNum() {
		return Generator.generateMasterCardNumber();
	}

	/**
	 * Generates valid amex card number.
	 * 
	 * @return String
	 */
	public static String ccNumAmex() {
		return Generator.generateAmexCardNumber();
	}

	// ----------------- Credit cards suppot methods

	@SuppressWarnings("unused")
	private static final String[] VISA_PREFIX_LIST = new String[] { "4539",
			"4556", "4916", "4532", "4929", "40240071", "4485", "4716", "4" };

	private static final String[] MASTERCARD_PREFIX_LIST = new String[] { "51",
			"52", "53", "54", "55" };

	private static final String[] AMEX_PREFIX_LIST = new String[] { "34", "37" };

	@SuppressWarnings("unused")
	private static final String[] DISCOVER_PREFIX_LIST = new String[] { "6011" };

	@SuppressWarnings("unused")
	private static final String[] DINERS_PREFIX_LIST = new String[] { "300",
			"301", "302", "303", "36", "38" };

	@SuppressWarnings("unused")
	private static final String[] ENROUTE_PREFIX_LIST = new String[] { "2014",
			"2149" };

	@SuppressWarnings("unused")
	private static final String[] JCB_16_PREFIX_LIST = new String[] { "3088",
			"3096", "3112", "3158", "3337", "3528" };

	@SuppressWarnings("unused")
	private static final String[] JCB_15_PREFIX_LIST = new String[] { "2100",
			"1800" };

	@SuppressWarnings("unused")
	private static final String[] VOYAGER_PREFIX_LIST = new String[] { "8699" };

	private static String strrev(String str) {
		if (str == null)
			return "";
		String revstr = "";
		for (int i = str.length() - 1; i >= 0; i--) {
			revstr += str.charAt(i);
		}

		return revstr;
	}

	/*
	 * 'prefix' is the start of the CC number as a string, any number of digits.
	 * 'length' is the length of the CC number to generate. Typically 13 or 16
	 */
	private static String completed_number(String prefix, int length) {

		String ccnumber = prefix;

		// generate digits

		while (ccnumber.length() < (length - 1)) {
			ccnumber += new Double(Math.floor(Math.random() * 10)).intValue();
		}

		// reverse number and convert to int

		String reversedCCnumberString = strrev(ccnumber);

		List<Integer> reversedCCnumberList = new Vector<Integer>();
		for (int i = 0; i < reversedCCnumberString.length(); i++) {
			reversedCCnumberList.add(new Integer(String
					.valueOf(reversedCCnumberString.charAt(i))));
		}

		// calculate sum

		int sum = 0;
		int pos = 0;

		Integer[] reversedCCnumber = reversedCCnumberList
				.toArray(new Integer[reversedCCnumberList.size()]);
		while (pos < length - 1) {

			int odd = reversedCCnumber[pos] * 2;
			if (odd > 9) {
				odd -= 9;
			}

			sum += odd;

			if (pos != (length - 2)) {
				sum += reversedCCnumber[pos + 1];
			}
			pos += 2;
		}

		// calculate check digit

		int checkdigit = new Double(
				((Math.floor(sum / 10) + 1) * 10 - sum) % 10).intValue();
		ccnumber += checkdigit;

		return ccnumber;

	}

	private static String[] credit_card_number(String[] prefixList, int length,
			int howMany) {

		Stack<String> result = new Stack<String>();
		for (int i = 0; i < howMany; i++) {
			int randomArrayIndex = (int) Math.floor(Math.random()
					* prefixList.length);
			String ccnumber = prefixList[randomArrayIndex];
			result.push(completed_number(ccnumber, length));
		}

		return result.toArray(new String[result.size()]);
	}

	@SuppressWarnings("unused")
	private static String[] generateMasterCardNumbers(int howMany) {
		return credit_card_number(MASTERCARD_PREFIX_LIST, 16, howMany);
	}

	private static String generateAmexCardNumber() {
		return credit_card_number(AMEX_PREFIX_LIST, 15, 1)[0];
	}

	private static String generateMasterCardNumber() {
		return credit_card_number(MASTERCARD_PREFIX_LIST, 16, 1)[0];
	}

	@SuppressWarnings("unused")
	private static boolean isValidCreditCardNumber(String creditCardNumber) {
		boolean isValid = false;

		try {
			String reversedNumber = new StringBuffer(creditCardNumber)
					.reverse().toString();
			int mod10Count = 0;
			for (int i = 0; i < reversedNumber.length(); i++) {
				int augend = Integer.parseInt(String.valueOf(reversedNumber
						.charAt(i)));
				if (((i + 1) % 2) == 0) {
					String productString = String.valueOf(augend * 2);
					augend = 0;
					for (int j = 0; j < productString.length(); j++) {
						augend += Integer.parseInt(String.valueOf(productString
								.charAt(j)));
					}
				}

				mod10Count += augend;
			}

			if ((mod10Count % 10) == 0) {
				isValid = true;
			}
		} catch (NumberFormatException e) {
		}

		return isValid;
	}


}
