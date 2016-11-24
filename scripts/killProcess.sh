#!/bin/bash
pid=$(cat /path/to/saved/pid/process.pid) 
kill -9 $pid
rm /path/to/saved/pid/process.pid
