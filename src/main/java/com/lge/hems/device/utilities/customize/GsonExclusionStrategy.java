package com.lge.hems.device.utilities.customize;

/**
 * Created by netsga on 2016. 6. 18..
 */

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * 이 클래스는 GsonExclude annotation을 사용하여 특정 필드의 정보를 gson serialize시 exclude 시키는 방법이다.
 * strategy class를 구성 후 gsonbuilder를 사용해 등록해야 한다.
 */
public class GsonExclusionStrategy implements ExclusionStrategy {

    private Class classToExclude;

    public GsonExclusionStrategy(Class classToExclude) {
        this.classToExclude = classToExclude;
    }

    // This method is called for all fields. if the method returns false the
    // field is excluded from serialization
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        if (f.getAnnotation(GsonExclude.class) == null)
            return false;

        return true;
    }

    // This method is called for all classes. If the method returns false the
    // class is excluded.
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        if (clazz.equals(classToExclude))
            return true;
        return false;
    }

}