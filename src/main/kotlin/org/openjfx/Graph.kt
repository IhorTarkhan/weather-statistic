package org.openjfx

import javafx.application.Platform
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart

class Graph(xTitle: String, yTitle: String, seriesTitle: String, chartsTitle: String) {
    val instance: LineChart<String, Number>
    private val series: XYChart.Series<String, Number>

    init {
        val xAxis = createXAxis(xTitle)
        val yAxis = createYAxis(yTitle)
        series = createSeries(seriesTitle)

        instance = LineChart(xAxis, yAxis)
        instance.title = chartsTitle
        instance.animated = false
        instance.data.add(series)
    }

    fun addPointAll(pairs: List<Pair<String, Int>>) {
        Platform.runLater {
            series.data.addAll(pairs.map { XYChart.Data(it.first, it.second) })
            series.data.sortBy { it.xValue }
        }
    }

    private fun createXAxis(title: String): CategoryAxis {
        val xAxis = CategoryAxis()
        xAxis.label = title
        xAxis.animated = false
        return xAxis
    }

    private fun createYAxis(title: String): NumberAxis {
        val yAxis = NumberAxis()
        yAxis.label = title
        yAxis.animated = false
        return yAxis
    }

    private fun createSeries(seriesTitle: String): XYChart.Series<String, Number> {
        val series = XYChart.Series<String, Number>()
        series.name = seriesTitle
        return series
    }
}
