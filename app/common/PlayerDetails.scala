package common

import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import models.Player

object PlayerDetails {

  final case class PlayerDetailsJson(
    playerUuid: String,
    balance: Double,
    gameType: String,
    pairedPlayer: String,
    dealtCards: String,
    playerResult: String
  )

  /**
   * Serialize player details to json
   *
   * @param player Player
   * @return Json
   */
  def createPlayerDetailsJson(player: Player): Json = {
    PlayerDetailsJson(
      playerUuid = player.playerUuid,
      balance = player.balance,
      gameType = player.gameType.toString,
      pairedPlayer = player.pairedPlayer,
      dealtCards = player.dealtCards.map(_.toString).mkString(""),
      playerResult = player.playerResult.toString
    ).asJson
  }
}
