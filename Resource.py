import requests
from datetime import datetime, timedelta
import statistics

end_time = datetime.utcnow()
start_time = (end_time - timedelta(minutes=5)).strftime("%Y-%m-%dT%H:%M:%S.%fZ")
end_time = end_time.strftime("%Y-%m-%dT%H:%M:%S.%fZ")
step = "10s"

class Resource:
    url = "http://localhost:9090/api/v1/"
    base_query = "query_range?query={}&start={}&end={}&step={}"
    utilization_queries = {
        "CPU": base_query.format("process_cpu_usage", start_time, end_time, step),
        "RAM": base_query.format("100 - ((node_memory_MemAvailable_bytes{instance='localhost:9100',job='node'} * 100) / node_memory_MemTotal_bytes{instance='localhost:9100',job='node'})", start_time, end_time, step),
        "Disk": base_query.format("irate(node_disk_io_time_seconds_total{instance='localhost:9100',job='node',device='sda'} [1m0s])", start_time, end_time, step)
    }
    
    def __init__(self, name):
        self.name = name

    def get_utilization(self):
        response = requests.get(Resource.url + Resource.utilization_queries[self.name]).json()
        if(response["status"] == "success"):
            data_pairs = response["data"]["result"][0]["values"] #Will crash on CPU if spring boot application isn't running
            data = list()
            sum = 0.0
            for value in (data_pairs):
                sum += (float(value[1]))

            return sum / len(data_pairs)

    def get_saturation(self):
        print("Saturation")

    def get_errors(self):
        print("errors")
