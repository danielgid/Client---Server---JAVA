
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 
 * @author daniel
 *
 */

public class secret {

	/**
	 * We decode with xor cryptograph
	 * @param str - the string we want to decode
	 * @param key - key decoding
	 * @return decoded string
	 */
	public static String decodeS(String str, String key) {
		byte[] keyB = key.getBytes(Charset.forName("UTF-8")), strB = str.getBytes(Charset.forName("UTF-8"));
		byte[] decoded = new byte[strB.length];

		for (int i = 0; i < strB.length; i++) {
			decoded[i] = (byte) (strB[i] ^ keyB[i % keyB.length]);
		}

		return new String(decoded, StandardCharsets.UTF_8);
	}

	/**
	 * We decode with xor cryptograph
	 * @param num
	 * @param key - key decoding
	 * @return decoded double numer
	 */
	public static int decodeI(int num, int key) {
		return key ^ num;
	}

	/**
	 * We decode with xor cryptograph
	 * @param num - the numer we wannt decode
	 * @param key - key decoding
	 * @return decoded integer
	 */
	public static double decodeD(double num, double key) {
		return Double.longBitsToDouble(Double.doubleToRawLongBits(key) ^ Double.doubleToRawLongBits(num));
	}

}
