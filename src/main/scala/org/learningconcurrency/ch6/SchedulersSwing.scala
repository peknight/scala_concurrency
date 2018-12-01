package org.learningconcurrency
package ch6

import rx.lang.scala.{Observable, Scheduler, Subscription}

import scala.swing._
import scala.swing.event._

object SchedulersSwing extends SimpleSwingApplication {
  override def top: Frame = new MainFrame {
    title = "Swing Observables"
    val button = new Button {
      text = "Click"
    }
    contents = button
    val buttonClicks = Observable.create[Button] {
      obs =>
        button.reactions += {
          case ButtonClicked(_) => obs.onNext(button)
        }
        Subscription()
    }
    buttonClicks.subscribe(_ => log("button clicked"))
  }


}
