#!/bin/bash

cd /home/samuel/IdeaProjects/spring-boot-health-monitor/prometheus/
./prometheus &
cd /home/samuel/IdeaProjects/spring-boot-health-monitor/grafana/bin/
./grafana-server &
/home/samuel/IdeaProjects/spring-boot-health-monitor/node_exporter/node_exporter &
exit 0
