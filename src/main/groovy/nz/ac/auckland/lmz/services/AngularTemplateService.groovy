package nz.ac.auckland.lmz.services

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.util.JacksonHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.servlet.ServletContext

/**
 * author: Richard Vowles - http://gplus.to/RichardVowles
 */
@UniversityComponent
@CompileStatic
class AngularTemplateService implements AngularTemplates {
  private static final Logger log = LoggerFactory.getLogger(AngularTemplateService)

  private Map<String, String> templates;

  @Inject
  private ServletContext context;
  private boolean inDevMode

  @PostConstruct
  public void init() {
    inDevMode = System.getProperty("webapp.devmode") != null
  }

  protected void collectResources(Set<String> paths, List<String> collected) {
    for(String path : paths) {
      if (path.endsWith(".html"))
        collected.add(path);
      else if (path.endsWith("/"))
        collectResources(context.getResourcePaths(path), collected);
    }
  }

  protected List<String> collectResources() {
    List<String> collected = new ArrayList<>();

    collectResources(context.getResourcePaths("/angular"), collected);

    return collected;
  }

  protected Map<String, String> collectAngularTemplates(List<String> resources) {
    Map<String, String> templateMapping = [:]

    resources.each { String url ->

      String mapping = url.substring(url.indexOf('/angular/') + '/angular/'.length())

      templateMapping[mapping] = context.getResourceAsStream(url).text

      if (log.isDebugEnabled())
        log.debug("angular-template: ${url}")

      if (log.isTraceEnabled())
        log.trace("angular-template: ${url}\n${templateMapping[url]} ")
    }

    templates = templateMapping

    return templateMapping
  }


  public Map<String, String> getAngularTemplates() {
    if (inDevMode || templates == null)
      return collectAngularTemplates(collectResources())
    else
      return templates
  }

  public String getAngularTemplatesAsJson() {
    return JacksonHelper.serialize(getAngularTemplates())
  }
}
