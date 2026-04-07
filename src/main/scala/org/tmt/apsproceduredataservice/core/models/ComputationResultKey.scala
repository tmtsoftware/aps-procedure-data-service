package org.tmt.apsproceduredataservice.core.models

import java.util.Optional

case class ComputationResultKey(
    computationName: String,
    fieldName: String,
    iterationNumber: Optional[Integer] = Optional.empty()
)
