package org.openjfx

import javafx.application.Platform
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart

class Graph(xTitle: String, yTitle: String, seriesTitle: String, chartsTitle: String) {
    val lineChart: LineChart<String, Number>
    private val series: XYChart.Series<String, Number>

    init {
        val xAxis = createXAxis(xTitle)
        val yAxis = createYAxis(yTitle)
        series = createSeries(seriesTitle)

        lineChart = LineChart(xAxis, yAxis)
        lineChart.title = chartsTitle
        lineChart.animated = false
        lineChart.data.add(series)
    }

    fun addPoint(xValue: String, yValue: Int) {
        Platform.runLater {
            series.data.add(XYChart.Data(xValue, yValue))
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
