#!/bin/bash
java -jar gserver.jar 4444 &

for i in 0 1 2 3 4 5 6 7
do
	java -jar soloShape.jar $i 5 &
done
