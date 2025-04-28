#!/bin/bash
SCRIPT_DIR=$(dirname "${BASH_SOURCE[0]}")

echo SCRIPT_DIR:$SCRIPT_DIR
source $SCRIPT_DIR/common.sh
SCRIPT_DIR=$($SCRIPT_DIR/abspath.sh $SCRIPT_DIR)
echo SCRIPT_DIR:$SCRIPT_DIR



TABLE_default="MY_TABLE"
echo -n "ENTER TABLE [$TABLE_default]:"
read TABLE
if [ -z $TABLE ] ; then
    TABLE=$TABLE_default
fi


JIRA_STORY_default="ADSCOSMOS-XXXX"
echo -n "ENTER JIRA STORY [$JIRA_STORY_default]:"
read JIRA_STORY
if [ -z $JIRA_STORY ] ; then
    JIRA_STORY=$JIRA_STORY_default
fi



PLACEHOLDERS_MAP=(
        "%__JIRA_STORY_PLACEHOLDER__%:$JIRA_STORY"
        "%__TABLE_PLACEHOLDER__%:$TABLE"
         "%__USER_PLACEHOLDER__%:$(whoami)"

)


function getDestinationFromTemplate(){
    local input="$1"
    local destination="$input"
    for PLACEHOLDERS_ENTRY in "${PLACEHOLDERS_MAP[@]}" ; do
        PLACEHOLDER=${PLACEHOLDERS_ENTRY%%:*}
        REPLACEMENT=${PLACEHOLDERS_ENTRY#*:}
        destination=$(echo $destination | sed "s:$PLACEHOLDER:$REPLACEMENT:")
    done
    echo $destination
}


function applyTemplates(){
    local input=$1
    __INFO "Processing template file: $input"
    for PLACEHOLDERS_ENTRY in "${PLACEHOLDERS_MAP[@]}" ; do
        echo -n "."
        PLACEHOLDER=${PLACEHOLDERS_ENTRY%%:*}
        REPLACEMENT=${PLACEHOLDERS_ENTRY#*:}
        tmpFile=/tmp/$(date +%s).tmp

        sed "s:$PLACEHOLDER:$REPLACEMENT:g" $input >  $tmpFile
        cp $tmpFile $input
        rm -rf $tmpFile
    done
    echo ""
}





TMP_DESTINATION_DIR=/tmp/$(date +%s)
mkdir -p $TMP_DESTINATION_DIR

__INFO "Creating Temporary Project Structure under : $TMP_DESTINATION_DIR"
process_templates_in_folder(){
    local TEMPLATES_PARENT_FOLDER=$1
    cd $TEMPLATES_PARENT_FOLDER
    templates=$(find  . -type f)
    cd -

    for template in $templates ; do
    echo -----------------------------------------
    echo -----------------------------------------
    echo -----------------------------------------
        __INFO "Copying template file: $template"
        file=$(getDestinationFromTemplate "$template")
        __INFO $file
        file=$TMP_DESTINATION_DIR/$file

        #create the directory
        mkdir -p $(dirname $file)

        cp -R $TEMPLATES_PARENT_FOLDER/$template $file
    #    set -x
        absoluteFile=$($SCRIPT_DIR/abspath.sh $file)
        applyTemplates $absoluteFile
    #    set +x
    done


}





__INFO "PROCESSING TEMPLATES  PROJECT"
process_templates_in_folder $SCRIPT_DIR/db-templates

project_DIR=$SCRIPT_DIR/

__INFO "Copying generated Temporary Project into final destination"
set -x
cp -R $TMP_DESTINATION_DIR/ $project_DIR/../
set +x



#update changelog file


CHANGESET_CONTENT=$(cat $SCRIPT_DIR/../src/main/changelog.xml | grep -v '</databaseChangeLog>' )
echo "$CHANGESET_CONTENT" > $SCRIPT_DIR/../src/main/changelog.xml
cat $SCRIPT_DIR/../src/main/changelog-part.xml >> $SCRIPT_DIR/../src/main/changelog.xml
echo  '</databaseChangeLog>' >> $SCRIPT_DIR/../src/main/changelog.xml
rm -rf "$SCRIPT_DIR/../src/main/changelog-part.xml"


