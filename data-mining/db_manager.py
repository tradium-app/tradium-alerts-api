# %%
import numpy as np
import pymongo
from environs import Env

env = Env()
env.read_env()


class DBManager:
    def __init__(self):
        mongo_uri = env("spring.data.mongodb.uri")
        mongoClient = pymongo.MongoClient(mongo_uri)
        self.salertdb = mongoClient["salertdb"]

    def loadStockHistory(self):
        usersCollection = self.salertdb["users"]
        users = usersCollection.find({}, {"watchList": 1})

        allStocks = []
        for x in users:
            if "watchList" in x:
                allStocks = np.concatenate((allStocks, x["watchList"]), axis=0)

        allStocks = np.unique(allStocks)

        stockHistoryCol = self.salertdb["stockHistory"]

        stockHistories = stockHistoryCol.find(
            {"symbol": {"$in": allStocks.tolist()}},
            {"symbol": 1, "daily_priceHistory": 1},
        )

        stockClosePriceMap = {}

        for history in stockHistories:
            result = map(lambda x: x["close"], history["daily_priceHistory"])
            stockClosePriceMap[history["symbol"]] = list(result)

        return stockClosePriceMap

    def saveSR(self, symbol, sr):
        stockCol = self.salertdb["stocks"]
        stockCol.update_one({"symbol": symbol}, {"$set": {"sr": sr}})


if __name__ == "__main__":
    stockClosePriceMap = DBManager().loadStockHistory()
    print(stockClosePriceMap)
