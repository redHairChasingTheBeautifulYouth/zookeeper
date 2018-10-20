package api;
/*
 *一句话描述该类作用:【】
 *@Author:LB
 *
 */

import java.io.Serializable;

public class DoOrderRequest implements Serializable{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DoOrderRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
