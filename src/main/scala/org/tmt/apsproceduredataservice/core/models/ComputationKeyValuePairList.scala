package org.tmt.apsproceduredataservice.core.models

import java.util.List

case class ComputationKeyValuePairList(
    procedureRunId: Int,
    keyValuePairList: List[ComputationKeyValuePair]
)
