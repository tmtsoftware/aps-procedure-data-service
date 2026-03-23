package org.tmt.apsproceduredataservice.service

import org.tmt.apsproceduredataservice.core.models.{
  AdminGreetResponse,
  ComputationKeyValuePair,
  ComputationKeyValuePairList,
  GetProcedureResultDataRequest,
  GreetResponse,
  UserInfo
}

import scala.concurrent.Future

trait ApsproceduredataserviceService {

  // Existing
  def greeting(userInfo: UserInfo): Future[GreetResponse]
  def adminGreeting(userInfo: UserInfo): Future[AdminGreetResponse]

  // POST /getProcedureResultData
  // Retrieves a list of computation result key/value pairs for the requested keys.
  def getProcedureResultData(request: GetProcedureResultDataRequest): Future[List[ComputationKeyValuePair]]

  // POST /storeProcedureComputationResults
  // Stores a set of computation result key/value pairs.
  def storeProcedureComputationResults(request: ComputationKeyValuePairList): Future[Unit]
}
