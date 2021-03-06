package com.jsongo.processor

import com.jsongo.annotation.anno.Page
import com.jsongo.annotation.configor.Configor
import com.jsongo.annotation.register.ViewConfigor
import com.jsongo.annotation.util.Util.getPkgClazzName
import com.jsongo.processor.bean.FourPair
import com.squareup.javapoet.*
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author jsongo
 * @date 19-9-9 上午11:01
 * @desc 用于 @Page 注解
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ViewConfigorProcessor : BaseProcessor() {

    /**
     * 需要生成的代码类的集合
     * 键为源类的全类名  值为 <包名,TypeSpec.Builder,MethodSpec.Builder>
     */
    private val genTypeSpecBuilders =
        HashMap<String, FourPair<String, TypeSpec.Builder, MethodSpec.Builder, Boolean>>()

    //目标类的变量名
    private val targetVarName = "targetObj"

    override fun doProcess(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        //找到被注解的元素

        val elementsAnnotatedWithPage = roundEnv?.getElementsAnnotatedWith(Page::class.java)
        elementsAnnotatedWithPage?.forEach {
            dealPageAnno(it)
        }

        //生成所有文件
        genTypeSpecBuilders.forEach { clazzName, pair ->
            try {
                pair.second.addMethod(pair.third.build())
                JavaFile.builder(pair.first, pair.second.build()).build().writeTo(mFiler)
            } catch (e: Exception) {
                mMessager?.printMessage(Diagnostic.Kind.WARNING, "Exception : ${e.message}\r\n")
            }
        }

        return true
    }

    /**
     * 处理Page注解
     */
    private fun dealPageAnno(ele: Element) {
        val eleStr = ele.toString()
        //需要构建的类和方法
        val fourPair = getTypeSpecBuilder(eleStr)

        val (pkgName, clazzName) = getPkgClazzName(eleStr)
        //注解对象
        val page = ele.getAnnotation(Page::class.java)

        //构建方法
        val methodName = "confPage"
        val var0Name = "var0"
        val methodSpec = MethodSpec.methodBuilder(methodName)
            .addParameter(ClassName.get(pkgName, clazzName), var0Name)
            .addModifiers(Modifier.PRIVATE)
            .returns(TypeName.VOID)
            .addStatement("${var0Name}.setMainLayoutId(${page.mainLayoutId})")
            .addStatement("${var0Name}.setContainerIndex(${page.containerIndex})")

        //添加方法
        fourPair.second.addMethod(methodSpec.build())
        //调用方法
        fourPair.third.addStatement("\$N(\$N)", methodName, targetVarName)
    }

    /**
     * 通过类名获取，对应要生成的代码类的builder
     */
    private fun getTypeSpecBuilder(rawClazzName: String): FourPair<String, TypeSpec.Builder, MethodSpec.Builder, Boolean> {
        val fourPair: FourPair<String, TypeSpec.Builder, MethodSpec.Builder, Boolean>

        //如果集合中有，从集合中取，如果没有，创建，添加到集合
        if (genTypeSpecBuilders.containsKey(rawClazzName)) {
            fourPair = genTypeSpecBuilders[rawClazzName]!!
        } else {
            val (pkgName, simpleClazzName) = getPkgClazzName(rawClazzName)
            val targetClass = ClassName.get(pkgName, simpleClazzName)

            //创建类
            val classBuilder =
                TypeSpec.classBuilder("${simpleClazzName}${ViewConfigor.clazzNameSuffix}")
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
                    CodeBlock.builder()
                        .beginControlFlow(
                            "if (!(${var0Name} instanceof \$T))", targetClass
                        ).addStatement("return")
                        .endControlFlow().build()
                )
                .addCode(
                    CodeBlock.builder().addStatement(
                        "\$T $targetVarName = (\$T)${var0Name}",
                        targetClass,
                        targetClass
                    ).build()
                )

            fourPair = FourPair(
                "${pkgName}${ViewConfigor.pkgSuffix}",
                classBuilder,
                methodBuilder,
                false
            )
            genTypeSpecBuilders[rawClazzName] = fourPair
        }
        return fourPair
    }

    override fun getSupportedAnnotationTypes() = linkedSetOf(
        Page::class.java.canonicalName
    )

}