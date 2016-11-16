#!/bin/bash

if [ ! -f /path/to/save/pid/process.pid ]; then
        python /path/to/script/process.py &
        echo $! > /path/to/save/pid/process.pid
else 
        echo "Process is running"
fi
