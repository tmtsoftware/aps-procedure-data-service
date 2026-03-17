package org.tmt.apsproceduredataservice.http

import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.server.Route
import csw.aas.http.AuthorizationPolicy.RealmRolePolicy
import csw.aas.http.SecurityDirectives
import org.tmt.apsproceduredataservice.service.ApsproceduredataserviceService
import org.tmt.apsproceduredataservice.core.models.UserInfo

import scala.concurrent.ExecutionContext

class ApsproceduredataserviceRoute(service1: ApsproceduredataserviceService, service2: JApsproceduredataserviceImplWrapper, securityDirectives: SecurityDirectives) (implicit  ec: ExecutionContext) extends HttpCodecs {

 val route: Route = post {
    path("greeting") {
      entity(as[UserInfo]) { userInfo =>
        complete(service1.greeting(userInfo))
      }
    } ~
    path("adminGreeting") {
      securityDirectives.sPost(RealmRolePolicy("Esw-user")) { token =>
        entity(as[UserInfo]) { userInfo => complete(service1.adminGreeting(userInfo)) }
      }
    }
  } ~
    path("sayBye") {
      complete(service2.sayBye())
    }
}

