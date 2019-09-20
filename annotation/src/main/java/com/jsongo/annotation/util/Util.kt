package com.jsongo.annotation.util

/**
 * @author ： jsongo
 * @date ： 19-9-20 上午8:45
 * @desc :
 */
object Util {
    /**
     * 通过全类名获取包名和类名
     */
    fun getPkgClazzName(rawClazzName: String): Pair<String, String> {
        val lastIndexOfDot = rawClazzName.lastIndexOf('.')
        //包名
        val pkgName = rawClazzName.subSequence(0, lastIndexOfDot)

        //类名
        val simpleName = rawClazzName.subSequence(lastIndexOfDot + 1, rawClazzName.length)

        return Pair(pkgName.toString(), simpleName.toString())
    }


}

/**
 * 获取注解的参数
 */
fun Annotation.getParamsMap(): Map<String, String> {

    // @com.jsongo.annotation.anno.Presenter(MainPresenter::class,"value a")
    // @com.jsongo.annotation.anno.Presenter(clazz=com.jsongo.mybasefrm.presenter.MainPresenter, s=value a)
    val annoStr = this.toString()
    val indexStart = annoStr.indexOf("(") + 1
    val indexEnd = annoStr.lastIndexOf(")")
    //clazz=com.jsongo.mybasefrm.presenter.MainPresenter, s=value a
    val paramsMapStr = annoStr.substring(indexStart, indexEnd)
    val split = paramsMapStr.split(", ")
    val paramMap = HashMap<String, String>()
    for (s in split) {
        val indexOfEq = s.indexOf('=')
        paramMap[s.substring(0, indexOfEq)] = s.substring(indexOfEq + 1, s.length)
    }
    return paramMap
}