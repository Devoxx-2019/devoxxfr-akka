package com.github.nsphung.devoxxfr.akka.models

import java.time.ZonedDateTime

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.java8.time._
import io.circe.{Decoder, Encoder, _}

case class Course
(
  COURSE_ID: String,
  COURSE_DATE: ZonedDateTime,
  COURSE_NUMBER: String,
  COURSE_FER_INDICATOR: String = "F",
  COURSE_CARRIER_CODE: String = "SN",
  BRAND: String
)

case class CourseBoardingStart
(
  EVENT_DATETIME: ZonedDateTime,
  EVENT_TYPE: String = "COURSE_BOARDING_START",
  MISSION_TYPE: String = "BOARDING",
  ORIGIN_STATION: String,
  DESTINATION_STATION: String,
  DEVICE_ID: String,
  DEVICE_TYPE: String = "COSMO",
  CP_NUMBER: String,
  PROFILE: String,
  COURSE: Course,
  EXPECTED_TICKETS_NUMBER: Int
)

case class MissionStatus(STATUS: String, MOTIF: String = "")

case class CourseBoardingEnd
(
  EVENT_DATETIME: ZonedDateTime,
  EVENT_TYPE: String = "COURSE_BOARDING_END",
  MISSION_TYPE: String = "BOARDING",
  ORIGIN_STATION: String,
  DESTINATION_STATION: String,
  DEVICE_ID: String,
  DEVICE_TYPE: String = "COSMO",
  CP_NUMBER: String,
  PROFILE: String,
  COURSE: Course,
  MISSION_STATUS: MissionStatus
)

case class TicketSegment
(
  SEGMENT_DEPARTURE_DATE_TIME: ZonedDateTime,
  SEGMENT_DESTINATION_STATION: String,
  SEGMENT_FARE_CODE: String,
  SEGMENT_ORIGIN_STATION: String,
  SEGMENT_TRAIN_NUMBER: String
)

case class TicketScan
(
  EVENT_DATETIME: ZonedDateTime,
  EVENT_TYPE: String = "TICKET_SCAN",
  COURSES: Seq[Course],
  ORIGIN_STATION: String,
  DESTINATION_STATION: String,
  MISSION_TYPE: String = "BOARDING",
  DEVICE_ID: String,
  DEVICE_TYPE: String = "COSMO",
  CP_NUMBER: String,
  PROFILE: String,
  TICKET_CODE: String,
  TICKET_PASSENGER_IUC: String,
  TICKET_DV: String,
  TICKET_TCN: String,
  TICKET_CHECK_STATUS: String,
  TICKET_TRAIN_OK: String,
  TICKET_STATUS: String,
  TICKET_LOYALTY_STATUS: String,
  TICKET_PASSENGER_CARD_NUMBER: String,
  TICKET_SEGMENTS: Seq[TicketSegment]
)

object JsonImplicits {
  implicit val courseEncoder: Encoder[Course] = deriveEncoder
  implicit val courseDecoder: Decoder[Course] = deriveDecoder

  implicit val courseBoardingStartEncoder: Encoder[CourseBoardingStart] = deriveEncoder
  implicit val courseBoardingStartDecoder: Decoder[CourseBoardingStart] = deriveDecoder

  implicit val missionStatusStartEncoder: Encoder[MissionStatus] = deriveEncoder
  implicit val missionStatusStartDecoder: Decoder[MissionStatus] = deriveDecoder

  implicit val courseBoardingEndEncoder: Encoder[CourseBoardingEnd] = deriveEncoder
  implicit val courseBoardingEndDecoder: Decoder[CourseBoardingEnd] = deriveDecoder

  implicit val ticketSegmentStartEncoder: Encoder[TicketSegment] = deriveEncoder
  implicit val ticketSegmentStartDecoder: Decoder[TicketSegment] = deriveDecoder

  implicit val ticketScanStatusStartEncoder: Encoder[TicketScan] = deriveEncoder
  implicit val ticketScantatusStartDecoder: Decoder[TicketScan] = deriveDecoder
}


