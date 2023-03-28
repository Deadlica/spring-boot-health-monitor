from Resource import CPU, RAM, Disk

current_station = "laptop" #Change this depeding on computer being used
disk_name = {
    "desktop": "sda",
    "laptop": "nvme0n1"
}

def use(resources):
    for resource in (resources):
        utilization = resource.get_utilization()
        #print(utilization)



if(__name__ == "__main__"):
    resources = list()
    resources.append(CPU("CPU"))
    resources.append(RAM("RAM"))
    resources.append(Disk("Disk", disk_name[current_station]))
    resources[0].get_saturation()

    use(resources)