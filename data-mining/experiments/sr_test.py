# %% Import libraries
from sklearn.cluster import KMeans
import numpy as np
from kneed import DataGenerator, KneeLocator
import yfinance

# %% Read data
df = yfinance.download("BABA", "2020-05-16", "2021-10-16")
X = np.array(df["Close"])

# %%

sum_of_squared_distances = []
K = range(1, 15)
for k in K:
    km = KMeans(n_clusters=k)
    km = km.fit(X.reshape(-1, 1))
    sum_of_squared_distances.append(km.inertia_)
kn = KneeLocator(
    K, sum_of_squared_distances, S=1.0, curve="convex", direction="decreasing"
)
kn.plot_knee()
# plt.plot(sum_of_squared_distances)

# %% Find Min Max of each cluster
kmeans = KMeans(n_clusters=kn.knee).fit(X.reshape(-1, 1))
# kmeans = KMeans(n_clusters=5).fit(X.reshape(-1, 1))
c = kmeans.predict(X.reshape(-1, 1))
minmax = []
for i in range(5):
    minmax.append([-np.inf, np.inf])
for i in range(len(X)):
    cluster = c[i]
    if X[i] > minmax[cluster][0]:
        minmax[cluster][0] = X[i]
    if X[i] < minmax[cluster][1]:
        minmax[cluster][1] = X[i]

# %%
from matplotlib import pyplot as plt

minmax

for i in range(len(X)):
    # colors = ["b", "g", "r", "c", "m", "y", "k", "w"]
    # c = kmeans.predict(X[i].reshape(-1, 1))[0]
    # color = colors[c]
    plt.scatter(i, X[i], s=1)
for i in range(len(minmax)):
    plt.hlines(minmax[i][0], xmin=0, xmax=len(X), colors="g")
    plt.hlines(minmax[i][1], xmin=0, xmax=len(X), colors="r")
