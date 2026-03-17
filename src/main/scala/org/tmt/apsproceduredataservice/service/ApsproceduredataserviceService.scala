package org.tmt.apsproceduredataservice.service

import org.tmt.apsproceduredataservice.core.models.{AdminGreetResponse, GreetResponse, UserInfo}

import scala.concurrent.Future

trait ApsproceduredataserviceService {
  def greeting(userInfo: UserInfo): Future[GreetResponse]
  def adminGreeting(userInfo: UserInfo): Future[AdminGreetResponse]
}
