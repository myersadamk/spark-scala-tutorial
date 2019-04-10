import java.nio.file.{Files, Paths, StandardCopyOption}

import org.scalatest.FunSpec
import util.FileUtil

import scala.reflect.io.File

// Run in local mode and local data.
class WordCount2Spec extends FunSpec {

  describe("WordCount2") {
    it("computes the word count of the input corpus") {
      val directories = cleanupPreviousTestRuns("kjv")

      WordCount2.main(Array.empty[String])

      cleanupAndVerifyThisTestRun(directories)
    }
  }

  describe("WordCount2 - Exercise 1") {
    it("parameterizes the sacred text being examined") {
      val sacredText = "vul"
      val directories = cleanupPreviousTestRuns(sacredText)

      WordCount2.main(Array(sacredText))

      cleanupAndVerifyThisTestRun(directories)
    }
  }

  case class TestFiles(out: String, outFile: String, goldenDir: String, goldenFile: String);

  def cleanupPreviousTestRuns(prefix: String = "kjv"): TestFiles = {
    val out = s"output/$prefix-wc2"
    val outFile = s"$out/part-00000"
    val goldenDir = s"golden/$prefix-wc2/"
    val goldenFile = s"$goldenDir/part-00000"

    FileUtil.rmrf(out) // Delete previous runs, if necessary.

    return TestFiles(out, outFile, goldenDir, goldenFile)
  }

  def cleanupAndVerifyThisTestRun(directories: TestFiles): Unit = {
    if (!File(directories.goldenDir).exists) {
      println(raw"""
        "No "expected" file exists in the "golden" directory. The current test results will be copied
        there. If these results are incorrect, please remove the file manually and rerun the test."
      """
      )
      Files.createDirectory(Paths.get(directories.goldenDir))
      Files.move(
        Paths.get(directories.outFile),
        Paths.get(directories.goldenFile),
        StandardCopyOption.REPLACE_EXISTING
      )
    }
    TestUtil.verifyAndClean(directories.outFile, directories.goldenFile, directories.out)
  }
}