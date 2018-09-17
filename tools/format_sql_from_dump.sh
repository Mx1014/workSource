#!/bin/bash

TARGET_FILE=
while getopts "f:" opt; do
    case "$opt" in
    \?)
        printf "SYNOPSIS\n"
        printf "    format_sql_from_dump [OPTIONS]\n"
        printf "DESCRIPTION\n"
        printf "    The pom.xml is copied to remote repository without variable translation when mvn deploy. So before deply, it is need to replace the version to a real version.\n"
        printf "OPTIONS\n"
        printf "    ?\n"
        printf "        Print a usage message briefly summarizing these command-line options, then exit.\n"
        printf "    -f <file to format>\n"
        printf "        Specify the file to be formated.\n"
        exit 1
        ;;
    f)  TARGET_FILE=$OPTARG
        #printf "[INFO] d $opt $OPTARG   $OPTIND\n"
        ;;
    esac
done

#if [ "$TARGET_FILE" =  "" ]; then
#    printf "[ERROR] The target file not found in command \'$0 $*\', please use -h option to check the usage.\n"
#    exit 2
#fi

sed -i 's#^\/\*\![0-9]\{1,\}.*\*\/;$##g' $TARGET_FILE
sed -i 's# AUTO_INCREMENT=[0-9]\{1,\}##g' $TARGET_FILE 
sed -i "s/ DEFAULT NULL//g" $TARGET_FILE
sed -i "s/bigint(20)/BIGINT/g" $TARGET_FILE
sed -i "s/bigint(11)/BIGINT/g" $TARGET_FILE
sed -i "s/bigint(22)/BIGINT/g" $TARGET_FILE
sed -i "s/int(11)/INTEGER/g" $TARGET_FILE
sed -i "s/tinyint(4)/TINYINT/g" $TARGET_FILE
sed -i "s/DEFAULT '0'/DEFAULT 0/g" $TARGET_FILE
sed -i "s/varchar/VARCHAR/g" $TARGET_FILE
sed -i "s/\` datetime /\` DATETIME /g" $TARGET_FILE
sed -i "s/\` datetime,/\` DATETIME,/g" $TARGET_FILE
sed -i "s/double/DOUBLE/g" $TARGET_FILE
sed -i "s/decimal/DECIMAL/g" $TARGET_FILE
sed -i "s/longtext/LONGTEXT/g" $TARGET_FILE
sed -i "s/mediumtext/MEDIUMTEXT/g" $TARGET_FILE
sed -i "s/\` text,/\` TEXT,/g" $TARGET_FILE
sed -i "s/\` text COMMENT/\` TEXT COMMENT/g" $TARGET_FILE
sed -i "s/\` text NOT NULL/\` TEXT NOT NULL/g" $TARGET_FILE
sed -i "s/\` blob/\` BLOB/g" $TARGET_FILE
sed -i "s/\` date COMMENT/\` DATE COMMENT/g" $TARGET_FILE
sed -i "s/\` date,/\` DATE,/g" $TARGET_FILE
sed -i "s/\` time,/\` TIME,/g" $TARGET_FILE
sed -i "s/\` time COMMENT/\` TIME COMMENT/g" $TARGET_FILE
sed -i "s/DEFAULT '0'/DEFAULT 0/g" $TARGET_FILE
sed -i "s/DEFAULT '1'/DEFAULT 1/g" $TARGET_FILE
sed -i "s/DEFAULT '2'/DEFAULT 2/g" $TARGET_FILE
sed -i "s/DEFAULT '7'/DEFAULT 7/g" $TARGET_FILE
