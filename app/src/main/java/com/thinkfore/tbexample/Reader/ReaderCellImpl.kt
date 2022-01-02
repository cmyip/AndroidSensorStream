package com.thinkfore.tbexample.Reader

import android.app.Activity
import android.content.Context

abstract class ReaderCellImpl : ReaderCell {
    var _context: Context? = null

    override fun configure(context: Context) {
        _context = context
    }


}