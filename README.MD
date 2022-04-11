# MachineLearningLinearRegression
Program calculates SSE and updating weight every step, uses batch gradient descent to update weights.

No pandas, numpy or similar libraries are used to extract data from csv files.

Program parameters are:
- data - The location of the data file (e.g. /media/data/yacht.csv).
- eta - The learning rate of the gradient descent approach.
- threshold - The threshold, that the change in error has to fall below, before the algorithm terminates.

Example program execution:
python3 gradientDescentML.py --data random3.csv --eta 0.00005 --threshold 0.0001

Output is like:
iteration_number,weight0,weight1,weight2,...,weightN,sum_of_squared_errors
