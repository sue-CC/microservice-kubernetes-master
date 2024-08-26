import pandas as pd

cvs_path = 'data_combine_rest/data_combine_rest/result_rest.csv'
output_path = 'data_combine_rest/data_combine_rest/average_class_rest.csv'

df = pd.read_csv(cvs_path)

df['Category'] = df['File Name'].apply(lambda x: '_'.join(x.split('_')[:3]))

print(df)

numeric_cols = df.select_dtypes(include='number').columns

mean_df = df.groupby('Category')[numeric_cols].mean().reset_index()

mean_df.to_csv(output_path, index=False)

print(f"Mean values have been written to {output_path}")



