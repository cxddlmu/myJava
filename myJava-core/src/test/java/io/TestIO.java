package io;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by cuixiaodong on 2017/3/21.
 */
public class TestIO implements Serializable{

    @Test
    public void test() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get("/Users/cuixiaodong/testObj")) ){
        }) {
            objectOutputStream.writeObject(new TestObj(10,22));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("/Users/cuixiaodong/testObj")));
            NewObj testObj = (NewObj) objectInputStream.readObject();
            System.out.println(testObj.getDay());
//            System.out.println(testObj.getName());
//            System.out.println(testObj.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Setter
    @Getter
    class TestObj implements Serializable{
        transient private int age;
        private String name = "name";
        transient private int time;

        public TestObj(int age) {
            this.age = age;
        }

        public TestObj(int age,int time) {
            this.age=age;
            this.time=time;
        }

        @Override
        public String toString() {
            return "TestObj{" +
                    "age=" + age +
                    '}';
        }
        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            String s = objectInputStream.readUTF();
            name=s;
        }
        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeUTF(age+"|"+time);
        }

        private Object writeReplace() {
            return new NewObj();
        }
    }

    @Setter
    @Getter
    class NewObj implements Serializable {
        private int day;
        private Object readResolve() {
            return new NewObj();
        }

        public NewObj() {
           this.day=1;
        }
    }

    class exterObj implements Externalizable{

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {

        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        }
    }
}
