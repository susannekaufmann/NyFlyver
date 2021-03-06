package zzz.akka.avitronics

import akka.actor.{Props, Actor, ActorSystem}
import akka.testkit.{TestKit, TestActorRef, ImplicitSender}
import org.scalatest.{WordSpec, BeforeAndAfterAll}
import org.scalatest.matchers.MustMatchers

/**
 * Created by susanne on 1/13/14.
 */

class EventSourceSpec extends TestKit(ActorSystem("EventSourceSpec"))
                        with WordSpec
                        with MustMatchers
                        with BeforeAndAfterAll {
  import EventSource._

  override def afterAll() { system.shutdown() }

    "EventSource" should {
      "allow us to register a listener" in {

        val real = TestActorRef[EventSourceTest].underlyingActor
        real.receive(RegisterListener(testActor))
        real.listeners must contain (testActor)
      }

    "allow us to unregister a listender" in {

      val real = TestActorRef[EventSourceTest].underlyingActor
      real.receive(RegisterListener(testActor))
      real.receive(UnregisterListener(testActor))
      real.listeners.size must be (0)
    }

    "send the event to our test actor" in {
      val testA = TestActorRef[EventSourceTest]
      testA ! RegisterListener(testActor)
      testA.underlyingActor.sendEvent("Fibonacci")
      expectMsg("Fibonacci")
    }
  }
}