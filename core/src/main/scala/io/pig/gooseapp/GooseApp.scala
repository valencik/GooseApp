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
import com.comcast.ip4s._
import org.http4s.client.Client
import org.http4s.ember.server._
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.HttpRoutes
import cats.effect.IOApp
import cats.effect.ExitCode

trait GooseApp extends IOApp {

  def routes(): HttpRoutes[IO]

  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(routes().orNotFound)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
}

object GooseApp {
  trait WithHttpClient extends IOApp {

    def routes(client: Client[IO]): HttpRoutes[IO]

    private def setupClient =
      EmberClientBuilder
        .default[IO]
        .build

    def run(args: List[String]): IO[ExitCode] =
      setupClient
        .flatMap { c =>
          EmberServerBuilder
            .default[IO]
            .withHost(ipv4"0.0.0.0")
            .withPort(port"8080")
            .withHttpApp(routes(c).orNotFound)
            .build
        }
        .use(_ => IO.never)
        .as(ExitCode.Success)
  }
}
