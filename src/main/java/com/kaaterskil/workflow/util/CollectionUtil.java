package com.kaaterskil.workflow.util;

import java.util.Collection;

public class CollectionUtil {

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Collection collection) {
        return !CollectionUtil.isEmpty(collection);
    }
}
