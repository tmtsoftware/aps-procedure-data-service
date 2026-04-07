package org.tmt.apsproceduredataservice.core.models

import java.util.List

case class GetProcedureResultDataRequest(
    procedureRunId: Int,
    computationResultKeys: List[ComputationResultKey]
)
