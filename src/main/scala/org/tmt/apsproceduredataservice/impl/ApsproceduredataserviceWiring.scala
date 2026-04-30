package org.tmt.apsproceduredataservice.impl

import org.apache.pekko.http.scaladsl.server.Route
import esw.http.template.wiring.ServerWiring
import org.tmt.apsproceduredataservice.db.ProcedureDbService
import org.tmt.apsproceduredataservice.http.{JApsproceduredataserviceImplWrapper, ApsproceduredataserviceRoute}

class ApsproceduredataserviceWiring(val port: Option[Int]) extends ServerWiring {
  override val actorSystemName: String = "apsproceduredataservice-actor-system"

  import actorRuntime.ec

  // ── DB service — pool opened via CSW DatabaseServiceFactory at startup ─────
  lazy val procedureDbService: ProcedureDbService =
    new ProcedureDbService(jCswServices.loggerFactory)

  // Initialise the DSL before the service starts accepting requests.
  // "peas" must match your [db.peas] entry in CSW config / application.conf.
  procedureDbService
    .init(actorRuntime.typedSystem, jCswServices, "peas")
    .get(5, java.util.concurrent.TimeUnit.SECONDS)

  lazy val jApsproceduredataserviceImpl: JApsproceduredataserviceImpl =
    new JApsproceduredataserviceImpl(jCswServices, procedureDbService)
  lazy val apsproceduredataserviceImpl = new ApsproceduredataserviceImpl()

  lazy val apsproceduredataserviceImplWrapper =
    new JApsproceduredataserviceImplWrapper(jApsproceduredataserviceImpl)

  // Just an example of subscribing to the Event Service, which the Exposure Service will do.
  // The example code here is for implementation example only, it is never used by the Procedure Data Service.
  // Start the exposureStoreCompleted subscription when the service wires up.
  // Called after jApsproceduredataserviceImpl is constructed so jCswServices
  // is fully initialised before the event subscriber connects.
  jApsproceduredataserviceImpl.subscribeToExposureEvents()

  // Shut down cleanly when the actor system terminates
  actorRuntime.typedSystem.whenTerminated.onComplete(_ => procedureDbService.close())

  override lazy val routes: Route =
    new ApsproceduredataserviceRoute(
      apsproceduredataserviceImpl,
      apsproceduredataserviceImplWrapper,
      securityDirectives
    ).route
}