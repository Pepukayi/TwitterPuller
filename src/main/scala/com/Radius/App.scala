package com.Radius

/**
 * @author ${user.name}
 */

import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.entities.{HashTag, Tweet}
import scala.concurrent.ExecutionContext.Implicits.global

object App {

  val Accesstoken = "190575331-J5SJ7Tc9YBZnbuRzP6Stvjw0xvJwTO4ADIwaNU6M"
  val AccessSecret = "2E9qL3ArhnxU5r1mxU6lTqLRPrXaK40A4qYhz5hV7cz8C"
  val ConsumerKey = "fiXrjWdCPdYl7IqqSag3tjkLG"
  val ConsumerSecret = "7JTD51VHxt3hV7OtyOe7Jfd0sXLrXsm3MnwpmR5pEwULSItY4S"


  def main(args: Array[String]) {


    val consumerToken = ConsumerToken(ConsumerKey, ConsumerSecret)
    val accessToken = AccessToken(Accesstoken, AccessSecret)
    val client = new TwitterClient(consumerToken, accessToken)

    def getTopHashtags(tweets: Seq[Tweet], n: Int = 10): Seq[(String, Int)] = {
      val hashtags: Seq[Seq[HashTag]] = tweets.map { tweet =>
        tweet.entities.map(_.hashtags).getOrElse(Seq.empty)
      }
      val hashtagTexts: Seq[String] = hashtags.flatten.map(_.text.toLowerCase)
      val hashtagFrequencies: Map[String, Int] = hashtagTexts.groupBy(identity).mapValues(_.size)
      hashtagFrequencies.toSeq.sortBy { case (entity, frequency) => -frequency }.take(n)
    }

    val user = "odersky"

    client.getUserTimelineForUser(screen_name = user, count = 200).map { tweets =>
      val topHashtags: Seq[((String, Int), Int)] = getTopHashtags(tweets).zipWithIndex
      val rankings = topHashtags.map { case ((entity, frequency), idx) => s"[${idx + 1}] $entity (found $frequency times)" }
      println(s"${user.toUpperCase}'S TOP HASHTAGS:")
      println(rankings.mkString("\n"))

    }
  }

}
