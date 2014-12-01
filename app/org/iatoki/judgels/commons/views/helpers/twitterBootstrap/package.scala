package org.iatoki.judgels.commons.views.helpers

import org.iatoki.judgels.commons.views.html.helpers.twitterBootstrap.twitterBootstrapFieldConstructor
import views.html.helper.{FieldConstructor, FieldElements}

package object twitterBootstrap {
  implicit val twitterBootstrapField = new FieldConstructor {
    def apply(elts: FieldElements) = twitterBootstrapFieldConstructor(elts)
  }
}
