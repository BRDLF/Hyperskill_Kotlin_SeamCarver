package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.sqrt

class CarverException(message: String?) : RuntimeException(message)

object Carver {
    class Node(val row: Int, val col: Int, val weight: Double){
        var parent: Node? = null
        var distance: Double = Double.MAX_VALUE
    }

    fun carve(args: Array<String>) {
        try {
            val inFile = File(args.findArgValue("-in"))
            val outFile = File(args.findArgValue("-out"))
            val removeCols = args.findArgValue("-width").toIntOrNull()?: throw CarverException("value passed for \"-width\" must be an integer")
            val removeRows = args.findArgValue("-height").toIntOrNull()?: throw CarverException("value passed for \"-height\" must be an integer")
            var outImage = ImageIO.read(inFile)?: throw CarverException("$inFile could not be read!")
            repeat(removeCols) { outImage = outImage.verticalSeam() }
            repeat(removeRows) { outImage = outImage.horizontalSeam() }
            ImageIO.write(outImage, "png", outFile)
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

    private fun BufferedImage.transpose(): BufferedImage {
        val returnImage = BufferedImage(this.height, this.width, BufferedImage.TYPE_INT_RGB)
        for (col in 0 until this.width) {
            for (row in 0 until this.height) {
                returnImage.setRGB(row,col, this.getRGB(col, row))
            }
        }
        return returnImage
    }
    private fun BufferedImage.horizontalSeam(): BufferedImage {
        return this.transpose().verticalSeam().transpose()
    }
    private fun BufferedImage.verticalSeam(): BufferedImage {
        val nodeArray: Array<Array<Node>> = Array(this.height) { row -> Array(this.width) { col -> Node(row, col, this.energyOf(passedX = col, passedY = row))} }

        fun Node.getNeighbors(): List<Node> {
            if (this.row == nodeArray.lastIndex) return emptyList() //no neighbors if on the final line
            if ((this.row !in nodeArray.indices) || (this.col !in nodeArray[this.row].indices)) throw CarverException("in getNeighbors(); passed illegal Node")

            val neighborList = mutableListOf<Node>()
            neighborList.add(nodeArray[this.row + 1][this.col])
            if (this.col != 0) neighborList.add(nodeArray[this.row + 1][this.col - 1])
            if (this.col != nodeArray[0].lastIndex) neighborList.add(nodeArray[this.row + 1][this.col + 1])

            return neighborList
        } //returns the 2-3 nodes below the current node

        //bottom-up fills distances
        for (row in nodeArray.indices.reversed()) {
            for (node in nodeArray[row]) {
                node.distance = node.weight
                val parentNode = node.getNeighbors().minByOrNull { it.distance }?: continue
                node.parent = parentNode
                node.distance += parentNode.distance
            }
        }

        var iterNode = nodeArray[0].toMutableList().minByOrNull { it.distance }?: throw CarverException("Empty Image?")
        val colList = mutableListOf<Int>()
        do {
            colList.add(iterNode.col)
            iterNode = iterNode.parent?: break
        } while (iterNode.row < this.height - 1)

        //shift left
        for (row in colList.indices) {
            val targetCol = colList[row]
            if (targetCol == this.width - 1) continue
            else for (col in targetCol until this.width - 1) {
                this.setRGB(col, row, this.getRGB(col + 1, row))
            }
        }

        return this.getSubimage(0, 0, this.width - 1, this.height)
    }

    private fun BufferedImage.energyOf(passedX: Int, passedY: Int): Double {
        //Check out of bounds
        if (passedX < 0 || passedX >= this.width) throw CarverException("Given x of $passedX is out of bounds")
        if (passedY < 0 || passedY >= this.height) throw CarverException("Given y of $passedY is out of bounds")
        //Adjust for border values
        val adjustX = passedX.coerceIn(1, this.width - 2)
        val adjustY = passedY.coerceIn(1, this.height - 2)

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