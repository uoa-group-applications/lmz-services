package nz.ac.auckland.lmz.services

import com.bluetrainsoftware.classpathscanner.ClasspathScanner
import com.bluetrainsoftware.classpathscanner.ResourceScanListener
import groovy.transform.CompileStatic
import nz.ac.auckland.util.JacksonHelper

/**
 * Expects messages to be in
 */
@CompileStatic
abstract class I18nMessageMapService implements I18nMessageMap {
  /**
   * Message map
   */
  final private Map<String, String> s_messageMap = [:]
	private boolean inDevMode

	public I18nMessageMapService() {
		String prefix = "i18n/messages/" + getResourceMatchingPattern();
		List<ResourceScanListener.ScanResource> resources = new ArrayList<>();

		inDevMode = System.getProperty("webapp.devmode") != null

		Map<String, String> messageMap = s_messageMap

		ClasspathScanner.getInstance().registerResourceScanner(new ResourceScanListener() {
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
				} else if (interestingResource.directory) {
					return ResourceScanListener.InterestAction.REPEAT;
				} else {
					return ResourceScanListener.InterestAction.ONCE;
				}
			}
		})
	}

  public Map<String, String> getMessagesMap() {
    if (inDevMode) {
	    ClasspathScanner.getInstance().notify(getClass().getClassLoader());
    }

    return s_messageMap
  }

	abstract protected String getResourceMatchingPattern()

  public String getMessagesMapAsJson() {
    return JacksonHelper.serialize(getMessagesMap())
  }
}
