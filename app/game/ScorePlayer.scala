package game

import common.GameServerHelpers.GameOutcome
import models.Player

trait ScorePlayer {

  /**
   * Compute results when both players fold
   *
   * @param player1 Player 1
   * @param player2 Player 2
   * @return GameOutcome
   */
  def twoFolds(player1: Player, player2: Player): GameOutcome

  /**
   * Compute results when one player plays and one player fold
   *
   * @param playPlayer Play Player
   * @param foldPlayer Fold Player
   * @return GameOutcome
   */
  def onePlayOneFold(playPlayer: Player, foldPlayer: Player): GameOutcome

  /**
   * Compute results when both players index
   *
   * @param player1 Player 1
   * @param player2 Player 2
   * @return GameOutcome
   */
  def twoPlays(player1: Player, player2: Player): GameOutcome
}
