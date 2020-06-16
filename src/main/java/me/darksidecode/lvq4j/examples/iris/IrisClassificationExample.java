/*
 * Copyright 2020 DarksideCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.darksidecode.lvq4j.examples.iris;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.darksidecode.lvq4j.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j (topic = "Main")
public class IrisClassificationExample {

    /**
     * The number of records from the input train data set (iris.csv)
     * that will be used explicitly for training.
     *
     * Defaults to 30. We have 150 data samples in total, and 3 different clusters
     * (3 different Iris species). By specifying the value of 30, if you use the N_FIRST
     * weights initialization, you will train the neural network from 30 samples of one
     * single Iris species type (setosa). With N_RANDOM, 30 random samples from the input
     * CSV file will be chosen, with duplicates allowed. N_RANDOM_UNIQUE will do the same
     * but also ensure that input samples are not reused. N_RANDOM_RATIONAL will choose
     * 10 random samples of each Iris species type.
     *
     * @see LVQNN#trainSamples
     */
    @SuppressWarnings ("JavadocReference")
    private static final int TRAIN_SAMPLES = 30;

    @Getter
    private static ModelWrapper<IrisDataRecord> modelWrapper;

    /**
     * When running this example, make sure to specify the path
     * to your iris.csv as the program argument. The path is allowed
     * to contain spaces. You can download the Iris dataset here:
     * https://archive.ics.uci.edu/ml/datasets/iris
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            log.error("Usage: java -jar <...>.jar <path to iris.csv>");
            return;
        }

        // Make sure to try setting this to Level.DEBUG to see a more
        // detailed output during training and testing. It contains a
        // lot of interesting details that you can use to enhance the
        // model and make its predictions more (or less) accurate!
        Level logLevel = Level.INFO;

        // Enable the LVQ4J logger so that we can see our training
        // progress reports and other details useful for debugging
        Configurator.setLevel("LVQ4J", logLevel);

        // Enable our own logging (tests' output)
        Configurator.setLevel("Main",                  logLevel);
        Configurator.setLevel("Train Finish Listener", logLevel);

        // Use StringBuilder to allow spaces in directory names.
        StringBuilder csvPathBuilder = new StringBuilder();

        for (String arg : args)
            csvPathBuilder.append(arg).append(' ');

        // Remove trailing space left in the for loop.
        csvPathBuilder.deleteCharAt(csvPathBuilder.length() - 1);
        String csvFilePath = csvPathBuilder.toString();
        File irisCsv = new File(csvFilePath);

        if (!irisCsv.isFile()) {
            log.error("{} does not exist or is a directory", irisCsv.getName());
            return;
        }

        // Parse the specified iris.csv file
        List<IrisDataRecord> trainData = loadDataRecordsFromCsv(irisCsv);

        // Setup our LVQ model using a safe abstraction layer (ModelWrapper).
        // Feel free to play with these values and see how each of them affects
        // the behavior of your model for our particular Iris dataset case
        modelWrapper = new ModelBuilder<IrisDataRecord>()
                .withTrainData(trainData, TRAIN_SAMPLES)
                .withInputNormalizationFunc(NormalizationFunction.MIN_MAX)
                .withWeightsInitializer(WeightsInitializer.N_RANDOM_UNIQUE)
                .withDistanceMetric(DistanceMetric.EUCLIDEAN_DISTANCE)
                .withRandomNumberGenerator(321895892175192714L) // something random in code but constant across tests
                .withProgressReportPeriod(5) // report current progress on every 5th iteration
                .withModelStateListener(new TrainFinishListener()) // this class will react to NN state updates
                .withLearnRate(0.3)
                .withQuitLearnRate(0.001)
                .withMomentumLearnRateDecay(0.97)
                .withMaxEpochs(200)
                .build();

        // Preprocess (normalize) input data, initialize weights and
        // begin the training process in a new dedicated thread.
        //
        // When our neural network will finish training, the TrainFinishListener
        // that we provided above (in the line where modelWrapper is created)
        // will be called, and there we will evaluate the accuracy of our
        // model's predictions
        new Thread(modelWrapper::preprocessInitializeAndTrain,
                "Iris Train Thread").start();

        // We can see this message immediately because the training
        // process will not block the current thread. In any moment
        // of time, we can do modelWrapper.getModel().halt() to stop
        // the training process forcefully
        log.info("Neural network will be training asynchronously!");
    }

    private static List<IrisDataRecord> loadDataRecordsFromCsv(File irisCsv) throws IOException {
        List<IrisDataRecord> trainData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(irisCsv)))) {
            String recordLineCsv;

            // If the dataset you have downloaded contains a header (column names
            // in the first line), then make sure to skip the first line of the file!
            while ((recordLineCsv = reader.readLine()) != null) {
                if (!recordLineCsv.isEmpty()) { // make sure we don't attempt to parse empty lines
                    IrisDataRecord record = new IrisDataRecord();
                    record.loadFromString(recordLineCsv);
                    trainData.add(record);
                }
            }
        }

        log.info("Successfully read {} Iris data records", trainData.size());

        return trainData;
    }

}
