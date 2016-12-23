package edu.nju.wxd.weather.model.entity;

import java.sql.Timestamp;

/**
 * Created by wxd on 2016/12/23.
 */

public class Entity {

    private boolean blockFlag;
    private Timestamp createAt;

    public Entity() {
        this.blockFlag = false;
        this.createAt = new Timestamp(System.currentTimeMillis());
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
