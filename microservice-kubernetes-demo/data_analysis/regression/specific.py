import pandas as pd
import statsmodels.api as sm

# Load the combined data
data = pd.read_csv('combined_average_results.csv')

# Create dummy variables for Frequency and Size
data = pd.get_dummies(data, columns=['Frequency', 'Size'], drop_first=True)

# Define the independent variables and add a constant for the intercept
independent_vars_joules = data[['Frequency_1', 'Frequency_2', 'Size_1', 'Size_2']]
independent_vars_joules = sm.add_constant(independent_vars_joules)

independent_vars_time = data[['Frequency_1', 'Frequency_2', 'Size_1', 'Size_2']]
independent_vars_time = sm.add_constant(independent_vars_time)

# Define the dependent variables
dependent_var_joules = data['Power']
dependent_var_time = data['Average Response Time']

# Perform linear regression for Power (Energy Consumption)
model_joules = sm.OLS(dependent_var_joules, independent_vars_joules).fit()

# Perform linear regression for Average Response Time
model_time = sm.OLS(dependent_var_time, independent_vars_time).fit()

# Summary of the models
summary_joules = model_joules.summary()
summary_time = model_time.summary()

print(summary_joules)

print(summary_time)
