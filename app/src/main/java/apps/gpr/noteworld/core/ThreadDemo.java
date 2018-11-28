package apps.gpr.noteworld.core;

public class ThreadDemo {
    private int i;

    public void printCount(){
        try{

            for (int i = 5; i > 0; i--){
                System.out.println("Selected number is: "+i);
            }
        }catch (Exception e){
            System.out.println("Thread has been interrupted");
        }
    }

    public static void main(String args[]){
        ThreadDemo demo = new ThreadDemo();

        Thread1 first = new Thread1("Thread1", demo);
        Thread1 second = new Thread1("Thread2", demo);

        try{
            first.start();
            second.start();
        }
        catch (Exception e){
            System.out.println("Interrupted");
        }
    }
}

class Thread1 implements Runnable{
    private Thread thread;
    private String threadName;
    ThreadDemo demo;

    public Thread1(String threadName, ThreadDemo demo) {
        this.threadName = threadName;
        this.demo = demo;
    }

    @Override
    public void run() {
        synchronized (demo) {
            demo.printCount();
        }
        System.out.println("Thread "+threadName+ " finishing");
    }

    public void start(){
        System.out.println("Starting "+threadName);
        if (thread == null){
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}

