import requests
from datetime import datetime, timedelta
import statistics
import time


class Resource:
    def __init__(self, name):
        self.name = name
        self.end_time = datetime.utcnow()
        self.start_time = (self.end_time - timedelta(minutes=5)).strftime("%Y-%m-%dT%H:%M:%S.%fZ")
        self.end_time = self.end_time.strftime("%Y-%m-%dT%H:%M:%S.%fZ")
        self.step = "10s"
        self.url = "http://localhost:9090/api/v1/"
        self.base_query = "query_range?query={}&start={}&end={}&step={}"

    def get_utilization(self, query):
        response = requests.get(self.url + query).json()
        if(response["status"] == "success"):
            data_pairs = response["data"]["result"][0]["values"] #Will crash on CPU if spring boot application isn't running
            data = list()
            sum = 0.0
            for value in (data_pairs):
                sum += (float(value[1]))

            return sum / len(data_pairs) #Average Utilization % over 5min

        return self.report_http_error(response)

    def get_saturation(self):
        print("Saturation")

    def get_errors(self):
        print("errors")

    def report_http_error(self, response):
        return "status: " + response["status"] + "\nerrorType: " + response["errorType"] + "\nerror: " + response["error"] + "\n"


class CPU(Resource):
    def __init__(self, name):
        super().__init__(name)
        self.utilization_query = self.base_query.format("process_cpu_usage", self.start_time, self.end_time, self.step)
        self.saturation_query = self.base_query.format("node_schedstat_waiting_seconds_total", self.start_time, self.end_time, self.step)
        self.error_query = self.base_query

    def get_utilization(self):
        return super().get_utilization(self.utilization_query) * 100

    def get_saturation(self):
        response = requests.get(self.url + self.saturation_query).json()
        if(response["status"] == "success"):
            cores = list()
            result = response["data"]["result"]
            for core in (result):
                values = core["values"]
                cores.append(self.__avg_rql(values))
                        
            return sum(cores)

        return self.report_http_error(response)
            

    def get_errors(self):
        pass

    def __avg_rql(self, values): #Returns the average run queue length over intervals
        rq_measurements = list()
        for i, rq_wait in enumerate(values):
            if(i + 1 < len(values)):
                next_rq_wait = values[i + 1]
                avg_rq_wait = statistics.fabs(float(rq_wait[1]) - float(next_rq_wait[1])) / int(self.step.split("s")[0]) #https://stackoverflow.com/questions/440756/how-to-find-the-processor-queue-length-in-linux
                rq_measurements.append(avg_rq_wait)
        return statistics.mean(rq_measurements)


class RAM(Resource):
    def __init__(self, name):
        super().__init__(name)
        self.utilization_query = self.base_query.format("100 - ((node_memory_MemAvailable_bytes{instance='localhost:9100',job='node'} * 100) / node_memory_MemTotal_bytes{instance='localhost:9100',job='node'})", self.start_time, self.end_time, self.step)
        self.saturation_query = self.base_query
        self.error_query = self.base_query

    def get_utilization(self):
        return super().get_utilization(self.utilization_query)

    def get_saturation(self):
        # High Swap In(SI), Swap Out(SO) are indicator of RAM saturation
        # vmstat 1 (column SI, SO)
        pass

    def get_errors(self):
        pass


class Disk(Resource):
    def __init__(self, name, disk_names):
        super().__init__(name)
        self.utilization_query = self.base_query.format("irate(node_disk_io_time_seconds_total{instance='localhost:9100',job='node',device=~'" + disk_names + "'} [1m0s])", self.start_time, self.end_time, self.step)
        self.saturation_query = self.base_query
        self.error_query = self.base_query

    def get_utilization(self):
        return super().get_utilization(self.utilization_query) * 100

    def get_saturation(self):
        pass

    def get_errors(self):
        pass