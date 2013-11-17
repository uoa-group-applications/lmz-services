package nz.ac.auckland.lmz.services

import nz.ac.auckland.common.jsresource.ApplicationResource
import nz.ac.auckland.common.jsresource.ResourceScope
import nz.ac.auckland.common.stereotypes.UniversityComponent

import javax.inject.Inject

/**
 * Author: Marnix
 *
 * Application resource that exposes angular templates
 */
@UniversityComponent
class AngularTemplateResource implements ApplicationResource {

    /**
     * Template service injected here
     */
    @Inject AngularTemplates angularTemplates;

    /**
     * @return the global resource scope
     */
    @Override
    ResourceScope getResourceScope() {
        return ResourceScope.Global;
    }

    /**
     * @return a map of angular templates
     */
    @Override
    Map<String, Object> getResourceMap() {
        return ['templates' : angularTemplates.angularTemplates ]
    }
}
