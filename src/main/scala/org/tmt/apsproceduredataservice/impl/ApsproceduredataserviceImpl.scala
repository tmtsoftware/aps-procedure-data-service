package org.tmt.apsproceduredataservice.impl

import org.tmt.apsproceduredataservice.core.models.{AdminGreetResponse, GreetResponse, UserInfo}
import org.tmt.apsproceduredataservice.service.ApsproceduredataserviceService

import scala.concurrent.Future

class ApsproceduredataserviceImpl() extends ApsproceduredataserviceService{
  def greeting(userInfo: UserInfo): Future[GreetResponse] = Future.successful(GreetResponse(userInfo))

  def adminGreeting(userInfo: UserInfo): Future[AdminGreetResponse] =
    Future.successful(AdminGreetResponse(userInfo))
}
