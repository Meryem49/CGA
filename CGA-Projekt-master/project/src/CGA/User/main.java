package CGA.User;
/**
 * Created by Fabian on 16.09.2017.
 */
import CGA.User.Game.*;
import org.joml.*;

import java.lang.Math;
import java.nio.file.Path;
import java.nio.file.Paths;


public class main
{
    public static void main(String[] args) {
        String filename="result.csv";
        Path pathToFile = Paths.get(filename);
        System.out.println(pathToFile.toAbsolutePath());
        Game game = new Game(1920, 1080, true, false, "Testgame", 3, 3);
        game.run();

        /*Vector3d a = new Vector3d(2, -3, -6);
        Vector3d agenormt = normen(a);

        Vector3d b = new Vector3d(Math.sqrt(2), -3, -6);
        Vector3d bgenormt = normen(b);

        Vector3d c = new Vector3d(-2, 2, Math.sqrt(8));
        Vector3d d = new Vector3d(3, 2, Math.sqrt(3));
        double winkelcd = vektorWinkel(c, d);

        Vector3d e = new Vector3d(-2, 2, Math.sqrt(8));
        Vector3d f = new Vector3d(3, 2, Math.sqrt(3));
        Vector3d kreuzef = kreuz(e, f);

        double alpha = 0;

        Vector3d punkta = new Vector3d(4,0,0);
        Vector3d punktb = new Vector3d(6,0,0);
        Vector3d punktc = new Vector3d(5,1,0);

        Matrix3d x = new Matrix3d(Math.cos(alpha), Math.sin(alpha), 0, (-Math.sin(alpha)), Math.cos(alpha), 0, 0, 0, 1);

        Vector3d ergebnisa = matrixVektorMultiplizieren(x, punkta);
        Vector3d ergebnisb = matrixVektorMultiplizieren(x, punktb);
        Vector3d ergebnisc = matrixVektorMultiplizieren(x, punktc);

        double aalpha = 0;
        double bbeta = 0;

        Vector3d punktaa = new Vector3d(2,2,0);
        Vector3d punktbb = new Vector3d(4,2,0);
        Vector3d punktcc = new Vector3d(4,4,0);
        Vector3d punktdd = new Vector3d(2,4,0);

        Matrix3d xx = new Matrix3d(aalpha, 0, 0, 0, bbeta, 0, 0, 0, 1);

        Vector3d ergebnisaa = matrixVektorMultiplizieren(x, punktaa);
        Vector3d ergebnisbb = matrixVektorMultiplizieren(x, punktbb);
        Vector3d ergebniscc = matrixVektorMultiplizieren(x, punktcc);
        Vector3d ergebnisdd = matrixVektorMultiplizieren(x, punktdd);
    }

    public static Vector3d normen(Vector3d n){
        n.normalize();
        System.out.println("Der Genormte Vector: \n" + n.x + "\n" + n.y + "\n" + n.z + "\n");
        return n;
    }

    public static double vektorWinkel(Vector3d n, Vector3d m){
        System.out.println("Der Winkel zwischen den Vektoren beträgt:" + n.angle(m) + "\n");
      return n.angle(m);
    }

    public static Vector3d kreuz(Vector3d n, Vector3d m){
        System.out.println("Das Kreuzprodukt lautet:\n" + n.cross(m).x + "\n" + n.cross(m).y + "\n" + n.cross(m).z + "\n");
        return n.cross(m);

    }

    public static Vector3d matrixVektorMultiplizieren (Matrix3d n, Vector3d m) {
        n.transform(m);
        System.out.println("Der Transformierte Vektor: \n" + m.x + "\n" + m.y + "\n" + m.z + "\n");
        return m;
    }*/
    }

}
