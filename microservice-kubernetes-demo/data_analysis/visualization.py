import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Define colors and palette
colors = ['#d9a540', '#d07c4b']
palette = {"gRPC": "orange", "REST": "brown"}

# Load the datasets
grpc_data = pd.read_csv('/Users/suecai/Downloads/microservice-kubernetes-master-5/microservice-kubernetes-demo/data_analysis/data/average_results_grpc.csv')
rest_data = pd.read_csv('/Users/suecai/Downloads/microservice-kubernetes-master-5/microservice-kubernetes-demo/data_analysis/data/average_results_rest.csv')

# Combine datasets and add a 'Type' column
grpc_data['Type'] = 'gRPC'
rest_data['Type'] = 'REST'
combined_data = pd.concat([grpc_data, rest_data])

# Calculate overall average response time and power consumption for gRPC and REST
grpc_avg_response_time = grpc_data['Average Response Time'].mean()
rest_avg_response_time = rest_data['Average Response Time'].mean()

grpc_avg_power = grpc_data['Power'].mean()
rest_avg_power = rest_data['Power'].mean()

# Prepare data for visualization
response_times = [grpc_avg_response_time, rest_avg_response_time]
power_consumptions = [grpc_avg_power, rest_avg_power]
labels = ['gRPC', 'REST']

# Create bar charts for average response time and power consumption
fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 8))

# Average Response Time
ax1.bar(labels, response_times, color=colors)
ax1.set_title('Average Response Time in order system: gRPC vs REST')
ax1.set_ylabel('Average Response Time (ms)')

# Average Power Consumption
ax2.bar(labels, power_consumptions, color=colors)
ax2.set_title('Average Power Consumption in order system: gRPC vs REST')
ax2.set_ylabel('Average Power Consumption')

plt.tight_layout()
plt.show()

# Create box plots for the response times and power consumption of both gRPC and REST using Seaborn
fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(12, 10))

# Box plot for Average Response Time
sns.boxplot(x='Type', y='Average Response Time', data=combined_data, ax=ax1, palette=palette)
ax1.set_title('Box Plot of Average Response Time in order system: gRPC vs REST')
ax1.set_ylabel('Average Response Time (ms)')

# Box plot for Power Consumption
sns.boxplot(x='Type', y='Power', data=combined_data, ax=ax2, palette=palette)
ax2.set_title('Box Plot of Power Consumption in order system: gRPC vs REST')
ax2.set_ylabel('Power Consumption')

plt.tight_layout()
plt.show()
