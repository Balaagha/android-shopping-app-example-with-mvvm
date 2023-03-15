package com.example.data.helper.models.typedefs

import androidx.annotation.StringDef

object BuildVersions {
    const val PROD = "prod"
    const val DEV = "dev"
    const val MOCK = "mock"

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(PROD, DEV, MOCK)
    annotation class BuildVersionsDef
}
