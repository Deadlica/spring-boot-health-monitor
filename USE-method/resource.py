import requests
from datetime import datetime, timedelta
import statistics
import time
import urllib.parse


class Resource:
    time_range = 5
    def __init__(self, name):
        self.name = name
        self.threshold = 80
        self.end_time = datetime.utcnow()
        self.start_time = (self.end_time - timedelta(minutes=Resource.time_range)).strftime("%Y-%m-%dT%H:%M:%S.%fZ")
        self.end_time = self.end_time.strftime("%Y-%m-%dT%H:%M:%S.%fZ")
        self.step = "10s"
        self.url = "http://localhost:9090/api/v1/"
        self.base_query = "query_range?query={}&start={}&end={}&step={}"

    def get_utilization(self, query):
        return self.compute_average(self.url, query)

    def get_saturation(self, query):
        return self.compute_average(self.url, query)

    def get_errors(self, query):
        return self.compute_average(self.url, query)

    def compute_average(self, url, query):
        response = requests.get(url + query).json()
        if(response["status"] == "success"):
            data_pairs = response["data"]["result"][0]["values"]
            data = list()
            sum = 0.0
            for value in (data_pairs):
                sum += (float(value[1]))

            return sum / len(data_pairs) #Average Utilization

        return self.report_http_error(response)

    def report_http_error(self, response):
        print("status: " + response["status"] + "\nerrorType: " + response["errorType"] + "\nerror: " + response["error"])
        exit(-1)


class CPU(Resource):
    def __init__(self, name):
        super().__init__(name)
        self.utilization_query = self.base_query.format("system_cpu_usage", self.start_time, self.end_time, self.step)
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
        return 0

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
        self.saturation_query = self.base_query.format("sum(rate(node_vmstat_pswpin{job%3D'node'}[1m])+%2Brate(node_vmstat_pswpout{job%3D'node'}[1m]))", self.start_time, self.end_time, self.step)
        self.error_query = self.base_query

    def get_utilization(self):
        return super().get_utilization(self.utilization_query)

    def get_saturation(self): #Number of swap in/out
        return super().get_saturation(self.saturation_query)

    def get_errors(self):
        return 0


class Disk(Resource):
    def __init__(self, name, disk_names):
        super().__init__(name)
        self.threshold = 60
        self.utilization_query = self.base_query.format("irate(node_disk_io_time_seconds_total{instance='localhost:9100',job='node',device=~'" + disk_names + "'} [1m0s])", self.start_time, self.end_time, self.step)
        self.saturation_query = self.base_query.format("node_disk_io_now{device=~'" + disk_names + "', instance='localhost:9100', job='node'}", self.start_time, self.end_time, self.step)
        self.error_query = self.base_query

    def get_utilization(self):
        return super().get_utilization(self.utilization_query) * 100

    def get_saturation(self):
        return super().get_saturation(self.saturation_query)

    def get_errors(self):
        return 0

    
class Network(Resource):
    def __init__(self, name, network_names, bandwidth):
        super().__init__(name)
        self.upload_utilization_query = self.base_query.format("100 * rate(node_network_transmit_bytes_total{device=~'" + network_names + "'}[1m]) / " + str(bandwidth["upload"]), self.start_time, self.end_time, self.step)
        self.download_utilization_query = self.base_query.format("100 * rate(node_network_receive_bytes_total{device=~'" + network_names + "'}[1m]) / " + str(bandwidth["download"]), self.start_time, self.end_time, self.step)
        self.saturation_query = self.base_query.format("sum%28rate%28node_network_receive_drop_total%7Bdevice%3D~%27" + network_names + "%27%7D%5B1m%5D%29+%2B+rate%28node_network_transmit_drop_total%7Bdevice%3D~%27" + network_names + "%27%7D%5B1m%5D%29%29", self.start_time, self.end_time, self.step)
        self.error_query = self.base_query.format("sum%28rate%28node_network_receive_errs_total%7Bdevice%3D~%27" + network_names + "%27%7D%5B1m%5D%29+%2B+rate%28node_network_transmit_errs_total%7Bdevice%3D~%27" + network_names + "%27%7D%5B1m%5D%29%29", self.start_time, self.end_time, self.step)

    def get_utilization(self):
        upload = super().get_utilization(self.upload_utilization_query)
        download = super().get_utilization(self.download_utilization_query)
        return [upload, download]

    def get_saturation(self):
        return super().get_saturation(self.saturation_query)

    def get_errors(self):
        return super().get_errors(self.error_query)