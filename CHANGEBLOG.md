# Hyperskill_Kotlin_Seam Carving

### Stage 3/6: Look at energy

Once again, I did not read the question thoroughly and caused myself unnecessary woe.
In the function `BufferedImage.energyOf()` I check the passed X,Y values to make sure they are on the border.
I then have `adjustX` and `adjustY` for border pixels.
Then, when calculating the bordering pixels, `leftPixel, rightPixel, topPixel, bottomPixel` I used the adjusted values for Both X and Y
My code looked like this:
```
val leftPixel = Color(this.getRGB(adjustX - 1, adjustY* ))
val rightPixel = Color(this.getRGB(adjustX + 1, adjustY* ))
val topPixel = Color(this.getRGB(adjustX*, adjustY - 1 ))
val bottomPixel = Color(this.getRGB(adjustX*, adjustY + 1 ))

\\ *this is where the problem is, in case you don't see it. 
```
What that did was unnecessarily shift what values I was evaluating away from the border.
Basically, I was ignoring a 1px trim around the entire image.

From the description
```
Energy for border pixels is calculated with a 1-pixel shift from the border. For example:
E(0,2)=Δ2x(1,2)+Δ2y(0,2)−−−−−−−−−−−−−−−√
E(1,0)=Δ2x(1,0)+Δ2y(1,1)−−−−−−−−−−−−−−−√
E(0,0)=Δ2x(1,0)+Δ2y(0,1)−−−−−−−−−−−−−−−√
```

But what I was doing was 
```
Energy for border pixels is calculated with a 1-pixel shift from the border. For example:
E(0,2)=Δ2x(1,2)+Δ2y(1,2)−−−−−−−−−−−−−−−√
E(1,0)=Δ2x(1,1)+Δ2y(1,1)−−−−−−−−−−−−−−−√
E(0,0)=Δ2x(1,1)+Δ2y(1,1)−−−−−−−−−−−−−−−√
```

So. I was getting errors that the Hash didn't match, but when I looked at the output images and compared to the example, everything SEEMED fine.
I even opened GIMP to check the difference of the two images. I didn't find a thing, but I"m guessing the difference was just very subtle.


Alas! A very small oversight made a very large nuisance!
But I guess that's just how coding goes sometimes. Measure twice cut once, etc.
I look forward to learning this lesson again, someday.


### Stage 2/6: Negative Photo

Ok. fun. Actually doing stuff with images again. Having to handle parameters. Fun stuff.

I ran into a silly issue with this. IDEA said there was no `main` found. It's because I had used `fun main(args: List<String>)` instead of `fun main(args: Array<String>)`

Oops! Other than that though, Easy enough.

### Stage 1/6: Create an image

This was the usual Stage 1 simple setup. "Create a program that does very little towards the intended goal of the project."

It was a nice refresher with working with BufferedImage and ImageIO, which I hadn't touched in a few weeks.