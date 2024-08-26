
import pandas as pd
import matplotlib.pyplot as plt

# Load the data from the uploaded file
file_path = 'data/average_results_grpc.csv'
file_path_rest = 'data/average_results_rest.csv'
data = pd.read_csv(file_path)
data_rest = pd.read_csv(file_path_rest)

# Grouping the data by Frequency and Size
grouped_data = data.groupby(['Frequency', 'Size'])
grouped_data_rest = data_rest.groupby(['Frequency', 'Size'])

# Calculating the min, mean, max, and standard deviation for Average Response Time for each group
stats = grouped_data['Average Response Time'].agg(['min', 'mean', 'max', 'std']).reset_index()
stats_rest = grouped_data_rest['Average Response Time'].agg(['min', 'mean', 'max', 'std']).reset_index()

# Formatting the statistics to two decimal places
stats = stats.round(2)
stats_rest = stats_rest.round(2)

# Calculating the min, mean, max, and standard deviation for Power (Energy Consumption) for each group
stats_energy = grouped_data['Power'].agg(['min', 'mean', 'max', 'std']).reset_index()
stats_energy = stats_energy.round(2)
stats_energy_rest = grouped_data_rest['Power'].agg(['min', 'mean', 'max', 'std']).reset_index()
stats_energy_rest = stats_energy_rest.round(2)

# Converting Power (Watts) to Energy (Joules) assuming each measurement corresponds to 150 seconds
stats_energy['min_joules'] = stats_energy['min'] * 150
stats_energy['mean_joules'] = stats_energy['mean'] * 150
stats_energy['max_joules'] = stats_energy['max'] * 150
stats_energy['std_joules'] = stats_energy['std'] * 150

stats_energy_rest['min_joules'] = stats_energy_rest['min'] * 150
stats_energy_rest['mean_joules'] = stats_energy_rest['mean'] * 150
stats_energy_rest['max_joules'] = stats_energy_rest['max'] * 150
stats_energy_rest['std_joules'] = stats_energy_rest['std'] * 150

# Mapping frequency and size to human-readable labels
frequency_map = {0: 'low', 1: 'medium', 2: 'high'}
size_map = {0: 'small', 1: 'medium', 2: 'large'}

# Creating human-readable labels for combined key
stats['Freq_Size_Label'] = stats['Frequency'].map(frequency_map) + '_' + stats['Size'].map(size_map)
stats_rest['Freq_Size_Label'] = stats_rest['Frequency'].map(frequency_map) + '_' + stats_rest['Size'].map(size_map)
stats_energy['Freq_Size_Label'] = stats_energy['Frequency'].map(frequency_map) + '_' + stats_energy['Size'].map(size_map)
stats_energy_rest['Freq_Size_Label'] = stats_energy_rest['Frequency'].map(frequency_map) + '_' + stats_energy_rest['Size'].map(size_map)

# Combining the two datasets for plotting
combined_stats = pd.DataFrame({
    'Combination': stats['Freq_Size_Label'],
    'gRPC': stats['mean'],
    'REST': stats_rest['mean']
})

combined_energy_stats = pd.DataFrame({
    'Combination': stats_energy['Freq_Size_Label'],
    'gRPC': stats_energy['mean_joules'],
    'REST': stats_energy_rest['mean_joules']
})

# Define the original colors for the bar charts
grpc_color_original = '#1f77b4'  # original color for gRPC
rest_color_original = '#ff7f0e'  # original color for REST

# Re-plotting the mean response time bar chart to use original colors
fig, ax = plt.subplots(figsize=(12, 8))
bar_width = 0.35
index = range(len(combined_stats))
bar1 = ax.bar(index, combined_stats['gRPC'], bar_width, label='gRPC', color=grpc_color_original)
bar2 = ax.bar([p + bar_width for p in index], combined_stats['REST'], bar_width, label='REST', color=rest_color_original)
ax.set_xlabel('Frequency_Size Combination')
ax.set_ylabel('Mean Response Time (ms)')
ax.set_title('Mean Response Time for gRPC and REST by Frequency and Size')
ax.set_xticks([p + bar_width / 2 for p in index])
ax.set_xticklabels(combined_stats['Combination'], rotation=45, ha='right')
ax.legend()
plt.tight_layout()
plt.grid(True)
plt.show()

# Re-plotting the combined bar chart for energy consumption to use original colors
fig, ax = plt.subplots(figsize=(12, 8))
bar1 = ax.bar(index, combined_energy_stats['gRPC'], bar_width, label='gRPC', color=grpc_color_original)
bar2 = ax.bar([p + bar_width for p in index], combined_energy_stats['REST'], bar_width, label='REST', color=rest_color_original)
ax.axhline(y=15.67 * 150, color='blue', linestyle='--', linewidth=1, label='gRPC Idle Energy')
ax.axhline(y=15.56 * 150, color='red', linestyle='--', linewidth=1, label='REST Idle Energy')
ax.set_xlabel('Frequency_Size Combination')
ax.set_ylabel('Mean Energy Consumption (Joules)')
ax.set_title('Mean Energy Consumption for gRPC and REST by Frequency and Size')
ax.set_xticks([p + bar_width / 2 for p in index])
ax.set_xticklabels(combined_energy_stats['Combination'], rotation=45, ha='right')
ax.legend()
ax.set_ylim(2000, combined_energy_stats[['gRPC', 'REST']].max().max() + 100)
plt.tight_layout()
plt.grid(True)
plt.show()


