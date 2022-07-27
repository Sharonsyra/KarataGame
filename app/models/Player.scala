package models

import common.CardDeck.Card
import common.GameServerHelpers._

final case class Player(
  playerUuid: String,
  playerName: String,
  var balance: Double = 1000,
  var gameType: Option[GameType] = None,
  var pairedPlayer: String = "",
  var playersPick: Option[PlayersPick] = None,
  var dealtCards: Seq[Card] = Seq.empty[Card],
  var playerResult: Option[PlayerResult] = None,
  var gameOutcome: Option[GameOutcome] = None
)
