package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    rectangleMaker(gatherInput())
}

fun rectangleMaker(info: Triple<Int, Int, String>){
    val outputImage = BufferedImage(info.first, info.second, BufferedImage.TYPE_INT_RGB)
    val graphics = outputImage.createGraphics()
    graphics.color = Color.RED
    graphics.drawLine(0, 0, outputImage.width - 1, outputImage.height - 1)
    graphics.drawLine(0,outputImage.height - 1, outputImage.width - 1, 0)
    ImageIO.write(outputImage, "png", File(info.third))
}

fun gatherInput(): Triple<Int, Int, String> {
    println("Enter rectangle width:")
    val width = readln().toIntOrNull()?: 1
    println("Enter rectangle height:")
    val height = readln().toIntOrNull()?: 1
    println("Enter output image name:")
    val name = readln()
    return Triple(width,height,name)
}