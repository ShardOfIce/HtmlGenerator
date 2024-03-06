import model.Quad
import model.Rectangle
import java.io.File

class HtmlGenerator {

    companion object {
        private val COLORS = listOf("red", "green", "blue", "yellow", "cyan", "magenta", "purple", "olive")
        private const val MAX_BRUSH_SIZE = 30
        private const val MIN_BRUSH_SIZE = 5
    }

    fun drawRectangles(jsonInput: String, outputPath: String) {
        val rectangles = SimpleJsonParser.parseRectangles(jsonInput)
            .mapIndexed { index, rectangle ->
            // To satisfy the condition of the task "Table cells must be colored"
            // making rectangles colorful
            rectangle.copy(color = COLORS[index % COLORS.size])
        }
        val bounds = getTableBounds(rectangles)
        generateHtmlTable(rectangles, bounds, outputPath)
    }

    private fun getTableBounds(rectangles: List<Rectangle>): Quad {
        val minX = rectangles.minOf { it.top_left.x }
        val minY = rectangles.minOf { it.top_left.y }
        val maxX = rectangles.maxOf { it.bottom_right.x }
        val maxY = rectangles.maxOf { it.bottom_right.y }
        return Quad(minX, minY, maxX, maxY)
    }

    private fun generateHtmlTable(rectangles: List<Rectangle>, bounds: Quad, outputPath: String) {
        val (minX, minY, maxX, maxY) = bounds
        val brushSize = calculateBrushSize(maxX - minX, maxY - minY)

        File(outputPath).bufferedWriter().use { writer ->
            writer.append("<table style='border-collapse: collapse;'>")
            for (y in minY..maxY) {
                writer.append("<tr>")
                for (x in minX..maxX) {
                    val color = findRectangle(x, y, rectangles)?.color ?: "white"
                    writer.append("<td style='width: ${brushSize}px; height: ${brushSize}px; background-color: $color;'></td>")
                }
                writer.append("</tr>")
            }
            writer.append("</table>")
        }
    }

    /* To satisfy the condition of the task "The table should be as compact as possible"
    set the brush size from 5 to 30 to try to display all the rectangles on the screen,
    regardless of their absolute sizes. */
    private fun calculateBrushSize(tableWidth: Int, tableHeight: Int): Int {
        val maxDimension = maxOf(tableWidth, tableHeight)
        return when {
            maxDimension <= 10 -> MAX_BRUSH_SIZE
            maxDimension > 100 -> MIN_BRUSH_SIZE
            else -> {
                val scale = (maxDimension - 10) / 90.0
                (MAX_BRUSH_SIZE - (scale * (MAX_BRUSH_SIZE - MIN_BRUSH_SIZE))).toInt()
                    .coerceIn(MIN_BRUSH_SIZE, MAX_BRUSH_SIZE)
            }
        }
    }

    private fun findRectangle(x: Int, y: Int, rectangles: List<Rectangle>): Rectangle? =
        rectangles.find { it.top_left.x <= x && it.bottom_right.x >= x && it.top_left.y <= y && it.bottom_right.y >= y }
}