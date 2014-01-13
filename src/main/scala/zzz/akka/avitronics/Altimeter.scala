package zzz.akka.avitronics

import akka.actor.{Props,Actor,ActorSystem,ActorLogging}
import scala.concurrent.duration._

/**
 * Created by susanne on 1/10/14.
 */
object Altimeter {
  case class RateChange(amount: Float)
  case class AltitudeUpdate(altitude: Double)
  def apply() = new Altimeter with EventSourceImpl
}

class Altimeter extends Actor with ActorLogging {
  this: EventSource =>

  import Altimeter._

  // our own internal message - akka has a Tick too
  case object Tick

  implicit val ec = context.dispatcher

  val ceiling = 43000
  val maxRateOfClimb = 5000

  // periodically update our position using a timer
  val ticker = context.system.scheduler.schedule(100.millis, 100.millis, self, Tick)

  var rateOfClimb = 0f
  var altitude = 0d

  var lastTick = System.currentTimeMillis

  def altimeterReceive: Receive = {

    case RateChange(amount) =>
      rateOfClimb = amount.min(1.0f).max(-1.0f) * maxRateOfClimb
      log.info(s"Altimeter changed rate of climb to $rateOfClimb")

    case Tick =>
      val tick = System.currentTimeMillis
      altitude = altitude + ((tick - lastTick) / 60000.0) * rateOfClimb
      lastTick = tick
      sendEvent(AltitudeUpdate(altitude))

  }

  def receive = eventSourceReceive orElse altimeterReceive

  // cleanup when we close up shop
  override def postStop(): Unit = ticker.cancel()
}
