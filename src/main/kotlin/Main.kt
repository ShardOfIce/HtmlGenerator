import java.io.File

fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Usage: convert <filename> [--open-in-chrome]")
        return
    }

    if (args[0] != "convert") {
        println("Unknown command. Available commands: convert")
        return
    }

    val fileName = args[1]

    val outputPath = fileName.substringBeforeLast('.') + ".html"

    val jsonInput = File(fileName).readText(Charsets.UTF_8)

    val drawer = HtmlGenerator()
    drawer.drawRectangles(jsonInput, outputPath)
    println("HTML with rectangles has been saved to $outputPath")

    if (args.contains("--open")) {
        openInChrome(outputPath)
    }
}

fun openInChrome(filePath: String) {
    try {
        val absolutePath = File(filePath).absolutePath
        val pathForUri = absolutePath.replace("\\", "/")
        val uri = "file:///$pathForUri"

        val osName = System.getProperty("os.name").toLowerCase()
        when {
            osName.contains("windows") -> Runtime.getRuntime().exec("cmd /c start chrome $uri")
            osName.contains("mac") -> Runtime.getRuntime().exec("open -a Google\\ Chrome $uri")
            osName.contains("nix") || osName.contains("nux") || osName.indexOf("aix") > 0 -> Runtime.getRuntime().exec("google-chrome $uri")
            else -> println("OS not supported for automatic opening in Chrome.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}