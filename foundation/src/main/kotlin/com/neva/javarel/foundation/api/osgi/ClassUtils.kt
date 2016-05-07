package com.neva.javarel.foundation.api.osgi

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import java.net.URL
import java.util.*

object ClassUtils {

    fun toClassName(classReader: ClassReader): String {
        return classReader.className.replace("/", ".")
    }

    fun toClassName(url: URL): String {
        val file = url.file
        val canonicalName = file.substring(1, file.length - ".class".length)

        return canonicalName.replace('/', '.')
    }

    fun isAnnotated(classReader: ClassReader, annotation: Class<out Annotation>): Boolean {
        return isAnnotated(classReader, setOf(annotation))
    }

    fun isAnnotated(classReader: ClassReader, annotations: Set<Class<out Annotation>>): Boolean {
        val annotationReader = AnnotationReader();
        classReader.accept(annotationReader, ClassReader.SKIP_DEBUG);

        return annotationReader.isAnnotationPresent(annotations);
    }

    class AnnotationReader : ClassVisitor(Opcodes.ASM5) {

        private val visited = ArrayList<String>()

        override fun visit(paramInt1: Int, paramInt2: Int, paramString1: String?, paramString2: String?,
                           paramString3: String?, paramArrayOfString: Array<String?>) {
            visited.clear()
        }

        override fun visitAnnotation(paramString: String?, paramBoolean: Boolean): AnnotationVisitor? {
            if (paramString != null) {
                val annotationClassName = getAnnotationClassName(paramString)
                visited.add(annotationClassName)
            }

            return super.visitAnnotation(paramString, paramBoolean)
        }

        fun isAnnotationPresent(annotations: Set<Class<out Annotation>>): Boolean {
            return annotations.any { annotation -> visited.contains(annotation.name) }
        }

        private fun getAnnotationClassName(rawName: String): String {
            return rawName.substring(1, rawName.length - 1).replace('/', '.')
        }
    }
}