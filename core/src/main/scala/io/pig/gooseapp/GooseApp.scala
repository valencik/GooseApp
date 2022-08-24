/*
 * Copyright 2022 io.pig
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pig.gooseapp

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.comcast.ip4s._
import org.http4s.client.Client
import org.http4s.ember.server._
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.HttpRoutes

trait GooseApp {

  def routes: HttpRoutes[IO]

  def httpApp = routes.orNotFound

  // TODO might be nice to finalize the client
  final lazy val httpClient: Client[IO] =
    EmberClientBuilder
      .default[IO]
      .build
      .allocated
      .unsafeRunSync()
      ._1

  final def main(args: Array[String]): Unit = {
    val server = EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build
      .use(_ => IO.never)
      .void
    server.unsafeRunSync()
  }
}
