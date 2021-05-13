#!/bin/bash -x

#TODO
# * Add volume for DB

# exit when any command fails
set -e
### keep track of the last executed command
##trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
### echo an error message before exiting
##trap 'echo "\"${last_command}\" command filed with exit code $?."' EXIT

ARECO_CURRENT_TEST_FOLDER=`dirname "$(realpath '$0')"`;
ARECO_HYBRIS_DIR=$(realpath $ARECO_CURRENT_TEST_FOLDER/../../hybris);
ARECO_DB_DATA_FOLDER=$(realpath $ARECO_CURRENT_TEST_FOLDER/../docker-volumes/oracle-xe/);
export COMPOSE_TLS_VERSION=TLSv1_2;

[[ -f $ARECO_HYBRIS_DIR/bin/platform/hybrisserver.sh ]] || (echo "Please configure ARECO_HYBRIS_DIR with the directory where SAP commerce is located." && exit 1);
[[ -d $ARECO_DB_DATA_FOLDER ]] || (echo "I can't found the shared directory with the database data" && exit 2);

cp -v $ARECO_CURRENT_TEST_FOLDER/../dbdriver/*.jar $ARECO_HYBRIS_DIR/bin/platform/lib/dbdriver/;

echo "Starting the oracle XE container"
docker-compose --file $ARECO_CURRENT_TEST_FOLDER/docker-compose.yml up -d;
$ARECO_CURRENT_TEST_FOLDER/../utils/wait-for-it.sh --host=127.0.0.1 --port=9500 --timeout=600 -- echo "Waiting for the oracle database to be ready";

timeout() {
   sleep 6
   kill -SIGUSR1 $1
}

watch_for_database() {
   docker-compose --file $ARECO_CURRENT_TEST_FOLDER/docker-compose.yml logs --follow areco-database | grep 'DATABASE IS READY TO USE'
}

trap 'echo "Oracle XE did not start"; exit 3' SIGUSR1
timeout $BASHPID &
watch_for_database
echo "Oracle XE is up";

echo "Configuring database connection and other properties";
export HYBRIS_OPT_CONFIG_DIR=$ARECO_CURRENT_TEST_FOLDER;

cd ../..;
. ./setantenv.sh;
echo "START TEST";

echo "Dropping and recreating the user and cleaning data folder"
rm -rf $ARECO_HYBRIS_DIR/data/*;

echo "Run all the tests on master tenant"
ant clean all yunitinit qa;

echo "TEST SUCCESS"
