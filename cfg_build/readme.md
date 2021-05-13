### 项目编译脚本统一管理。
 1. build_dependency.gradle   依赖库的版本管理
 2. app_compile.gradle        编译app时引入这个库，同时app里面只依赖自己需要的，会根据需求更新
 3. build_productor_flavor.gradle   区分不同渠道的打包脚本,目前计划是只需要application模块才用依赖
 4. build_version.gradle      编译版本号的生成及输出apk文件名修改
 5. lib_compile.gradle        库模块依赖
### 注意

1. app_compile.gradle ， lib_compile.gradle为不同的application/library模块提供了共同的依赖，这样在新建模块时
   可以只关注库本身提供功能时所需要依赖的库

2. 对于一些需要在plugins里面要添加插件的模块， 本方式不能支持，因为在plugins只能在build.gradle中才有这个字段
   所以像这类依赖需要分别在相应的模块中自己添加一次，比如libraryA需要用hilt,则
   ```groovy
   plugins {
   ....
       id 'dagger.hilt.android.plugin'
   }
   android {
    ....

    //hilt
    implementation deps.hilt.android
    kapt deps.hilt.kapt
    implementation deps.hilt.viewmodel
    kapt deps.hilt.viewmodel_kapt
   }

   dependencies {
   ....

   }
   ```