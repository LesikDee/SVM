package dema.GeometryModule;

public class HyperPlane {
    public static double determ(double a, double b, double c, double d){
        return a*d -b*c;
    }

    public static Plane getPlane(Point p1, Point p2, Point p3)
    {
        Plane plane = new Plane();
        double alpha1 = determ(p2.y - p1.y, p3.y-p1.y, p2.z - p1.z, p3.z - p1.z);
        double alpha2 = determ(p2.x - p1.x, p3.x-p1.x, p2.z - p1.z, p3.z - p1.z);
        double alpha3 = determ(p2.x - p1.x, p3.x-p1.x, p2.y - p1.y, p3.y - p1.y);
        plane.A = alpha1;
        plane.B = -alpha2;
        plane.C = alpha3;
        plane.D = -p1.x*alpha1 + p1.y*alpha2 - p1.z*alpha3;
        return plane;
    }
    public static double pointPosition(Point p, Plane plane){
        return plane.A*p.x + plane.B*p.y + plane.C*p.z +plane.D;
    }
    public static double pointDistance(Point p, Plane plane){
        return Math.abs(plane.A*p.x + plane.B*p.y + plane.C*p.z +plane.D)
                /Math.sqrt(Math.pow(plane.A,2)+ Math.pow(plane.B,2) + Math.pow(plane.C,2));
    }
    public static double pointToPointDistance(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(p1.z - p2.z, 2));
    }
}
