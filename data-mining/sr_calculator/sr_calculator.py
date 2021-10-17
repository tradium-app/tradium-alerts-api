# %%
import sys

sys.path.insert(0, "../")
from sklearn.cluster import KMeans
import numpy as np
from kneed import KneeLocator
from datetime import datetime
import logging
from db_manager import DBManager

logging.root.setLevel(logging.INFO)


class SRCalculator:
    def calculate(self):
        stockClosePriceMaps = DBManager().loadStockHistory()

        for symbol in stockClosePriceMaps:
            knees = self.getKnees(stockClosePriceMaps[symbol])
            minmax = self.getClusterMinMax(stockClosePriceMaps[symbol], knees)
            self.saveSR(symbol, minmax)

        logging.info(f"SRCalculator rat at {datetime.now()}.")

    def saveSR(self, symbol, minmax):
        sr = []
        for x in minmax:
            sr = np.concatenate((sr, x), axis=0)

        sr = np.sort(sr)
        DBManager().saveSR(symbol, sr.tolist())

    def getClusterMinMax(self, X, cluster_no):
        X = np.array(X)
        kmeans = KMeans(n_clusters=cluster_no).fit(X.reshape(-1, 1))
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

        return list(filter(lambda x: x[0] != 0 and x[0] != -np.inf, minmax))

    def getKnees(self, X):
        X = np.array(X)
        sum_of_squared_distances = []
        K = range(1, 15)
        for k in K:
            km = KMeans(n_clusters=k)
            km = km.fit(X.reshape(-1, 1))
            sum_of_squared_distances.append(km.inertia_)
        kn = KneeLocator(
            K, sum_of_squared_distances, S=1.0, curve="convex", direction="decreasing"
        )

        return kn.knee


if __name__ == "__main__":
    srCalculator = SRCalculator()
    srCalculator.calculate()
