package org.tmt.apsproceduredataservice.http

import csw.location.api.codec.LocationCodecs
import io.bullet.borer.Codec
import io.bullet.borer.compat.PekkoHttpCompat
import io.bullet.borer.derivation.MapBasedCodecs.deriveCodec
import org.tmt.apsproceduredataservice.core.models.{
  AdminGreetResponse,
  ComputationKeyValuePair,
  ComputationKeyValuePairKey,
  ComputationKeyValuePairList,
  ComputationResultKey,
  GenericValue,
  GetProcedureResultDataRequest,
  GreetResponse,
  UserInfo
}

// #for-docs-snippet
object HttpCodecs extends HttpCodecs
// #for-docs-snippet

trait HttpCodecs extends PekkoHttpCompat with LocationCodecs {

  // Existing codecs
  implicit lazy val greetResponseCodec: Codec[GreetResponse]           = deriveCodec
  implicit lazy val adminGreetResponseCodec: Codec[AdminGreetResponse] = deriveCodec
  implicit lazy val userInfoCodec: Codec[UserInfo]                     = deriveCodec

  // New codecs — order matters: leaf types before composite types
  implicit lazy val genericValueCodec: Codec[GenericValue]                         = deriveCodec
  implicit lazy val computationResultKeyCodec: Codec[ComputationResultKey]         = deriveCodec
  implicit lazy val computationKvpKeyCodec: Codec[ComputationKeyValuePairKey]      = deriveCodec
  implicit lazy val computationKvpCodec: Codec[ComputationKeyValuePair]            = deriveCodec
  implicit lazy val computationKvpListCodec: Codec[ComputationKeyValuePairList]    = deriveCodec
  implicit lazy val getProcResultDataReqCodec: Codec[GetProcedureResultDataRequest] = deriveCodec
}
