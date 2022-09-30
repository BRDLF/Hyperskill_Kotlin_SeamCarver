# Hyperskill_Kotlin_Seam Carving

### Stage 6/6: Resize

Program now takes arguments for number of vertical and horizontal seams to remove. Then outputs an image with the given number of seams removed.

### Stage 5/6: Horizontal seam

Program now finds and draws a horizontal seam.

### Stage 4/6: Find the seam

Program now finds and draws the seam. A line from the top of the image to the bottom of the image that has the lowest energy among pixels travelled.

### Stage 3/6: Look at energy

Program no longer returns image negative. Now returns an energy map of the image based on dual-gradient energy function.
The energy of the pixel `(x,y)` is `E(x,y)=sqrt(Δ2x(x,y)+Δ2y(x,y))`

### Stage 2/6: Negative Photo

Program instead takes 2 arguments, `-in` and `-out`
`-in` is the filename of the image to be inverted
`-out` is the name of the file the program will output.

### Stage 1/6: Create an image

Created a program that takes a width, height, and filename, then creates a rectangle of the given size with a red cross over it.
This is a placeholder.