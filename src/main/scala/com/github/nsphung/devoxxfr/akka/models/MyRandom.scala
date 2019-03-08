package com.github.nsphung.devoxxfr.akka.models

import java.time.{ZoneOffset, ZonedDateTime}

import org.apache.commons.lang3.StringUtils
import io.circe.syntax._
import com.github.nsphung.devoxxfr.akka.models.JsonImplicits._

import scala.util.Random

object MyRandom {
  // -- Random EXPECTED_TICKETS_NUMBER
  val r = scala.util.Random

  // -- Random values list
  val originStationValues = Seq("FRNTE", "FRPMO", "FRBOJ", "FRLPD", "FRVLA", "FRRNS", "FRMLW", "FRXYT", "FRAIE")
  val profileValues = Seq("ESCALE", "ASCT", "")
  val coursesIdValues = Seq("2017-11-21-1511218800-SN-8832", "2018-03-09-1520550000-SN-8389", "2018-03-09-1520550000-SN-165661", "2018-03-09-1520550000-SN-165645", "2018-03-09-1520550000-SN-8127", "2018-03-09-1520550000-SN-862481")
  val brandValues = Seq("TGV", "TGV Inoui", "Intercités", "Transilien", "TER", "123456789123456789123456789ppp")
  val cpValues = Seq("FLLX11831", "FMBT12931", "FMBT12931", "FGAG04831", "FTPT04751")
  val deviceIdValues = Seq("b8dc4bc81029d8f0", "7f9df0de7a613090", "3ad48595a05f6b95", "247d23ac681a5b44")
  val missionStatusStatus = Seq("OK", "KO")
  val motifsValues = Seq("motifs dE LEVEéèA¨^ÏD'EMBARQUement GGGGGGqlskdjGGzERZERZEREZEQQSDQSDQDQDsdfjdj5", "réfusé", "Un certain motif", "Motif un peu long")
  val ticketPassengerCardNumberValues = Seq("", "29090109062582266", "29090109937531695", "29090109000020925")
  val ticketCodeValues = Seq("DV", "CV", "FV", "DT", "IR", "IV", "IY")
  val segmentFareCodeValues = Seq("FA00", "PR11", "PN00", "GAFBDE", "MBSPRM13", "MBSECTX", "MBWP2SRO")

  def generateTicketTrainOk(n: Int): String = {
    var result = ""
    for (i <- 1 to n) {
      if (result.isEmpty) {
        result = s"${extractTrainNumberFromCourseId(generateRandomFromList(coursesIdValues))}"
      } else {
        result = s"$result-${extractTrainNumberFromCourseId(generateRandomFromList(coursesIdValues))}"
      }
    }
    result
  }

  def extractTrainNumberFromCourseId(courseId: String) = {
    StringUtils.leftPad(StringUtils.substringAfterLast(courseId, "-"), 6, "0")
  }

  def generateRandomFromList[T](mySeq: Seq[T]) = {
    Random.shuffle(mySeq.toList).head
  }

  def generateTicketScan() = TicketScan(
    EVENT_DATETIME = generateZoneDateTime(),
    COURSES = generateRandomCourses(generateRandomNumberFromTo(1, 3)),
    ORIGIN_STATION = generateRandomFromList(originStationValues),
    DESTINATION_STATION = generateRandomFromList(originStationValues),
    DEVICE_ID = generateRandomFromList(deviceIdValues),
    CP_NUMBER = generateRandomFromList(cpValues),
    PROFILE = generateRandomFromList(profileValues),
    TICKET_CODE = "CW",
    TICKET_PASSENGER_IUC = "565f4847-2ee3-4545-ac2d-a611e8f6d422",
    TICKET_DV = "QRODOZ",
    TICKET_TCN = "577813165",
    TICKET_CHECK_STATUS = "ACCEPTED",
    TICKET_TRAIN_OK = generateTicketTrainOk(generateRandomNumberFromTo(1, 6)),
    TICKET_STATUS = "ACTIVE",
    TICKET_LOYALTY_STATUS = "VO",
    TICKET_PASSENGER_CARD_NUMBER = generateRandomFromList(ticketPassengerCardNumberValues),
    TICKET_SEGMENTS = generateTicketSegments(generateRandomNumberFromTo(1, 3)) // 1 -> 3
  )

