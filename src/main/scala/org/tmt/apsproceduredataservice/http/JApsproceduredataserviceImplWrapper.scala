package org.tmt.apsproceduredataservice.http

import org.tmt.apsproceduredataservice.impl.JApsproceduredataserviceImpl
import org.tmt.apsproceduredataservice.core.models.{
  ComputationKeyValuePair,
  ComputationKeyValuePairList,
  GetProcedureResultDataRequest,
  GreetResponse
}

import scala.jdk.FutureConverters.*
import scala.jdk.CollectionConverters.*
import scala.concurrent.{ExecutionContext, Future}

class JApsproceduredataserviceImplWrapper(jApsproceduredataserviceImpl: JApsproceduredataserviceImpl)(implicit ec: ExecutionContext) {

  // Existing
  def sayBye(): Future[GreetResponse] =
    jApsproceduredataserviceImpl.sayBye().asScala

  // POST /storeProcedureComputationResults
  def storeProcedureComputationResults(request: ComputationKeyValuePairList): Future[Unit] =
    jApsproceduredataserviceImpl.storeProcedureComputationResults(request).asScala.map(_ => ())

  // POST /getProcedureResultData
  def getProcedureResultData(request: GetProcedureResultDataRequest): Future[List[ComputationKeyValuePair]] =
    jApsproceduredataserviceImpl.getProcedureResultData(request).asScala.map(_.asScala.toList)
}
