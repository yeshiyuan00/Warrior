package com.ysy.warrior.otto;

import com.squareup.otto.Bus;

/**
 * User: ysy
 * Date: 2015/12/30
 * Time: 14:17
 */
public class BusProvider {
    private static final Bus bus = new Bus();
    public static Bus getInstance(){
        return bus;
    }
    private BusProvider(){}
}
