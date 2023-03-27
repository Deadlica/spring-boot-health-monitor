#Prometheus timezone is 1 hour before swedish time hence all the -1h in  the program
from Resource import Resource

def use(resources):
    for resource in (resources):
        utilization = resource.get_utilization()
        print(utilization)














if(__name__ == "__main__"):
    url = "http://localhost:9090/api/v1/"
    resources = list()
    resources.append(Resource("CPU"))
    resources.append(Resource("RAM"))
    resources.append(Resource("Disk"))

    use(resources)