import pandas as pd

# Load the provided CSV files
grpc_df = pd.read_csv('/Users/suecai/Downloads/microservice-kubernetes-master-5/microservice-kubernetes-demo/data_analysis/data/average_results_grpc.csv')
rest_df = pd.read_csv('/Users/suecai/Downloads/microservice-kubernetes-master-5/microservice-kubernetes-demo/data_analysis/data/average_results_rest.csv')

# Multiply the last column of each dataframe by 150
grpc_df.iloc[:, -1] = grpc_df.iloc[:, -1] * 150
rest_df.iloc[:, -1] = rest_df.iloc[:, -1] * 150

# Save the modified dataframes to new CSV files
grpc_df.to_csv('/Users/suecai/Downloads/microservice-kubernetes-master-5/microservice-kubernetes-demo/data_analysis/regression/calculated_average_results_grpc.csv', index=False)
rest_df.to_csv('/Users/suecai/Downloads/microservice-kubernetes-master-5/microservice-kubernetes-demo/data_analysis/regression/calculated_average_results_rest.csv', index=False)

# Combine the two dataframes
combined_df = pd.concat([grpc_df, rest_df])

# Save the combined dataframe to a new CSV file
combined_df.to_csv('/Users/suecai/Downloads/microservice-kubernetes-master-5/microservice-kubernetes-demo/data_analysis/regression/combined_average_results.csv', index=False)

# Display the modified dataframes and the combined dataframe
print("Modified gRPC Data:")
print(grpc_df.head())
print("\nModified REST Data:")
print(rest_df.head())
print("\nCombined Data:")
print(combined_df.head())