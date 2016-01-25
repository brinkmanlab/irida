#!/bin/bash
rm -rf /tmp/gxbootstrap*
rm -rf /tmp/shed_tools/
pkill -u gitlab-runner -f "python ./scripts/paster.py" || true
echo 'drop database if exists irida_test; drop database if exists irida_galaxy_test; create database irida_test;create database irida_galaxy_test;' | mysql -u test -ptest
pushd lib
./install-libs.sh
popd
xvfb-run -a mvn clean verify -Pgalaxy_testing -Dliquibase.config.port=3309 -Djetty.port=8083 -Dtest.galaxy.database.connection="mysql://test:test@localhost/irida_galaxy_test?unix_socket=/var/run/mysqld/mysqld.sock"
