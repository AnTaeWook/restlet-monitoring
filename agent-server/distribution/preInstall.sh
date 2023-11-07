#!/bin/sh

# change sysstat schedule time 10 -> 1
sed -i "s/\*\/10/\*\/1/g" /etc/cron.d/sysstat
