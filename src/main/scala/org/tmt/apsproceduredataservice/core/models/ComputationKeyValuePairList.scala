package org.tmt.apsproceduredataservice.core.models

case class ComputationKeyValuePairList(
    procedureRunId: Int,
    keyValuePairList: List[ComputationKeyValuePair]
)
