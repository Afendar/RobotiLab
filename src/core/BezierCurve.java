package core;

public class BezierCurve {
    
    public static int easeInBounce(float t, float b, float c, float d){
        return (int)(c - easeOutBounce(d - t, 0, c, d) + b);
    }
    
    public static int easeOutBounce(float t, float b, float c, float d){
        if((t /= d) < (1 / 2.75f))
            return (int)((c * 7.5625f * t * t) + b);
        else if(t < (2 / 2.75f))
            return (int)(c * (7.5625f * (t -= (1.5f / 2.75f)) * t + .75f) + b);
        else if(t < (2.5 / 2.75))
            return (int)(c * (7.5625f * (t -= (2.25f / 2.75f)) * t + .9375f) + b);
        else
            return (int)(c * (7.5625f * (t -= (2.625f / 2.75f)) * t + .984375f) + b);
    }
}
