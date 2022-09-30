# Hyperskill_Kotlin_Seam Carving

This was the 8th project I completed as part of Jetbrain Academy's Kotlin course, and the 1st under the "Kotlin Developer" course.

It is rated as a challenging difficulty project. The goal is to make a seam carving tool to process and compress images.

The program takes four arguments, all are mandatory
- `-in`  The filename of the image to reduce
- `-out` The filename of the output image
- `-width` The number of vertical seams to rip from the image
- `-height` The number of horizontal seams to rip from the image.

The program then removes seams from the image until the result is the desired size, and outputs to the `-out` file as a .png

I have included the tests that Hyperskill uses to determine if a project is complete.
