from resource import CPU, RAM, Disk, Network, Resource
import urllib.parse
import sys

args = sys.argv

if(len(args) > 2):
    exit("ERROR: expected 1 argument, received " + str(len(args) - 1) + "\nStandard time range is 5m, extra argument can be provided to indicate the time range up until current system time.")

if(len(args) == 2):
    if(args[1].isdigit()):
        Resource.time_range = int(args[1])
    else:
        exit("ERROR: invalid argument, please provide a digit as an argument.")

disk_names = "sda|nvme0n1"
network_names = "enp0s3|wlo1"
bandwidth = { # in bytes
    "download": 100000000 / 8, #100Mbps
    "upload": 100000000 / 8    #100Mbps
}

bottlenecks = {}

def use(resources):
    answer = ""
    for resource in (resources):
        print(resource.name)
        print(resource.get_utilization())
        print(resource.get_saturation())
        print(resource.get_errors())
        if(resource.get_errors() >= 1):
            investigate_error(resource)
        elif(resource.get_saturation() > 1):
            investigate_saturation(resource)
        elif(resource.name != "Network" and resource.get_utilization() > resource.threshold):
            investigate_utlization(resource)
        elif(resource.name == "Network" and (resource.get_utilization()[0] > resource.threshold or resource.get_utilization()[1] > resource.threshold)):
            investigate_utlization(resource)
            


    for resource in (bottlenecks):
        if(len(bottlenecks) == 1):
            bottlenecks[resource] = "is overloaded and most likely a bottleneck, try upgrading the current " + resource
        print(resource + " " + bottlenecks[resource])
    if(len(bottlenecks) == 0):
        print("Could not identify any bottlenecks")

def investigate_error(resource):
    if(resource.get_saturation() > 1 and resource.get_utilization() > resource.threshold):
        bottlenecks[resource.name] = "is overloaded and most likely a bottleneck, try upgrading the current " + resource.name

def investigate_saturation(resource):
    if(resource.get_utilization() > resource.threshold):
        bottlenecks[resource.name] = "is overloaded and most likely a bottleneck, try upgrading the current " + resource.name
    else:
        bottlenecks[resource.name] = "is most likely a nestled bottleneck caused by another resource"

def investigate_utlization(resource):
    bottlenecks[resource.name] = "has only high utilization but could potentially become a bottleneck"

if(__name__ == "__main__"):
    resources = list()
    resources.append(CPU("CPU"))
    resources.append(RAM("RAM"))
    resources.append(Disk("Disk", disk_names))
    resources.append(Network("Network", network_names, bandwidth))

    use(resources)
