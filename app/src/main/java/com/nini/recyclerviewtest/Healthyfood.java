package com.nini.recyclerviewtest;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 食物类
 * Parcelable:序列化接口,可在intent中传递对象,实现后全部由AS自动生成代码
 */
public class Healthyfood implements Parcelable {

    public static ArrayList<Healthyfood> datas;

    /*
食品名字	     圆圈内容	食品类别	营养物质		背景色
大豆		     粮	       	粮食		蛋白质			#BB4C3B
十字花科蔬菜	 蔬		    蔬菜		维生素C			#C48D30
牛奶		     饮	       	饮品		钙			    #4469B0
海鱼		     肉	       	肉食		蛋白质			#20A17B
菌菇类		     蔬	       	蔬菜		微量元素		#BB4C3B
番茄		     蔬	       	蔬菜		番茄红素		#4469B0
胡萝卜		     蔬	       	蔬菜		胡萝卜素		#20A17B
荞麦		     粮	       	粮食		膳食纤维		#BB4C3B
鸡蛋		     杂	       	杂		几乎所有营养物质	#C48D30
     */

    private String foodName;//食品名字
    private String simpleType;//圆圈内容
    private String fullType;//食品类别
    private String nutrientSubstance;//营养物质
    private String color;//背景色
    private boolean isCollected;//是否被收藏


    public Healthyfood() {
    }


    public Healthyfood(String foodName, String simpleType, String fullType, String nutrientSubstance, String color) {
        this.foodName = foodName;
        this.simpleType = simpleType;
        this.fullType = fullType;
        this.nutrientSubstance = nutrientSubstance;
        this.color = color;
    }

    protected Healthyfood(Parcel in) {
        foodName = in.readString();
        simpleType = in.readString();
        fullType = in.readString();
        nutrientSubstance = in.readString();
        color = in.readString();
    }


    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public static final Creator<Healthyfood> CREATOR = new Creator<Healthyfood>() {
        @Override
        public Healthyfood createFromParcel(Parcel in) {
            return new Healthyfood(in);
        }

        @Override
        public Healthyfood[] newArray(int size) {
            return new Healthyfood[size];
        }
    };

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getSimpleType() {
        return simpleType;
    }

    public void setSimpleType(String simpleType) {
        this.simpleType = simpleType;
    }

    public String getFullType() {
        return fullType;
    }

    public void setFullType(String fullType) {
        this.fullType = fullType;
    }

    public String getNutrientSubstance() {
        return nutrientSubstance;
    }

    public void setNutrientSubstance(String nutrientSubstance) {
        this.nutrientSubstance = nutrientSubstance;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodName);
        dest.writeString(simpleType);
        dest.writeString(fullType);
        dest.writeString(nutrientSubstance);
        dest.writeString(color);
    }
}
