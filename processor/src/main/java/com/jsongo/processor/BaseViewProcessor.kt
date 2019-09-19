package com.jsongo.processor

import com.jsongo.annotation.anno.ConfPage
import com.jsongo.annotation.anno.Presenter
import com.jsongo.annotation.configor.Configor
import com.jsongo.annotation.register.ViewConfigorRegister
import com.squareup.javapoet.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author jsongo
 * @date 19-9-9 上午11:01
 * @desc 用于 @ConfPage  和 @Presenter注解
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class BaseViewProcessor : AbstractProcessor() {
    private var filer: Filer? = null
    private var messager: Messager? = null

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)

        filer = processingEnv?.filer
        messager = processingEnv?.messager
    }

    /**
     * 需要生成的代码类的集合
     * 键为源类的全类名  值为 <包名,TypeSpec.Builder,MethodSpec.Builder>
     */
    private val genTypeSpecBuilders =
        HashMap<String, ThreePair<String, TypeSpec.Builder, MethodSpec.Builder>>()
    //目标类的变量名
    private val targetVarName = "targetObj"

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        //找到被注解的元素

        val elementsAnnotatedWithConfPage = roundEnv?.getElementsAnnotatedWith(ConfPage::class.java)
        elementsAnnotatedWithConfPage?.forEach {
            dealConfPageAnno(it)
        }

        val elementsAnnotatedWithPresenter =
            roundEnv?.getElementsAnnotatedWith(Presenter::class.java)
        elementsAnnotatedWithPresenter?.forEach {
            dealPresenterAnno(it)
        }
        //生成所有文件
        genTypeSpecBuilders.forEach { clazzName, pair ->
            try {
                pair.second.addMethod(pair.third.build())
                JavaFile.builder(pair.first, pair.second.build()).build().writeTo(filer)
            } catch (e: Exception) {
                messager?.printMessage(Diagnostic.Kind.WARNING, "Exception : ${e.message}\r\n")
            }
        }

        return true
    }

    /**
     * 处理ConfPage注解
     */
    private fun dealConfPageAnno(ele: Element) {
        val confPage = ele.getAnnotation(ConfPage::class.java)
        val eleStr = ele.toString()
        val threePair = getTypeSpecBuilder(eleStr)
        val (pkgName, clazzName) = getPkgClazzName(eleStr)
        val methodName = "confPage"
        val var0Name = "var0"
        val methodSpec = MethodSpec.methodBuilder(methodName)
            .addParameter(ClassName.get(pkgName, clazzName), var0Name)
            .addModifiers(Modifier.PRIVATE)
            .returns(TypeName.VOID)
            .addStatement("${var0Name}.setMainLayoutId(${confPage.mainLayoutId})")
            .addStatement("${var0Name}.setContainerIndex(${confPage.containerIndex})")

        threePair.second.addMethod(methodSpec.build())
        threePair.third.addStatement("\$N(\$N)", methodName, targetVarName)
    }

    /**
     * 处理Presenter注解
     */
    private fun dealPresenterAnno(ele: Element) {
        val clazzName = ele.enclosingElement.toString()
        val threePair = getTypeSpecBuilder(clazzName)
    }

    /**
     * 通过类名获取，对应要生成的代码类的builder
     */
    private fun getTypeSpecBuilder(rawClazzName: String): ThreePair<String, TypeSpec.Builder, MethodSpec.Builder> {
        val threePair: ThreePair<String, TypeSpec.Builder, MethodSpec.Builder>

        val (pkgName, simpleClazzName) = getPkgClazzName(rawClazzName)
        val targetClass = ClassName.get(pkgName, simpleClazzName)

        //如果集合中有，从集合中取，如果没有，创建，添加到集合
        if (genTypeSpecBuilders.containsKey(rawClazzName)) {
            threePair = genTypeSpecBuilders[rawClazzName]!!
        } else {
            val (genPkgName, genClazzName) = ViewConfigorRegister.getGenPkgName(rawClazzName)
            //创建类
            val classBuilder = TypeSpec.classBuilder(genClazzName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Configor::class.java)
            //创建重写的方法
            val var0Name = "obj"
            val methodBuilder = MethodSpec.methodBuilder("config")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(TypeName.OBJECT, var0Name)
                .addCode(
                    CodeBlock.builder().add("if (!(${var0Name} instanceof \$T)){", targetClass)
                        .addStatement("return")
                        .add("}").build()
                )
                .addCode(
                    CodeBlock.builder().addStatement(
                        "\$T $targetVarName = (\$T)${var0Name}",
                        targetClass,
                        targetClass
                    ).build()
                )
            threePair = ThreePair(genPkgName, classBuilder, methodBuilder)
            genTypeSpecBuilders[rawClazzName] = threePair
        }
        return threePair
    }


    override fun getSupportedAnnotationTypes() = linkedSetOf(
        ConfPage::class.java.canonicalName,
        Presenter::class.java.canonicalName
    )

    fun getPkgClazzName(rawClazzName: String): Pair<String, String> {
        val lastIndexOfDot = rawClazzName.lastIndexOf('.')
        //包名
        val pkgName = rawClazzName.subSequence(0, lastIndexOfDot)

        //类名
        val simpleName = rawClazzName.subSequence(lastIndexOfDot + 1, rawClazzName.length)

        return Pair(pkgName.toString(), simpleName.toString())
    }

}