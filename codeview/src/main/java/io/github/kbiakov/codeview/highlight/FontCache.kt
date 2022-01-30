package io.github.kbiakov.codeview.highlight

import android.content.Context
import android.graphics.Typeface
import io.github.kbiakov.codeview.highlight.Font.Companion.Default
import io.github.kbiakov.codeview.highlight.FontCache
import java.util.*

class FontCache private constructor(context: Context) {
    private val fonts: MutableMap<String, Typeface?>

    // - Public methods
    fun getTypeface(context: Context): Typeface? {
        return getTypeface(context, Default)
    }

    fun getTypeface(context: Context, font: Font): Typeface? {
        return getTypeface(context, getLocalFontPath(font))
    }

    fun getLocalTypeface(context: Context, fontPath: String): Typeface? {
        return getTypeface(context, getLocalFontPath(fontPath))
    }

    fun getTypeface(context: Context, fontPath: String): Typeface? {
        var font = fonts[fontPath]
        return if (font != null) {
            font
        } else {
            font = loadFont(context, fontPath)
            fonts[fontPath] = font
            font
        }
    }

    fun saveTypeface(fontTypeface: Typeface) {
        fonts[fontTypeface.toString()] = fontTypeface
    }

    companion object {
        @Volatile
        private var instance: FontCache? = null
        operator fun get(context: Context): FontCache? {
            var localInstance = instance
            if (localInstance == null) {
                synchronized(FontCache::class.java) {
                    localInstance = instance
                    if (localInstance == null) {
                        localInstance = FontCache(context)
                        instance = localInstance
                    }
                }
            }
            return localInstance
        }

        private fun getLocalFontPath(font: Font): String {
            return String.format("%s.ttf", getLocalFontPath(font.name))
        }

        private fun getLocalFontPath(fontName: String): String {
            return String.format("fonts/%s", fontName)
        }

        private fun loadFont(context: Context, fontPath: String): Typeface {
            return Typeface.createFromAsset(context.assets, fontPath)
        }
    }

    init {
        fonts = object : WeakHashMap<String, Typeface?>() {
            init {
                val fontPath = getLocalFontPath(Default)
                put(fontPath, loadFont(context, fontPath))
            }
        }
    }
}