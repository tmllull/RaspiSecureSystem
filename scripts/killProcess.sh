#!/bin/bash
pid=$(cat /path/to/save/pid/process.pid) 
kill -9 $pid
rm /path/to/save/pid/process.pid
