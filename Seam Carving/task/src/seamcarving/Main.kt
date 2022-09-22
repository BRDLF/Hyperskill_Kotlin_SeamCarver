package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Carver {

    fun carve(args: Array<String>) {
        try {
            val inFile = File(args.findArgValue("-in"))
            val outFile = File(args.findArgValue("-out"))
            ImageIO.write(invert(inFile), "png", outFile)
        }
        catch (e: RuntimeException) {
            println(e.message)
        }

    }

    private fun Array<String>.findArgValue(arg: String): String {
        val argIndex = this.indexOf(arg)
        if (argIndex == -1 ||
            argIndex == this.lastIndex ||
            this[argIndex+1].startsWith("-")) throw RuntimeException("$arg not defined")
        else return this[argIndex + 1]
    }

    private fun invert(src: File): BufferedImage {
        val invertedImage = ImageIO.read(src) ?: throw RuntimeException("Couldn't read $src")
        for (x in 0 until invertedImage.width) {
            for (y in 0 until invertedImage.height) {
                val originalColor = Color(invertedImage.getRGB(x, y))
                val invertedColor = Color(255 - originalColor.red, 255 - originalColor.green, 255 - originalColor.blue)
                invertedImage.setRGB(x, y, invertedColor.rgb)
            }
        }
        return invertedImage
    }
}

fun main(args: Array<String>) {
    Carver.carve(args)
}