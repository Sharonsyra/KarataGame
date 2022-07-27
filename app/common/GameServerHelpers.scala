package common

object GameServerHelpers {

  sealed trait GameType

  object SingleCardGame extends GameType {
    override def toString: String = "SingleCardGame"
  }

  object DoubleCardGame extends GameType {
    override def toString: String = "DoubleCardGame"
  }

  sealed trait PlayersPick

  object Play extends PlayersPick {
    override def toString: String = "Play"
  }

  object Fold extends PlayersPick {
    override def toString: String = "Fold"
  }

  sealed trait GameOutcome

  object TwoLosses extends GameOutcome {
    override def toString: String = "TwoLosses"
  }

  object OneWinOneLoss extends GameOutcome {
    override def toString: String = "OneWinOneLoss"
  }

  object SessionDraw extends GameOutcome {
    override def toString: String = "Draw"
  }

  sealed trait PlayerResult

  object GainTokens extends PlayerResult {
    override def toString: String = "gained tokens"
  }

  object LoseTokens extends PlayerResult {
    override def toString: String = "lost tokens"
  }

  object Draw extends PlayerResult {
    override def toString: String = "Drew"
  }
}
