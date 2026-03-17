package org.tmt.apsproceduredataservice.http

import java.util.concurrent.CompletableFuture

import org.mockito.Mockito.{verify, when}
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.tmt.apsproceduredataservice.impl.JApsproceduredataserviceImpl
import org.tmt.apsproceduredataservice.core.models.GreetResponse

class JApsproceduredataserviceImplWrapperTest extends AnyWordSpec with Matchers {

  "ApsproceduredataserviceImplWrapper" must {
    "delegate sayBye to JApsproceduredataserviceImpl.sayBye" in {
      val jApsproceduredataserviceImpl       = mock[JApsproceduredataserviceImpl]
      val apsproceduredataserviceImplWrapper = new JApsproceduredataserviceImplWrapper(jApsproceduredataserviceImpl)

      val apsproceduredataserviceResponse = mock[GreetResponse]
      when(jApsproceduredataserviceImpl.sayBye()).thenReturn(CompletableFuture.completedFuture(apsproceduredataserviceResponse))

      apsproceduredataserviceImplWrapper.sayBye().futureValue should ===(apsproceduredataserviceResponse)
      verify(jApsproceduredataserviceImpl).sayBye()
    }
  }
}
