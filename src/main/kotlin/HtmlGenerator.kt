import model.Point
import model.Rectangle
import java.io.File

class HtmlGenerator {

    companion object {
        private val COLORS = listOf("red", "green", "blue", "yellow", "cyan", "magenta", "purple", "olive")
    }

    fun drawRectangles(jsonInput: String, outputPath: String) {
        var rectangles = SimpleJsonParser.parseRectangles(jsonInput)
            .mapIndexed { index, rectangle ->
                // To satisfy the condition of the task "Table cells must be colored"
                // making rectangles colorful
                rectangle.copy(color = COLORS[index % COLORS.size])
            }
        rectangles = optimizeDimensions(rectangles)
        generateHtmlTable(rectangles, outputPath)
    }

    private fun generateHtmlTable(rectangles: List<Rectangle>, outputPath: String) {
        File(outputPath).bufferedWriter().use { writer ->
            writer.append("<table style='border-collapse: collapse;'>\n")

            // collecting unique horizontal and vertical borders
            val uniqueXs = rectangles.flatMap { listOf(it.top_left.x, it.bottom_right.x) }.toSortedSet()
            val uniqueYs = rectangles.flatMap { listOf(it.top_left.y, it.bottom_right.y) }.toSortedSet()

            for (yIndex in 0 until uniqueYs.size - 1) {
                writer.append("<tr>\n")
                for (xIndex in 0 until uniqueXs.size - 1) {
                    val cellLeft = uniqueXs.elementAt(xIndex)
                    val cellRight = uniqueXs.elementAt(xIndex + 1)
                    val cellTop = uniqueYs.elementAt(yIndex)
                    val cellBottom = uniqueYs.elementAt(yIndex + 1)

                    val cellColor = rectangles.find { rect ->
                        cellLeft >= rect.top_left.x && cellRight <= rect.bottom_right.x &&
                                cellTop >= rect.top_left.y && cellBottom <= rect.bottom_right.y
                    }?.color ?: "white"

                    val cellWidth = cellRight - cellLeft
                    val cellHeight = cellBottom - cellTop

                    writer.append(
                        "<td style='width: ${cellWidth}px; height: ${cellHeight}px; background-color: $cellColor;'></td>\n"
                    )
                }
                writer.append("</tr>\n")
            }

            writer.append("</table>")
        }
    }

    /* To satisfy the condition of the task "The table should be as compact as possible"
    set the maximum size in the range from 200 to 800 */
    private fun optimizeDimensions(rectangles: List<Rectangle>): List<Rectangle> {
        if (rectangles.isEmpty()) return rectangles

        val maxDimension = rectangles.maxOf { rectangle ->
            maxOf(rectangle.bottom_right.x - rectangle.top_left.x, rectangle.bottom_right.y - rectangle.top_left.y)
        }

        val scale = when {
            maxDimension < 200 -> 200.0 / maxDimension
            maxDimension > 800 -> 800.0 / maxDimension
            else -> 1.0
        }

        return rectangles.map { rectangle ->
            Rectangle(
                top_left = Point((rectangle.top_left.x * scale).toInt(), (rectangle.top_left.y * scale).toInt()),
                bottom_right = Point((rectangle.bottom_right.x * scale).toInt(), (rectangle.bottom_right.y * scale).toInt()),
                color = rectangle.color
            )
        }
    }

}