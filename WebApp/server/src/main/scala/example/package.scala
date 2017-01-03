import akka.actor.ActorSystem
import akka.stream.Materializer

import scala.concurrent.ExecutionContextExecutor

package object example {

  trait ServiceContext {
    implicit val system: ActorSystem
    implicit val executor: ExecutionContextExecutor
    implicit val materializer: Materializer
  }

}