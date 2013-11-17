package nz.ac.auckland.lmz.services

import nz.ac.auckland.common.stereotypes.UniversityComponent

/**
 * author: Richard Vowles - http://gplus.to/RichardVowles
 */
@UniversityComponent
class I18nClientMessageMap extends I18nMessageMapService {
  @Override
  protected String getResourceMatchingPattern() {
    return "client_"
  }
}
