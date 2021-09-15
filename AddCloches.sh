#!/bin/sh
javac -cp lib/guava-21.0.jar:lib/mysql.jar -sourcepath src -d bin src/transform/AddCloches.java
if [ $? = 0 ]; then
  java -cp bin:lib/guava-21.0.jar:lib/mysql.jar transform.AddCloches $@
fi
