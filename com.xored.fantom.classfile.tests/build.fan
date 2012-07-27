using build
class Build : build::BuildPod
{
  new make()
  {
    podName = "testJavaBytecode"
    summary = ""
    srcDirs = [`fan/`]
    depends = ["sys 1.0", "javaBytecode 1.0"]
    outPodDir  = `./`
  }
}
