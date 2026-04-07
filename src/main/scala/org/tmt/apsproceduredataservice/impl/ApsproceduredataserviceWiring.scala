package org.tmt.apsproceduredataservice.impl

import org.apache.pekko.http.scaladsl.server.Route
import esw.http.template.wiring.ServerWiring
import org.tmt.apsproceduredataservice.http.{JApsproceduredataserviceImplWrapper, ApsproceduredataserviceRoute}

class ApsproceduredataserviceWiring(val port: Option[Int]) extends ServerWiring {
  override val actorSystemName: String = "apsproceduredataservice-actor-system"

  lazy val jApsproceduredataserviceImpl: JApsproceduredataserviceImpl = new JApsproceduredataserviceImpl(jCswServices)
  lazy val apsproceduredataserviceImpl               = new ApsproceduredataserviceImpl()

  import actorRuntime.ec
  lazy val apsproceduredataserviceImplWrapper        = new JApsproceduredataserviceImplWrapper(jApsproceduredataserviceImpl)

  override lazy val routes: Route = new ApsproceduredataserviceRoute(apsproceduredataserviceImpl, apsproceduredataserviceImplWrapper, securityDirectives).route
}
