package com.tools.json;

import java.util.List;

import com.google.gson.JsonElement;
import com.tools.util.Log;


/**
 * 
 * 
{
    "GJsonData": [
        {
            "ID": 678,
            "Title": "vvvv    "
        },
        {
            "ID": 677,
            "Title": "wwwwww"
        },
        {
            "ID": 675,
            "Title": "dddd"
        }
    ],
    "GJsonSub": {
        "a": "a      ",
        "b": 1,
        "c": true
    },
    "Msg": "",
    "Count": 0,
    "tStringEmpty": "",
    "tStringText": "text.aaaa.bbbb",
    "tStringNull": null,
    "tBooleanTrue": true,
    "tBooleanFalse": false,
    "tInteger": 100,
    "t_Integer": -200,
    "tDouble": 55.26584596585452,
    "t_Double": -66.26584596585452
}

		GJsonClient.isKeyExists();
		GJsonClient.isObject();
		GJsonClient.isArray();
		GJsonClient.isNull();
		GJsonClient.isPrimitive();
		GJsonClient.isString();
		GJsonClient.isNumber();
		GJsonClient.isBoolean();
		GJsonClient.isDouble();
		GJsonClient.isLong();
		GJsonClient.isJson();

		GJsonClient.toJsonString_Object();
		GJsonClient.toJsonString_JsonElement();

		GJsonClient.init();
		GJsonClient.get();

 * @author LMC
 *
 */
public class GJsonClient {

	private static final String TAG = GJsonClient.class.getSimpleName();

	private static String jsonObjectString = "{    \"GJsonData\": [        {            \"ID\": 678,            \"Title\": \"vvvv    \"        },        {            \"ID\": 677,            \"Title\": \"wwwwww\"        },        {            \"ID\": 675,            \"Title\": \"dddd\"        }    ],    \"GJsonSub\": {        \"a\": \"a      \",        \"b\": 1,        \"c\": true    },    \"Msg\": \"\",    \"Count\": 0,    \"tStringEmpty\": \"\",    \"tStringText\": \"text.aaaa.bbbb\",    \"tStringNull\": null,    \"tBooleanTrue\": true,    \"tBooleanFalse\": false,    \"tInteger\": 100,    \"t_Integer\": -200,    \"tDouble\": 55.26584596585452,    \"t_Double\": -66.26584596585452}";

	private static String jsonArrayString = "[\"a\",\"b\",\"c\"]";

	public static void main() {


	}

	public static void init() {

		// {...}
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"init():isObject:"+gJson.isObject("GJsonData"));
		Log.e(TAG,"init():isObject:"+gJson.isObject("GJsonSub"));
		Log.e(TAG,"init():isJson:"+gJson.isJson());

		// [{...},[...]]
		String aString = jsonArrayString;
		gJson = new GJson(aString);
		Log.e(TAG,"init():isJson():"+gJson.isJson());

		gJson = new GJson(aString);
		Log.e(TAG,"init():toJsonString():"+gJson.toJsonString());
		Log.e(TAG,"init():isArray():"+gJson.isArray());
		Log.e(TAG,"init():isObject():"+gJson.isObject());

		gJson = new GJson(jsonObjectString);
		Log.e(TAG,"22222toJsonString():"+GJson.toJsonString( gJson.getJsonObject("GJsonSub") ));
		Log.e(TAG,"22222toJsonString():"+GJson.toJsonString( gJson.getJsonObject("GJsonSub") ));
		Log.e(TAG,"22222toJsonString():"+GJson.toJsonString( gJson.getJsonObject("GJsonData") ));
		Log.e(TAG,"22222toJsonString():"+GJson.toJsonString( gJson.getJsonArray("GJsonData") ));
		Log.e(TAG,"22222toJsonString():"+GJson.toJsonString( gJson.getJsonArray("GJsonData") ));

		gJson = new GJson("[    true,    false]");
		Log.e(TAG,"init(1):isObject():"+gJson.isObject());
		Log.e(TAG,"init(2):isArray():"+gJson.isArray());
		Log.e(TAG,"init(3):isJson():"+gJson.isJson());

