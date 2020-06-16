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

import lombok.extern.slf4j.Slf4j;
import me.darksidecode.lvq4j.ModelStateListener;
import me.darksidecode.lvq4j.ModelWrapper;
import me.darksidecode.lvq4j.NeuralNetwork;

@Slf4j (topic = "Train Finish Listener")
public class TrainFinishListener implements ModelStateListener {

    /**
     * @see me.darksidecode.lvq4j.LVQNN#modelStateListener
     * @see ModelStateListener#onStateUpdate(NeuralNetwork, int, double, double, boolean)
     */
    @SuppressWarnings ("JavadocReference")
    @Override
    public void onStateUpdate(NeuralNetwork model, int currentEpoch, double currentLearnRate,
                              double currentErrorSquare, boolean finishedTraining) throws Throwable {
        if (finishedTraining) {
            // The model has finished training. Let's test it!
            log.info("The neural network has finished training!");

            ModelWrapper<IrisDataRecord> modelWrapper = IrisClassificationExample.getModelWrapper();

            // We will use this all to evaluate the accuracy of our model's predictions.
            // Use doubles instead of ints to avoid cumbersome casting later during division.
            // 3 is the number of Iris species on our dataset
            double totalPredictions = 0.0, correctPredictions = 0.0;
            double[] perSpeciesTotalPreds = new double[3];
            double[] perSpeciesCorrectPreds = new double[3];

            // Do the testing: tell the model to classify samples from our
            // input set and check if its guesses match our expectations
            for (int i = 0; i < modelWrapper.getInputRecords().size(); i++) {
                IrisDataRecord record = modelWrapper.getInputRecords().get(i);

                int predicted = modelWrapper.getModel().classify(record.getData());
                int actual = record.getLabelId();
                boolean correct = predicted == actual;

                // Debug the result of this particular test case
                log.debug("Predicted: {} ({}), actual: {} ({}), dist: {} -----> {}",
                        predicted, record.labelIdToLabelText(predicted),
                        actual, record.getLabelText(),
                        modelWrapper.getModel().getLastWinnerDistance(),
                        correct ? "correct" : "WRONG");

                totalPredictions += 1.0;
                perSpeciesTotalPreds[actual] += 1.0;

                if (correct) {
                    // Yay! The prediction was correct!
                    correctPredictions += 1.0;
                    perSpeciesCorrectPreds[actual] += 1.0;
                }
            }

            // Output summary results
            log.info("===============================================");
            log.info("  SUMMARY");
            log.info("    Overall accuracy: {}%", correctPredictions / totalPredictions * 100.0);
            log.info("    Accuracy per cluster (per Iris species):");

            for (int species = 0; species < perSpeciesTotalPreds.length; species++)
                log.info("      {}: {}%", species,
                        perSpeciesCorrectPreds[species] / perSpeciesTotalPreds[species] * 100.0);

            log.info("===============================================");
        }
    }

}
