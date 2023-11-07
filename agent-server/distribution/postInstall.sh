#!/bin/sh

if [ "$1" -ge 1 ]; then
    # start and enable client-agent service
    # Initial installation & upgrade
    systemctl daemon-reload > /dev/null 2>&1 || :
fi
