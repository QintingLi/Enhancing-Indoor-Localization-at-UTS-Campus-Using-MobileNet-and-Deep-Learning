

# GoogleNet

## Overview

This project aims to classify building images into different categories using a deep learning model based on the GoogLeNet architecture. The dataset is partitioned into training, validation, and test sets, and the model is trained to achieve high accuracy in image classification.

## Dataset

The dataset consists of building images organized into different classes. The data is partitioned into three sets:
- **Training Set** (80%): Used to train the model.
- **Validation Set** (10%): Used to validate the model during training.
- **Test Set** (10%): Used to evaluate the final model performance.

### Data Preparation

1. **Original Data Location**: `data/origin`
2. **Partitioned Data Location**: `data/building`

The dataset is automatically partitioned into training, validation, and test sets using the provided script.

## Setup and Requirements

Ensure you have the following dependencies installed:
- Python 3.x
- PyTorch
- torchvision
- tqdm
- pandas
- seaborn
- matplotlib
- PIL (Pillow)

To install the dependencies, run:
```bash
pip install torch torchvision tqdm pandas seaborn matplotlib pillow
```

## Running the Code

### 1. Data Preparation

To partition the dataset into training, validation, and test sets, run the script:

```bash
python partition_data.py
```

This will organize the images into the respective folders under `data/building`.

### 2. Training the Model

To train the GoogLeNet model on the partitioned dataset:

```bash
python train_model.py
```

The training process includes:
- Training the model over 50 epochs.
- Validating the model after each epoch.
- Saving the best-performing model and the final model.

### 3. Testing and Evaluation

After training, evaluate the model on the test set:

```bash
python test_model.py
```

This will compute the test accuracy and display a confusion matrix for the predictions.

### 4. Predicting a Single Image

To predict a single image, use the following script:

```bash
python predict_image.py
```

Modify the `image_path` variable to point to the image you want to predict.

## Results

The model achieved a test accuracy of **99.7%**. The confusion matrix visualizes the performance across different building classes.

## Model Summary

The GoogLeNet model summary, including layers, parameters, and size, can be printed using:

```bash
python model_summary.py
```

## File Descriptions

- `partition_data.py`: Script to partition the dataset into training, validation, and test sets.
- `train_model.py`: Script to train the GoogLeNet model.
- `test_model.py`: Script to evaluate the trained model on the test set.
- `predict_image.py`: Script to predict a single image's class.
- `model_summary.py`: Script to print the model's architecture and parameters.
- `results/`: Directory containing saved model checkpoints and performance metrics.

