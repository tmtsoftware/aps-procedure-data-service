package org.tmt.apsproceduredataservice.impl

import org.tmt.apsproceduredataservice.core.models.{
  AdminGreetResponse,
  ComputationKeyValuePair,
  ComputationKeyValuePairKey,
  ComputationKeyValuePairList,
  GetProcedureResultDataRequest,
  GreetResponse,
  UserInfo
}
import org.tmt.apsproceduredataservice.service.ApsproceduredataserviceService

import scala.collection.concurrent.TrieMap
import scala.concurrent.Future

class ApsproceduredataserviceImpl() extends ApsproceduredataserviceService {

  // In-memory store keyed by (procedureRunId, computationName, fieldName, iterationNumber).
  // This prototype implementation holds results in memory only.
  // A production implementation would persist to the APS-PEAS database.
  private val store = TrieMap.empty[StoreKey, ComputationKeyValuePair]

  private case class StoreKey(
      procedureRunId: Int,
      computationName: String,
      fieldName: String,
      iterationNumber: Option[Int]
  )

  // ── Existing ───────────────────────────────────────────────────────────────

  def greeting(userInfo: UserInfo): Future[GreetResponse] =
    Future.successful(GreetResponse(userInfo))

  def adminGreeting(userInfo: UserInfo): Future[AdminGreetResponse] =
    Future.successful(AdminGreetResponse(userInfo))

  // ── POST /storeProcedureComputationResults ─────────────────────────────────

  def storeProcedureComputationResults(request: ComputationKeyValuePairList): Future[Unit] = {
    request.keyValuePairList.foreach { kvp =>
      val k = StoreKey(
        procedureRunId  = kvp.key.procedureRunId.getOrElse(request.procedureRunId),
        computationName = kvp.key.computationName,
        fieldName       = kvp.key.fieldName,
        iterationNumber = kvp.key.iterationNumber
      )
      store.put(k, kvp)
    }
    Future.unit
  }

  // ── POST /getProcedureResultData ───────────────────────────────────────────

  def getProcedureResultData(request: GetProcedureResultDataRequest): Future[List[ComputationKeyValuePair]] = {
    val results = request.computationResultKeys.flatMap { resultKey =>
      val k = StoreKey(
        procedureRunId  = request.procedureRunId,
        computationName = resultKey.computationName,
        fieldName       = resultKey.fieldName,
        iterationNumber = resultKey.iterationNumber
      )
      store.get(k)
    }
    Future.successful(results)
  }
}
