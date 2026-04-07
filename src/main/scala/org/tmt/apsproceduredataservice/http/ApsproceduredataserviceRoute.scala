package org.tmt.apsproceduredataservice.http

import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.server.Route
import csw.aas.http.AuthorizationPolicy.RealmRolePolicy
import csw.aas.http.SecurityDirectives
import org.tmt.apsproceduredataservice.core.models.{
  ComputationKeyValuePairList,
  GetProcedureResultDataRequest,
  UserInfo
}
import org.tmt.apsproceduredataservice.service.ApsproceduredataserviceService

import scala.concurrent.ExecutionContext

class ApsproceduredataserviceRoute(
    service1: ApsproceduredataserviceService,
    service2: JApsproceduredataserviceImplWrapper,
    securityDirectives: SecurityDirectives
)(implicit ec: ExecutionContext)
    extends HttpCodecs {

  val route: Route =
    post {
      // ── Existing routes ───────────────────────────────────────────────────
      path("greeting") {
        entity(as[UserInfo]) { userInfo =>
          complete(service1.greeting(userInfo))
        }
      } ~
      path("adminGreeting") {
        securityDirectives.sPost(RealmRolePolicy("Esw-user")) { _ =>
          entity(as[UserInfo]) { userInfo =>
            complete(service1.adminGreeting(userInfo))
          }
        }
      } ~
      // ── POST /storeProcedureComputationResults — Java impl ─────────────────
      path("storeProcedureComputationResults") {
        entity(as[ComputationKeyValuePairList]) { request =>
          complete(service2.storeProcedureComputationResults(request))
        }
      } ~
      // ── POST /getProcedureResultData — Java impl ───────────────────────────
      path("getProcedureResultData") {
        entity(as[GetProcedureResultDataRequest]) { request =>
          complete(service2.getProcedureResultData(request))
        }
      }
    } ~
    path("sayBye") {
      complete(service2.sayBye())
    }
}
