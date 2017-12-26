import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.*;
public class Hanio extends JApplet implements ActionListener, Runnable
{
    /**
     *diskNum是盘子的数量
     */
    private int diskNum ;
    /**
     *各个组件的句柄
     */
    private JButton begin, stop;
    private JLabel lDiskNum;
    private JTextField text;
    JPanel pane;
    /**
     *定义一个线程句柄
     */
    private Thread animate;
    /**
     *定义a,b,c三个柱子上是否有盘子，有哪些盘子
     */
    private int adisk[];
    private int bdisk[];
    private int cdisk[];
    public void init()
    {

        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        lDiskNum = new JLabel("盘子的数目");

        text = new JTextField(8);

        begin = new JButton("开始");
        begin.addActionListener(this);

        stop = new JButton("停止");
        stop.addActionListener(this);

        pane = new JPanel();
        pane.setLayout(new FlowLayout());
        pane.add(lDiskNum);
        pane.add(text);
        pane.add(begin);
        pane.add(stop);
        content.add(pane, BorderLayout.SOUTH);

    }
    public void paint(Graphics g)
    {
        Graphics2D g2D = (Graphics2D)g;
        Ellipse2D.Double ellipse;
        g2D.setPaint(getBackground());
        if(adisk != null)
        {
            /**
             *消除以前画的盘子
             */
            for(int j=adisk.length, i=0; --j>=0; i++ )
            {
                ellipse = new Ellipse2D.Double(20+i*5, 180-i*10, 180-i*10, 20);
                g2D.fill(ellipse);
                ellipse = new Ellipse2D.Double(220+i*5, 180-i*10, 180-i*10, 20);
                g2D.fill(ellipse);
                ellipse = new Ellipse2D.Double(420+i*5, 180-i*10, 180-i*10, 20);
                g2D.fill(ellipse);

            }
            drawEllipse(g, 20, adisk);//画A组盘子
            drawEllipse(g, 220, bdisk);//画B组盘子
            drawEllipse(g, 420, cdisk);//画C组盘子

        }
        pane.repaint();
    }
    public void update(Graphics g)
    {
        paint(g);
    }
    /**画出椭圆代表盘子，g是图形环境，x是最下面的盘子的横坐标，
     *arr是柱子数组
     */
    public void drawEllipse(Graphics g,int x,int arr[])
    {
        Graphics2D g2D = (Graphics2D)g;
        Ellipse2D.Double ellipse;
        g2D.setPaint(Color.gray);
        g2D.draw(new Line2D.Double(x+90, 10, x+90, 180));
        for(int j=arr.length, i=0; --j>=0; i++ )
            if(arr[j] != 0)
            {
                if(i%2 == 0)
                    g2D.setPaint(Color.blue);
                else
                    g2D.setPaint(Color.red);
                ellipse = new Ellipse2D.Double(x+i*5, 180-i*10, 180-i*10, 20);
                g2D.fill(ellipse);
            }
    }
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("开始"))
        {
            /**
             *进行初始化，开始的时候只有a柱子上有盘子，其他柱子都没有
             */
            diskNum = Integer.parseInt(text.getText());

            adisk = new int[diskNum];
            for(int i=0; i<adisk.length; i++)
                adisk[i] = 1;
            bdisk = new int[diskNum];
            for(int k=0; k<bdisk.length; k++)
                bdisk[k] = 0;
            cdisk = new int[diskNum];
            for(int i=0; i<cdisk.length; i++)
                cdisk[i] = 0;
            repaint();
            if(animate == null || !animate.isAlive())//创建一个线程
            {
                animate = new Thread(this);
                animate.start();
            }
        }
        if(command.equals("停止"))
        {
            for(int k=0; k<bdisk.length; k++)
                bdisk[k] = 0;
            for(int i=0; i<cdisk.length; i++)
                cdisk[i] = 0;
            repaint();
            text.setText("");
            animate = null;
        }
    }
    /**
     *线程方法，在此调用汉诺塔执行移动盘子操作
     */
    public void run()
    {
        hanio(diskNum, 'A', 'B', 'C');
        repaint();
    }
    /**
     *汉诺塔递规调用程序，n是盘子的数量，A，B，C分别代表三个柱子
     */
    public  void hanio(int n, char A, char B, char C)
    {
        if(n > 1)
        {
            hanio(n-1, A, C, B);
            pause();//停顿几秒在执行
            switch(A)
            {
                case 'A':adisk[n-1] = 0;break;
                case 'B':bdisk[n-1] = 0;break;
                case 'C':cdisk[n-1] = 0;break;
                default:break;
            }
            switch(C)
            {
                case 'A':adisk[n-1] = 1;break;
                case 'B':bdisk[n-1] = 1;break;
                case 'C':cdisk[n-1] = 1;break;
                default:break;
            }
            repaint();
            hanio(n-1, B, A, C);
        }
        pause();
        switch(A)
        {
            case 'A':adisk[n-1] = 0;break;
            case 'B':bdisk[n-1] = 0;break;
            case 'C':cdisk[n-1] = 0;break;
            default:break;
        }
        switch(C)
        {
            case 'A':adisk[n-1] = 1;break;
            case 'B':bdisk[n-1] = 1;break;
            case 'C':cdisk[n-1] = 1;break;
            default:break;
        }
        repaint();

    }


    /**
     *每隔半妙钟移动一个盘子
     */
    public void pause()
    {
        try{
            Thread.sleep(500);//可以修改此值加快盘子移动的速度
        }catch(InterruptedException e){}
    }
}
