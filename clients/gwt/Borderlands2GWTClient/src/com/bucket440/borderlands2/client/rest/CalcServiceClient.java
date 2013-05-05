package com.bucket440.borderlands2.client.rest;

import com.bucket440.borderlands2.client.gun.CalculationResults;
import com.bucket440.borderlands2.client.gun.Gun;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class CalcServiceClient {
	private static String URL = "http://www.bucket440.com/borderlands2/dps.php";
	private RequestBuilder requestBuilder;
	private OnSuccess onSuccess;
	private OnError onError;
	
	public CalcServiceClient(OnSuccess onSuccess, OnError onError){
		this.onSuccess = onSuccess;
		this.onError = onError;
	}
	
	public void getCalculationsForGun(Gun input){
		requestBuilder = new RequestBuilder(RequestBuilder.GET, URL + buildQueryStringFromGun(input));
		try {
			requestBuilder.sendRequest(null, buildCallbackForGun(input));
		} catch (RequestException e) {
			onError.onError(input, "Request Failed: " + e.getMessage());
		}
	}
	
	private String buildQueryStringFromGun(Gun input){
		StringBuilder result = new StringBuilder();
		
		result.append("?");
		result.append(getURLValue("accuracy", input.accuracy));
		result.append(getURLValue("damage", input.damage));
		result.append(getURLValue("damageMultiplier", input.damageMultiplier));
		result.append(getURLValue("fireRate", input.fireRate));
		result.append(getURLValue("reloadTime", input.reloadTime));
		result.append(getURLValue("magazineSize", input.magazineSize));
		result.append(getURLValue("roundsPerShot", input.roundsPerShot));
		result.append(getURLValue("elementalDPS", input.elementalDPS));
		result.append(getURLValue("elementalChance", input.elementalChance));
		
		return result.toString();
	}
	
	private String getURLValue(String key, Object value){
		if(value != null){
			return key+"="+value.toString()+"&";
		} else {
			return "";
		}
	}
	
	private RequestCallback buildCallbackForGun(Gun gun){
		final Gun myGun = gun;
		
		return new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				if(response.getStatusCode() == 200){
					JSONObject resultJson = JSONParser.parseStrict(response.getText()).isObject();
					myGun.calculationResults = getCalculationResultsFromResponse(resultJson);
					fixGunInputsFromResponse(myGun, resultJson);
					onSuccess.onSuccess(myGun);
				} else {
					JSONObject result = JSONParser.parseStrict(response.getText()).isObject();
					
					String errorMessage = "Server returned response: " + response.getStatusCode() + " : " + response.getStatusText();
					
					if(result != null && result.containsKey("message")){
						errorMessage = result.get("message").isString().stringValue();
					}
					onError.onError(myGun, errorMessage);
				}
			}
			
			@Override
			public void onError(Request request, Throwable exception) {
				onError.onError(myGun, "Unable to complete request: " + exception.getMessage());
			}
		};
	}
	
	private static Gun fixGunInputsFromResponse(Gun gun, JSONObject responseObject){
		JSONObject inputsJson = responseObject.get("inputs").isObject();
		gun.damage = inputsJson.get("damage").isNumber().doubleValue();
		gun.damageMultiplier =  convertDoubleToInteger(inputsJson.get("damageMultiplier").isNumber().doubleValue());
		gun.accuracy =  inputsJson.get("accuracy").isNumber().doubleValue();
		gun.fireRate =  inputsJson.get("fireRate").isNumber().doubleValue();
		gun.reloadTime =  inputsJson.get("reloadTime").isNumber().doubleValue();
		gun.magazineSize =  convertDoubleToInteger(inputsJson.get("magazineSize").isNumber().doubleValue());
		gun.roundsPerShot =  convertDoubleToInteger(inputsJson.get("roundsPerShot").isNumber().doubleValue());
		gun.elementalDPS =  inputsJson.get("elementalDPS").isNumber().doubleValue();
		gun.elementalChance =  inputsJson.get("elementalChance").isNumber().doubleValue();
		return gun;
	}
	
	private static Integer convertDoubleToInteger(Double input){
		Long longValue = Math.round(input);
		if(longValue > Integer.MAX_VALUE){
			return Integer.MAX_VALUE;
		}
		
		if(longValue < Integer.MIN_VALUE){
			return Integer.MIN_VALUE;
		}
		return Integer.valueOf(String.valueOf(input));
	}
	
	private static CalculationResults getCalculationResultsFromResponse(JSONObject responseObject){
		JSONObject calcResultsJson = responseObject.get("results").isObject();
		CalculationResults calcResults = new CalculationResults();
		calcResults.bottomLine = getNumberFromObject("bottomLine", responseObject);
		calcResults.baseDPS = getNumberFromObject("baseDPS", calcResultsJson);
		calcResults.withAccuracy = getNumberFromObject("withAccuracy", calcResultsJson);
		calcResults.withReload = getNumberFromObject("withReload", calcResultsJson);
		calcResults.withElemental = getNumberFromObject("withElemental", calcResultsJson);
		calcResults.withReloadAndAccuracy = getNumberFromObject("withReloadAndAccuracy", calcResultsJson);
		calcResults.withReloadAndElemental = getNumberFromObject("withReloadAndElemental", calcResultsJson);
		calcResults.withElementalAndAccuracy = getNumberFromObject("withElementalAndAccuracy", calcResultsJson);
		calcResults.withReloadAndElementalAndAccuracy = getNumberFromObject("withReloadAndElementalAndAccuracy", calcResultsJson);
		return calcResults;
	}
	
	public static Double getNumberFromObject(String key, JSONObject object){
		if(object.containsKey(key)){
			return object.get(key).isNumber().doubleValue();
		}
		return 0D;
	}
	
	public interface OnSuccess{
		public void onSuccess(Gun result);
	}
	
	public interface OnError{
		public void onError(Gun input, String errorMessage);
	}
}
