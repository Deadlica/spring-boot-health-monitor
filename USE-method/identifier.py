from Resource import CPU, RAM, Disk

disk_names = "sda|nvme0n1"

def use(resources):
    for resource in (resources):
        utilization = resource.get_utilization()
        print(resource.name + ":")
        print("Utilization = " + str(utilization))
        print("Saturation = " + str(resource.get_saturation()))
        print("Errors = " + str(resource.get_errors()))



if(__name__ == "__main__"):
    resources = list()
    resources.append(CPU("CPU"))
    resources.append(RAM("RAM"))
    resources.append(Disk("Disk", disk_names))

    use(resources)