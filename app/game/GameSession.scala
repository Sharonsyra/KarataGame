package game

import common.CardDeck.{Card, deck}
import common.GameServerHelpers
import common.GameServerHelpers._
import models.Player

import scala.util.Random

object GameSession {

  /**
   * Set up a session
   *
   * @param player1  Player 1
   * @param player2  Player 2
   * @param gameType Game Type
   */
  def sessionSetup(player1: Player, player2: Player, gameType: GameType): Unit = {
    player1.pairedPlayer = player2.playerUuid
    player2.pairedPlayer = player1.playerUuid
  }

  /**
   * Deal cards for specified game type
   *
   * @param gameType Game Type to deal cards for
   * @return Cards dealt Seq[Card]
   */
  def dealCards(gameType: GameType): (Seq[Card], Seq[Card]) =
    gameType match {
      case SingleCardGame => Random.shuffle(deck()).take(2).splitAt(1)
      case DoubleCardGame => Random.shuffle(deck()).take(4).splitAt(2)
    }

  /**
   * Deal player cards and index
   *
   * @param player1  Player 1
   * @param player2  Player 2
   * @param gameType Game Type
   * @return GameOutcome
   */
  def assignPlayerDealCards(player1: Player, player2: Player, gameType: GameType): Unit = {
    player1.dealtCards = dealCards(gameType = gameType)._1
    player2.dealtCards = dealCards(gameType = gameType)._2
  }

  /**
   * Start a game session
   *
   * @param player1  Player 1
   * @param player2  Player 2
   * @param gameType Game Type
   * @return GameOutcome
   */
  def startGameSession(player1: Player, player2: Player, gameType: GameType): Option[GameOutcome] = {
    println(
      s"""
         |Starting a $gameType game session between:\n
         |${player1.playerName} with balance - ${player1.balance}
         |and ${player2.playerName} with balance ${player2.balance}
         |\n
         |""".stripMargin
    )

    sessionSetup(player1 = player1, player2 = player2, gameType = gameType)

    assignPlayerDealCards(player1 = player1, player2 = player2, gameType = gameType)

    Thread.sleep(25000) // Wait for players to index or pick

    (player1.playersPick, player2.playersPick) match {
      case (None, None) => None
      case _ =>
        val gameOutcome: GameOutcome = scorePlayers(gameType = gameType, player1 = player1, player2 = player2)
        println(
          s"""
             |The Game Session Outcome is $gameOutcome\n
             |${player1.playerName}'s new balance is ${player1.balance}
             |and ${player2.playerName}'s new balance is ${player2.balance}
             |\n
             |""".stripMargin
        )
        player1.gameOutcome = Some(gameOutcome)
        player2.gameOutcome = Some(gameOutcome)

        if (gameOutcome.equals(SessionDraw)) {
          assignPlayerDealCards(player1, player2, gameType)
        }
        Some(gameOutcome)
    }
  }

  /**
   * Compute game session results
   *
   * @param gameType Game Type
   * @param player1  Player 1
   * @param player2  Player 2
   * @return GameOutCome
   */
  def scorePlayers(gameType: GameType, player1: Player, player2: Player): GameOutcome = {
    (player1.playersPick, player2.playersPick) match {
      case (Some(Fold), Some(Fold)) =>
        gameType match {
          case SingleCardGame =>
            SingleCardGameScorePlayer.twoFolds(player1 = player1, player2 = player2)
          case DoubleCardGame =>
            DoubleCardGameScorePlayer.twoFolds(player1 = player1, player2 = player2)
        }
      case (Some(Play), Some(Fold)) =>
        gameType match {
          case SingleCardGame =>
            SingleCardGameScorePlayer.onePlayOneFold(playPlayer = player1, foldPlayer = player2)
          case DoubleCardGame =>
            DoubleCardGameScorePlayer.onePlayOneFold(playPlayer = player1, foldPlayer = player2)
        }
      case (Some(Fold), Some(Play)) =>
        gameType match {
          case SingleCardGame =>
            SingleCardGameScorePlayer.onePlayOneFold(playPlayer = player2, foldPlayer = player1)
          case DoubleCardGame =>
            DoubleCardGameScorePlayer.onePlayOneFold(playPlayer = player2, foldPlayer = player1)
        }
      case (Some(Play), Some(Play)) =>
        gameType match {
          case SingleCardGame =>
            SingleCardGameScorePlayer.twoPlays(player1 = player1, player2 = player2)
          case DoubleCardGame =>
            DoubleCardGameScorePlayer.twoPlays(player1 = player1, player2 = player2)
        }
      case _ => throw new Exception("invalid index")
    }
  }

  /**
   * Compare two cards
   *
   * @param card1 Card 1
   * @param card2 Card 2
   * @return Int
   */
  def rateCards(card1: Card, card2: Card): Int =
    card1.rank.value.compare(card2.rank.value)
}