		gJson = new GJson("[34,45,56]");
		Log.e(TAG,"init(1):isObject():"+gJson.isObject());
		Log.e(TAG,"init(2):isArray():"+gJson.isArray());
		Log.e(TAG,"init(3):isJson():"+gJson.isJson());

		gJson = new GJson("[\"a\",\"b\",\"c\"]");
		Log.e(TAG,"init(1):isObject():"+gJson.isObject());
		Log.e(TAG,"init(2):isArray():"+gJson.isArray());
		Log.e(TAG,"init(3):isJson():"+gJson.isJson());

		gJson = new GJson("[\"a\":23,\"b\",45]");
		Log.e(TAG,"init(1):isObject():"+gJson.isObject());
		Log.e(TAG,"init(2):isArray():"+gJson.isArray());
		Log.e(TAG,"init(3):isJson():"+gJson.isJson());
	}

	public static void get() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"getString:"+gJson.getString("tStringText"));
		Log.e(TAG,"getBoolean:"+gJson.getBoolean("tBooleanTrue"));
		Log.e(TAG,"getDouble:"+gJson.getDouble("t_Double"));
		Log.e(TAG,"getLong:"+gJson.getLong("tInteger"));

		Log.e(TAG,"getLong222:"+gJson.getLong("GJsonSub"));
	}

	public static void isKeyExists() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isKeyExists:"+gJson.isKeyExists("GJsonData"));
		Log.e(TAG,"isKeyExists:"+gJson.isKeyExists("DDDDDDD"));
	}

	public static void isObject() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isObject:"+gJson.isObject("GJsonData"));
		Log.e(TAG,"isObject:"+gJson.isObject("GJsonSub")); // true
		Log.e(TAG,"isObject:"+gJson.isObject("Msg"));
		Log.e(TAG,"isObject:"+gJson.isObject("Count"));
		Log.e(TAG,"isObject:"+gJson.isObject("tStringNull"));
		Log.e(TAG,"isObject:"+gJson.isObject("tStringEmpty"));
		Log.e(TAG,"isObject:"+gJson.isObject("tStringText"));
		Log.e(TAG,"isObject:"+gJson.isObject("tStringNull"));
		Log.e(TAG,"isObject:"+gJson.isObject("tBooleanTrue"));
		Log.e(TAG,"isObject:"+gJson.isObject("tBooleanFalse"));
		Log.e(TAG,"isObject:"+gJson.isObject("tInteger"));
		Log.e(TAG,"isObject:"+gJson.isObject("t_Integer"));
		Log.e(TAG,"isObject:"+gJson.isObject("tDouble"));
		Log.e(TAG,"isObject:"+gJson.isObject("t_Double"));
	}

	public static void isArray() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isArray:"+gJson.isArray("GJsonData")); // true
		Log.e(TAG,"isArray:"+gJson.isArray("GJsonSub"));
		Log.e(TAG,"isArray:"+gJson.isArray("Msg"));
		Log.e(TAG,"isArray:"+gJson.isArray("Count"));
		Log.e(TAG,"isArray:"+gJson.isArray("tStringNull"));
		Log.e(TAG,"isArray:"+gJson.isArray("tStringEmpty"));
		Log.e(TAG,"isArray:"+gJson.isArray("tStringText"));
		Log.e(TAG,"isArray:"+gJson.isArray("tStringNull"));
		Log.e(TAG,"isArray:"+gJson.isArray("tBooleanTrue"));
		Log.e(TAG,"isArray:"+gJson.isArray("tBooleanFalse"));
		Log.e(TAG,"isArray:"+gJson.isArray("tInteger"));
		Log.e(TAG,"isArray:"+gJson.isArray("t_Integer"));
		Log.e(TAG,"isArray:"+gJson.isArray("tDouble"));
		Log.e(TAG,"isArray:"+gJson.isArray("t_Double"));
	}

	public static void isNull() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isNull:"+gJson.isNull("GJsonData"));
		Log.e(TAG,"isNull:"+gJson.isNull("GJsonSub"));
		Log.e(TAG,"isNull:"+gJson.isNull("Msg"));
		Log.e(TAG,"isNull:"+gJson.isNull("Count"));
		Log.e(TAG,"isNull:"+gJson.isNull("tStringNull")); // true
		Log.e(TAG,"isNull:"+gJson.isNull("tStringEmpty"));
		Log.e(TAG,"isNull:"+gJson.isNull("tStringText"));
		Log.e(TAG,"isNull:"+gJson.isNull("tStringNull")); // true
		Log.e(TAG,"isNull:"+gJson.isNull("tBooleanTrue"));
		Log.e(TAG,"isNull:"+gJson.isNull("tBooleanFalse"));
		Log.e(TAG,"isNull:"+gJson.isNull("tInteger"));
		Log.e(TAG,"isNull:"+gJson.isNull("t_Integer"));
		Log.e(TAG,"isNull:"+gJson.isNull("tDouble"));
		Log.e(TAG,"isNull:"+gJson.isNull("t_Double"));
	}

	public static void isPrimitive() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("GJsonData"));
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("GJsonSub"));
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("Msg")); // true
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("Count")); // true
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("tStringNull"));
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("tStringEmpty")); // true
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("tStringText")); // true
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("tStringNull"));
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("tBooleanTrue")); // true
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("tBooleanFalse")); // true
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("tInteger")); // true
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("t_Integer")); // true
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("tDouble")); // true
		Log.e(TAG,"isPrimitive:"+gJson.isPrimitive("t_Double")); // true
	}

	public static void isString() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isString:"+gJson.isString("GJsonData"));
		Log.e(TAG,"isString:"+gJson.isString("GJsonSub"));
		Log.e(TAG,"isString:"+gJson.isString("Msg")); // true
		Log.e(TAG,"isString:"+gJson.isString("Count"));
		Log.e(TAG,"isString:"+gJson.isString("tStringNull"));
		Log.e(TAG,"isString:"+gJson.isString("tStringEmpty")); // true
		Log.e(TAG,"isString:"+gJson.isString("tStringText")); // true
		Log.e(TAG,"isString:"+gJson.isString("tStringNull"));
		Log.e(TAG,"isString:"+gJson.isString("tBooleanTrue"));
		Log.e(TAG,"isString:"+gJson.isString("tBooleanFalse"));
		Log.e(TAG,"isString:"+gJson.isString("tInteger"));
		Log.e(TAG,"isString:"+gJson.isString("t_Integer"));
		Log.e(TAG,"isString:"+gJson.isString("tDouble"));
		Log.e(TAG,"isString:"+gJson.isString("t_Double"));
	}

	public static void isNumber() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isNumber:"+gJson.isNumber("GJsonData"));
		Log.e(TAG,"isNumber:"+gJson.isNumber("GJsonSub"));
		Log.e(TAG,"isNumber:"+gJson.isNumber("Msg"));
		Log.e(TAG,"isNumber:"+gJson.isNumber("Count")); // true
		Log.e(TAG,"isNumber:"+gJson.isNumber("tStringNull"));
		Log.e(TAG,"isNumber:"+gJson.isNumber("tStringEmpty"));
		Log.e(TAG,"isNumber:"+gJson.isNumber("tStringText"));
		Log.e(TAG,"isNumber:"+gJson.isNumber("tStringNull"));
		Log.e(TAG,"isNumber:"+gJson.isNumber("tBooleanTrue"));
		Log.e(TAG,"isNumber:"+gJson.isNumber("tBooleanFalse"));
		Log.e(TAG,"isNumber:"+gJson.isNumber("tInteger")); // true
		Log.e(TAG,"isNumber:"+gJson.isNumber("t_Integer")); // true
		Log.e(TAG,"isNumber:"+gJson.isNumber("tDouble")); // true
		Log.e(TAG,"isNumber:"+gJson.isNumber("t_Double")); // true
	}

	public static void isBoolean() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("GJsonData"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("GJsonSub"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("Msg"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("Count"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("tStringNull"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("tStringEmpty"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("tStringText"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("tStringNull"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("tBooleanTrue")); // true
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("tBooleanFalse")); // true
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("tInteger"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("t_Integer"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("tDouble"));
		Log.e(TAG,"isBoolean:"+gJson.isBoolean("t_Double"));
	}

	public static void isDouble() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isDouble:"+gJson.isDouble("GJsonData"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("GJsonSub"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("Msg"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("Count"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("tStringNull"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("tStringEmpty"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("tStringText"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("tStringNull"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("tBooleanTrue"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("tBooleanFalse"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("tInteger"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("t_Integer"));
		Log.e(TAG,"isDouble:"+gJson.isDouble("tDouble")); // true
		Log.e(TAG,"isDouble:"+gJson.isDouble("t_Double")); // true
	}

	public static void isLong() {
		GJson gJson = new GJson(jsonObjectString);
		Log.e(TAG,"isLong:"+gJson.isLong("GJsonData"));
		Log.e(TAG,"isLong:"+gJson.isLong("GJsonSub"));
		Log.e(TAG,"isLong:"+gJson.isLong("Msg"));
		Log.e(TAG,"isLong:"+gJson.isLong("Count")); // true
		Log.e(TAG,"isLong:"+gJson.isLong("tStringNull"));
		Log.e(TAG,"isLong:"+gJson.isLong("tStringEmpty"));
		Log.e(TAG,"isLong:"+gJson.isLong("tStringText"));
		Log.e(TAG,"isLong:"+gJson.isLong("tStringNull"));
		Log.e(TAG,"isLong:"+gJson.isLong("tBooleanTrue"));
		Log.e(TAG,"isLong:"+gJson.isLong("tBooleanFalse"));
		Log.e(TAG,"isLong:"+gJson.isLong("tInteger")); // true
		Log.e(TAG,"isLong:"+gJson.isLong("t_Integer")); // true
		Log.e(TAG,"isLong:"+gJson.isLong("tDouble"));
		Log.e(TAG,"isLong:"+gJson.isLong("t_Double"));
	}

	public static void isJson() {
		Log.e(TAG,"isJson:"+GJson.isJson(jsonObjectString));
		Log.e(TAG,"isJson:"+GJson.isJson("{dddd}"));
	}

	public static void toJsonString_Object() {
		GJsonSub a = new GJsonSub();
		a.setA("aaaa");
		a.setB(23);
		a.setC(true);
		Log.e(TAG,"toJsonString(Object):"+GJson.toJsonString(a));
	}

	public static void toJsonString_JsonElement() {
		GJson gjson = new GJson(jsonObjectString);
		JsonElement ellll = gjson.getJsonElement("tStringEmpty");
		Log.e(TAG,"toJsonString(JsonElement):"+GJson.toJsonString(ellll));

		ellll = gjson.getJsonElement("tStringText");
		Log.e(TAG,"toJsonString(JsonElement):"+GJson.toJsonString(ellll));

		ellll = gjson.getJsonElement("tStringNull");
		Log.e(TAG,"toJsonString(JsonElement):"+GJson.toJsonString(ellll));

		ellll = gjson.getJsonElement( "tBooleanFalse");
		Log.e(TAG,"toJsonString(JsonElement):"+GJson.toJsonString(ellll));

		ellll = gjson.getJsonElement("t_Integer");
		Log.e(TAG,"toJsonString(JsonElement):"+GJson.toJsonString(ellll));

		ellll = gjson.getJsonElement("tDouble");
		Log.e(TAG,"toJsonString(JsonElement):"+GJson.toJsonString(ellll));

		ellll = gjson.getJsonElement("GJsonData");
		Log.e(TAG,"toJsonString(JsonElement):"+GJson.toJsonString(ellll));

		ellll = gjson.getJsonElement("GJsonSub");
		Log.e(TAG,"toJsonString(JsonElement):"+GJson.toJsonString(ellll));
	}

	public static void parseObject3() {
		Log.i(TAG, "--- parseObject3() start ---");

		GJsonSub object = GJson.parseObject(jsonObjectString, "aaaa", GJsonSub.class);
		if (object != null) {
			Log.e(TAG, "object != null");
			object.print();
		}else{
			Log.e(TAG, "object == null");
		}

		object = GJson.parseObject(jsonObjectString, "tStringText", GJsonSub.class);
		if (object != null) {
			Log.e(TAG, "object != null");
			object.print();
		}else{
			Log.e(TAG, "object == null");
		}

		object = GJson.parseObject(jsonObjectString, "tBooleanTrue", GJsonSub.class);
		if (object != null) {
			Log.e(TAG, "object != null");
			object.print();
		}else{
			Log.e(TAG, "object == null");
		}

		object = GJson.parseObject(jsonObjectString, "tDouble", GJsonSub.class);
		if (object != null) {
			Log.e(TAG, "object != null");
			object.print();
		}else{
			Log.e(TAG, "object == null");
		}

		object = GJson.parseObject(jsonObjectString, "GJsonSub", GJsonSub.class);
		if (object != null) {
			Log.e(TAG, "object != null");
			object.print();
		}else{
			Log.e(TAG, "object == null");
		}

		object = GJson.parseObject(jsonObjectString, "GJsonData", GJsonSub.class);
		if (object != null) {
			Log.e(TAG, "object != null");
			object.print();
		}else{
			Log.e(TAG, "object == null");
		}

		Log.e(TAG, "--- GJsonData DDDDDD.....");

		GJsonData object2 = GJson.parseObject(jsonObjectString, "GJsonData", GJsonData.class);
		if (object2 != null) {
			Log.e(TAG, "object != null");
			object2.print();
		}else{
			Log.e(TAG, "object == null");
		}

		Log.i(TAG, "--- parseObject3() end ---");
	}

	public static void parseObject2() {
		Log.i(TAG, "--- parseObject2() start ---");

		GJsonBean object = GJson.parseObject(jsonObjectString, GJsonBean.class);
		if (object != null) {
			Log.e(TAG, "object != null");
			object.print();

			Log.e(TAG, "dddddddddddddd:"+object.getGJsonData().length);
			object.getGJsonData()[0].print();

		}else{
			Log.e(TAG, "object == null");
		}

		Log.i(TAG, "--- parseObject2() end ---");
	}

	public static void parseList3() {
		Log.i(TAG, "--- parseList3() start ---");

		List<GJsonData> list = GJson.parseList(jsonObjectString, "GJsonData", GJsonData.class);
		if (list == null) {
			Log.i(TAG, "list == null");
		}else{
			Log.i(TAG, "list != null");
			list.get(0).print();
		}

		list = GJson.parseList(jsonObjectString, "GJsonSub", GJsonData.class);
		if (list == null) {
			Log.i(TAG, "list == null");
		}else{
			Log.i(TAG, "list != null");
			list.get(0).print();
		}

		list = GJson.parseList(jsonObjectString, "dddddd", GJsonData.class);
		if (list == null) {
			Log.i(TAG, "list == null");
		}else{
			Log.i(TAG, "list != null");
			list.get(0).print();
		}

		list = GJson.parseList(jsonObjectString, "tStringText", GJsonData.class);
		if (list == null) {
			Log.i(TAG, "list == null");
		}else{
			Log.i(TAG, "list != null");
			list.get(0).print();
		}

		Log.i(TAG, "--- parseList3() end ---");
	}

	public static void parseList2() {
		Log.e(TAG, "--- parseList2() start ---");

		String jsonArrayString = "[56.98,0.988,78.09]";
//		jsonArrayString = "[34,45,56]";
//		jsonArrayString = "[true,false,true]";
//		jsonArrayString = "[\"a\",\"b\",\"c\"]";
		jsonArrayString = "[{\"a\":\"ccc\",\"b\":34,\"c\":true},{\"a\":\"case\",\"b\":5998,\"c\":true}]";

//		List<Double> list = GJson.parseList(jsonArrayString, Double.class);
//		List<Boolean> list = GJson.parseList(jsonArrayString, Boolean.class);
//		List<String> list = GJson.parseList(jsonArrayString, String.class);
		List<GJsonSub> list = GJson.parseList(jsonArrayString, GJsonSub.class);
//		List<Integer> list = GJson.parseList(jsonArrayString, Integer.class);
		if (list == null) {
			Log.i(TAG, "list == null");
		}else{
			Log.e(TAG, "list != null");
			Log.e(TAG, "list != null:"+list.get(0).getA());
//			Log.e(TAG, "list != null:"+list.get(0).print());
//			Log.i(TAG, "list != null:"+list.get(0).intValue());
		}

		Log.e(TAG, "--- parseList2() end ---");
	}

}