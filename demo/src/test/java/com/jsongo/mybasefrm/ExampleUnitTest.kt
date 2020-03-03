package com.jsongo.mybasefrm

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun rxJavaTest() {
        Observable.just(ObservableOnSubscribe<Int>() {
            println("position 1")
        }).map {
            println("position 2")
        }.subscribe {
            println("position 3")
        }
    }

    @Test
    fun rxJavaTest2() {
/*        Observable.just(ObservableOnSubscribe<Int>() {
            println("position 1")
//            it.on
        }).map {
            println("position 2")
            it.subscribe(object : ObservableEmitter<Int> {

            })
        }.subscribe {
            println("position 3")
        }*/

/*        ObservableOnSubscribe<Int> {
        }.subscribe()*/


        Observable.create(ObservableOnSubscribe<Int> {
            println("position 1")
            it.onNext(10111)
            println("position 3")

        }).subscribe {
            println("position 2")
            println(it)
        }

    }
/*
    @Test
    fun annoTest(){
        val a = aClass()
        println(a.mainModel)
    }
    class aClass{
        @MyAnno1
        lateinit var mainModel:DemoModel

        init {
            AnnoProcess.bind(this)
        }
    }*/
}
