package org.tmt.apsproceduredataservice.http

import csw.location.api.codec.LocationCodecs
import io.bullet.borer.Codec
import io.bullet.borer.compat.PekkoHttpCompat
import io.bullet.borer.derivation.MapBasedCodecs.deriveCodec
import org.tmt.apsproceduredataservice.core.models.{AdminGreetResponse, GreetResponse, UserInfo}

// #for-docs-snippet
object HttpCodecs extends HttpCodecs
// #for-docs-snippet

trait HttpCodecs extends PekkoHttpCompat with LocationCodecs {
  implicit lazy val greetResponseCodec: Codec[GreetResponse]           = deriveCodec
  implicit lazy val adminGreetResponseCodec: Codec[AdminGreetResponse] = deriveCodec
  implicit lazy val userInfoCodec: Codec[UserInfo]                     = deriveCodec
}
