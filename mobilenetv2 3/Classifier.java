package com.example.mobilenetv2;

import android.graphics.Bitmap;

import org.pytorch.Tensor;
import org.pytorch.Module;
import org.pytorch.IValue;
import org.pytorch.torchvision.TensorImageUtils;

import java.util.Arrays;


class PredictResult {
    float score;
    String className;

    public PredictResult(float s, String name) {
        score = s;
        className = name;
    }
}

public class Classifier {

    Module model;
    float[] mean = {0.485f, 0.456f, 0.406f};
    float[] std = {0.229f, 0.224f, 0.225f};

    public Classifier(String modelPath) {
        model = Module.load(modelPath);
    }

    public void setMeanAndStd(float[] mean, float[] std) {
        this.mean = mean;
        this.std = std;
    }

    public Tensor preprocess(Bitmap bitmap, int size) {
        bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);
        return TensorImageUtils.bitmapToFloat32Tensor(bitmap, this.mean, this.std);
    }

    public int argMax(float[] inputs) {

        int maxIndex = -1;
        float maxvalue = 0.0f;

        for (int i = 0; i < inputs.length; i++) {

            if (inputs[i] > maxvalue) {
                maxIndex = i;
                maxvalue = inputs[i];
            }
        }

        return maxIndex;
    }

    public static float[] softmax(float[] logits) {
        // Step 1: Compute the maximum logit value for numerical stability
        float maxLogit = Float.NEGATIVE_INFINITY;
        for (float logit : logits) {
            if (logit > maxLogit) {
                maxLogit = logit;
            }
        }

        // Step 2: Compute the exponentials and their sum
        float sum = 0.0f;
        float[] expValues = new float[logits.length];
        for (int i = 0; i < logits.length; i++) {
            expValues[i] = (float) Math.exp(logits[i] - maxLogit);
            sum += expValues[i];
        }

        // Step 3: Compute the probabilities
        float[] probabilities = new float[logits.length];
        for (int i = 0; i < logits.length; i++) {
            probabilities[i] = expValues[i] / sum;
        }

        return probabilities;
    }


    public PredictResult predict(Bitmap bitmap) {
        Tensor tensor = preprocess(bitmap, 224);
        IValue inputs = IValue.from(tensor);
        Tensor outputs = model.forward(inputs).toTensor();
        float[] logits = outputs.getDataAsFloatArray();
        float[] scores = softmax(logits);
        int classIndex = argMax(scores);
        PredictResult result = new PredictResult(scores[classIndex], Constants.CLASSES[classIndex]);
        return result;
    }
}
