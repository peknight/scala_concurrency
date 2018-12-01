package org.learningconcurrency
package ch1

object Practice {

  def compose[A, B, C](g: B => C, f: A => B): A => C = x => g(f(x))

  def fuse[A, B](a: Option[A], b: Option[B]): Option[(A, B)] = for (x <- a; y <- b) yield (x, y)

  def check[T](xs: Seq[T])(pred: T => Boolean): Boolean = xs forall pred

  case class Pair[P, Q](first: P, second: Q)

  def permutations(x: String): Seq[String] = x.sortBy(_.toLower).reverse.map(_.toString);

  def main(args: Array[String]): Unit = {
    def g: Int => Int = _ * 2
    def f: String => Int = _.toInt
    log(compose(g, f)("5"))



    log(fuse(Some(1), Some(2)))
    log(fuse(Some(1), None))
    log(fuse(None, Some(1)))
    log(fuse(None, None))

    log(check(1 until 10)(40 / _ > 0))

    log(permutations("Hello World!"))


  }
}
