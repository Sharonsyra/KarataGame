package common

object CardDeck {

  sealed trait Suit

  sealed case class Hearts() extends Suit {
    override def toString = "♡"
  }

  sealed case class Diamonds() extends Suit {
    override def toString = "♢"
  }

  sealed case class Clubs() extends Suit {
    override def toString = "♧"
  }

  sealed case class Spades() extends Suit {
    override def toString = "♤"
  }

  sealed class Rank(val value: Int) {
    override def toString = s"$value"
  }

  sealed case class Ace() extends Rank(14) {
    override def toString = "A"
  }

  sealed case class King() extends Rank(13) {
    override def toString = "K"
  }

  sealed case class Queen() extends Rank(12) {
    override def toString = "Q"
  }

  sealed case class Jack() extends Rank(11) {
    override def toString = "J"
  }

  sealed case class Ten() extends Rank(10)

  sealed case class Nine() extends Rank(9)

  sealed case class Eight() extends Rank(8)

  sealed case class Seven() extends Rank(7)

  sealed case class Six() extends Rank(6)

  sealed case class Five() extends Rank(5)

  sealed case class Four() extends Rank(4)

  sealed case class Three() extends Rank(3)

  sealed case class Two() extends Rank(2)

  case class Card(suit: Suit, rank: Rank) {
    override def toString: String = rank.toString + suit.toString
  }

  def suits(): Seq[Suit] = Seq(Hearts(), Diamonds(), Clubs(), Spades())

  def ranks(): Seq[Rank] = Seq(
    Ace(), King(), Queen(), Jack(), Ten(), Nine(), Eight(), Seven(), Six(), Five(), Four(), Three(), Two()
  )

  type Deck = Seq[Card]
  type Hand = Seq[Card]

  def deck(): Deck = for (
    s <- suits();
    r <- ranks()
  ) yield Card(s, r)

  implicit object DefaultOrder extends Ordering[Card] {
    def compare(card1: Card, card2: Card): Int = card1.rank.value compare card2.rank.value
  }
}
