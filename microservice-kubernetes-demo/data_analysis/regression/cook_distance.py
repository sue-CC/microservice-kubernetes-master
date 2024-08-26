import pandas as pd
import statsmodels.api as sm
import matplotlib.pyplot as plt

# Load the CSV files
file_path_grpc = 'calculated_average_results_grpc.csv'
file_path_rest = 'calculated_average_results_rest.csv'

data_grpc = pd.read_csv(file_path_grpc)
data_rest = pd.read_csv(file_path_rest)

# Define a function to calculate Cook's distance and create a combined scatter plot with specified colors
# and a horizontal line at y=1 to identify influential points
def analyze_and_plot_combined_with_specified_colors(data_grpc, data_rest):
    # Define a nested function to calculate Cook's distance and return results
    def calculate_cooks_distance(data):
        # Dependent variables
        y_power = data['Power']
        y_response_time = data['Average Response Time']

        # Independent variables (predictors)
        X = data[['Frequency', 'Size']]
        X = sm.add_constant(X)  # Add a constant term for the intercept

        # Fit the models
        model_power = sm.OLS(y_power, X).fit()
        model_response_time = sm.OLS(y_response_time, X).fit()

        # Calculate Cook's distance
        influence_power = model_power.get_influence()
        cooks_d_power = influence_power.cooks_distance[0]

        influence_response_time = model_response_time.get_influence()
        cooks_d_response_time = influence_response_time.cooks_distance[0]

        return cooks_d_power, cooks_d_response_time

    # Calculate Cook's distance for both datasets
    cooks_d_power_grpc, cooks_d_response_time_grpc = calculate_cooks_distance(data_grpc)
    cooks_d_power_rest, cooks_d_response_time_rest = calculate_cooks_distance(data_rest)

    # Create the plot
    fig, ax = plt.subplots(figsize=(14, 8))

    ax.scatter(range(len(cooks_d_response_time_grpc)), cooks_d_response_time_grpc, alpha=1, label="gRPC Response Time", color='brown', marker='x')
    ax.scatter(range(len(cooks_d_power_grpc)), cooks_d_power_grpc, alpha=0.6, label="gRPC Energy Consumption", color='purple', marker='x')
    ax.scatter(range(len(cooks_d_response_time_rest)), cooks_d_response_time_rest, alpha=0.6, label="REST Response Time", color='orange', marker='x')
    ax.scatter(range(len(cooks_d_power_rest)), cooks_d_power_rest, alpha=0.6, label="REST Energy Consumption", color='pink', marker='x')

    ax.axhline(y=1, color='red', linestyle='--', label='Cook\'s Distance = 1')

    ax.set_title("Cook's Distance for Energy Consumption and Response Time in order system", fontsize=20)
    ax.set_xlabel('Observations', fontsize=16)
    ax.set_ylabel("Cook's Distance", fontsize=16)
    ax.tick_params(axis='both', which='major', labelsize=14)

    ax.legend(fontsize=14)
    plt.show()

# Call the function with the datasets and specify the path to save the plot
analyze_and_plot_combined_with_specified_colors(data_grpc, data_rest)
