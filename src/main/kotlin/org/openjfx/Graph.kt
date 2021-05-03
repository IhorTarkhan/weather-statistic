package org.openjfx

import javafx.application.Platform
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Graph(
    xTitle: String,
    yTitle: String,
    seriesTitle: String,
    chartsTitle: String,
    private val isAutoRanging: Boolean = true
) {
    val instance: LineChart<String, Number>
    private val series: XYChart.Series<String, Number>
    private val xAxis = createXAxis(xTitle)
    private val yAxis = createYAxis(yTitle, isAutoRanging)
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy, HH")


    init {
        series = createSeries(seriesTitle)

        instance = LineChart(xAxis, yAxis)
        instance.title = chartsTitle
        instance.animated = false
        instance.data.add(series)
    }

    fun addPointAll(pairs: List<Pair<LocalDateTime, Double>>) {
        Platform.runLater {
            series.data.addAll(pairs.map {
                XYChart.Data(it.first.format(formatter), it.second)
            })
            series.data.sortBy {
                LocalDateTime.parse(it.xValue, formatter);
            }

            if (!isAutoRanging) {
                val sorted = series.data.sortedBy { it.yValue.toDouble() }
                val min = sorted[0].yValue.toDouble()
                val max = sorted[series.data.size - 1].yValue.toDouble()
                val gep = (max - min) * 0.1
                yAxis.lowerBound = max + gep
                yAxis.upperBound = min - gep
            }
        }
    }

    private fun createXAxis(title: String): CategoryAxis {
        val xAxis = CategoryAxis()
        xAxis.label = title
        xAxis.animated = false
        return xAxis
    }

    private fun createYAxis(title: String, isAutoRanging: Boolean): NumberAxis {
        val yAxis = NumberAxis()
        yAxis.label = title
        yAxis.animated = false
        yAxis.isAutoRanging = isAutoRanging
        return yAxis
    }

    private fun createSeries(seriesTitle: String): XYChart.Series<String, Number> {
        val series = XYChart.Series<String, Number>()
        series.name = seriesTitle
        return series
    }
}
