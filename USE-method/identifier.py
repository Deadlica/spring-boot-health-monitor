from Resource import CPU, RAM, Disk

disk_names = "sda|nvme0n1"

def use(resources):
    for resource in (resources):
        utilization = resource.get_utilization()
        #print(utilization)



if(__name__ == "__main__"):
    resources = list()
    resources.append(CPU("CPU"))
    resources.append(RAM("RAM"))
    resources.append(Disk("Disk", disk_names))
    print(resources[0].get_saturation())

    use(resources)