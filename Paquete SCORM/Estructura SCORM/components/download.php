<?php
$FILES_PATH = "content";

$file = $_GET['file'];
if(file_exists(FILES_PATH . "/$file")) {
	$data = fopen(FILES_PATH . "/$file", "r");
	$size = filesize(FILES_PATH . "/$file");
	$type= filetype(FILES_PATH . "/$file");
	$file_content = fread($data,$size);
	header("Content-type: $type");
	header("Content-length: $size");
	header("Content-Disposition: attachment; filename=$file");
	header("Content-Description: PHP Generated Data");
	echo $file_content;
} 
else {
	echo "<script languaje='javascript'>
	alert('This file was not found. Maybe was deleted or moved manually');
	</script>";
}