package com.jsongo.processor

import com.jsongo.annotation.anno.Model
import com.jsongo.annotation.configor.Configor
import com.jsongo.annotation.register.PresenterConfigor
import com.jsongo.annotation.util.Util
import com.jsongo.annotation.util.getParamsMap
import com.jsongo.processor.bean.FourPair
import com.squareup.javapoet.*
import javafx.beans.property.SimpleBooleanProperty
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author ： jsongo
 * @date ： 19-9-20 下午8:52
 * @desc :
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class PresenterConfigorProcessor : AbstractProcessor() {

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
        HashMap<String, FourPair<String, TypeSpec.Builder, MethodSpec.Builder, SimpleBooleanProperty>>()

    //目标类的变量名
    private val targetVarName = "targetObj"

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        val elementsAnnotatedWithModel = roundEnv?.getElementsAnnotatedWith(Model::class.java)
        elementsAnnotatedWithModel?.forEach {
            dealModelAnno(it)
        }

        genTypeSpecBuilders.forEach { rawClazzName, pair ->
            try {
                pair.second.addMethod(pair.third.build())
                JavaFile.builder(pair.first, pair.second.build()).build().writeTo(filer)
            } catch (e: Exception) {
                messager?.printMessage(Diagnostic.Kind.WARNING, "Exception : ${e.message}\r\n")
            }
        }
        return true
    }

    private fun dealModelAnno(element: Element) {
        //所在类的全类名
        val rawClazzName = element.enclosingElement.toString()
        //所在类的包名和类名
        val (pkgName, simpleClazzName) = Util.getPkgClazzName(rawClazzName)

        //注解的参数，因为直接拿到KClass对象无法获取值，出次下策
        val modelAnnoParams = element.getAnnotation(Model::class.java).getParamsMap()
        //注解传入的model类
        val modelClazzNameStr = modelAnnoParams["clazz"]
        if (modelClazzNameStr.isNullOrEmpty()) {
            return
        }
        val (modelPkgName, modelSimpleName) = Util.getPkgClazzName(modelClazzNameStr)
        val modelClassName = ClassName.get(modelPkgName, modelSimpleName)

        //需要构建的类和方法
        val fourPair = getTypeSpecBuilder(rawClazzName)

        val modelVarName = "modelGen"
        //如果没有变量
        if (fourPair.fourth.value.not()) {
            //声明model变量
            fourPair.second.addField(
                FieldSpec.builder(modelClassName, modelVarName, Modifier.PRIVATE).build()
            )
            //在config方法中初始化变量
            fourPair.third.addStatement("${modelVarName} = new \$T()", modelClassName)

            fourPair.fourth.value = true
        }

        //构建方法
        val methodName = "inject_${element.simpleName}"
        val var0Name = "var0"
        val methodBuilder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PRIVATE)
            //方法参数
            .addParameter(ClassName.get(pkgName, simpleClazzName), var0Name)
            .returns(TypeName.VOID)
            //赋值
            .addStatement("${var0Name}.${element.simpleName} = ${modelVarName}")

        //类中添加方法
        fourPair.second.addMethod(methodBuilder.build())
        //调用方法
        fourPair.third.addStatement("${methodName}(${targetVarName})")
    }

    /**
     * 根据键值获取要生成的类
     */
    private fun getTypeSpecBuilder(rawClazzName: String): FourPair<String, TypeSpec.Builder, MethodSpec.Builder, SimpleBooleanProperty> {
        val fourPair: FourPair<String, TypeSpec.Builder, MethodSpec.Builder, SimpleBooleanProperty>

        if (genTypeSpecBuilders.containsKey(rawClazzName)) {
            fourPair = genTypeSpecBuilders.get(rawClazzName)!!
        } else {
            //原包名和类名
            val (pkgName, simpleClazzName) = Util.getPkgClazzName(rawClazzName)
            val targetClassName = ClassName.get(pkgName, simpleClazzName)
            //创建类
            val typeBuilder =
                TypeSpec.classBuilder(simpleClazzName + PresenterConfigor.clazzNameSuffix)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(Configor::class.java)

            //创建重载的方法
            val var0Name = "obj"
            val methodBuilder = MethodSpec.methodBuilder("config")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(TypeName.OBJECT, var0Name)
                .addCode(
                    CodeBlock.builder()
                        .beginControlFlow("if (!(${var0Name} instanceof \$T))", targetClassName)
                        .addStatement("return")
                        .endControlFlow().build()
                )
                .addCode(
                    CodeBlock.builder()
                        .addStatement(
                            "\$T ${targetVarName} = (\$T)${var0Name}",
                            targetClassName,
                            targetClassName
                        ).build()
                )

            fourPair = FourPair(
                pkgName + PresenterConfigor.pkgSuffix,
                typeBuilder,
                methodBuilder,
                SimpleBooleanProperty(false)
            )
            genTypeSpecBuilders[rawClazzName] = fourPair
        }
        return fourPair
    }

    override fun getSupportedAnnotationTypes() = linkedSetOf(
        Model::class.java.canonicalName
    )
}