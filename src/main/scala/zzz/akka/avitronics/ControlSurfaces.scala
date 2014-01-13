package zzz.akka.avitronics

import akka.actor.{Actor, ActorRef}


/**
 * Created by susanne on 1/10/14.
 */
object ControlSurfaces {
  // messages we send
  case class StickBack(amount: Float)
  case class StickForward(amount: Float)
}

class ControlSurfaces(altimeter: ActorRef) extends Actor {

  import ControlSurfaces._
  import Altimeter._

  def receive = {

    case StickBack(amount) =>
      altimeter ! RateChange(amount)

    case StickForward(amount) =>
      altimeter ! RateChange(-1 * amount)

  }

}
