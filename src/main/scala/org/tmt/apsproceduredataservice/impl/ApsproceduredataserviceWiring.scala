package org.tmt.apsproceduredataservice.impl

import org.apache.pekko.http.scaladsl.server.Route
import esw.http.template.wiring.ServerWiring
import org.tmt.apsproceduredataservice.http.{JApsproceduredataserviceImplWrapper, ApsproceduredataserviceRoute}

class ApsproceduredataserviceWiring(val port: Option[Int]) extends ServerWiring {
  override val actorSystemName: String = "apsproceduredataservice-actor-system"

  lazy val jApsproceduredataserviceImpl: JApsproceduredataserviceImpl = new JApsproceduredataserviceImpl(jCswServices)
  lazy val apsproceduredataserviceImpl               = new ApsproceduredataserviceImpl()

  import actorRuntime.ec
  lazy val apsproceduredataserviceImplWrapper =
    new JApsproceduredataserviceImplWrapper(jApsproceduredataserviceImpl)

  // Just an example of subscribing to the Event Service, which the Exposure Service will do.
  // The example code here is for implementation example only, it is never used by the Procedure Data Service.
  // Start the exposureStoreCompleted subscription when the service wires up.
  // Called after jApsproceduredataserviceImpl is constructed so jCswServices
  // is fully initialised before the event subscriber connects.
  jApsproceduredataserviceImpl.subscribeToExposureEvents()

  override lazy val routes: Route =
    new ApsproceduredataserviceRoute(
      apsproceduredataserviceImpl,
      apsproceduredataserviceImplWrapper,
      securityDirectives
    ).route
}