package nz.ac.auckland.lmz.services

import nz.ac.auckland.common.jsresource.ApplicationResource
import nz.ac.auckland.common.jsresource.ResourceScope
import nz.ac.auckland.common.stereotypes.UniversityComponent

import javax.inject.Inject

/**
 * Author: Marnix
 *
 * The internationalization messages map
 */
@UniversityComponent
class I18nClientMessagesResource implements ApplicationResource {

    /**
     * Messages map bound here
     */
    @Inject private I18nClientMessageMap messageMap;

    /**
     * @return the global resource scope
     */
    @Override
    ResourceScope getResourceScope() {
        return ResourceScope.Global
    }

    /**
     * @return a map with all internationalization data in it
     */
    @Override
    Map<String, Object> getResourceMap() {
        return [
            i18n : messageMap.messagesMap
        ]
    }
}
