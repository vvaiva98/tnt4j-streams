#! /bin/bash
PARSERS_CFG=$*
if [ "$1" == "" ]; then
	PARSERS_CFG=parsers.xml
fi
../../bin/tnt4j-streams -p:${PARSERS_CFG}