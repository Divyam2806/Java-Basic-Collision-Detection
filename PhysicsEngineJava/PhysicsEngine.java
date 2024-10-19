import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PhysicsEngine extends JPanel implements ActionListener{

    private ArrayList<CObject> objectsList;
    private Timer timer;
    private static float e=1f;         //coefficient of restitution
    private static int velocityRange=10;
    private static int fwidth=700;
    private static int fheight=700;
    private static int w=40;
    private static int h=40;
    private static int numOfObjects=20;
    //private static int collisionCounter;

    public PhysicsEngine() {

        objectsList = new ArrayList<CObject>();

        //Color fixedColor = new Color(255,255,255);

        // objectsList.add(new CObject(new Vector2D(100, 300), new Vector2D(5, 0),new Color(12,124,165)));
        // objectsList.add(new CObject(new Vector2D(700, 300), new Vector2D(-2, 0),new Color(12,124,165)));

        //create numOfObjects objects and add them to the array list
        for(int i=0;i<numOfObjects;i++){

            //Generate random start positions
            int xi = Math.max((int)(Math.random()*(fwidth-w)),0);
            int yi = Math.max((int)(Math.random()*(fheight-h)),0);

            //Generate random velocity x and y component
            int vx= (int)(Math.random()*2*velocityRange)-velocityRange;
            int vy= (int)(Math.random()*2*velocityRange)-velocityRange;

            // Generate random values for red, green, and blue components
            int red = (int) (Math.random() * 256); // Random value between 0 and 255
            int green = (int) (Math.random() * 256); // Random value between 0 and 255
            int blue = (int) (Math.random() * 256); // Random value between 0 and 255

            objectsList.add(new CObject(new Vector2D(xi, yi), new Vector2D(vx, vy),new Color(red,green,blue)));
            //objectsList.add(new CObject(new Vector2D(xi, yi), new Vector2D(vx, vy),fixedColor));
            
            //System.out.println("Velocity of object number "+i+" is "+vx+"i + "+vy+"j")
        }
        
        timer = new Timer(8, this); // delay = 16, Roughly 60 FPS,  delay=4  250 FPS
        timer.start();

        this.setBackground(Color.BLACK);
    }

    public static void main(String[] args) {

        PhysicsEngine engine = new PhysicsEngine();
        JFrame frame = new JFrame("Simple Physics Engine");
        frame.add(engine);
        frame.setSize(fwidth, fheight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for(CObject obj : objectsList){
            g.setColor(obj.c);
            g.fillOval(obj.position.x, obj.position.y, w, h);
        }
    }
    
    public void actionPerformed(ActionEvent e) {

        for(int i=0;i<numOfObjects;i++){

            CObject obj=objectsList.get(i);
            // Update position based on velocity
            obj.position.add(obj.velocity);

            // Simple boundary collision detection
            if (obj.position.x < 0 || obj.position.x > getWidth() - w) {
                obj.velocity.x = -obj.velocity.x;

            }
            if (obj.position.y < 0 || obj.position.y > getHeight() - h) {
                obj.velocity.y = -obj.velocity.y;
            }

            //Collision detection with other objects
            for(int j=i+1;j<numOfObjects;j++){
                CObject objn=objectsList.get(j);
                if(isCollision(obj, objn)){
                    handleCollision(obj,objn);
                }
            }
        }   

        repaint();
    }


    boolean isCollision(CObject obj1, CObject obj2){

        //Calcultating centres of the objects;
        float c1x=obj1.position.x+w/2;
        float c1y=obj1.position.y+h/2;
        float c2x=obj2.position.x+w/2;
        float c2y=obj2.position.y+h/2;

        float dx=c2x-c1x;
        float dy= c2y-c1y;

        //Calculating distance between centres
        double dist=Math.sqrt(dx*dx+dy*dy);

        if(dist<=w/2+w/2){
            return true;
        }
        else
        {return false;}

    }

    // void handleCollision(CObject obj1, CObject obj2){

    //     //Calculate velocity after collision
    //     obj1.velocity.x=(int)(obj1.velocity.x*((1-e)/2)+ obj2.velocity.x*((1+e)/2));
    //     obj1.velocity.y=(int)(obj1.velocity.y*((1-e)/2)+ obj2.velocity.y*((1+e)/2));

    //     obj2.velocity.x=(int)(obj2.velocity.x*((1-e)/2)+ obj1.velocity.x*((1+e)/2));
    //     obj2.velocity.y=(int)(obj2.velocity.y*((1-e)/2)+ obj1.velocity.y*((1+e)/2));
    // }

    void handleCollision(CObject obj1, CObject obj2) {
        // Calculate velocities after collision
        Vector2D v1f = new Vector2D();
        Vector2D v2f = new Vector2D();
    
        v1f.x =(int)((2 * obj2.velocity.x + (e - 1) * obj1.velocity.x) / (1 + e));
        v1f.y =(int)((2 * obj2.velocity.y + (e - 1) * obj1.velocity.y) / (1 + e));
    
        v2f.x = (int)((2 * obj1.velocity.x + (e - 1) * obj2.velocity.x) / (1 + e));
        v2f.y = (int)((2 * obj1.velocity.y + (e - 1) * obj2.velocity.y) / (1 + e));
    
        // Update velocities
        obj1.velocity = v1f;
        obj2.velocity = v2f;
    }
    

    public class Vector2D {
        public int x;
        public int y;
        
        public Vector2D(){
            x=0;
            y=0;
        }
        public Vector2D(int x, int y) {
            this.x = x;
            this.y = y;
        }
    
        public void add(Vector2D other) {
            this.x += other.x;
            this.y += other.y;
        }
    
        public void multiply(double scalar) {
            this.x *= scalar;
            this.y *= scalar;
        }
    }

    public class CObject{
        public Vector2D position;
        public Vector2D velocity;
        public Color c;

        public CObject(Vector2D position, Vector2D velocity, Color c){
            this.position=position;
            this.velocity=velocity;
            this.c=c;
        }

    }
}