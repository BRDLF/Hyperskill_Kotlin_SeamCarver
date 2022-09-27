# Hyperskill_Kotlin_Seam Carving

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