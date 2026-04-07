package org.tmt.apsproceduredataservice.impl

import org.tmt.apsproceduredataservice.core.models.{AdminGreetResponse, GreetResponse, UserInfo}
import org.tmt.apsproceduredataservice.service.ApsproceduredataserviceService

import scala.concurrent.Future

// The two new procedure data methods are implemented in Java via
// JApsproceduredataserviceImpl — this Scala impl handles only the
// existing greeting endpoints.
class ApsproceduredataserviceImpl() extends ApsproceduredataserviceService {

  def greeting(userInfo: UserInfo): Future[GreetResponse] =
    Future.successful(GreetResponse(userInfo))

  def adminGreeting(userInfo: UserInfo): Future[AdminGreetResponse] =
    Future.successful(AdminGreetResponse(userInfo))
}
