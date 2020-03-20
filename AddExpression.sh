#!/bin/sh
java -cp bin:lib/guava-21.0.jar:lib/mysql.jar transform.AddExpression $@
