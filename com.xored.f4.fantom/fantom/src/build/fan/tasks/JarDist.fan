//
// Copyright (c) 2010, Brian Frank and Andy Frank
// Licensed under the Academic Free License version 3.0
//
// History:
//   1 Feb 10  Brian Frank  Creation
//

**
** JarDist compiles a set of Fantom pods into a single Java JAR file.
**
class JarDist : JdkTask
{

//////////////////////////////////////////////////////////////////////////
// Construction
//////////////////////////////////////////////////////////////////////////

  **
  ** Construct uninitialized task
  **
  new make(BuildScript script)
    : super(script)
  {
  }

//////////////////////////////////////////////////////////////////////////
// Run
//////////////////////////////////////////////////////////////////////////

  **
  ** Run the javac task
  **
  override Void run()
  {
    log.info("JarDist")
    verifyConfig
    log.indent
    initTempDir
    sysClasses
    podNames.each |name| { podClasses(name) }
    etcFiles
    main
    manifest
    jar
    cleanupTempDir
    log.info("Success [$outFile]")
    log.unindent
  }

  private Void verifyConfig()
  {
    if (podNames.isEmpty) throw fatal("Not configured: JarDist.podNames")
    if (podNames.contains("sys")) throw fatal("sys is implied in JarDist.podNames")
    if (outFile == null) throw fatal("Not configured: JarDist.outFile")
    if (mainMethod == null) throw fatal("Not configured: JarDist.mainMethod")

    m := Slot.findMethod(mainMethod, false)
    if (m == null) throw fatal("mainMethod not found: $mainMethod")
    if (!m.isStatic) throw fatal("mainMethod not static: $mainMethod")
    if (!m.params.isEmpty) throw fatal("mainMethod must have no params: $mainMethod")
  }

  private Void initTempDir()
  {
    tempDir = (Env.cur.tempDir + `jardist-$Int.random.toHex/`).create
    log.debug("TempDir [$tempDir]")
  }

  private Void cleanupTempDir()
  {
    tempDir.delete
    manifestFile.delete
  }

  private Void sysClasses()
  {
    log.info("Pod [sys]")
    extractClassfilesToTemp(script.devHomeDir + `lib/java/sys.jar`)
    reflect("sys")
  }

  private Void podClasses(Str podName)
  {
    log.info("Pod [$podName]")

    // open as zip and
    podFile := script.devHomeDir + `lib/fan/${podName}.pod`
    if (!podFile.exists) throw Err("Pod not found: $podFile")
    podZip  := Zip.open(podFile)
    meta := podZip.contents[`/meta.props`].readProps
    podZip.close

    // double check dependencies; for basic sanity check
    // we just check names not version numbers
    meta.get("pod.depends", "").split(';').each |dependStr|
    {
      if (dependStr.isEmpty) return
      depend := Depend(dependStr)
      if (!podNames.contains(depend.name) && depend.name != "sys")
        throw fatal("Missing dependency for '$podName': $depend")
    }

    // if pod already has its java native code, then the pod
    // zip should already contain all the classfiles, otherwise
    // we need to kick off a JStub
    if (meta["pod.native.java"] == "true")
    {
      log.info("  Extract pre-stubbed classfiles")
      extractClassfilesToTemp(podFile)
    }
    else
    {
      log.info("  JStub to classfiles")
      // stub into Java classfiles using JStub
      Exec(script,
        [javaExe,
         "-cp", (script.devHomeDir + `lib/java/sys.jar`).osPath,
         "-Dfan.home=$Env.cur.workDir.osPath",
         "fanx.tools.Jstub",
         "-d", tempDir.osPath,
         podName]).run

      // stub is "tempDir/{pod}.jar" - extract to tempDir and then delete it
      jar := tempDir + `${podName}.jar`
      extractClassfilesToTemp(jar)
      jar.delete
    }

    reflect(podName)
  }

