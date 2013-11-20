package nz.ac.auckland.lmz.services

import com.bluetrainsoftware.classpathscanner.ClasspathScanner
import com.bluetrainsoftware.classpathscanner.ResourceScanListener
import groovy.transform.CompileStatic
import nz.ac.auckland.lmz.flags.Flags
import nz.ac.auckland.util.JacksonHelper

/**
 * Expects messages to be in
 */
@CompileStatic
abstract class I18nMessageMapService implements I18nMessageMap {
  /**
   * Message map
   */
  protected Map<String, String> s_messageMap = [:]
	private boolean inDevMode

	public I18nMessageMapService() {
		String prefix = "i18n/messages/" + getResourceMatchingPattern();
		List<ResourceScanListener.ScanResource> resources = new ArrayList<>();

		inDevMode = Flags.DEVMODE.on()

		ClasspathScanner.getInstance().registerResourceScanner(new ResourceScanListener() {
			Map<String, String> messageMap = [:]

			@Override
			List<ResourceScanListener.ScanResource> resource(List<ResourceScanListener.ScanResource> scanResources) throws Exception {
				resources.clear()

				for (ResourceScanListener.ScanResource scanResource : scanResources) {
					if (scanResource.resourceName.startsWith(prefix) && scanResource.resourceName.endsWith(".properties")) {
						resources.add(scanResource);
					}
				}

				return resources
			}

			@Override
			void deliver(ResourceScanListener.ScanResource desire, InputStream inputStream) {
				Properties p = new Properties()
				p.load(inputStream)

				p.stringPropertyNames().each { String key ->
					messageMap.put(key, p.getProperty(key))
				}
			}

			@Override
			ResourceScanListener.InterestAction isInteresting(ResourceScanListener.InterestingResource interestingResource) {
				String urlString = interestingResource.url.toString().toString().toLowerCase();

				if (urlString.contains("jdk") || urlString.contains("jre") || urlString.contains("spring") || urlString.contains("jackson") || urlString.contains("stickycode") || urlString.contains("groovy-all")) {
					return ResourceScanListener.InterestAction.NONE;
				} else {
					return ResourceScanListener.InterestAction.REPEAT;
				}
			}

			@Override
			void scanAction(ResourceScanListener.ScanAction action) {
				if (action == ResourceScanListener.ScanAction.STARTING) {
					messageMap = [:]
				} else if (action == ResourceScanListener.ScanAction.COMPLETE) {
					s_messageMap = Collections.unmodifiableMap(messageMap)
				}
			}
		})
	}

  public Map<String, String> getMessagesMap() {
    if (inDevMode) {
	    ClasspathScanner.getInstance().scan(getClass().getClassLoader());
    }

    return s_messageMap
  }

	abstract protected String getResourceMatchingPattern()

  public String getMessagesMapAsJson() {
    return JacksonHelper.serialize(getMessagesMap())
  }
}
