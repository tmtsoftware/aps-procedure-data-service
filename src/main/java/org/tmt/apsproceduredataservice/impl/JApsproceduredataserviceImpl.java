package org.tmt.apsproceduredataservice.impl;

import esw.http.template.wiring.JCswServices;
import org.tmt.apsproceduredataservice.core.models.GreetResponse;

import java.util.concurrent.CompletableFuture;

public class JApsproceduredataserviceImpl {
  JCswServices jCswServices;

  public JApsproceduredataserviceImpl(JCswServices jCswServices) {
    this.jCswServices = jCswServices;
  }

  public CompletableFuture<GreetResponse> sayBye() {
    return CompletableFuture.completedFuture(new GreetResponse("Bye!!!"));
  }

}