  private Void extractClassfilesToTemp(File zipFile)
  {
    zip := Zip.open(zipFile)
    copyOpts := ["overwrite":true]
    zip.contents.each |f|
    {
      if (f.isDir) return
      if (f.ext != "class") return
      path := f.uri.toStr[1..-1]
      dest := tempDir + path.toUri
      f.copyTo(dest, copyOpts)
    }
    zip.close
  }

  private Void reflect(Str podName)
  {
    copyOpts := ["overwrite":true]
    resources := Str[,]
    zip := Zip.open(script.devHomeDir + `lib/fan/${podName}.pod`)
    zip.contents.each |f|
    {
      if (f.isDir) return
      if (f.name == "meta.props" || f.ext == "def" || f.ext == "fcode")
      {
        dest := tempDir + "reflect/${podName}${f.pathStr}".toUri
        f.copyTo(dest, copyOpts)
      }
      else
      {
        // decide if this is a resource file we should bundle
        if (f.ext == "class") return
        if (f.ext == "apidoc") return

        resources.add(f.pathStr)
        dest := tempDir + "res/${podName}${f.pathStr}".toUri
        f.copyTo(dest, copyOpts)
      }
    }
    (tempDir + `res/${podName}/res-manifest.txt`).out.print(resources.join("\n")).close
  }

  private Void etcFiles()
  {
    copyEtcFile(`etc/sys/timezones.ftz`)
    copyEtcFile(`etc/sys/ext2mime.props`, `res/sys/ext2mime.props`)
    copyEtcFile(`etc/sys/units.txt`)
  }

  private Void copyEtcFile(Uri uri, Uri destUri := uri)
  {
    src  := script.devHomeDir + uri
    dest := tempDir + destUri
    src.copyTo(dest)
  }

  private Void manifest()
  {
    log.info("Manifest")
    this.manifestFile = Env.cur.workDir + `Manifest.mf`
    out := this.manifestFile.out
    out.printLine("Manifest-Version: 1.0")
    out.printLine("Main-Class: fanjardist.Main")
    out.printLine("Created-By: Fantom JarDist $typeof.pod.version")
    out.close
  }

  private Void main()
  {
    log.info("Main")

    // explicitly initialize all the pod constants
    podInits := StrBuf();
    podNames.each |podName|
    {
      podInits.add("""      Env.cur().loadPodClass(Pod.find("$podName"));\n""")
    }

    // write out Main Java class
    file := tempDir + `fanjardist/Main.java`
    file.out.print(
      """package fanjardist;
         import fan.sys.*;
         public class Main
         {
           public static void main(String[] args)
           {
             try
             {
               System.getProperties().put("fan.jardist", "true");
               System.getProperties().put("fan.home",    ".");
               Sys.boot();
               Sys.bootEnv.setArgs(args);
         $podInits
               Method m = Slot.findMethod("$mainMethod");
               m.call();
             }
             catch (Err.Val e) { e.err().trace(); }
             catch (Throwable e) { e.printStackTrace(); }
           }
         }
         """).close

    // compile main
    Exec(script,
      [javacExe,
       "-cp", tempDir.osPath,
       "-d", tempDir.osPath,
       file.osPath], tempDir).run

    // delete source file once we have compiled into .class file
    Delete(script, file).run
  }

  private Void jar()
  {
    // jar everything back up outFile
    log.info("Jar [$outFile]")
    Exec(script,
      [jarExe,
       "cfm", outFile.osPath, manifestFile.osPath,
       "-C", tempDir.osPath,
       "."], tempDir).run
  }

//////////////////////////////////////////////////////////////////////////
// Fields
//////////////////////////////////////////////////////////////////////////

  ** Required output jar file to create
  File? outFile

  ** Qualified name of main method to run for JAR.
  ** This must be a static void method with no arguments.
  Str? mainMethod

  ** List of pods to compile into JAR; sys is always implied
  Str[] podNames := Str[,]

  private File? tempDir       // initTempDir
  private File? manifestFile  // manifest
}