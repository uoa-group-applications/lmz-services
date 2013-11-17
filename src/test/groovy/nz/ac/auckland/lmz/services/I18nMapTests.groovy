package nz.ac.auckland.lmz.services

import com.bluetrainsoftware.classpathscanner.ClasspathScanner
import org.junit.Test

/**
 *
 * @author: Richard Vowles - https://plus.google.com/+RichardVowles
 */
class I18nMapTests {
	@Test
	public void testClient() {
		I18nClientMessageMap resource = new I18nClientMessageMap();

		ClasspathScanner.getInstance().scan(getClass().getClassLoader())

		assert resource.messagesMap['client'] == '1'
	}

	@Test
	public void testServer() {
		I18nServerMessageMap resource = new I18nServerMessageMap()
		ClasspathScanner.getInstance().scan(getClass().getClassLoader())

		assert resource.messagesMap['server'] == '1'
	}
}
