package org.shc.kakaochatbot.tools;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;

public class getMeal {

    private String[] allergyList = {"난류", "우유", "메밀", "땅콩", "대두", "밀", "고등어", "게", "새우", "돼지고기", "복숭아", "토마토", "아황산류", "호두", "닭고기", "쇠고기", "오징어", "조개류(굴, 전복, 홍합 포함)", "잣"};

    public HashMap<String, ArrayList<String>> getMeal(String key, String cityCode, String schoolCode, String dateNow) {
        JSONParser jsonParser = new JSONParser();
        HashMap<String, ArrayList<String>> mealData = new HashMap<String, ArrayList<String>>();

        String uri = String.format("https://open.neis.go.kr/hub/mealServiceDietInfo" + // url 제작
                "?KEY=%s" +
                "&Type=json" +
                "&ATPT_OFCDC_SC_CODE=%s" +
                "&SD_SCHUL_CODE=%s" +
                "&MMEAL_SC_CODE=2" +
                "&MLSV_YMD=%s", key, cityCode, schoolCode, dateNow);

        OkHttpClient client = new OkHttpClient(); // 클라이언트 생성
        Request.Builder builder = new Request.Builder().url(uri).get(); // uri, 요청방식 설정
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute(); // Request 진행
            if (response.isSuccessful()) {
                ResponseBody body = response.body(); // Response 받음
                if (body != null) {
                    try {
                        ArrayList<String> mealMenuList = new ArrayList<String>();
                        ArrayList<String> mealAllergyList = new ArrayList<String>();

                        JSONObject requestDataObject =(JSONObject) jsonParser.parse(body.string()); // 받은 데이터를 정리
                        JSONArray requestDataArray = (JSONArray) requestDataObject.get("mealServiceDietInfo");
                        requestDataObject = (JSONObject) jsonParser.parse(requestDataArray.get(1).toString());
                        requestDataArray = (JSONArray) jsonParser.parse(requestDataObject.get("row").toString());
                        requestDataObject = (JSONObject) jsonParser.parse(requestDataArray.get(0).toString());

                        String requestDataString = requestDataObject.get("DDISH_NM").toString();
                        for (String i : requestDataString.split("<br/>")) {
                            String[] split = i.split(" ");
                            if (split.length == 2) {
                                split[1] = split[1].replace("(", "");
                                split[1] = split[1].replace(")", "");
                                for (String k : split[1].split("\\.")) {
                                    if (!mealAllergyList.contains(allergyList[Integer.parseInt(k) - 1])) {
                                        mealAllergyList.add(allergyList[Integer.parseInt(k) - 1]);
                                    }
                                }
                            }
                            mealMenuList.add(split[0]);
                        }
                        mealData.put("menu_list", mealMenuList);
                        mealData.put("allergy_list", mealAllergyList);
                        return mealData;
                    } catch (NullPointerException e) {
                        return null;
                    }

                }
            } else
                System.err.println("Error Occurred");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
