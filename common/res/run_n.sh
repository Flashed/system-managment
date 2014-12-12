#!/bin/bash
for  ((i=1; i<=$1; i++));
do
    exec $2 &
done
exit 0;