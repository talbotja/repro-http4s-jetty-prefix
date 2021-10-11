package com.github.talbotja.http4sjettyprefix

import cats.effect.{Async, Resource}
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.jetty.server.JettyBuilder
import org.http4s.server.Router
import org.http4s.server.Server

object TestServer {
  def resource[F[_]: Async]: Resource[F, Unit] = {
    object dsl extends org.http4s.dsl.Http4sDsl[F]
    import dsl._

    def jettyServerResource(routes: HttpRoutes[F]): Resource[F, Server] =
      JettyBuilder[F]
        .bindHttp(8080)
        .mountHttpApp(routes.orNotFound, "/")
        .resource

    def blazeServerResource(routes: HttpRoutes[F]): Resource[F, Server] =
      BlazeServerBuilder[F]
        .bindHttp(8081)
        .withHttpApp(routes.orNotFound)
        .resource

    val routes = HttpRoutes.of[F] {
      case GET -> Root / "test-resource" => Ok("should be found")
      case GET -> Root / "pref-b" / "test-resource" => InternalServerError("should not be found")
    }

    def router(r: HttpRoutes[F]) = Router(
      "pref-a/pref-b" -> r
    )

    for {
      _ <- jettyServerResource(router(routes))
      _ <- blazeServerResource(router(routes))
    } yield ()
  }
}
