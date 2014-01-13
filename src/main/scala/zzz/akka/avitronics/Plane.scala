package zzz.akka.avitronics

import akka.actor.{Props, Actor, ActorLogging}

/**
 * Created by susanne on 1/10/14.
 */
object Plane {
  case object GiveMeControl
}

class Plane extends Actor with ActorLogging {

  import Altimeter._
  import Plane._
  import EventSource._

  val altimeter = context.actorOf(Props[Altimeter], "Altimeter")
  val controls = context.actorOf(Props(new ControlSurfaces(altimeter)), "ControlSurfaces")

  override def preStart() {
    // add me as a listener for events from altimeter!
    log.info(s"Plane registers as listener for Altimeter")
    altimeter ! RegisterListener(self)
  }

  def receive = {

    case GiveMeControl =>
      log.info("Plane giving control")
      sender ! controls

    case AltitudeUpdate(altitude) => {
      log.info(s"Altitude is now: $altitude")
    }
  }
}

