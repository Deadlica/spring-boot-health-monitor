#!/bin/bash

cd /home/samuel/IdeaProjects/spring-boot-health-monitor/prometheus/
./prometheus &
cd /home/samuel/IdeaProjects/spring-boot-health-monitor/grafana/bin/
./grafana-server &
cd /home/samuel/IdeaProjects/spring-boot-health-monitor/node_exporter/
./node_exporter --collector.interrupts --collector.sysctl --collector.perf --collector.cpu.info &
exit 0
