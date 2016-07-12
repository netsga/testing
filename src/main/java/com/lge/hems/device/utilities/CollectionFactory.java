/**
 * CREATIVE INOVATION CENTER, LG ELECTRONICS INC., SEOUL, KOREA
 * Copyright(c) 2013 by LG Electronics Inc.
 *
 * All rights reserved. No part of this work may be reproduced, stored in a
 * retrieval system, or transmitted by any means without prior written
 * Permission of LG Electronics Inc.
 * 
 * @Author     dongwook.lee@lge.com
 * @Date    2014/06/18
 */

package com.lge.hems.device.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class CollectionFactory {
    private CollectionFactory() {
        super();
    }
    public static<V> ArrayList<V> newList() {
        return new ArrayList<V>();
    }

    public static<K, V> HashMap<K, V> newMap() {
        return new HashMap<K, V>();
    }
    
    public static<V> HashSet<V> newSet() {
    	return new HashSet<V>();
    }
}
