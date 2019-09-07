package com.jsongo.processor

import com.jsongo.annotation.AjsApi
import com.squareup.javapoet.*
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import kotlin.collections.LinkedHashSet

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

        val registerListType = ParameterizedTypeName.get(
            ClassName.get(List::class.java),
            ClassName.get("com.jsongo.ajs.interaction.register", "BaseInteractionRegister")
        )

        //构建private static final List<BaseInteractionRegister> interactionRegisterList = new ArrayList<>();
        val fieldbuilder = FieldSpec.builder(
            registerListType,
            "interactionRegisterList",
            Modifier.PRIVATE,
            Modifier.STATIC,
            Modifier.FINAL
        ).initializer("new \$T<>()", ArrayList::class.java)

        //添加静态代码快，添加BaseInteractionRegister实例
        val codeBlockBuilder = CodeBlock.builder()
        val elementsAnnotatedWith = roundEnv?.getElementsAnnotatedWith(AjsApi::class.java)
        if (elementsAnnotatedWith != null) {
            for (element in elementsAnnotatedWith) {
                if (element.kind == ElementKind.CLASS) {
                    codeBlockBuilder.addStatement(
                        "interactionRegisterList.add(\$N.INSTANCE)",
                        element.toString()
                    )
                }
            }
        }
        // 添加get方法
        val methodBuilder = MethodSpec.methodBuilder("getInteractionRegisterList")
            .addModifiers(Modifier.PUBLIC)
            .addModifiers(Modifier.STATIC)
        methodBuilder.addStatement("return interactionRegisterList")
        methodBuilder.returns(registerListType)

        // 构建类
        val finderClass = TypeSpec.classBuilder("InteractionRegisterCollector_Gen")
            .addModifiers(Modifier.PUBLIC)
            .addField(fieldbuilder.build())
            .addStaticBlock(codeBlockBuilder.build())
            .addMethod(methodBuilder.build())
            .build()
        try {
            JavaFile.builder("com.jsongo.ajs.helper", finderClass).build().writeTo(mFiler)
        } catch (e: IOException) {
            mMessager?.printMessage(
                Diagnostic.Kind.WARNING,
                "${e.message}\r\n"
            )
//            e.printStackTrace()
        }

        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val types = LinkedHashSet<String>()
        types.add(AjsApi::class.java.canonicalName)
        return types
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return super.getSupportedSourceVersion()
    }

}
