package org.tmt.apsproceduredataservice

import caseapp.{CommandName, ExtraName, HelpMessage}

sealed trait ApsproceduredataserviceAppCommand

object ApsproceduredataserviceAppCommand {

  @CommandName("start")
  final case class StartOptions(
      @HelpMessage("port of the app")
      @ExtraName("p")
      port: Option[Int]
  ) extends ApsproceduredataserviceAppCommand

}
