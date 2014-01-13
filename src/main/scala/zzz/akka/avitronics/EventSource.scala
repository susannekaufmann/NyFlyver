package zzz.akka.avitronics

import akka.actor.{Actor, ActorRef}
/**
 * Created by susanne on 1/10/14.
 */
object EventSource {

  case class RegisterListener(listener: ActorRef)
  case class UnregisterListener(listener: ActorRef)
}

trait EventSource {
  def sendEvent[T](event: T): Unit
  def eventSourceReceive: Actor.Receive
}

trait EventSourceImpl extends EventSource { this: Actor =>

  import EventSource._

  // basicaally constant time for access including random. Each node in the tree (graph) holds 32 nodes,
  // so one level can hold 32 elements
  var listeners = Vector.empty[ActorRef]

  // a method that sends an event to each element in listeneres (_ === each element)
  def sendEvent[T](event: T): Unit = listeners foreach { _ ! event }

  // since we are a trait and via selftyping declare that we can only be mixed in with an Actor, we need to change
  // our receive to be a partial function
  def eventSourceReceive: Receive = {

    case RegisterListener(listener) =>
     listeners = listeners :+ listener

    case UnregisterListener(listener) =>
      listeners = listeners filter { _ != listener }

  }
}

/** test class */
class EventSourceTest extends Actor with EventSourceImpl {

  def receive = eventSourceReceive
}

