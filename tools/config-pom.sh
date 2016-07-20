#!/bin/bash

TOOL_DIR=$(cd "$(dirname "$0")"; pwd)
#cd $TOOL_DIR
#cd ..
#WORKSPACE_DIR=$(pwd)
WORKSPACE_DIR=$(dirname "$TOOL_DIR")
echo WORKSPACE_DIR=$WORKSPACE_DIR

POM_FILE=
TARGET_VERSION=

while getopts "d:v:" opt; do
    case "$opt" in
    \?)
        printf "SYNOPSIS\n"
        printf "    reflash_pom_version [OPTIONS]\n"
        printf "DESCRIPTION\n"
        printf "    The pom.xml is copied to remote repository without variable translation when mvn deploy. So before deply, it is need to replace the version to a real version.\n"
        printf "OPTIONS\n"
        printf "    ?\n"
        printf "        Print a usage message briefly summarizing these command-line options, then exit.\n"
        printf "    -v <version>\n"
        printf "        Specify the real target version to replace. The version in pom.xml will be replaced to this value.\n"
        printf "\n"
        exit 1
        ;;
    v)  TARGET_VERSION=$OPTARG
        #printf "[INFO] b $opt $OPTARG   $OPTIND\n"
        ;;
    esac
done

if [ "$TARGET_VERSION" =  "" ]; then
    printf "[ERROR] The target version not found in command  \'$0 $*\', please use -h option to check the usage.\n"
    exit 2
fi
printf "[INFO] Parameter is set to command, WORKSPACE_DIR=$WORKSPACE_DIR, TARGET_VERSION=$TARGET_VERSION\n"


# check the setting in .m2, if it is not existed, it can not found the platform jar version.
prop_template_file=${WORKSPACE_DIR}/pom.properties.template
if [ ! -f $prop_template_file ]; then
    printf "[ERROR] The property template file not found in system, path=$prop_template_file.\n"
    exit 3
else
    printf "[INFO] property template file is found, path=$prop_template_file.\n"
fi

property_file=${WORKSPACE_DIR}/pom.properties
if [ -f $property_file ]; then
    rm -rf $property_file
fi
cp $prop_template_file $property_file

sed -i "s/\(ehapp\.version=\).*/\1$TARGET_VERSION/g" $property_file
printf "[INFO] Replace ehapp.version to target version in pom.properties, path=$property_file\n"

"${JAVA_HOME}/bin/java" -jar ${WORKSPACE_DIR}/tools/pom-sync-1.0.jar -R -e ${property_file} ${WORKSPACE_DIR}/
