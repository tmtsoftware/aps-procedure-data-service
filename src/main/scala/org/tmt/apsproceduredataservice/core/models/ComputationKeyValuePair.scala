package org.tmt.apsproceduredataservice.core.models

case class ComputationKeyValuePairKey(
    procedureRunId: Option[Int] = None,
    iterationNumber: Option[Int] = None,
    computationName: String,
    fieldName: String
)

case class ComputationKeyValuePair(
    key: ComputationKeyValuePairKey,
    value: GenericValue
)
