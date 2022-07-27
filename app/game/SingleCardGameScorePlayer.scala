package game

import GameSession.rateCards
import common.GameServerHelpers.GameOutcome
import models.Player
import game.SessionResult.{drawResult, oneWinOneLossResult, twoLossesResult}

object SingleCardGameScorePlayer extends ScorePlayer {

  /**
   * Compute results when both players fold
   *
   * @param player1 Player 1
   * @param player2 Player 2
   * @return GameOutcome
   */
  def twoFolds(player1: Player, player2: Player): GameOutcome = {
    twoLossesResult(player1 = player1, player2 = player2, token = 1)
  }

  /**
   * Compute results when one player plays and one player fold
   *
   * @param playPlayer Play Player
   * @param foldPlayer Fold Player
   * @return GameOutcome
   */
  def onePlayOneFold(playPlayer: Player, foldPlayer: Player): GameOutcome = {
    oneWinOneLossResult(winPlayer = playPlayer, losePlayer = foldPlayer, token = 3)
  }

  /**
   * Compute results when both players index
   *
   * @param player1 Player 1
   * @param player2 Player 2
   * @return GameOutcome
   */
  def twoPlays(player1: Player, player2: Player): GameOutcome = {
    rateCards(player1.dealtCards.head, player2.dealtCards.head) match {
      case 1 =>
        oneWinOneLossResult(winPlayer = player1, losePlayer = player2, token = 10)
      case -1 =>
        oneWinOneLossResult(winPlayer = player2, losePlayer = player1, token = 10)
      case 0 =>
        drawResult(player1 = player1, player2)
    }
  }
}

