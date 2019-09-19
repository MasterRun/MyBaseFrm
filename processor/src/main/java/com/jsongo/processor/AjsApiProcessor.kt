package com.jsongo.processor

import com.jsongo.annotation.anno.AjsApi
import com.squareup.javapoet.*
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author jsongo
 * @date 19-9-9 上午10:59
 * @desc 生成 com.jsongo.ajs.helper.CustomInteractionRegister_Gen ,将注解添加到映射
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class AjsApiProcessor : AbstractProcessor() {

    private var mFiler: Filer? = null
    private var mMessager: Messager? = null

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        mMessager = processingEnv?.messager
        mFiler = processingEnv?.filer
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

//        此处使用到了square公司的javapoet库，用来辅助生成 类的代码

        val registerMapType = ParameterizedTypeName.get(
            ClassName.get(Map::class.java),
            ClassName.get(String::class.java),
            ClassName.get(String::class.java)
        )

        val mapName = "apiMap"

        // 添加 重写的getInteractionAPI方法
        val methodBuilder = MethodSpec.methodBuilder("getInteractionAPI")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(registerMapType)
            .addStatement("\$T $mapName = new \$T<>()", registerMapType, HashMap::class.java)
        roundEnv?.getElementsAnnotatedWith(AjsApi::class.java)?.forEach {
            val ajsApiAnno = it.getAnnotation(AjsApi::class.java)
            var prefix = ajsApiAnno.prefix
            if (prefix.isEmpty()) {
                prefix = it.enclosingElement.simpleName.toString()
            }
            var methodName = ajsApiAnno.methodName
            if (methodName.isEmpty()) {
                methodName = it.simpleName.toString()
            }
            methodBuilder.addStatement(
                "$mapName.put(\$S,\$S)",
                prefix + "." + methodName,
                it.enclosingElement.toString() + "." + it.simpleName.toString()
            )
        }
        methodBuilder.addStatement("return $mapName")

        try {
            //直接使用Class.forName会报错
            val baseInteractionRegisterClassName =
                ClassName.get("com.jsongo.ajs.interaction.register", "BaseInteractionRegister")

            // 构建类
            val finderClass = TypeSpec.classBuilder("CustomInteractionRegister_Gen")
                .superclass(baseInteractionRegisterClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build()
            JavaFile.builder("com.jsongo.ajs.helper", finderClass).build().writeTo(mFiler)
        } catch (e: Exception) {
            mMessager?.printMessage(Diagnostic.Kind.WARNING, "Exception : ${e.message}\r\n")
            //e.printStackTrace()
        }

        return true
    }

    override fun getSupportedAnnotationTypes() = linkedSetOf(AjsApi::class.java.canonicalName)

}
