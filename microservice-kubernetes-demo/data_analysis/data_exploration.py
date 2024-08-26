import pandas as pd

# Load the data from the CSV files
grpc_data = pd.read_csv('data/average_results_grpc.csv')
rest_data = pd.read_csv('data/average_results_rest.csv')

# Display the first few rows of each dataframe
grpc_data_head = grpc_data.head()
rest_data_head = rest_data.head()

grpc_data_head, rest_data_head


# Function to compute descriptive statistics
def compute_statistics(df):
    stats = {
        'Min': df.min(),
        '1st Qu': df.quantile(0.25),
        'Median': df.median(),
        'Mean': df.mean(),
        '3rd Qu': df.quantile(0.75),
        'Max': df.max(),
        'St.Deviation': df.std()
    }
    return pd.DataFrame(stats)

# Group by 'Request Type' and 'Frequency' and compute statistics for 'Power'
grpc_stats = grpc_data.groupby(['Request Type', 'Frequency'])['Power'].apply(compute_statistics).unstack(level=2)
rest_stats = rest_data.groupby(['Request Type', 'Frequency'])['Power'].apply(compute_statistics).unstack(level=2)

grpc_stats, rest_stats

