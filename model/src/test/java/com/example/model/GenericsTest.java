package com.example.model;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * @author zrq
 * @time 2025/11/24 19:52
 * @description
 */

public class GenericsTest<E extends Cat> {

    public E e;

    public GenericsTest(E e) {
        this.e = e;
    }

    public E getE() {
        return e;
    }

    public <M, T> M generics(M m, T t) {
        return m;
    }

    public <M extends Number> M returnTest(M m) {
        return m;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        GenericsTest<Cat> genericsTest = new GenericsTest<>(new MiniCat());
        System.out.println(genericsTest.getE().getClass());
        for (Method method : genericsTest.getClass().getDeclaredMethods()) {
            System.out.println(method.getName() + "=" + method.getReturnType().getSimpleName());
        }
    }
}

class MiniCat extends Cat {

}

class Cat extends Animal implements Comparator<Cat> {
    public String name;

    public Cat() {
    }

    public Cat(String name) {
        this.name = name;
    }

    @Override
    public int compare(Cat o1, Cat o2) {
        return o1.name.compareTo(o2.name);
    }
}

class Animal {
    public Integer age;
}

interface Queue<T> {

    T pop();

    void offer(T t);
}

class LjcQueue implements Queue<String> {
    @Override
    public String pop() {
        return "";
    }

    @Override
    public void offer(String s) {

    }
}

@Slf4j
class ZrqQueue<E> implements Queue<E> {

    private E e;

    @Override
    public E pop() {
        return e;
    }

    @Override
    public void offer(E e) {

    }

    public <M> M getTest(M m) {
        return m;
    }

    public void getTest1(E m) {

    }

    public static <M, T, O> M getTest2(O o, T t, M m) {
        log.info("o-->{},t-->{}", o.getClass(), t.getClass());
        return m;
    }

    public void getTest3(List<? extends Number> list) {
        Number e1 = list.get(0);
        if (e1 instanceof Integer) {
            System.out.println(((Integer) e1).getClass());
        } else if (e1 instanceof Double) {
            System.out.println(((Double) e1).getClass());
        }
        log.info("queue-->{}", list.get(0).getClass());
    }

    public void getTest5(List<? super Cat> list) {
        list.add(new MiniCat());
    }

    public static void main1(String[] args) {
        Cat cat1 = new Cat("a");
        Cat cat2 = new Cat("d");
        Cat cat3 = new Cat("f");
        Cat cat4 = new Cat("c");
        Animal animal = new Animal();

        List<Integer> list = new ArrayList<>();
        TreeSet<Cat> treeSet = new TreeSet<>(new Cat());
        treeSet.add(cat1);
        treeSet.add(cat2);
        treeSet.add(cat3);
        treeSet.add(cat4);
        for (Cat cat : treeSet) {
            System.out.println(cat.name);
        }
    }
}