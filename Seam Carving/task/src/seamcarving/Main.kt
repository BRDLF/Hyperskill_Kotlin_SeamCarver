package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.sqrt

class CarverException(message: String?) : RuntimeException(message)

object Carver {

    fun carve(args: Array<String>) {
        try {
            val inFile = File(args.findArgValue("-in"))
            val outFile = File(args.findArgValue("-out"))
            ImageIO.write(ImageIO.read(inFile).intensityMap(), "png", outFile)
        }
        catch (e: CarverException) {
            println(e.message)
        }

    }

    private fun Array<String>.findArgValue(arg: String): String {
        val argIndex = this.indexOf(arg)
        if (argIndex == -1 ||
            argIndex == this.lastIndex ||
            this[argIndex+1].startsWith("-")) throw CarverException("$arg not defined")
        else return this[argIndex + 1]
    }

    private fun invert(src: File): BufferedImage {
        val invertedImage = ImageIO.read(src) ?: throw CarverException("Couldn't read $src")
        for (x in 0 until invertedImage.width) {
            for (y in 0 until invertedImage.height) {
                val originalColor = Color(invertedImage.getRGB(x, y))
                val invertedColor = Color(255 - originalColor.red, 255 - originalColor.green, 255 - originalColor.blue)
                invertedImage.setRGB(x, y, invertedColor.rgb)
            }
        }
        return invertedImage
    }

    private fun BufferedImage.intensityMap(): BufferedImage {
        var maxEnergyValue = Double.MIN_VALUE
        val energyGrid = Array(this.width) { Array (this.height) {0.0} }

        val intenseImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)

        for (x in 0 until this.width) {
            for (y in 0 until this.height) {
                energyGrid[x][y] = this.energyOf(x, y)
                maxEnergyValue = if (energyGrid[x][y] > maxEnergyValue) energyGrid[x][y] else maxEnergyValue
            }
        }

        for (x in 0 until this.width) {
            for (y in 0 until this.height) {
                val normalizedIntensity = (255.0 * energyGrid[x][y] / maxEnergyValue).toInt()
                intenseImage.setRGB(x, y, Color(normalizedIntensity, normalizedIntensity, normalizedIntensity).rgb)
            }
        }

        return intenseImage
    }

    private fun BufferedImage.energyOf(passedX: Int, passedY: Int): Double {
        //Check out of bounds
        if (passedX < 0 || passedX >= this.width) throw CarverException("Given x of $passedX is out of bounds")
        if (passedY < 0 || passedY >= this.height) throw CarverException("Given y of $passedY is out of bounds")
        //Adjust for border values
        val adjustX = when (passedX) {
            0 -> 1
            this.width - 1 -> passedX - 1
            else -> passedX
        }
        val adjustY = when (passedY) {
            0 -> 1
            this.height - 1 -> passedY - 1
            else -> passedY
        }

        val leftPixel = Color(this.getRGB(adjustX - 1, passedY ))
        val rightPixel = Color(this.getRGB(adjustX + 1, passedY ))
        val topPixel = Color(this.getRGB(passedX, adjustY - 1 ))
        val bottomPixel = Color(this.getRGB(passedX, adjustY + 1 ))
        val xRed = (leftPixel.red - rightPixel.red)
        val xGreen = (leftPixel.green - rightPixel.green)
        val xBlue = (leftPixel.blue - rightPixel.blue)
        val yRed = (topPixel.red - bottomPixel.red)
        val yGreen = (topPixel.green - bottomPixel.green)
        val yBlue = (topPixel.blue - bottomPixel.blue)
        val xGrad = (xRed * xRed + xGreen * xGreen + xBlue * xBlue)
        val yGrad = (yRed * yRed + yGreen * yGreen + yBlue * yBlue)

        return sqrt((xGrad + yGrad).toDouble())
    }
}

fun main(args: Array<String>) {
    Carver.carve(args)
}