package org.tmt.apsproceduredataservice.core.models

case class ComputationResultKey(
    computationName: String,
    fieldName: String,
    iterationNumber: Option[Int] = None
)
