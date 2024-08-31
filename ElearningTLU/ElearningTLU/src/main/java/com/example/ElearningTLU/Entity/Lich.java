package com.example.ElearningTLU.Entity;

import lombok.Data;

@Data

public class Lich {
    private int start;
    private int finish;
    public Lich(int a, int b)
    {
        this.start=a;
        this.finish=b;
    }
    public Lich(Lich a)
    {
        this.start=a.getStart();
        this.finish=a.getFinish();
    }
    public Lich()
    {}
}
