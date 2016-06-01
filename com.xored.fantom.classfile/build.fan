using build
class Build : build::BuildPod
{
  new make()
  {
    podName = "javaBytecode"
    summary = ""
    srcDirs = [`examples/`, `fan/`, `fan/classfile/`, `fan/classfile/attributes/`, `fan/java/`]
    depends = ["sys 1.0"]
    outPodDir = `./`
  }
}
