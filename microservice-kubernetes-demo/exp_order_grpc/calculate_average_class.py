import pandas as pd

# 定义CSV文件路径和输出文件路径
cvs_path = 'data_combine_rest/data_combine_rest/result_rest.csv'
output_path = 'data_combine_rest/data_combine_rest/average_class_rest.csv'

# 读取CSV文件
df = pd.read_csv(cvs_path)

# 提取类别
df['Category'] = df['File Name'].apply(lambda x: '_'.join(x.split('_')[:3]))

# 打印数据框用于调试
print(df)

# 选择数值列
numeric_cols = df.select_dtypes(include='number').columns

# 分组计算均值
mean_df = df.groupby('Category')[numeric_cols].mean().reset_index()

# 将结果保存到新的CSV文件
mean_df.to_csv(output_path, index=False)

print(f"Mean values have been written to {output_path}")



