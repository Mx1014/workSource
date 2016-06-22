#!/bin/sh
# setup-ehcore-db.sh -- deploys database configuration.
# set -x

DEVELOPER=0
PASSWORD=

while getopts "h?p:d" opt; do
    case "$opt" in
    h|\?)
    	printf "Usage:\n\tsetup-ehcore-db.sh [-d] [-p <DB root password>]\n"
        exit 0
        ;;
    p)  PASSWORD=$OPTARG
        ;;
    d)  DEVELOPER=1
        ;;
    esac
done

if [ ! -f ehcore-database.sql ]; then
    printf "Error: Unable to find ehcore-database.sql\n"
    exit 2
fi

# if [ ! -f ehcore-platform-schema.sql ]; then
#    printf "Error: Unable to find ehcore-platform-schema.sql\n"
#    exit 3
# fi

handle_error() {
    mysqlout=$?
    if [ $mysqlout -eq 1 ]; then
        printf "Please enter root password for MySQL.\n" 
        mysql --user=root --password < $1
        if [ $? -ne 0 ]; then
            printf "Error: Cannot execute $1\n"
            exit 4
        fi
    elif [ $mysqlout -eq 127 ]; then
        printf "Error: Cannot execute $1 - mysql command not found.\n"
        exit 5
    elif [ $mysqlout -ne 0 ]; then
        printf "Error: Cannot execute $1\n"
        exit 6
    fi
}

echo "Creating Database ehcore."
mysql --user=root --password=$PASSWORD < ehcore-database.sql > /dev/null 2>/dev/null
handle_error ehcore-database.sql

# mysql --user=ehcore --password=ehcore ehcore < ehcore-platform-schema.sql
# if [ $? -ne 0 ]; then
#  printf "Error: Cannot execute ehcore-platform-schema.sql\n"
#  exit 7
# fi
  
mysql --user=ehcore --password=ehcore ehcore < ehcore-server-schema.sql
if [ $? -ne 0 ]; then
  printf "Error: Cannot execute ehcore-server-schema.sql\n"
  exit 7
fi

# mysql --user=ehcore --password=ehcore ehcore < server-schema-3.0.1-3.1.0.sql
# if [ $? -ne 0 ]; then
#   printf "Error: Cannot execute ehcore-server-schema-3.0.1-3.1.0.sql\n"
#   exit 7
# fi

# mysql --user=ehcore --password=ehcore ehcore < ehcore-park-schema.sql
# if [ $? -ne 0 ]; then
#  printf "Error: Cannot execute ehcore-park-schema.sql\n"
#  exit 7
# fi

# mysql --user=ehcore --password=ehcore ehcore < ehcore-conf-schema.sql
# if [ $? -ne 0 ]; then
#  printf "Error: Cannot execute ehcore-conf-schema.sql\n"
#  exit 7
# fi
  
echo "Initializing Database ehcore."
mysql --user=ehcore --password=ehcore < ehcore-system-init.sql
if [ $? -ne 0 ]; then
  printf "Error: Cannot execute ehcore-system-init.sql\n"
  exit 7
fi

mysql --user=ehcore --password=ehcore < ehcore-init-data.sql
if [ $? -ne 0 ]; then
  printf "Error: Cannot execute ehcore-init-data.sql\n"
  exit 7
fi

if [ $DEVELOPER == "1" ]; then
	echo "Initializing developer setup."
	mysql --user=ehcore --password=ehcore ehcore < ehcore-developer-init.sql
	if [ $? -ne 0 ]; then
	  printf "Error: Cannot execute ehcore-developer-init.sql\n"
	  exit 7
	fi
fi
	
