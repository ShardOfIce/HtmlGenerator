import model.Point
import model.Rectangle

object SimpleJsonParser {

    fun parseRectangles(json: String): List<Rectangle> {
        val rectangles = mutableListOf<Rectangle>()
        val cleanJson = json.filterNot { it.isWhitespace() }
        val regex = """\{("top_left":\{"x":(\d+),"y":(\d+)},"bottom_right":\{"x":(\d+),"y":(\d+)})}""".toRegex()
        val matches = regex.findAll(cleanJson)

        //check whether the input json contains data for building the rectangles
        if (matches.none()) {
            throw IllegalArgumentException("Input JSON does not match the expected format")
        }

        for (match in matches) {
            val (x1, y1, x2, y2) = match.destructured.toList().drop(1).map(String::toInt)
            rectangles.add(Rectangle(Point(x1, y1), Point(x2, y2), ""))
        }

        return rectangles
    }
}