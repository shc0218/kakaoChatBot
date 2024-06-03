package org.shc.kakaochatbot.Controller;

import org.springframework.web.bind.annotation.*;
import org.shc.kakaochatbot.tools.getMeal;
import org.shc.kakaochatbot.Data.mealData;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class mealController {
    getMeal getMeal = new getMeal();
    private String key = "9361f9c21e834ef58bec4e49db0f2a31";
    private String cityCode = "B10";
    private String schoolCode = "7010092";;

    @ResponseBody
    @PostMapping("/meal")
    public mealData meal() {
        mealData mealData = new mealData();
        LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateNow = date.format(formatter);
        HashMap<String, ArrayList<String>> getMealData = getMeal.getMeal(key, cityCode, schoolCode, dateNow);

        mealData.setMealMenuList(String.join(", ", getMealData.get("menu_list")));
        mealData.setMealAllergyList(String.join(", ", getMealData.get("allergy_list")));
        return mealData;


    }
}