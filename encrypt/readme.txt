        ENCRYPT_CONFIG_JSON= "{'ignoreUseSimpleEncrypt':true," +
                "'EncryptClassSign':'cn/qssq666/CoreLibrary0'," +
                //cn.qssq666.insertqqmodule.qssqproguard.keepnotpro.AllEncrypt
                "'SimpleEncryptClassSign':'cn/qssq666/AllEncrypt'," +
                "'simpleEncryptMethod':'decodeSimple'," +
                "'soEncryptMethod':'a7'}"


         ext {

         }


//libraryVariants.all { variant ->
    applicationVariants.all { variant ->
        variant.javaCompile.doLast {
            println("start classes obfuscation " + "${variant.javaCompile.destinationDir}")

            try {

                javaexec {
                    setDefaultCharacterEncoding("utf-8")
//                        systemProperty("file.encoding","-utf-8")
//                        syystemProperties()
                    main("-jar")
                    args(

                            "${projectDir.absolutePath}/encrypt/obfuseStringGradle.jar",
                            project.name,
                            variant.javaCompile.destinationDir,
                            "${projectDir.absolutePath}/encrypt/ignore_class.txt",
                            ENCRYPT_CONFIG_JSON
                    )
                }
            } catch (e) {
                println("执行失败.. " + "${e.getMessage()}")
            }
        }
    }
}