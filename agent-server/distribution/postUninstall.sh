#!/bin/sh

if [ "$1" -eq 0 ]; then
    # Package removal, not upgrade
    systemctl daemon-reload > /dev/null 2>&1 || :
    rm -rf /opt/agent-server
fi
