package com.tradiumapp.swingtradealerts.scheduledtasks.SupportResistance;

import java.util.List;

public class PointScore {
    Double point;
    Double score;
    List<PointEvent> pointEventList;

    public PointScore(Double point, Double score, List<PointEvent> pointEventList) {
        this.point = point;
        this.score = score;
        this.pointEventList = pointEventList;
    }
}
