package com.example.androidstompsocketclient19;


import com.example.dlog.DLog;

class Geek1 extends Thread {
    public void run()
    {
        synchronized(this)
        {
            DLog.write(Thread.currentThread().getName() + "...starts");
            try {
                DLog.write(Thread.currentThread().getName() + "...before");
                this.wait();
                DLog.write(Thread.currentThread().getName() + "...after");
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            DLog.write(Thread.currentThread().getName() + "...notified");
        }
    }
} class Geek2 extends Thread {
    Geek1 geeks1;
    Geek2(Geek1 geeks1)
    {
        this.geeks1 = geeks1;
    }
    public void run()
    {
        synchronized(this.geeks1)
        {
            DLog.write(Thread.currentThread().getName() + "...starts");

            try {
                this.geeks1.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            DLog.write(Thread.currentThread().getName() + "...notified");
        }
    }
} class Geek3 extends Thread {
    Geek1 geeks1;
    Geek3(Geek1 geeks1)
    {
        this.geeks1 = geeks1;
    }
    public void run()
    {
        synchronized(this.geeks1)
        {
            DLog.write(Thread.currentThread().getName() + "...starts");
            this.geeks1.notify();
            DLog.write(Thread.currentThread().getName() + "...notified");
        }
    }
}