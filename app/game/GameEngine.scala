package game

import common.GameServerHelpers.{DoubleCardGame, SingleCardGame}
import game.GameSession.startGameSession
import models.PlayersDetails.{doubleCardList, playersDict, removePlayerFromWaitingList, singleCardList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GameEngine {

  /**
   * Tracks the waiting lists for SingleGameType and DoubleGameType
   *
   */
  def waitingListTracker(): Future[Unit] = {
    Future {
      while (true) {
        // Keep querying the waiting list to start game sessions
        println(s"SingleCardGame waiting list: ${singleCardList.mkString(", ")}")
        println(s"DoubleCardGame waiting list: ${doubleCardList.mkString(", ")}")
        if (singleCardList.length >= 2) {
          println(s"we are 2")
          val players = singleCardList.take(2)
          removePlayerFromWaitingList(playerUuid = players.head, gameType = SingleCardGame)
          removePlayerFromWaitingList(playerUuid = players.last, gameType = SingleCardGame)
          startGameSession(
            player1 = playersDict(players.head),
            player2 = playersDict(players.last),
            gameType = SingleCardGame
          )
        }

        if (doubleCardList.length >= 2) {
          println(s"starting a double card game session")
          val players = doubleCardList.take(2)
          removePlayerFromWaitingList(playerUuid = players.head, gameType = DoubleCardGame)
          removePlayerFromWaitingList(playerUuid = players.last, gameType = DoubleCardGame)
          startGameSession(
            player1 = playersDict(players.head),
            player2 = playersDict(players.last),
            gameType = DoubleCardGame
          )
        }

        Thread.sleep(5000)
      }
    }
  }
}
