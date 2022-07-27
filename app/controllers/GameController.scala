package controllers

import common.GameServerHelpers._
import game.GameEngine
import models.Player
import models.PlayersDetails.{addPlayerToWaitingList, playersDict, removePlayerAtEndOfGame, retrievePlayerDetails}
import play.api.mvc._

import java.util.UUID
import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class GameController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  val gameEngineInstance = new GameEngine

  gameEngineInstance.waitingListTracker()

  /**
   *
   *
   * @return
   */
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(balance = 1000))
  }

  def playerGameType: Action[AnyContent] = Action { implicit request =>
    request.body.asFormUrlEncoded match {
      case Some(value) =>
        val playerGameType: GameType = value.getOrElse("game_type", List.empty).head match {
          case "SingleCardGame" => SingleCardGame
          case "DoubleCardGame" => DoubleCardGame
          case _ => throw new Exception("invalid game type")
        }

        val playerName: Option[String] = value.getOrElse("player_name", List.empty).headOption

        val playedId: String = UUID.randomUUID().toString

        val playerDetails: Player = Player(
          playerUuid = playedId,
          playerName = playerName.getOrElse(""),
          gameType = Some(playerGameType)
        )

        playersDict += (
          playedId -> playerDetails
          )

        addPlayerToWaitingList(playerUuid = playedId, gameType = playerGameType)

        // wait for 20 seconds to be paired with another player.
        //TODO: Use events so a message is sent as soon as player is paired
        Thread.sleep(20000)

        if (playerDetails.pairedPlayer.nonEmpty) {
          Redirect(routes.GameController.getPlayersPick(playedId))
        } else {
          removePlayerAtEndOfGame(playedId)
          Ok("<p>time ran out. try again later</p>").as("text/html; charset=utf-8")
        }
      case None => Redirect(routes.GameController.playerGameType)
    }
  }

  def getPlayersPick(playerUuid: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val playerDetails = retrievePlayerDetails(playerUuid)
    val pairedPlayerDetails = retrievePlayerDetails(playerDetails.pairedPlayer)
    Ok(views.html.playorfold(playerUuid, pairedPlayerDetails.playerName, playerDetails.dealtCards.map(_.toString)))
  }

  def playOrFold(playerUuid: String): Action[AnyContent] = Action { implicit request =>
    val playerDetails = retrievePlayerDetails(playerUuid)

    request.body.asFormUrlEncoded match {
      case Some(value) =>
        val playersPick: PlayersPick = value.getOrElse("play_or_fold", List.empty).head match {
          case "Play" => Play
          case "Fold" => Fold
          case _ => throw new Exception("invalid index")
        }

        playerDetails.playersPick = Some(playersPick)

        // wait for scoring and pair player pick
        //TODO: Use events so a message is sent as soon as players pick and the game outcome is ready
        Thread.sleep(10000)

        playerDetails.gameOutcome match {
          case Some(gameOutcome) =>
            if (gameOutcome == SessionDraw) {
              println("The game ended in a draw. You have been assigned new cards to pick or index again")
              Redirect(routes.GameController.getPlayersPick(playerUuid))
            } else {
              removePlayerAtEndOfGame(playerUuid)
              Ok(s"<p>you ${playerDetails.playerResult.getOrElse("")}. " +
                s"You new balance is ${playerDetails.balance}</p>")
                .as("text/html; charset=utf-8")
            }
          case None =>
            removePlayerAtEndOfGame(playerUuid)
            Ok("<p>your pair did not pick or index. please try playing again</p>")
              .as("text/html; charset=utf-8")
        }
      case None => Redirect(routes.GameController.playOrFold(playerUuid))
    }
  }

}