  def generateCourseBoardingStart(): CourseBoardingStart = CourseBoardingStart(
    EVENT_DATETIME = generateZoneDateTime(),
    COURSE = generateRandomCourse(),
    ORIGIN_STATION = generateRandomFromList(originStationValues),
    CP_NUMBER = generateRandomFromList(cpValues),
    DESTINATION_STATION = generateRandomFromList(originStationValues),
    DEVICE_ID = generateRandomFromList(deviceIdValues),
    EXPECTED_TICKETS_NUMBER = generateRandomNumberFromTo(0, 9999),
    PROFILE = generateRandomFromList(profileValues)
  )

  def generateCourseBoardingEnd(): CourseBoardingEnd = CourseBoardingEnd(
    EVENT_DATETIME = generateZoneDateTime(),
    ORIGIN_STATION = generateRandomFromList(originStationValues),
    DESTINATION_STATION = generateRandomFromList(originStationValues),
    DEVICE_ID = generateRandomFromList(deviceIdValues),
    CP_NUMBER = generateRandomFromList(cpValues),
    PROFILE = generateRandomFromList(profileValues),
    COURSE = generateRandomCourse(),
    MISSION_STATUS = generateMissionStatus()
  )

  def generateRandomCourse(): Course = {
    val courseId = generateRandomFromList(coursesIdValues)
    Course(
      COURSE_ID = courseId,
      COURSE_DATE = generateZoneDateTime(),
      COURSE_NUMBER = extractTrainNumberFromCourseId(courseId),
      BRAND = generateRandomFromList(brandValues)
    )
  }

  def generateRandomCourses(n: Int): Seq[Course] = {
    val courseId = generateRandomFromList(coursesIdValues)

    var result = Seq.empty[Course]

    for (i <- 1 to n) {
      result = result :+ generateRandomCourse()
    }
    result
  }

  def generateMissionStatus(): MissionStatus = {
    val status = generateRandomFromList(missionStatusStatus)
    if ("OK".equalsIgnoreCase(status)) {
      MissionStatus(status)
    } else {
      MissionStatus(
        status,
        generateRandomFromList(motifsValues)
      )
    }
  }

  def generateTicketSegments(n: Int): Seq[TicketSegment] = {
    val courseId = generateRandomFromList(coursesIdValues)

    var result = Seq.empty[TicketSegment]

    for (i <- 1 to n) {
      result = result :+ TicketSegment(
        SEGMENT_DEPARTURE_DATE_TIME = generateZoneDateTime(),
        SEGMENT_DESTINATION_STATION = generateRandomFromList(originStationValues),
        SEGMENT_FARE_CODE = generateRandomFromList(segmentFareCodeValues),
        SEGMENT_ORIGIN_STATION = generateRandomFromList(originStationValues),
        SEGMENT_TRAIN_NUMBER = extractTrainNumberFromCourseId(courseId)
      )
    }
    result
  }

  def generateZoneDateTime(): ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)
    .plusHours(generateRandomNumberFromTo(1, 6))
    .plusMinutes(generateRandomNumberFromTo(12, 35))


  def generateRandomNumberFromTo(start: Int, end: Int) = {
    start + r.nextInt((end - start) + 1)
  }

  // Generator
  def generateLine(): String = {
    generateRandomNumberFromTo(1, 10) match {
      case 1 => s"${generateCourseBoardingStart().asJson.noSpaces}\n"
      case 2 => s"${generateCourseBoardingEnd().asJson.noSpaces}\n"
      case _ => s"${generateTicketScan().asJson.noSpaces}\n"
    }
  }

}
