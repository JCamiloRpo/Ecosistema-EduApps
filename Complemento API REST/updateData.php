<?php

/**
 * Automatic Api Rest
 *
 * @package  Automatic Api Rest
 * @author   Alejandro Esquiva Rodríguez [@alex_esquiva] <alejandro@geekytheory.com>
 * @license  Apache License, Version 2.0
 * @link     https://github.com/GeekyTheory/Automatic-API-REST
 */

require_once 'inc/functions.php';
$blacklist = new BlackList();
$objectTools = new Tools();

if(isset($_POST['t'])) $table = $_POST['t'];
if(isset($_POST['c'])) $fields = $_POST['c'];
if(isset($_POST['v'])) $values = $_POST['v'];
if(isset($_POST['w'])) $where = $_POST['w'];

if(!isset($_POST['c'])) die ($objectTools->JSONError(301));
if(!isset($_POST['v'])) die ($objectTools->JSONError(301));
if(!isset($_POST['t'])) die ($objectTools->JSONError(301));
if(!isset($_POST['w'])) die ($objectTools->JSONError(301));

$values_split = explode(",",$values);
$fields_full = explode(",", $fields);
$where = str_replace(":","=",$where);
if(count($fields_full) != count($values_split)) die($objectTools->JSONError(301));



/**
 * check the blacklist
 */
if(isset($fields)){
    $exist = $blacklist->existItem("G",$_POST["t"],"*"); 
    if(!$exist){
        $exist = $blacklist->existItem("G",$table,$fields);
    }
}else{
    $exist = $blacklist->existItem("G",$table,"*"); 
}

/**
 * If the query is not allowed -> die
 */
if($exist){
    die($objectTools->JSONError(401));
}

/**
 * Create the sql sentence with get parameters
 */

if(isset($fields)){
    
    //get the fields which are not in the black list
    
    $fields_allowed = "";
    for($i=0;$i<count($fields);$i++){
        if($blacklist->existItem("G", $_POST["t"], $fields[$i])){
            die($objectTools->JSONError(401));
        }
    }
    
    /**
     * Update Sentence
     */
    
    
    $values = "";
    for($i=0;$i<count($values_split);$i++){
        if($values == ""){
            if(is_numeric($values_split[$i])){
                $values = $values_split[$i]; 
            }else if(is_string($values_split[$i])){
                $values = "'".$values_split[$i]."'";
            }
            
        }else{
            if(is_numeric($values_split[$i])){
                $values = $values.",".$values_split[$i]; 
            }else if(is_string($values_split[$i])){
                $values = $values.",'".$values_split[$i]."'";
            }
        }
    }
    
    $sql = "UPDATE $table SET $fields=$values WHERE $where";
    
}

$function = "";

if(isset($_POST["f"])){
    
    $function = $_POST["f"];
}else{
    $function = "json";
}


if($function=="json"){
    header('Content-Type: application/json');
        
    $result = $objectTools->setDataBySQL($sql);

    if(!$result) die ($objectTools->JSONError (303));
    
    // QUERY Variables
    $columns=$fields;

    $objectTools->getData($table,$columns,$order,$sort,$limit,$where,$format,$option);
    
}else if($function=="xml"){

}else{
    die($objectTools->JSONError(301));
}