#!/bin/sh

if [ "$1" -eq 0 ]; then
    # Package removal, not upgrade
    systemctl --no-reload disable agent-server > /dev/null 2>&1 || :
    systemctl stop agent-server > /dev/null 2>&1 || :
fi
