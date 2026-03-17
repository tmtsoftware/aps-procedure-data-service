package org.tmt.apsproceduredataservice.impl

import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.tmt.apsproceduredataservice.core.models.{AdminGreetResponse, GreetResponse, UserInfo}

class ApsproceduredataserviceImplTest extends AnyWordSpec with Matchers {

  "ApsproceduredataserviceImpl" must {
    "greeting should return greeting response of 'Hello user'" in {
      val apsproceduredataserviceImpl = new ApsproceduredataserviceImpl()
      apsproceduredataserviceImpl.greeting(UserInfo("John", "Smith")).futureValue should ===(GreetResponse("Hello user: John Smith!!!"))
    }

    "adminGreeting should return greeting response of 'Hello admin user'" in {
      val apsproceduredataserviceImpl = new ApsproceduredataserviceImpl()
      apsproceduredataserviceImpl.adminGreeting(UserInfo("John", "Smith")).futureValue should ===(AdminGreetResponse("Hello admin user: John Smith!!!"))
    }
  }
}
