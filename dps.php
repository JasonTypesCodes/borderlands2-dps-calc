<?php

// Constant for required input.
$REQUIRED = "REQUIRED";

// Input name => default value.
$EXPECTED_INPUTS = array(
	"accuracy" => 100,
	"damage" => $REQUIRED,
	"fireRate" => $REQUIRED,
	"reloadTime" => 0,
	"magazineSize" => 1,
	"roundsPerShot" => 1,
	"elementalDPS" => 0,
	"elementalChance" => 0,
	"ignoreReloadTime" => false,
	"ignoreAccuracy" => false,
	"ignoreElemental" => false,
);

$RETURN_CODES = array(
  "200" => "OK",
  "400" => "Bad Request",
  "405" => "Method Not Allowed",
);

if($_SERVER['REQUEST_METHOD'] !== "GET"){
  doJSONReturn("405", array("message" => "Method " . $_SERVER['REQUEST_METHOD'] . " not allowed."));
} else if(!requiredInputSupplied()){
  doJSONReturn("400", array(
    "message" => "One or more required inputs not supplied",
    "expected_inputs" => $EXPECTED_INPUTS,
  ));
} else {
  $a = getCleanInputs();

  $result = calculateBaseDPS($a["damage"], $a["fireRate"]);
  
  if(!$a["ignoreReloadTime"]){
    $result = calculateTrueDPS(
      $result,
      calculateSecondsUntilReload($a["magazineSize"], $a["fireRate"]),
      $a["reloadTime"]
    );
  }

  if(!$a["ignoreElemental"]){
    $result = $result + calculateLikelyElementalDPS(
      $a["elementalDPS"], 
      decimalPercent($a["elementalChance"]), 
      $a["fireRate"]
    );
  }

  if(!$a["ignoreAccuracy"]){
    $result = $result * decimalPercent($a["accuracy"]);
  }

  doJSONReturn("200", array(
    "result" => $result,
    "inputs" => $a
  ));
}

function decimalPercent($input){
  if($input > 1){
    return $input / 100;
  }
  return $input;
}

function calculateLikelyElementalDPS($elementalDPS, $elementalChance, $fireRate){
  //I know this isn't technically correct.  You have the same chance each bullet, not better
  //chances the more bullets you fire, but this was the best I could come up with.
  $elementalChancePerSecond = $fireRate * $elementalChance;
  if($elementalChancePerSecond >= 1){
    return $elementalDPS;
  } else {
    //Again... this isn't quite right either.  I'm basically just trying to say that in a second of shooting
    //you may not get the elemental DPS started, so over time it should even out to this... or something.
    return $elementalDPS * $elementalChancePerSecond;
  }


}

function calculateTrueDPS($baseDPS, $secondsUntilReload, $reloadTime){
  // Damage per clip divided by total clip time.
  return ($baseDPS * $secondsUntilReload) / ($secondsUntilReload + $reloadTime); 
}

function calculateBaseDPS($damage, $fireRate){
  return $damage * $fireRate;
}

function calculateSecondsUntilReload($magazineSize, $fireRate){
  if($fireRate == 0 || $magazineSize == 0){
    doJSONReturn("400", array("message" => "fireRate or magazineSize cannot be 0."));
  } else {
    return $magazineSize / $fireRate;
  }
}

function calculateTrueMagazineSize($magazineSize, $roundsPerShot){
  if($roundsPerShot == 0){
    doJSONReturn("400", array("message" => "roundsPerShot cannot be 0."));
  } else {
    $result = $magazineSize / $roundsPerShot;
    //  I seem to remember still getting the power of roundsPerShot even if there are less than that available
    //  in the clip...
    if($magazineSize % $roundsPerShot > 0){ 
      $result = $result + 1;
    }
    return $result;
  }
}


function getCleanInputs(){
  global $EXPECTED_INPUTS;
  $result = array();
  foreach($EXPECTED_INPUTS as $key => $defaultValue){
    $cleanValue = $defaultValue;
    if(isset($_GET[$key])){
      if(gettype($defaultValue) == "boolean"){
        $cleanValue = asBoolean($_GET[$key]);  
      } else {
        $cleanValue = doubleval($_GET[$key]);
      }
    }
    $result[$key] = $cleanValue;
  }
  return $result;
}

function asBoolean($input){
  if(gettype($input) == "integer"){
    if($input == 1){
      return true;
    } else {
      return false;
    }
  } else if(gettype($input) == "string"){
    if(strtoupper($input) == "TRUE"){
      return true;
    } else {
      return false;
    }
  } else if(gettype($input) == "boolean"){
    return $input;
  } else {
    return false;
  }
}

function doJSONReturn($code, $content){
  global $RETURN_CODES;

  header("HTTP/1.1 " . $code . " " . $RETURN_CODES[$code]);
  header("Content-type: application/json");
  echo json_encode($content);
  exit(0);
}

function requiredInputSupplied(){
  global $REQUIRED, $EXPECTED_INPUTS;

  foreach($EXPECTED_INPUTS as $key => $value){
    if($value === $REQUIRED){
      if(!isset($_GET[$key]) || $_GET[$key] == ""){
        return false;
      }    
    }
  }
  
  return true;    
}

?>