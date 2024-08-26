import pandas as pd
import statsmodels.api as sm

# Load the combined data
data = pd.read_csv('calculated_average_results_grpc.csv')

# Define the independent variables and add a constant for the intercept
independent_vars = data[['Frequency', 'Size']]
independent_vars = sm.add_constant(independent_vars)

# Define the dependent variables
dependent_var_joules = data['Power']
dependent_var_time = data['Average Response Time']

# Perform linear regression for Power (Energy Consumption)
model_joules = sm.OLS(dependent_var_joules, independent_vars).fit()

# Perform linear regression for Average Response Time
model_time = sm.OLS(dependent_var_time, independent_vars).fit()

# Summary of the models
summary_joules = model_joules.summary()
summary_time = model_time.summary()

print(summary_joules)
print(summary_time)
