package org.openjfx.property

import java.io.File
import java.io.FileInputStream
import java.util.*

class DarkSkyProperty {
    companion object {
        @JvmStatic
        private var isInitiated: Boolean = false
        private var key: String? = null

        @JvmStatic
        fun load(args: Array<String>) {
            if (isInitiated) throw UnsupportedOperationException("Can not loaded properties second time")
            val prop = loadProps(args)

            key = prop.getProperty("dark-sky.key")

            isInitiated = true
        }

        private fun loadProps(args: Array<String>): Properties {
            if (args.isEmpty())
                throw IllegalArgumentException("First Program Argument must be property file path")

            val prop = Properties()
            FileInputStream(File(args[0])).use {
                prop.load(it)
            }
            return prop
        }

        fun getKey(): String {
            return key ?: throw UnsupportedOperationException("Dark-Sky Properties was not loaded")
        }
    }

    init {
        throw UnsupportedOperationException("Can not create this class")
    }
}