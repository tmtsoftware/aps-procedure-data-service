package org.tmt.apsproceduredataservice.impl;

import esw.http.template.wiring.JCswServices;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.scalatestplus.testng.TestNGSuite;
import org.tmt.apsproceduredataservice.core.models.GreetResponse;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;

public class JApsproceduredataserviceImplTest extends TestNGSuite {

  @Test
  public void shouldCallBye() throws ExecutionException, InterruptedException {
    JCswServices mock = Mockito.mock(JCswServices.class);
    JApsproceduredataserviceImpl jApsproceduredataservice = new JApsproceduredataserviceImpl(mock);
    GreetResponse greetResponse = new GreetResponse("Bye!!!");
    assertThat(jApsproceduredataservice.sayBye().get(), CoreMatchers.is(greetResponse));
  }
}