# Adjusting the colors of the box plots for response time to use original colors and setting median lines to black
fig, ax = plt.subplots(figsize=(12, 8))
data_combined = [data[(data['Frequency'] == freq) & (data['Size'] == size)]['Average Response Time']
                 for freq in range(3) for size in range(3)]
data_rest_combined = [data_rest[(data_rest['Frequency'] == freq) & (data_rest['Size'] == size)]['Average Response Time']
                      for freq in range(3) for size in range(3)]
bp = ax.boxplot(data_combined + data_rest_combined, patch_artist=True)
colors = [grpc_color_original] * 9 + [rest_color_original] * 9
for patch, color in zip(bp['boxes'], colors):
    patch.set_facecolor(color)
for median in bp['medians']:
    median.set_color('black')
ax.set_xlabel('Frequency_Size Combination')
ax.set_ylabel('Response Time (ms)')
ax.set_title('Box Plot of Response Time for gRPC and REST by Frequency and Size')
ax.set_xticks(range(1, 19))
ax.set_xticklabels(combined_stats['Combination'].tolist() * 2, rotation=45, ha='right')
# Add legend for the box plots
handles = [plt.Line2D([0], [0], color=grpc_color_original, lw=4),
           plt.Line2D([0], [0], color=rest_color_original, lw=4)]
labels = ['gRPC', 'REST']
ax.legend(handles, labels)
plt.tight_layout()
plt.grid(True)
plt.show()

# Adjusting the colors of the box plots for energy consumption to use original colors and setting median lines to black
fig, ax = plt.subplots(figsize=(12, 8))
data_energy_combined = [data[(data['Frequency'] == freq) & (data['Size'] == size)]['Power'] * 150
                        for freq in range(3) for size in range(3)]
data_energy_rest_combined = [data_rest[(data_rest['Frequency'] == freq) & (data_rest['Size'] == size)]['Power'] * 150
                             for freq in range(3) for size in range(3)]
bp = ax.boxplot(data_energy_combined + data_energy_rest_combined, patch_artist=True)
colors = [grpc_color_original] * 9 + [rest_color_original] * 9
for patch, color in zip(bp['boxes'], colors):
    patch.set_facecolor(color)
for median in bp['medians']:
    median.set_color('black')

ax.axhline(y=15.67 * 150, color='blue', linestyle='--', linewidth=1, label='gRPC Idle Energy')
ax.axhline(y=15.56 * 150, color='red', linestyle='--', linewidth=1, label='REST Idle Energy')
ax.set_xlabel('Frequency_Size Combination')
ax.set_ylabel('Energy Consumption (Joules)')
ax.set_xticks(range(1, 19))
ax.set_xticklabels(combined_stats['Combination'].tolist() * 2, rotation=45, ha='right')
ax.set_title('Box Plot of Energy Consumption for gRPC and REST by Frequency and Size')
# Add legend for the box plots
handles = [plt.Line2D([0], [0], color=grpc_color_original, lw=4),
           plt.Line2D([0], [0], color=rest_color_original, lw=4),
           plt.Line2D([0], [0], color='blue', linestyle='--', lw=1),
           plt.Line2D([0], [0], color='red', linestyle='--', lw=1)]
labels = ['gRPC', 'REST', 'gRPC Idle Energy', 'REST Idle Energy']
ax.legend(handles, labels)

# Annotate idle lines
ax.annotate('2350.50', xy=(0.5, 15.67 * 150), xytext=(-0.8, 15.67 * 150), textcoords='data',
            arrowprops=dict(facecolor='#1f77b4', shrink=0.01))
ax.annotate('2334.00', xy=(0.5, 15.56 * 150), xytext=(-0.8, 15.56 * 150), textcoords='data',
            arrowprops=dict(facecolor='#ff7f0e', shrink=0.01))

plt.xticks(rotation=45)
plt.tight_layout()
plt.grid(True)
plt.show()


# # Re-plotting the combined bar chart for energy consumption in watts to use original colors
# fig, ax = plt.subplots(figsize=(12, 8))
# bar1 = ax.bar(index, stats_energy['mean'], bar_width, label='gRPC', color=grpc_color_original)
# bar2 = ax.bar([p + bar_width for p in index], stats_energy_rest['mean'], bar_width, label='REST', color=rest_color_original)
# ax.axhline(y=15.67, color='blue', linestyle='--', linewidth=1, label='gRPC Idle Energy')
# ax.axhline(y=15.56, color='red', linestyle='--', linewidth=1, label='REST Idle Energy')
# ax.set_xlabel('Frequency_Size Combination')
# ax.set_ylabel('Mean Energy Consumption (Joules)')
# ax.set_title('Mean Energy Consumption for gRPC and REST by Frequency and Size')
# ax.set_xticks([p + bar_width / 2 for p in index])
# ax.set_xticklabels(stats_energy['Freq_Size_Label'], rotation=45, ha='right')
# ax.legend()
# ax.set_ylim(15, stats_energy[['mean']].max().max() + 5)
# plt.tight_layout()
# plt.grid(True)
# plt.show()