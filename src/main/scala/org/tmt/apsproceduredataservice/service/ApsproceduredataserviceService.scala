package org.tmt.apsproceduredataservice.service

import org.tmt.apsproceduredataservice.core.models.{AdminGreetResponse, GreetResponse, UserInfo}

import scala.concurrent.Future

// The two new procedure data methods (getProcedureResultData,
// storeProcedureComputationResults) are implemented in Java via
// JApsproceduredataserviceImpl and exposed through
// JApsproceduredataserviceImplWrapper — they do not belong on this trait.
trait ApsproceduredataserviceService {
  def greeting(userInfo: UserInfo): Future[GreetResponse]
  def adminGreeting(userInfo: UserInfo): Future[AdminGreetResponse]
}
