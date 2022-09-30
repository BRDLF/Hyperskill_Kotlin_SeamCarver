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
        var processed = false
    }

    fun carve(args: Array<String>) {
        try {
            val inFile = File(args.findArgValue("-in"))
            val outFile = File(args.findArgValue("-out"))
            ImageIO.write(ImageIO.read(inFile).printSeam(), "png", outFile)
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

    private fun BufferedImage.printSeam(): BufferedImage {
        val nodeMap: Array<Array<Node>> = Array(this.height) { row -> Array(this.width) { col -> Node(row, col, this.energyOf(passedX = col, passedY = row))} }

        fun Node.getNeighbors(): List<Node> {
            if (this.row == nodeMap.lastIndex) return emptyList() //no neighbors if on the final line
            if ((this.row !in nodeMap.indices) || (this.col !in nodeMap[this.row].indices)) throw CarverException("in getNeighbors(); passed illegal Node")

            val neighborList = mutableListOf<Node>()
            neighborList.add(nodeMap[this.row + 1][this.col])
            if (this.col != 0) neighborList.add(nodeMap[this.row + 1][this.col - 1])
            if (this.col != nodeMap[0].lastIndex) neighborList.add(nodeMap[this.row + 1][this.col + 1])

            return neighborList
        } //returns the 2-3 nodes below the current node

        fun MutableList<Node>.shortestPath(): Node {
            this.forEach{it.distance = it.weight}
            while (this.isNotEmpty()) {
                this.sortBy { it.distance }
                val evalNode = this.first()
                if (evalNode.row == nodeMap.lastIndex) {
                    return evalNode
                }
                for (neighbor in evalNode.getNeighbors()) {    //take all neighbors
                    if (neighbor.weight + evalNode.distance < neighbor.distance) {      //check whether distance is shorter than current dist
                        neighbor.parent = evalNode
                        neighbor.distance = neighbor.weight + evalNode.distance
                    }
                    if (!neighbor.processed && !this.contains(neighbor)) this.add(neighbor)
                }
                evalNode.processed = true
                this.remove(evalNode)
            }
            return Node(-1,-1,-0.0)
        }

        val seamList = mutableListOf(nodeMap[0].toMutableList().shortestPath())
        while (true) {
            seamList.add(seamList.last().parent?: break) //adds to seamList until we've reached the top of the image, breaks if parent is null (initial value of Node.parent)
        }

        for (node in seamList) {
            this.setRGB(node.col, node.row, Color(255, 0, 0).rgb)
        }

        return this
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