package collection.queue;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by cuixiaodong on 2017/4/7.
 */
public class TestDelayQueueSpecificCase{
    /**
     *
     *2014-1-10 下午9:43:48 by 孙振超
     *
     *void
     * @throws InterruptedException
     */
    public static void main(String [] arg) throws InterruptedException {
        // TODO Auto-generated method stub
        int studentNumber = 20;
        CountDownLatch countDownLatch = new CountDownLatch(studentNumber+1);
        DelayQueue< Student> students = new DelayQueue<Student>();
        Random random = new Random();
        for (int i = 0; i < studentNumber; i++) {
            students.put(new Student("student"+(i+1), 3000+random.nextInt(12000), countDownLatch));
        }
        Thread teacherThread =new Thread(new Teacher(students));
        students.put(new EndExam(students, 120, countDownLatch, teacherThread));
        teacherThread.start();
        countDownLatch.await();
        System.out.println(" 考试时间到，全部交卷！");
    }
}
class NewExamMain{
    public static void main(String[] args) throws InterruptedException {
        int studentNumber = 20;
        CountDownLatch countDownLatch = new CountDownLatch(studentNumber+1);
        DelayQueue< Student> students = new DelayQueue<>();
        Random random = new Random();
        for (int i = 0; i < studentNumber; i++) {
            students.put(new Student("student"+(i+1), 3000+random.nextInt(12000), countDownLatch));
        }
        Thread teacherThread =new Thread(new Teacher(students));
        new Thread(new Exam(students, countDownLatch, teacherThread)).start();
    }
}
@AllArgsConstructor
class Exam implements Runnable{
    private DelayQueue<Student> students;
    private CountDownLatch countDownLatch;
    private Thread teacherThread;
    @Override
    public void run() {
        teacherThread.start();
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {
            System.out.println();
            stopExam();
        }, 5000L, 5000L, TimeUnit.MILLISECONDS);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" 考试时间到，全部交卷！");

    }

    public void stopExam() {
        // TODO Auto-generated method stub

        teacherThread.interrupt();
        Student tmpStudent;
        for (Iterator<Student> iterator2 = students.iterator(); iterator2.hasNext();) {
            tmpStudent = iterator2.next();
            tmpStudent.setForce(true);
            tmpStudent.handIn();
        }
        countDownLatch.countDown();
    }
}

class Student implements Delayed {

    private String name;
    private long workTime;
    private long submitTime;
    private boolean isForce = false;
    private CountDownLatch countDownLatch;

    public Student(){}

    public Student(String name,long workTime,CountDownLatch countDownLatch){
        this.name = name;
        this.workTime = workTime;
        this.submitTime = workTime+System.currentTimeMillis();
        this.countDownLatch = countDownLatch;
    }

    @Override
    public int compareTo(Delayed o) {
        // TODO Auto-generated method stub
        if(o == null || ! (o instanceof Student)) return 1;
        if(o == this) return 0;
        Student s = (Student)o;
        if (this.workTime > s.workTime) {
            return 1;
        }else if (this.workTime == s.workTime) {
            return 0;
        }else {
            return -1;
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        // TODO Auto-generated method stub
        return unit.convert(submitTime - System.currentTimeMillis(),  TimeUnit.MILLISECONDS);
    }

    public void handIn() {
        // TODO Auto-generated method stub
        if (isForce) {
            System.out.println(name + " 交卷, 希望用时" + workTime/100 + "分钟"+" ,实际用时 120分钟" );
        }else {
            System.out.println(name + " 交卷, 希望用时" + workTime/100 + "分钟"+" ,实际用时 "+workTime/100 +" 分钟");
        }
        countDownLatch.countDown();
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean isForce) {
        this.isForce = isForce;
    }

}

class EndExam extends Student {

    private DelayQueue<Student> students;
    private CountDownLatch countDownLatch;
    private Thread teacherThread;

    public EndExam(DelayQueue<Student> students, long workTime, CountDownLatch countDownLatch, Thread teacherThread) {
        super("强制收卷", workTime,countDownLatch);
        this.students = students;
        this.countDownLatch = countDownLatch;
        this.teacherThread = teacherThread;
    }



    @Override
    public void handIn() {
        // TODO Auto-generated method stub

        teacherThread.interrupt();
        Student tmpStudent;
        for (Iterator<Student> iterator2 = students.iterator(); iterator2.hasNext();) {
            tmpStudent = iterator2.next();
            tmpStudent.setForce(true);
            tmpStudent.handIn();
        }
        countDownLatch.countDown();
    }

}

class Teacher implements Runnable{

    private DelayQueue<Student> students;
    public Teacher(DelayQueue<Student> students){
        this.students = students;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            System.out.println(" test start");
            while(!Thread.interrupted()){
                students.take().handIn();
            }
        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
        }
    }

}