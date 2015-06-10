package com.storecove.rollbar

import java.io.{ByteArrayOutputStream, PrintStream}
import java.net.InetAddress

import dispatch.{Http => HTTP, url => URL, _}
import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by acidghost on 07/06/15.
 */
private class RollbarNotifierImpl extends RollbarNotifier {

    def this(url: String, apiKey: String, environment: String, language: String, platform: String) = {
        this()
        setUrl(url)
        setApiKey(apiKey)
        setEnvironment(environment)
        setLanguage(language)
        setPlatform(platform)
    }

    override def notify(level: String, message: String, throwable: Option[Throwable], mdc: mutable.Map[String, String]): JValue = {
        val payload = buildPayload(level, message, throwable, mdc)
        //log(s"PAYLOAD is\n${compact(payload)}")

        //send payload to Rollbar
        val request = URL(url).
            setHeader("Content-Type", "application/json").
            setHeader("Accept", "application/json").
            setBody(compact(payload)).
            POST
        val future: Future[Either[Throwable, String]] = HTTP(request > as.String).either
        val response = for (e <- future.left) yield "Error!\t" + e.getMessage

        response() match {
            case Left(error) => error
            case Right(json) =>
                val respBody = parse(json)
                val err = respBody \\ "err"
                if (err == JInt(0)) {
                    respBody
                } else {
                    respBody \\ "message"
                }
        }
    }

    private def buildPayload(level: String, message: String, throwable: Option[Throwable], mdc: mutable.Map[String, String]): JObject = {
        val root = "access_token" -> apiKey

        val data = ("environment" -> environment) ~
                    ("level" -> level) ~
                    ("platform" -> mdc.getOrElse("platform", platform)) ~
                    ("framework" -> mdc.getOrElse("framework", platform)) ~
                    ("language" -> scala.language) ~
                    ("timestamp" -> System.currentTimeMillis() / 1000)

        val body = "body" -> getBody(message, throwable)

        //TODO HTTP request data

        val person = "person" -> getPerson(mdc)

        //TODO client data

        val server = "server" -> getServer

        val notifier = "notifier" -> getNotifier

        root ~ ("data" -> (data ~ body ~ person ~ server ~ notifier))
    }

    private def getBody(message: String, throwable: Option[Throwable]): JObject = {
        throwable match {
            case Some(t) =>
                val traces = mutable.ListBuffer.empty[JObject]
                var thr = t
                do {
                    traces += createTrace(thr)
                    thr = thr.getCause
                } while (thr != null)
                "trace_chain" -> traces
            case None =>
                "message" -> ("body" -> message)
        }
    }

    private def createTrace(throwable: Throwable): JObject = {
        val frames = throwable.getStackTrace.map { element =>
            val frame = ("class_name" -> element.getClassName) ~
                ("filename" -> element.getFileName) ~
                ("method" -> element.getMethodName)

            if (element.getLineNumber > 0) {
                frame ~ ("lineno" -> element.getLineNumber)
            } else {
                frame
            }
        }

        val raw = try {
            val baos = new ByteArrayOutputStream()
            val ps = new PrintStream(baos)
            throwable.printStackTrace(ps)
            ps.close()
            baos.close()
            Some(baos.toString("UTF-8"))
        } catch {
            case e: Exception => log("Exception printing stack trace.", e) ; None
        }

        ("frames" -> frames.toList) ~
            ("exception" ->
                ("class" -> throwable.getClass.getCanonicalName) ~
                    ("message" -> throwable.getMessage)) ~
            ("raw" -> raw)
    }

    private def getPerson(mdc: mutable.Map[String, String]): Option[JObject] = {
        mdc.get("user") match {
            case Some(id) => Some(("id" -> id) ~ ("username" -> mdc.get("username")) ~ ("email" -> mdc.get("email")))
            case _ => None
        }
    }

    private def getServer: JObject = {
        val localhost = InetAddress.getLocalHost
        ("host" -> localhost.getHostName) ~ ("ip" -> localhost.getHostAddress)
    }

    private def getNotifier: JObject = {
        ("name" -> notifierName) ~ ("version" -> notifierVersion)
    }
}
