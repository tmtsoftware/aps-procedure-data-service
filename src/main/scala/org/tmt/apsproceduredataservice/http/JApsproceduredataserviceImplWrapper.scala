package org.tmt.apsproceduredataservice.http

import org.tmt.apsproceduredataservice.impl.JApsproceduredataserviceImpl
import org.tmt.apsproceduredataservice.core.models.GreetResponse

import scala.jdk.FutureConverters.*
import scala.concurrent.Future

class JApsproceduredataserviceImplWrapper(jApsproceduredataserviceImpl: JApsproceduredataserviceImpl) {
  def sayBye(): Future[GreetResponse] = jApsproceduredataserviceImpl.sayBye().asScala
}
