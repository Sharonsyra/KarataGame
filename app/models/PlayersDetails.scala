package models

import common.GameServerHelpers.{DoubleCardGame, GameType, SingleCardGame}

object PlayersDetails {

  var playersDict: Map[String, Player] = Map.empty[String, Player]

  var singleCardList: Seq[String] = Seq.empty[String]

  var doubleCardList: Seq[String] = Seq.empty[String]

  /**
   * Retrieve player details from player uuid
   *
   * @param playerUuid player uuid
   * @return Player
   */
  def retrievePlayerDetails(playerUuid: String): Player = {
    playersDict.get(playerUuid) match {
      case Some(player) => player
      case None => throw new Exception("player not found")
    }
  }

  /**
   * Remove player details at the end of the game
   *
   * @param playerUuid player uuid
   */
  def removePlayerAtEndOfGame(playerUuid: String): Unit = {
    playersDict = playersDict - playerUuid
  }

  /**
   * Add player to game waiting list
   *
   * @param playerUuid player uuid
   * @param gameType   player game type
   */
  def addPlayerToWaitingList(playerUuid: String, gameType: GameType): Unit = {
    val player = retrievePlayerDetails(playerUuid)
    player.gameType = Some(gameType)
    println(s"adding player ${player.playerName} to $gameType waiting list")
    gameType match {
      case SingleCardGame =>
        singleCardList = singleCardList.appended(playerUuid)
      case DoubleCardGame =>
        doubleCardList = doubleCardList.appended(playerUuid)
    }
  }

  /**
   * Remove player from game waiting list
   *
   * @param playerUuid player uuid
   * @param gameType   player game type
   */
  def removePlayerFromWaitingList(playerUuid: String, gameType: GameType): Unit = {
    val player = retrievePlayerDetails(playerUuid)
    player.gameType = None
    println(s"removing player ${player.playerName} from $gameType waiting list")
    gameType match {
      case SingleCardGame =>
        singleCardList = singleCardList.filterNot(_.equals(playerUuid))
      case DoubleCardGame =>
        doubleCardList = doubleCardList.filterNot(_.equals(playerUuid))
    }
  }

}
