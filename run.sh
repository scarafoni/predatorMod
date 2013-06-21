#!/bin/bash

for i in 1 2 3 4 5 6 7 8
do
	java -jar soloShape.jar $i 9 &
done