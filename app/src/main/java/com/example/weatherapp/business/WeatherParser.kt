package com.example.weatherapp.business

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherParser {

    // ONLY FIXED ONE THING: ":timeSeries" → "timeSeries":
    private val _forecastData = MutableStateFlow(
        """{
  "approvedTime": "2021-11-01T13:04:14Z",
  "referenceTime": "2021-11-01T13:00:00Z",
  "geometry": {
    "type": "Point",
    "coordinates": [ [ 14.342548, 60.374385 ] ]
  },
  "timeSeries": [
    {
      "validTime": "2021-11-01T14:00:00Z",
      "parameters": [
        {
          "name": "spp",
          "levelType": "hl",
          "level": 0,
          "unit": "percent",
          "values": [ 0.0 ]
        },
        {
          "name": "pcat",
          "levelType": "hl",
          "level": 0,
          "unit": "category",
          "values": [ 3 ]
        },
        {
          "name": "pmin",
          "levelType": "hl",
          "level": 0,
          "unit": "kg/m2/h",
          "values": [ 0.0 ]
        },
        {
          "name": "pmean",
          "levelType": "hl",
          "level": 0,
          "unit": "kg/m2/h",
          "values": [ 0.1 ]
        },
        {
          "name": "pmax",
          "levelType": "hl",
          "level": 0,
          "unit": "kg/m2/h",
          "values": [ 0.1 ]
        },
        {
          "name": "pmedian",
          "levelType": "hl",
          "level": 0,
          "unit": "kg/m2/h",
          "values": [ 0.1 ]
        },
        {
          "name": "tcc_mean",
          "levelType": "hl",
          "level": 0,
          "unit": "octas",
          "values": [ 8 ]
        },
        {
          "name": "lcc_mean",
          "levelType": "hl",
          "level": 0,
          "unit": "octas",
          "values": [ 8 ]
        },
        {
          "name": "mcc_mean",
          "levelType": "hl",
          "level": 0,
          "unit": "octas",
          "values": [ 6.0 ]
        },
        {
          "name": "hcc_mean",
          "levelType": "hl",
          "level": 0,
          "unit": "octas",
          "values": [ 8 ]
        },
        {
          "name": "t",
          "levelType": "hl",
          "level": 2,
          "unit": "Cel",
          "values": [ 7.6 ]
        },
        {
          "name": "msl",
          "levelType": "hmsl",
          "level": 0,
          "unit": "hPa",
          "values": [ 997.4 ]
        },
        {
          "name": "vis",
          "levelType": "hl",
          "level": 2,
          "unit": "km",
          "values": [ 2.2 ]
        },
        {
          "name": "wd",
          "levelType": "hl",
          "level": 10,
          "unit": "degree",
          "values": [ 148 ]
        },
        {
          "name": "ws",
          "levelType": "hl",
          "level": 10,
          "unit": "m/s",
          "values": [ 2.5 ]
        },
        {
          "name": "r",
          "levelType": "hl",
          "level": 2,
          "unit": "percent",
          "values": [ 100 ]
        },
        {
          "name": "tstm",
          "levelType": "hl",
          "level": 0,
          "unit": "percent",
          "values": [ 0.0 ]
        },
        {
          "name": "gust",
          "levelType": "hl",
          "level": 10,
          "unit": "m/s",
          "values": [ 7.7 ]
        },
        {
          "name": "Wsymb2",
          "levelType": "hl",
          "level": 0,
          "unit": "category",
          "values": [ 6 ]
        }
      ]
    }
  ]
}"""
    )

    val forecastData: StateFlow<String>
        get() = _forecastData.asStateFlow()

    private val format = DateTimeFormatter.ISO_DATE_TIME


    fun parseWeather(): WeatherData {
        val root = Json.parseToJsonElement(forecastData.value).jsonObject

        val approved = LocalDateTime.parse(
            root["approvedTime"]!!.jsonPrimitive.content,
            format
        )

        val series = root["timeSeries"]!!
            .jsonArray
            .map { element ->
                val obj = element.jsonObject

                val captureTime = LocalDateTime.parse(
                    obj["validTime"]!!.jsonPrimitive.content,
                    format
                )

                val parameters = obj["parameters"]!!.jsonArray

                fun getValue(name: String): Float {
                    return parameters.first {
                        it.jsonObject["name"]!!.jsonPrimitive.content == name
                    }.jsonObject["values"]!!
                        .jsonArray[0]
                        .jsonPrimitive.float
                }

                SerieData(
                    temperature = getValue("t"),
                    cloudDispersity = getValue("tcc_mean").toInt(),
                    precipitation = getValue("pmean"),
                    weatherCode = getValue("Wsymb2").toInt(),
                    captureTime = captureTime
                )
            }

        return WeatherData(
            ApprovedTime = approved,
            series = series
        )
    }
}