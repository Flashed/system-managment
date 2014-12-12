#!/bin/bash
kill -9 `ps -Cjava -F | grep Client | grep -v grep | awk '{print $2}'`

