import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.lines as mlines

# Load the data from the provided file paths
file_path_grpc = 'data/average_results_grpc.csv'
file_path_rest = 'data/average_results_rest.csv'

# Read the CSV files into DataFrames
data_grpc = pd.read_csv(file_path_grpc)
data_rest = pd.read_csv(file_path_rest)

# Converting Power (Watts) to Energy (Joules) assuming each measurement corresponds to 150 seconds
data_grpc['Energy (Joules)'] = data_grpc['Power'] * 150
data_rest['Energy (Joules)'] = data_rest['Power'] * 150

# Mapping frequency and size to human-readable labels
frequency_map = {0: 'low', 1: 'medium', 2: 'high'}
size_map = {0: 'small', 1: 'medium', 2: 'large'}

# Define a function to create bar charts and box plots for a given grouping
def plot_barchart_and_boxplot_by_frequency(data_grpc, data_rest, value_col, title_prefix, ylabel, idle_values=None):
    frequencies = sorted(data_grpc['Frequency'].unique())

    fig, axes = plt.subplots(2, 1, figsize=(12, 12))

    # Bar Chart
    means = data_grpc.groupby('Frequency')[value_col].mean().reindex(frequencies)
    means_rest = data_rest.groupby('Frequency')[value_col].mean().reindex(frequencies)
    axes[0].bar([frequency_map[freq] for freq in frequencies], means.values, width=0.4, label='gRPC', align='center')
    axes[0].bar([frequency_map[freq] for freq in frequencies], means_rest.values, width=0.4, label='REST', align='edge')
    axes[0].set_title(f'{title_prefix} Mean {ylabel} by Frequency')
    axes[0].set_xlabel('Frequency')
    axes[0].set_ylabel(f'Mean {ylabel}')
    if idle_values:
        axes[0].axhline(y=idle_values['gRPC'], color='blue', linestyle='--', linewidth=1, label='gRPC Idle Energy')
        axes[0].axhline(y=idle_values['REST'], color='red', linestyle='--', linewidth=1, label='REST Idle Energy')
    axes[0].legend(loc='upper right')

    # Box Plot
    boxplot_data_grpc = [data_grpc[data_grpc['Frequency'] == freq][value_col] for freq in frequencies]
    boxplot_data_rest = [data_rest[data_rest['Frequency'] == freq][value_col] for freq in frequencies]
    bplot = axes[1].boxplot(boxplot_data_grpc + boxplot_data_rest, patch_artist=True,
                            tick_labels=[frequency_map[freq] for freq in frequencies] * 2)

    colors = ['#1f77b4'] * len(frequencies) + ['#ff7f0e'] * len(frequencies)
    for patch, color in zip(bplot['boxes'], colors):
        patch.set_facecolor(color)

    # Change median line color to black
    for median in bplot['medians']:
        median.set_color('black')

    axes[1].set_title(f'{title_prefix} Box Plot of {ylabel} by Frequency')
    axes[1].set_xlabel('Frequency')
    axes[1].set_ylabel(ylabel)
    if idle_values:
        axes[1].axhline(y=idle_values['gRPC'], color='blue', linestyle='--', linewidth=1, label='_nolegend_')
        axes[1].axhline(y=idle_values['REST'], color='red', linestyle='--', linewidth=1, label='_nolegend_')

        # Create custom legend lines
        grpc_line = mlines.Line2D([], [], color='blue', linestyle='--', linewidth=1, label='gRPC Idle Energy')
        rest_line = mlines.Line2D([], [], color='green', linestyle='--', linewidth=1, label='REST Idle Energy')
        axes[1].legend(handles=[grpc_line, rest_line], loc='upper right')

    plt.tight_layout()
    plt.show()

# Define a function to create bar charts and box plots for a given grouping
def plot_barchart_and_boxplot_by_size(data_grpc, data_rest, value_col, title_prefix, ylabel, idle_values=None):
    sizes = sorted(data_grpc['Size'].unique())

    fig, axes = plt.subplots(2, 1, figsize=(12, 12))

    # Bar Chart
    means = data_grpc.groupby('Size')[value_col].mean().reindex(sizes)
    means_rest = data_rest.groupby('Size')[value_col].mean().reindex(sizes)
    axes[0].bar([size_map[size] for size in sizes], means.values, width=0.4, label='gRPC', align='center')
    axes[0].bar([size_map[size] for size in sizes], means_rest.values, width=0.4, label='REST', align='edge')
    axes[0].set_title(f'{title_prefix} Mean {ylabel} by Size')
    axes[0].set_xlabel('Size')
    axes[0].set_ylabel(f'Mean {ylabel}')
    if idle_values:
        axes[0].axhline(y=idle_values['gRPC'], color='blue', linestyle='--', linewidth=1, label='gRPC Idle Energy')
        axes[0].axhline(y=idle_values['REST'], color='red', linestyle='--', linewidth=1, label='REST Idle Energy')
    axes[0].legend(loc='upper right')

    # Box Plot
    boxplot_data_grpc = [data_grpc[data_grpc['Size'] == size][value_col] for size in sizes]
    boxplot_data_rest = [data_rest[data_rest['Size'] == size][value_col] for size in sizes]
    bplot = axes[1].boxplot(boxplot_data_grpc + boxplot_data_rest, patch_artist=True,
                            tick_labels=[size_map[size] for size in sizes] * 2)

    colors = ['#1f77b4'] * len(sizes) + ['#ff7f0e'] * len(sizes)
    for patch, color in zip(bplot['boxes'], colors):
        patch.set_facecolor(color)

    # Change median line color to black
    for median in bplot['medians']:
        median.set_color('black')

    axes[1].set_title(f'{title_prefix} Box Plot of {ylabel} by Size')
    axes[1].set_xlabel('Size')
    axes[1].set_ylabel(ylabel)
    if idle_values:
        axes[1].axhline(y=idle_values['gRPC'], color='blue', linestyle='--', linewidth=1, label='_nolegend_')
        axes[1].axhline(y=idle_values['REST'], color='red', linestyle='--', linewidth=1, label='_nolegend_')

        # Create custom legend lines
        grpc_line = mlines.Line2D([], [], color='blue', linestyle='--', linewidth=1, label='gRPC Idle Energy')
        rest_line = mlines.Line2D([], [], color='red', linestyle='--', linewidth=1, label='REST Idle Energy')
        axes[1].legend(handles=[grpc_line, rest_line], loc='upper right')

    plt.tight_layout()
    plt.show()

# Bar chart and box plot for response time under different request frequencies
plot_barchart_and_boxplot_by_frequency(data_grpc, data_rest, 'Average Response Time', 'Response Time', 'Response Time (ms)')

# Bar chart and box plot for energy consumption under different request frequencies
plot_barchart_and_boxplot_by_frequency(data_grpc, data_rest, 'Energy (Joules)', 'Energy Consumption', 'Energy Consumption (Joules)',
                                       idle_values={'gRPC': 15.67 * 150, 'REST': 15.56 * 150})

# Bar chart and box plot for response time under different request sizes
plot_barchart_and_boxplot_by_size(data_grpc, data_rest, 'Average Response Time', 'Response Time', 'Response Time (ms)')

# Bar chart and box plot for energy consumption under different request sizes
plot_barchart_and_boxplot_by_size(data_grpc, data_rest, 'Energy (Joules)', 'Energy Consumption', 'Energy Consumption (Joules)',
                                  idle_values={'gRPC': 15.67 * 150, 'REST': 15.56 * 150})
