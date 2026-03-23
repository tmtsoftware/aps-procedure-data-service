package org.tmt.apsproceduredataservice.core.models

case class GetProcedureResultDataRequest(
    procedureRunId: Int,
    computationResultKeys: List[ComputationResultKey]
)
