package com.gaea.common.query;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tiantiea on 15/6/9.
 */
public class Sort {

    public static final Sort EMPTY = new Sort();

    private Direction direction;

    private String property;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = Direction.fromString(direction);
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public static enum Direction {

        ASC, DESC;
        public static Direction fromString(String value) {
            if (StringUtils.isBlank(value)) {
                return DESC;
            }

            for (Direction direction : Direction.values() ) {
                if (StringUtils.equalsIgnoreCase(direction.name(), value)) {
                    return direction;
                }
            }

            return DESC;
        }
    }

}
