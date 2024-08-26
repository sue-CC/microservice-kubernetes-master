import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from scipy import stats

# Load the datasets
grpc_data = pd.read_csv('/Users/suecai/Downloads/microservice-kubernetes-master-5/microservice-kubernetes-demo/data_analysis/data/average_results_grpc.csv')
rest_data = pd.read_csv('/Users/suecai/Downloads/microservice-kubernetes-master-5/microservice-kubernetes-demo/data_analysis/data/average_results_rest.csv')

# Extract the relevant columns for analysis
grpc_response_time = grpc_data['Average Response Time']
grpc_power = grpc_data['Power'] * 150
rest_response_time = rest_data['Average Response Time']
rest_power = rest_data['Power'] * 150

# Combine data for box plots
combined_data = pd.DataFrame({
    'Request Type': ['gRPC'] * len(grpc_response_time) + ['REST'] * len(rest_response_time),
    'Response Time (ms)': list(grpc_response_time) + list(rest_response_time),
    'Energy Consumption (J)': list(grpc_power) + list(rest_power)
})

# Function to perform normality check
def normality_check(data, title, xlabel):
    # QQ-plot
    plt.figure(figsize=(12, 6))
    plt.subplot(1, 2, 1)
    stats.probplot(data, dist="norm", plot=plt)
    plt.title(f'QQ-Plot of {title}')

    # Density plot
    plt.subplot(1, 2, 2)
    sns.histplot(data, kde=True)
    plt.title(f'Density Plot of {title}')
    # plt.xlim(0, data.max())
    plt.xlabel(xlabel)
    plt.show()

    # Shapiro-Wilk test
    stat, p_value = stats.shapiro(data)
    return stat, p_value

# Perform normality check for gRPC response time
grpc_response_time_stat, grpc_response_time_p = normality_check(grpc_response_time, 'gRPC Response Time in order system', 'Response Time (ms)')
grpc_power_stat, grpc_power_p = normality_check(grpc_power, 'gRPC Energy Consumption in order system', 'Response Time (ms)')
rest_response_time_stat, rest_response_time_p = normality_check(rest_response_time, 'REST Response Time in order system', 'Response Time (ms)')
rest_power_stat, rest_power_p = normality_check(rest_power, 'REST Energy Consumption ', 'Response Time (ms)')

# Print the Shapiro-Wilk test results
print(f'gRPC Response Time: stat={grpc_response_time_stat}, p-value={grpc_response_time_p}')
print(f'gRPC Energy Consumption: stat={grpc_power_stat}, p-value={grpc_power_p}')
print(f'REST Response Time: stat={rest_response_time_stat}, p-value={rest_response_time_p}')
print(f'REST Energy Consumption: stat={rest_power_stat}, p-value={rest_power_p}')

# Perform the Mann-Whitney U test
response_time_u_stat, response_time_u_p = stats.mannwhitneyu(grpc_response_time, rest_response_time)
power_u_stat, power_u_p = stats.mannwhitneyu(grpc_power, rest_power)

# Print the Mann-Whitney U test results
print(f'Response Time Mann-Whitney U Test: U-statistic={response_time_u_stat}, p-value={response_time_u_p}')
print(f'Power Mann-Whitney U Test: U-statistic={power_u_stat}, p-value={power_u_p}')

# Function to calculate Cliff's Delta
def cliffs_delta(x, y):
    m, n = len(x), len(y)
    more = sum(x_i > y_j for x_i in x for y_j in y)
    less = sum(x_i < y_j for x_i in x for y_j in y)
    delta = (more - less) / (m * n)
    magnitude = ""
    abs_delta = abs(delta)
    if abs_delta < 0.147:
        magnitude = "negligible"
    elif abs_delta < 0.33:
        magnitude = "small"
    elif abs_delta < 0.474:
        magnitude = "medium"
    else:
        magnitude = "large"
    return delta, magnitude

# Perform Cliff's Delta test
response_time_cliffs_delta, response_time_cliffs_delta_magnitude = cliffs_delta(grpc_response_time, rest_response_time)
power_cliffs_delta, power_cliffs_delta_magnitude = cliffs_delta(grpc_power, rest_power)

# Print the Cliff's Delta test results
print(f'Response Time Cliff\'s Delta: delta={response_time_cliffs_delta}, magnitude={response_time_cliffs_delta_magnitude}')
print(f'Power Cliff\'s Delta: delta={power_cliffs_delta}, magnitude={power_cliffs_delta_magnitude}')

# 设置全局字体大小
plt.rcParams.update({'font.size': 14})  # 你可以根据需要调整大小

# 定义颜色
grpc_color_original = '#1f77b4'  # original color for gRPC
rest_color_original = '#ff7f0e'  # original color for REST

# 生成带有指定颜色的箱线图
plt.figure(figsize=(12, 6))

# Response Time 的箱线图
plt.subplot(1, 2, 1)
sns.boxplot(x='Request Type', y='Response Time (ms)', data=combined_data, palette={'gRPC': grpc_color_original, 'REST': rest_color_original})
plt.title('Box Plot of Response Time (ms)', fontsize=16)  # 增大标题字体
plt.xlabel('Request Type', fontsize=14)  # 增大 x 轴标签字体
plt.ylabel('Response Time (ms)', fontsize=14)  # 增大 y 轴标签字体

# Energy Consumption 的箱线图
plt.subplot(1, 2, 2)
sns.boxplot(x='Request Type', y='Energy Consumption (J)', data=combined_data, palette={'gRPC': grpc_color_original, 'REST': rest_color_original})
plt.title('Box Plot of Energy Consumption (J)', fontsize=16)  # 增大标题字体
plt.xlabel('Request Type', fontsize=14)  # 增大 x 轴标签字体
plt.ylabel('Energy Consumption (J)', fontsize=14)  # 增大 y 轴标签字体

plt.show()