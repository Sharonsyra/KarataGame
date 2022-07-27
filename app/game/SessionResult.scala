package game

import common.CardDeck.Card
import common.GameServerHelpers.{Draw, GainTokens, GameOutcome, LoseTokens, OneWinOneLoss, SessionDraw, TwoLosses}
import models.Player

object SessionResult {

  /**
   * Update player details for two losses
   *
   * @param player1 Player 1
   * @param player2 Player 2
   * @param token   token value to deduct
   * @return Game outcome
   */
  def twoLossesResult(player1: Player, player2: Player, token: Int): GameOutcome = {
    player1.balance = player1.balance - token
    player2.balance = player2.balance - token
    player1.playerResult = Some(LoseTokens)
    player2.playerResult = Some(LoseTokens)
    TwoLosses
  }

  /**
   * Update player details for one win one loss
   *
   * @param winPlayer  winning player
   * @param losePlayer losing player
   * @param token      token value to add and deduct
   * @return Game outcome
   */
  def oneWinOneLossResult(winPlayer: Player, losePlayer: Player, token: Int): GameOutcome = {
    winPlayer.balance = winPlayer.balance + token
    losePlayer.balance = losePlayer.balance - token
    winPlayer.playerResult = Some(GainTokens)
    losePlayer.playerResult = Some(LoseTokens)
    OneWinOneLoss
  }

  /**
   * Update player details for a draw outcome
   *
   * @param player1 Player 1
   * @param player2 Player 2
   * @return Game outcome
   */
  def drawResult(player1: Player, player2: Player): GameOutcome = {
    player1.dealtCards = Seq.empty[Card]
    player2.dealtCards = Seq.empty[Card]
    player1.playersPick = None
    player2.playersPick = None
    player1.playerResult = Some(Draw)
    player2.playerResult = Some(Draw)
    SessionDraw
  }
}
