# About
This repository demonstrates a simple example of the **[LVQ4J library](https://github.com/MeGysssTaa/lvq4j)** usage on the **[Iris Data Set](https://en.wikipedia.org/wiki/Iris_flower_data_set)**, which is, perhaps, the best known study case in the **[machine classification](https://en.wikipedia.org/wiki/Statistical_classification)** field.


# Building and running (Gradle)
**Step 1.** Download or clone this repository.
```bash
git clone https://github.com/MeGysssTaa/lvq4j-example-iris
```


**Step 2.** Build the example.
```bash
cd lvq4j-example-iris
./gradlew build
```


**Step 3.** [Download the Iris Data Set](https://archive.ics.uci.edu/ml/datasets/iris).


**Step 4.** Run the example:
```bash
java -jar build/libs/lvq4j-example-iris-1.0.0.jar <train data file>
```


**Profit!** If you did everything correctly, the output you'll see will be similar to this:
```
lvq4j-example-iris $ java -jar build/libs/lvq4j-example-iris-1.0.0.jar /mnt/e/iris.csv
19:44:56.689 [main]              INFO  Main - Successfully read 150 Iris data records
19:44:56.709 [main]              INFO  Main - Neural network will be training asynchronously!
19:44:56.710 [Iris Train Thread] INFO  LVQ4J - Normalized input in 0 millis with function me.darksidecode.lvq4j.NormalizationFunction$$Lambda$24/1209669119
19:44:56.711 [Iris Train Thread] INFO  LVQ4J - Initialized weights in 0 millis with strategy me.darksidecode.lvq4j.WeightsInitializer$$Lambda$17/1884122755
19:44:56.711 [Iris Train Thread] INFO  LVQ4J - Neural network will begin training from scratch.
19:44:56.753 [Iris Train Thread] INFO  Train Finish Listener - The neural network has finished training!
19:44:56.754 [Iris Train Thread] INFO  Train Finish Listener - ===============================================
19:44:56.755 [Iris Train Thread] INFO  Train Finish Listener -   SUMMARY
19:44:56.756 [Iris Train Thread] INFO  Train Finish Listener -     Overall accuracy: 98.0%
19:44:56.757 [Iris Train Thread] INFO  Train Finish Listener -     Accuracy per cluster (per Iris species):
19:44:56.757 [Iris Train Thread] INFO  Train Finish Listener -       0: 100.0%
19:44:56.757 [Iris Train Thread] INFO  Train Finish Listener -       1: 96.0%
19:44:56.757 [Iris Train Thread] INFO  Train Finish Listener -       2: 98.0%
19:44:56.757 [Iris Train Thread] INFO  Train Finish Listener - ===============================================
19:44:56.758 [Iris Train Thread] INFO  LVQ4J - Training completed. It took 46 millis to run 188 iterations for a final error square
sum of 13.05253438308561
```


# Next steps
**See LVQ4J Wiki** and try playing with the code, and then write an own classifier that makes use of LVQ4J.

* If you have any questions or issues, **[don't hesitate to open an issue](https://github.com/MeGysssTaa/lvq4j-example-iris/issues)**!
* If you believe something isn't working as intended, and you know how to fix it, or if you have some ideas for improvements, **[please create a pull request](https://github.com/MeGysssTaa/lvq4j-example-iris/pulls)**.


# License
**[Apache License 2.0](https://github.com/MeGysssTaa/lvq4j/blob/master/LICENSE)**
