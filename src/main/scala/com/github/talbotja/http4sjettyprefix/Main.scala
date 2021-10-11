package com.github.talbotja.http4sjettyprefix

import cats.effect.{IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    TestServer.resource[IO].useForever
}
