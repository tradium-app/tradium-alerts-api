# %%
import sys

sys.path.insert(0, "../")
from environs import Env
from pytz import utc
from apscheduler.schedulers.background import BackgroundScheduler
from apscheduler.jobstores.mongodb import MongoDBJobStore

# from apscheduler.jobstores.sqlalchemy import SQLAlchemyJobStore
from apscheduler.executors.pool import ThreadPoolExecutor, ProcessPoolExecutor
from apscheduler.triggers.cron import CronTrigger
from apscheduler.events import EVENT_JOB_ERROR, EVENT_JOB_EXECUTED
from sr_calculator.sr_calculator import SRCalculator
from pymongo import MongoClient

# %%

env = Env()
env.read_env()
DATABASE_URL = env("spring.data.mongodb.uri")

jobstores = {
    "default": MongoDBJobStore(
        client=MongoClient(host=[DATABASE_URL]), database="salertdb"
    ),
}

executors = {"default": ThreadPoolExecutor(20), "processpool": ProcessPoolExecutor(5)}

job_defaults = {"coalesce": False, "max_instances": 1}
scheduler = BackgroundScheduler(
    jobstores=jobstores, executors=executors, job_defaults=job_defaults, timezone=utc
)

scheduler.add_job(
    id="sr_calculator",
    func=SRCalculator().calculate,
    trigger=CronTrigger(hour="8-20/4", minute="0", timezone="est"),
    replace_existing=True,
)


def on_job_completed(event):
    if event.exception:
        print(f"Job crashed: {event.job_id}")
    pass


scheduler.add_listener(on_job_completed, EVENT_JOB_EXECUTED | EVENT_JOB_ERROR)

scheduler.start()

# while True:
#     time.sleep(10)
#     gc.collect()
