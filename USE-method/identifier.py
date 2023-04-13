from Resource import CPU, RAM, Disk, Network

disk_names = "sda|nvme0n1"
network_names = "enp0s3|wlo1"

def use(resources):
    for resource in (resources):
        print(resource.name + ":")
        print("Utilization = " + str(resource.get_utilization()))
        print("Saturation = " + str(resource.get_saturation()))
        print("Errors = " + str(resource.get_errors()))



if(__name__ == "__main__"):
    resources = list()
    resources.append(CPU("CPU"))
    resources.append(RAM("RAM"))
    resources.append(Disk("Disk", disk_names))
    resources.append(Network("Network", network_names))

    use(resources)