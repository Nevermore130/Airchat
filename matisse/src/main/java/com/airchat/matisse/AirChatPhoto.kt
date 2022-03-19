package com.airchat.matisse

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import com.airchat.matisse.engine.impl.GlideEngine
import com.airchat.matisse.internal.entity.CaptureStrategy

object AirChatPhoto {
    const val REQUEST_CODE_CHOOSE = 23

    fun selector(
        activity: Activity,
        count: Int,
        onSelectListener: AirPhotoSelectListener
    ) {
        Matisse.from(activity)
            .choose(MimeType.ofImage(), false)
            .countable(true)
            .capture(true)
            .captureStrategy(
                CaptureStrategy(
                    true,
                    "com.example.dengluzhuce",
                    "test"
                )
            )
            .maxSelectable(count)
            .gridExpectedSize(
                activity.resources.getDimensionPixelSize(R.dimen.grid_expected_size)
            )
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .setOnSelectedListener { uriList, pathList ->
                onSelectListener.onSelected(uriList, pathList)
            }.originalEnable(true)
            .maxOriginalSize(10)
            .autoHideToolbarOnSingleTap(true)
            .setOnCheckedListener {

            }
            .forResult(REQUEST_CODE_CHOOSE);
    }

    /**
     * Obtain user selected media path list in the starting Activity or Fragment.
     *
     * @param data Intent passed by {@link Activity#onActivityResult(int, int, Intent)} or
     *             {@link Fragment#onActivityResult(int, int, Intent)}.
     * @return User selected media path list.
     */
    fun obtainResult(data: Intent): List<Uri> = Matisse.obtainResult(data)

    fun obtainPathResult(data: Intent): List<String> = Matisse.obtainPathResult(data)
}


interface AirPhotoSelectListener {
    fun onSelected(uriList: MutableList<Uri>, pathList: MutableList<String>)
}